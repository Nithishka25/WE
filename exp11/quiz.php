<?php
session_start();
include "db.php";

if (!isset($_SESSION['username'])) {
    header("Location: index.html");
    exit();
}

$sql = "SELECT * FROM questions ORDER BY RAND() LIMIT 5";
$result = $conn->query($sql);
$_SESSION['quiz'] = [];
?>

<!DOCTYPE html>
<html>
<head>
    <title>Quiz</title>
    <style>
        body { font-family: Arial; background: #f9fafb; padding: 40px; }
        .container {
            background: #fff;
            padding: 30px;
            width: 600px;
            margin: auto;
            border-radius: 10px;
            box-shadow: 0 0 10px #bbb;
        }
        h2 { text-align: center; }
        .question { margin-bottom: 20px; }
        input[type="submit"] {
            background: #10b981;
            color: white;
            border: none;
            padding: 10px 20px;
            margin: 20px auto;
            display: block;
            cursor: pointer;
        }
        input[type="submit"]:hover { background: #059669; }
    </style>
</head>
<body>
<div class="container">
    <h2>Quiz for <?php echo htmlspecialchars($_SESSION['username']); ?></h2>
    <form method="post" action="result.php">
        <?php
        $i = 1;
        while ($row = $result->fetch_assoc()) {
            $_SESSION['quiz'][$row['id']] = $row['answer'];
            echo "<div class='question'>";
            echo "<p><b>Q$i. {$row['question']}</b></p>";
            for ($j = 1; $j <= 4; $j++) {
                echo "<label><input type='radio' name='ans[{$row['id']}]' value='$j' required> {$row['option'.$j]}</label><br>";
            }
            echo "</div>";
            $i++;
        }
        ?>
        <input type="submit" value="Submit Quiz">
    </form>
</div>
</body>
</html>
