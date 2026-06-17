<?php
header('Content-Type: application/json; charset=utf-8');
include 'db.php';

$email = $_POST['email'];
$password = $_POST['password'];

$sql = "SELECT id, username, password FROM users WHERE email = ?"; // Променено
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($user = $result->fetch_assoc()) {
    if (password_verify($password, $user['password'])) {
        unset($user['password']);
        echo json_encode(["status" => "success", "user" => $user]);
    } else {
        echo json_encode(["status" => "error", "message" => "Грешна парола"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Потребителят не е намерен"]);
}
?>