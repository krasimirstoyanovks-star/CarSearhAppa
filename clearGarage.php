<?php
include 'db.php';
$user_id = $_POST['user_id'];

// Изтриваме всички записи за този потребител от гаража
$sql = "DELETE FROM user_garage WHERE user_id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $user_id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error"]);
}
?>
