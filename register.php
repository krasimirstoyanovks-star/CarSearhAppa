<?php
include 'db.php';

$username = $_POST['username']; // Променено
$email = $_POST['email'];
$password = $_POST['password'];

$hashed_password = password_hash($password, PASSWORD_BCRYPT);

$sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)"; // Променено
$stmt = $conn->prepare($sql);
$stmt->bind_param("sss", $username, $email, $hashed_password);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Успешна регистрация"]);
} else {
    echo json_encode(["status" => "error", "message" => "Грешка: " . $conn->error]);
}
?>