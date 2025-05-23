<?php
session_start();
include "db.php";

if (!isset($_SESSION['username']) || !isset($_POST['ans'])) {
    header("Location: index.html");
    exit();
}

$username = $_SESSION['username'];
$userAnswers = $_POST['ans'];
$correctAnswers = $_SESSION['quiz'];

$score = 0;

foreach ($correctAnswers as $qid => $correct) {
    if (isset($userAnswers[$qid]) && $userAnswers[$qid] == $correct) {
        $score++;
    }
}

$stmt = $conn->prepare("INSERT INTO users (username, score) VALUES (?, ?)");
$stmt->bind_param("si", $username, $score);
$stmt->execute();
$stmt->close();
$conn->close();
?>

<!DOCTYPE html>
<html>
<head>
    <title>Result</title>
    <style>
        body {
            font-family: Arial;
            background: #f3f4f6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px #aaa;
            text-align: center;
        }
        h2 { color: green; }
        h3 { color: #2563eb; }
    </style>
</head>
<body>
<div class="container">
    <h2>Quiz Completed Successfully!</h2>
    <p>Thank you, <strong><?php echo htmlspecialchars($username); ?></strong>, for attending the quiz.</p>
    <h3>Your Score: <?php echo $score; ?> / 5</h3>
    <a href="logout.php">Logout</a>
</div>
</body>
</html>
