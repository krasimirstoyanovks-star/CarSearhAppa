<?php
include 'db.php';
$user_id = $_GET['user_id'];

// Взимаме детайлите за колите от таблицата vehicles чрез връзка с user_garage
$sql = "SELECT v.* FROM vehicles v 
        INNER JOIN user_garage ug ON v.vin = ug.vin 
        WHERE ug.user_id = ?";
        
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$garage = [];
while($row = $result->fetch_assoc()) {
    $garage[] = $row;
}
echo json_encode($garage);
?>