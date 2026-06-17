<?php

$conn = mysqli_connect(
    "localhost",
    "root",
    "",
    "carsearchdb"
);

if(!$conn){
    die("Connection failed");
}
?>