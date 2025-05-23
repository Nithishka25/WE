<?php
$conn = new mysqli("localhost", "root", "kani1625", "ecommerce_db");
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$successMsg = "";

if (isset($_POST['submit'])) {
    $product_name = $_POST['product_name'];
    $category = $_POST['category'];
    $price = $_POST['price'];
    $stock = $_POST['stock'];

    if ($product_name && $category && $price && $stock) {
        $stmt = $conn->prepare("INSERT INTO products (product_name, category, price, stock) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("ssdi", $product_name, $category, $price, $stock);
        if ($stmt->execute()) {
            $successMsg = "Product added successfully!";
        } else {
            $successMsg = "Error adding product: " . $stmt->error;
        }
        $stmt->close();
    }
}

$search = isset($_POST['search']) ? $_POST['search'] : '';
$search = $conn->real_escape_string($search);

$sql = "SELECT * FROM products";
if ($search != "") {
    $sql .= " WHERE product_name LIKE '%$search%' OR category LIKE '%$search%'";
}

$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html>
<head>
    <title>E-Commerce Products</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 40px;
            background-color: #f8f9fa;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        h2 {
            color: #333;
            margin-bottom: 15px;
        }
        form {
            background-color: #fff;
            padding: 20px 30px;
            border-radius: 8px;
            box-shadow: 0 0 8px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            width: 350px;
            text-align: center;
        }
        input[type=text], input[type=number] {
            width: 90%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        input[type=submit] {
            padding: 10px 20px;
            background-color: #007bff;
            border: none;
            color: white;
            font-weight: bold;
            cursor: pointer;
            border-radius: 5px;
            font-size: 16px;
            margin-top: 10px;
        }
        input[type=submit]:hover {
            background-color: #0056b3;
        }
        table {
            border-collapse: collapse;
            width: 80%;
            max-width: 900px;
            margin-bottom: 40px;
            background-color: #fff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        th, td {
            border: 1px solid #ddd;
            padding: 14px 20px;
            text-align: center;
            font-size: 16px;
        }
        th {
            background-color: #007bff;
            color: white;
            font-weight: 600;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        p.success {
            color: green;
            font-weight: bold;
            margin-bottom: 20px;
        }
        p.error {
            color: red;
            font-weight: bold;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<h2>Add New Product</h2>



<form method="post">
    <input type="text" name="product_name" placeholder="Product Name" required><br>
    <input type="text" name="category" placeholder="Category" required><br>
    <input type="number" step="0.01" name="price" placeholder="Price" required><br>
    <input type="number" name="stock" placeholder="Stock Quantity" required><br>
    <input type="submit" name="submit" value="Add Product">

    <?php if ($successMsg != ""): ?>
    <p class="<?= strpos($successMsg, 'Error') === 0 ? 'error' : 'success' ?>">
        <?= htmlspecialchars($successMsg) ?>
    </p>
<?php endif; ?>
</form>

<h2>Search Products</h2>
<form method="post">
    <input type="text" name="search" placeholder="Search by name or category" value="<?= htmlspecialchars($search) ?>">
    <input type="submit" value="Search">
</form>

<?php
if ($result->num_rows > 0) {
    echo "<table><tr><th>ID</th><th>Product Name</th><th>Category</th><th>Price</th><th>Stock</th></tr>";
    while ($row = $result->fetch_assoc()) {
        echo "<tr>
            <td>{$row['id']}</td>
            <td>" . htmlspecialchars($row['product_name']) . "</td>
            <td>" . htmlspecialchars($row['category']) . "</td>
            <td>{$row['price']}</td>
            <td>{$row['stock']}</td>
        </tr>";
    }
    echo "</table>";
} else {
    echo "<p><strong>No results found.</strong></p>";
}

$conn->close();
?>

</body>
</html>
