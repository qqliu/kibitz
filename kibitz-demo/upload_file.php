<?php
include 'login.php';
ini_set('memory_limit', '500M');

if((!empty($_FILES["uploaded_file"])) && ($_FILES['uploaded_file']['error'] == 0)) {
  //Check if the file is JPEG image and it's size is less than 350Kb
  $dst =  $_POST['recommender_name'];
  $url = "http://localhost/kibitz-demo/" . $dst;
  $src = getcwd();
  $dst = $src . "/" . $dst;
  $src = $src . "/base";
  mkdir($dst);
  function recurse_copy($src,$dst) { 
    $dir = opendir($src); 
    @mkdir($dst); 
    while(false !== ( $file = readdir($dir)) ) { 
        if (( $file != '.' ) && ( $file != '..' )) { 
            if ( is_dir($src . '/' . $file) ) { 
                recurse_copy($src . '/' . $file,$dst . '/' . $file); 
            } 
            else { 
                copy($src . '/' . $file,$dst . '/' . $file); 
            } 
        } 
    } 
    closedir($dir);
  }
  recurse_copy($src, $dst);
  
  $filename = basename($_FILES['uploaded_file']['name']);
  $databaseitemtable = md5(uniqid(rand(), true));
  
  $content = '<?php $client -> initiateModel("' . $databaseitemtable . '"); $name = "' . $_POST['recommender_name'] . '";?>'; 
  file_put_contents($dst . "/initiate_model.php", $content);
  
  $databaseratingtable = $databaseitemtable . "ratings";
  $databaseusertable = $databaseitemtable . "users";
  $sql = "CREATE TABLE " . $databaseitemtable . " (id MEDIUMINT NOT NULL AUTO_INCREMENT, item VARCHAR(500), description VARCHAR(1000), image VARCHAR(500), primary key (id))";
  $usertablesql = "CREATE TABLE " . $databaseratingtable . " (user_id INT, item_id INT, rating INT);";
  $usersql = "CREATE TABLE " . $databaseusertable . " (id MEDIUMINT NOT NULL AUTO_INCREMENT, username VARCHAR(100), email VARCHAR(100), password VARCHAR(500), primary key (id));";
  
  if (mysqli_query($con,$sql)) {
    //echo "Table " . $databaseitemtable . " created successfully";
  } else {
    echo "Error creating table: " . mysqli_error($con);
  }
  
  if (mysqli_query($con,$usertablesql)) {
    //echo "Table " . $databaseratingtable . " created successfully";
  } else {
    echo "Error creating table: " . mysqli_error($con);
  }
  
  if (mysqli_query($con,$usersql)) {
    //echo "Table " . $databaseusertable . " created successfully";
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
            mysqli_query($con, "INSERT INTO $databaseitemtable (item, description, image) VALUES " . implode(', ', $allrows) . ";"); //or die(mysqli_error($con));
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
      mysqli_query($con, "INSERT INTO $databaseitemtable (item, description, image) VALUES " . implode(', ', $allrows) . ";"); //or die(mysqli_error($con));
      mysqli_close($con);
  } else {
     echo "Error: Only .txt and .csv files under 350Kb are accepted for upload";
  }
} else {
 echo "Error: No file uploaded";
}
?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8"> 
        <title>Kibitz</title>
        <meta name="generator" content="Bootply" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css" rel="stylesheet">
        
        <!--[if lt IE 9]>
          <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.min.css" type="text/css" rel="stylesheet">









        <!-- CSS code from Bootply.com editor -->
        
        <style type="text/css">
            html,body {
  height:100%;
}

h1 {
  font-family: Arial,sans-serif
  font-size:80px;
  color:#DDCCEE;
}

.lead {
	color:#DDCCEE;
}


/* Custom container */
.container-full {
  margin: 0 auto;
  width: 100%;
  min-height:100%;
  background-color:#110022;
  color:#eee;
  overflow:hidden;
}

.container-full a {
  color:#efefef;
  text-decoration:none;
}

.v-center {
  margin-top:7%;
}
  
        </style>
    </head>
    
    <!-- HTML code from Bootply.com editor -->
    
    <body  >
        
        <div class="container-full">

      <div class="row">
       
        <div class="col-lg-12 text-center v-center">
          
          <h1>Kibitz</h1>
          <p class="lead">Your recommender is located at <a href= "<?php echo $url; ?>" ><?php echo $url; ?></a>.</p>
        </div>
        
      </div> <!-- /row -->
  
  	  <div class="row">
       
        <div class="col-lg-12 text-center v-center" style="font-size:39pt;">
          <a href="https://github.com/qqliu/kibitz"><i class="icon-github"></i>
        </div>
      
      </div>
  
  	<br><br><br><br><br>

</div> <!-- /container full -->


        
        <script type='text/javascript' src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>


        <script type='text/javascript' src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
        
    </body>
</html>