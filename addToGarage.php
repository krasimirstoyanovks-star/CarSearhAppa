<?php
include 'db.php';
$user_id = $_POST['user_id'];
$vin = $_POST['vin'];

$sql = "INSERT IGNORE INTO user_garage (user_id, vin) VALUES (?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $user_id, $vin);

if ($stmt->execute()) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>