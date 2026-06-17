<?php
include 'db.php';

$make = mysqli_real_escape_string($conn, $_GET['make']);
$model = mysqli_real_escape_string($conn, $_GET['model']);

// ПРОВЕРЕТЕ ТОЗИ РЕД: трябва да има imageUrl, description И paymentUrl
$sql = "SELECT name, category, price, availability, imageUrl, description, paymentUrl FROM parts WHERE make='$make' AND model='$model'";

$result = mysqli_query($conn, $sql);
$parts = array();

if ($result) {
    while($row = mysqli_fetch_assoc($result)) {
        $parts[] = $row;
    }
}

echo json_encode($parts);
?>