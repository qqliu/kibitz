<?php
include 'login.php';

if((!empty($_FILES["uploaded_file"])) && ($_FILES['uploaded_file']['error'] == 0)) {
  //Check if the file is JPEG image and it's size is less than 350Kb
  $filename = basename($_FILES['uploaded_file']['name']);
  echo $filename;
  $ext = substr($filename, strrpos($filename, '.') + 1);
  if (($ext == "txt") && ($_FILES["uploaded_file"]["type"] == "text/plain") && 
    ($_FILES["uploaded_file"]["size"] < 350000)) {
      $txt_file    = file_get_contents($_FILES['uploaded_file']['tmp_name']);
      $rows        = explode("\n", $txt_file);
      
      foreach($rows as $row)
      {
          echo $row;
          $row_data = mysqli_real_escape_string($con, $row);
          echo $row_data;
          mysqli_query($con, "INSERT INTO options (item) VALUES ('$row_data');") or die(mysqli_error($con));
      }
      mysqli_close($con);
  } else {
     echo "Error: Only .txt and .csv files under 350Kb are accepted for upload";
  }
} else {
 echo "Error: No file uploaded";
}
?>

<?php
/*$allowedExts = array("csv", "txt");
$temp = explode(".", $_FILES["file"]["name"]);
$extension = end($temp);
if (($_FILES["file"]["type"] == "text/plain")
&& in_array($extension, $allowedExts))
{
    $txt_file    = file_get_contents("/Users/qliu/Documents/kibitz/allitems.txt");
    $rows        = explode("\n", $txt_file);
    array_shift($rows);
    echo $rows;
    $con = mysqli_connect("sql.mit.edu","quanquan","hof9924ne@!", "quanquan+datahub");
    
    // Check connection
    if (mysqli_connect_errno()) {
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }
    
    foreach($rows as $row => $data)
    {
        //get row data
        $row_data = explode(',', $data);
        echo $row_data;
        mysqli_query($con, "INSERT INTO options ('item') VALUES ('$row_data');") or die(mysqli_error($con));
    }
    mysqli_close($con);
  if ($_FILES["file"]["error"] > 0)
    {
    echo "Return Code: " . $_FILES["file"]["error"] . "<br>";
    }
  else
    {
    echo "Upload: " . $_FILES["file"]["name"] . "<br>";
    echo "Type: " . $_FILES["file"]["type"] . "<br>";
    echo "Size: " . ($_FILES["file"]["size"] / 1024) . " kB<br>";
    echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br>";

    $newname = dirname(__FILE__).'/upload/'.$temp;
    if (file_exists($newname))
      {
      echo $_FILES["file"]["name"] . " already exists. ";
      }
    else
      {
        move_uploaded_file($_FILES['file']['tmp_name'],$newname);
        echo "Stored in: " . $newname;
      }
    }
  }
else
  {
  echo "Invalid file";
  }*/
?>