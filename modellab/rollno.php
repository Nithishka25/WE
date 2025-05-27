<!DOCTYPE html>
<html>
<head>
    <title>Student Lookup</title>
</head>
<body>
    <h2>Enter Roll Number</h2>
    <form method="POST" action="">
        <input type="text" name="rollno" required placeholder="Enter Roll Number">
        <input type="submit" name="submit" value="Get Student Data">
    </form>

    <?php
    if (isset($_POST['submit'])) {
        $rollno = $_POST['rollno'];

        // Database credentials
        $host = 'localhost';
        $username = 'root';      // Change if needed
        $password = '';          // Change if needed
        $dbname = 'student_db';  // Make sure this matches the DB name

        // Connect to DB
        $conn = new mysqli($host, $username, $password, $dbname);

        // Check connection
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        // Prepare and execute query
        $stmt = $conn->prepare("SELECT * FROM students WHERE rollno = ?");
        $stmt->bind_param("s", $rollno);
        $stmt->execute();
        $result = $stmt->get_result();

        // Display result
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo "<h3>Student Information</h3>";
            echo "Roll No: " . htmlspecialchars($row['rollno']) . "<br>";
            echo "Name: " . htmlspecialchars($row['name']) . "<br>";
            echo "Class: " . htmlspecialchars($row['class']) . "<br>";
        } else {
            echo "<p>No record found for Roll Number: <strong>" . htmlspecialchars($rollno) . "</strong></p>";
        }

        // Clean up
        $stmt->close();
        $conn->close();
    }
    ?>
</body>
</html>
