<?php

include 'db.php';

$vin = $_GET['vin'];

$sql = "SELECT * FROM vehicles WHERE vin='$vin'";

$result = mysqli_query($conn,$sql);

echo json_encode(mysqli_fetch_assoc($result));

?>