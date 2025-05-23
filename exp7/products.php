<?php
session_start();

// Database credentials
$host = "localhost";
$user = "root";
$pass = "kani1625";
$dbname = "ecommerce_db";

// Create DB connection
$conn = new mysqli($host, $user, $pass, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Add product
if (isset($_POST['add_product'])) {
    $product_name = $conn->real_escape_string(trim($_POST['product_name']));
    $category = $conn->real_escape_string(trim($_POST['category']));
    $price = floatval($_POST['price']);
    $stock = intval($_POST['stock']);

    $sql = "INSERT INTO products (product_name, category, price, stock) VALUES ('$product_name', '$category', $price, $stock)";
    if ($conn->query($sql) === TRUE) {
        $_SESSION['message'] = "Product added successfully!";
    } else {
        $_SESSION['message'] = "Error adding product: " . $conn->error;
    }
    header("Location: " . $_SERVER['PHP_SELF']);
    exit;
}

// Apply filters
if (isset($_POST['apply_filters'])) {
    $_SESSION['filters'] = [
        'product_name' => trim($_POST['filter_product_name']),
        'category' => trim($_POST['filter_category']),
        'price_min' => $_POST['filter_price_min'] !== '' ? floatval($_POST['filter_price_min']) : '',
        'price_max' => $_POST['filter_price_max'] !== '' ? floatval($_POST['filter_price_max']) : '',
        'stock_min' => $_POST['filter_stock_min'] !== '' ? intval($_POST['filter_stock_min']) : '',
        'stock_max' => $_POST['filter_stock_max'] !== '' ? intval($_POST['filter_stock_max']) : '',
    ];
    header("Location: " . $_SERVER['PHP_SELF']);
    exit;
}

// Clear filters
if (isset($_POST['clear_filters'])) {
    unset($_SESSION['filters']);
    header("Location: " . $_SERVER['PHP_SELF']);
    exit;
}

// Build WHERE clause from filters
$whereClauses = [];
$filtersApplied = [];

if (!empty($_SESSION['filters'])) {
    $filters = $_SESSION['filters'];

    if (!empty($filters['product_name'])) {
        $name = $conn->real_escape_string($filters['product_name']);
        $whereClauses[] = "product_name LIKE '%$name%'";
        $filtersApplied[] = "Product Name contains '{$filters['product_name']}'";
    }
    if (!empty($filters['category'])) {
        $cat = $conn->real_escape_string($filters['category']);
        $whereClauses[] = "category LIKE '%$cat%'";
        $filtersApplied[] = "Category contains '{$filters['category']}'";
    }
    if ($filters['price_min'] !== '' && $filters['price_max'] !== '') {
        $whereClauses[] = "price BETWEEN {$filters['price_min']} AND {$filters['price_max']}";
        $filtersApplied[] = "Price between {$filters['price_min']} and {$filters['price_max']}";
    } elseif ($filters['price_min'] !== '') {
        $whereClauses[] = "price >= {$filters['price_min']}";
        $filtersApplied[] = "Price >= {$filters['price_min']}";
    } elseif ($filters['price_max'] !== '') {
        $whereClauses[] = "price <= {$filters['price_max']}";
        $filtersApplied[] = "Price <= {$filters['price_max']}";
    }
    if ($filters['stock_min'] !== '' && $filters['stock_max'] !== '') {
        $whereClauses[] = "stock BETWEEN {$filters['stock_min']} AND {$filters['stock_max']}";
        $filtersApplied[] = "Stock between {$filters['stock_min']} and {$filters['stock_max']}";
    } elseif ($filters['stock_min'] !== '') {
        $whereClauses[] = "stock >= {$filters['stock_min']}";
        $filtersApplied[] = "Stock >= {$filters['stock_min']}";
    } elseif ($filters['stock_max'] !== '') {
        $whereClauses[] = "stock <= {$filters['stock_max']}";
        $filtersApplied[] = "Stock <= {$filters['stock_max']}";
    }
}

$sql = "SELECT * FROM products";
if (count($whereClauses) > 0) {
    $sql .= " WHERE " . implode(" AND ", $whereClauses);
}

$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Product Management with Sessions & Cookies</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background: #f9f9f9;
        margin: 40px 0;
        display: flex;
        justify-content: center;
    }
    .container {
        width: 90%;
        max-width: 900px;
        background: #fff;
        padding: 30px 40px;
        border-radius: 8px;
        box-shadow: 0 0 15px rgba(0,0,0,0.1);
        text-align: center;
    }
    h2 {
        color: #2c3e50;
        margin-bottom: 15px;
    }

    form {
        max-width: 900px;
        margin: 0 auto 30px auto;
        text-align: left;
    }

    /* Flex container for labels + inputs side by side */
    .form-row {
        display: flex;
        gap: 20px;
        flex-wrap: wrap;
        align-items: flex-end;
        justify-content: center;
        margin-bottom: 15px;
    }

    label {
        display: flex;
        flex-direction: column;
        font-weight: 600;
        color: #34495e;
        font-size: 14px;
        min-width: 140px;
        max-width: 180px;
    }

    input[type=text], input[type=number] {
        padding: 8px 10px;
        border: 1.5px solid #ccc;
        border-radius: 5px;
        font-size: 14px;
        box-sizing: border-box;
        transition: border-color 0.3s ease;
        margin-top: 5px;
        width: 100%;
    }
    input[type=text]:focus, input[type=number]:focus {
        border-color: #2980b9;
        outline: none;
    }

    /* Center the submit buttons */
    input[type=submit] {
        background-color: #2980b9;
        color: white;
        padding: 10px 35px;
        font-size: 16px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        transition: background-color 0.3s ease;
        margin: 0 10px;
        min-width: 120px;
    }
    input[type=submit]:hover {
        background-color: #1f6391;
    }

    /* Message box styling */
    .message {
        padding: 12px;
        background: #d4edda;
        border: 1px solid #c3e6cb;
        margin: 15px auto 30px auto;
        max-width: 600px;
        color: #155724;
        border-radius: 5px;
        text-align: center;
    }

    .filters-applied {
        margin-bottom: 20px;
        font-style: italic;
        color: #555;
        max-width: 600px;
        margin-left: auto;
        margin-right: auto;
        text-align: center;
    }

    /* Products table */
    table {
        border-collapse: collapse;
        width: 100%;
        max-width: 900px;
        margin: 0 auto 40px auto;
        text-align: center;
        box-shadow: 0 2px 8px rgb(0 0 0 / 0.1);
        border-radius: 6px;
        overflow: hidden;
    }
    th, td {
        border: 1px solid #ddd;
        padding: 12px 15px;
    }
    th {
        background-color: #2980b9;
        color: white;
        font-weight: 600;
    }
    tr:nth-child(even) {
        background-color: #f7f9fb;
    }
    tr:hover {
        background-color: #e9f0f7;
    }

    /* Clear Filters button style */
    input[name="clear_filters"] {
        background-color: #c0392b;
        margin-left: 10px;
    }

    /* Price and stock min/max inputs aligned horizontally */
    .range-inputs {
        display: flex;
        gap: 8px;
        margin-top: 5px;
    }
    .range-inputs input {
        flex: 1;
    }
</style>
</head>
<body>

<div class="container">
    <h2>Add Product</h2>
    <form method="post" novalidate>
        <div class="form-row">
            <label>Product Name:
                <input type="text" name="product_name" required>
            </label>
            <label>Category:
                <input type="text" name="category" required>
            </label>
            <label>Price:
                <input type="number" step="0.01" name="price" required>
            </label>
            <label>Stock:
                <input type="number" name="stock" required>
            </label>
            <input type="submit" name="add_product" value="Add Product">
        </div>
    </form>

    <?php if (isset($_SESSION['message'])): ?>
        <div class="message">
            <?php
            echo $_SESSION['message'];
            unset($_SESSION['message']);
            ?>
        </div>
    <?php endif; ?>

    <hr>

    <h2>Filter Products</h2>
    <form method="post" novalidate>
        <div class="form-row">
            <label>Product Name contains:
                <input type="text" name="filter_product_name" value="<?php echo isset($_SESSION['filters']['product_name']) ? htmlspecialchars($_SESSION['filters']['product_name']) : ''; ?>">
            </label>
            <label>Category contains:
                <input type="text" name="filter_category" value="<?php echo isset($_SESSION['filters']['category']) ? htmlspecialchars($_SESSION['filters']['category']) : ''; ?>">
            </label>
            <label>Price between:
                <div class="range-inputs">
                    <input type="number" step="0.01" name="filter_price_min" placeholder="Min" value="<?php echo isset($_SESSION['filters']['price_min']) ? htmlspecialchars($_SESSION['filters']['price_min']) : ''; ?>">
                    <input type="number" step="0.01" name="filter_price_max" placeholder="Max" value="<?php echo isset($_SESSION['filters']['price_max']) ? htmlspecialchars($_SESSION['filters']['price_max']) : ''; ?>">
                </div>
            </label>
            <label>Stock between:
                <div class="range-inputs">
                    <input type="number" name="filter_stock_min" placeholder="Min" value="<?php echo isset($_SESSION['filters']['stock_min']) ? htmlspecialchars($_SESSION['filters']['stock_min']) : ''; ?>">
                    <input type="number" name="filter_stock_max" placeholder="Max" value="<?php echo isset($_SESSION['filters']['stock_max']) ? htmlspecialchars($_SESSION['filters']['stock_max']) : ''; ?>">
                </div>
            </label>
            <input type="submit" name="apply_filters" value="Apply Filters">
            <input type="submit" name="clear_filters" value="Clear Filters">
        </div>
    </form>

    <?php if (!empty($filtersApplied)): ?>
        <div class="filters-applied">
            <strong>Filters applied:</strong> <?php echo implode(", ", $filtersApplied); ?>
        </div>
    <?php endif; ?>

    <h2>Products List</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Product Name</th>
                <th>Category</th>
                <th>Price ($)</th>
                <th>Stock</th>
            </tr>
        </thead>
        <tbody>
        <?php if ($result && $result->num_rows > 0): ?>
            <?php while ($row = $result->fetch_assoc()): ?>
                <tr>
                    <td><?php echo htmlspecialchars($row['id']); ?></td>
                    <td><?php echo htmlspecialchars($row['product_name']); ?></td>
                    <td><?php echo htmlspecialchars($row['category']); ?></td>
                    <td><?php echo number_format($row['price'], 2); ?></td>
                    <td><?php echo htmlspecialchars($row['stock']); ?></td>
                </tr>
            <?php endwhile; ?>
        <?php else: ?>
            <tr><td colspan="5">No products found.</td></tr>
        <?php endif; ?>
        </tbody>
    </table>
</div>

</body>
</html>

<?php
$conn->close();
?>
