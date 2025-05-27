<!DOCTYPE html>
<html>
<head>
    <title>Fetch Student Data</title>
</head>
<body>
    <h2>Enter Roll Number</h2>
    <form method="POST" action="">
        <input type="text" name="rollno" required>
        <input type="submit" name="submit" value="Get Data">
    </form>

    <?php
    if (isset($_POST['submit'])) {
        $rollno = $_POST['rollno'];

        // DB credentials
        $host = 'localhost';
        $username = 'root'; // replace with your DB username
        $password = 'kani1625';     // replace with your DB password
        $dbname = 'student_dbs';

        // Create connection
        $conn = new mysqli($host, $username, $password, $dbname);

        // Check connection
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        // Prepare query
        $stmt = $conn->prepare("SELECT * FROM students WHERE rollno = ?");
        $stmt->bind_param("s", $rollno);
        $stmt->execute();
        $result = $stmt->get_result();

        // Display result
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo "<h3>Student Information:</h3>";
            echo "Roll No: " . htmlspecialchars($row["rollno"]) . "<br>";
            echo "Name: " . htmlspecialchars($row["name"]) . "<br>";
            echo "Class: " . htmlspecialchars($row["class"]) . "<br>";
        } else {
            echo "No record found.";
        }

        $stmt->close();
        $conn->close();
    }
    ?>
</body>
</html>