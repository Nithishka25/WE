<!DOCTYPE html>
<html>
<head>
    <title>Login Result</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #eef;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .result-box {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.2);
            width: 400px;
            text-align: center;
        }
        .success {
            color: green;
            font-weight: bold;
            font-size: 18px;
        }
        .error {
            color: red;
            font-weight: bold;
            font-size: 18px;
        }
    </style>
</head>
<body>
    <div class="result-box">
        <?php
        if (isset($_POST['username']) && isset($_POST['password'])) {
            $username = trim($_POST['username']);
            $password = trim($_POST['password']);

            if (!empty($username) && !empty($password)) {
                echo "<p class='success'>Login Successful</p>";
                echo "<p>Welcome, <strong>" . htmlspecialchars($username) . "</strong>!</p>";
            } else {
                echo "<p class='error'>Login Failed! Username or Password is empty.</p>";
            }
        } else {
            echo "<p class='error'>Login Failed! Invalid Request.</p>";
        }
        ?>
    </div>
</body>
</html>
