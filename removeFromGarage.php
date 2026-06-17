<?php
include 'db.php';
$user_id = $_POST['user_id'];
$vin = $_POST['vin'];

$sql = "DELETE FROM user_garage WHERE user_id = ? AND vin = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $user_id, $vin);

if ($stmt->execute()) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>
