<?php
include 'db.php';

$email = $_POST['email'];

$sql = "SELECT id FROM users WHERE email = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // В реално приложение тук се генерира токен и се праща истински имейл
    echo json_encode(["status" => "success", "message" => "Инструкциите за възстановяване са изпратени на вашия имейл"]);
} else {
    echo json_encode(["status" => "error", "message" => "Потребител с този имейл не съществува"]);
}
?>