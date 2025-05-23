<?php
session_start();
if (isset($_POST['username'])) {
    $_SESSION['username'] = $_POST['username'];
    header("Location: quiz.php");
    exit();
} else {
    header("Location: index.html");
    exit();
}
?>
