<?php
include 'login.php';
ini_set('memory_limit', '500M');

if((!empty($_FILES["uploaded_file"])) && ($_FILES['uploaded_file']['error'] == 0)) {
  //Check if the file is JPEG image and it's size is less than 350Kb
  $filename = basename($_FILES['uploaded_file']['name']);
  $databaseitemtable = md5(uniqid(rand(), true));
  $databaseratingtable = $databaseitemtable . "ratings";
  $databaseusertable = $databaseitemtable . "users";
  $sql = "CREATE TABLE " . $databaseitemtable . " (id MEDIUMINT NOT NULL AUTO_INCREMENT, item VARCHAR(500), description VARCHAR(1000), image VARCHAR(500), primary key (id))";
  $usertablesql = "CREATE TABLE " . $databaseratingtable . " (user_id INT, item_id INT, rating INT);";
  $usersql = "CREATE TABLE " . $databaseusertable . " (id MEDIUMINT NOT NULL AUTO_INCREMENT, username VARCHAR(100), email VARCHAR(100), password VARCHAR(500), primary key (id));";
  
  if (mysqli_query($con,$sql)) {
    echo "Table " . $databaseitemtable . " created successfully";
  } else {
    echo "Error creating table: " . mysqli_error($con);
  }
  
  if (mysqli_query($con,$usertablesql)) {
    echo "Table " . $databaseratingtable . " created successfully";
  } else {
    echo "Error creating table: " . mysqli_error($con);
  }
  
  if (mysqli_query($con,$usersql)) {
    echo "Table " . $databaseusertable . " created successfully";
  } else {
    echo "Error creating table: " . mysqli_error($con);
  }

  $ext = substr($filename, strrpos($filename, '.') + 1);
  if (($ext == "txt" || $ext == "csv") && ($_FILES["uploaded_file"]["type"] == "text/plain" || $_FILES["uploaded_file"]["type"] == "text/csv")) {
      $txt_file    = file_get_contents($_FILES['uploaded_file']['tmp_name']);
      $rows        = explode("\n", $txt_file);
      $allrows = array();
      foreach($rows as $key => $row) {
        if (count($allrows) > 1000) {
            mysqli_query($con, "INSERT INTO $databaseitemtable (item, description, image) VALUES " . implode(', ', $allrows) . ";") or die(mysqli_error($con));
            unset($allrows);
            $allrows = array();
        }
        $row_data = mysqli_real_escape_string($con, $row);
        unset($rows[$key]);
        $data = explode(",", $row_data);
        for ($i = 0; $i < count($data); ++$i) {
          $data[$i] = "'" . $data[$i] . "'";
        }
        $row_data = implode(",", $data);
        //mysqli_query($con, "INSERT INTO $databaseitemtable (item, description, image) VALUES (" . $row_data . ");") or die(mysqli_error($con));
        //echo $row_data;
        array_push($allrows, "(" . $row_data . ")");
      }
      //echo implode(', ', $allrows);
      //echo "INSERT INTO $databaseitemtable (item, description, image) VALUES " . implode(', ', $allrows) . ";";
      mysqli_query($con, "INSERT INTO $databaseitemtable (item, description, image) VALUES " . implode(', ', $allrows) . ";") or die(mysqli_error($con));
      mysqli_close($con);
  } else {
     echo "Error: Only .txt and .csv files under 350Kb are accepted for upload";
  }
} else {
 echo "Error: No file uploaded";
}
?>