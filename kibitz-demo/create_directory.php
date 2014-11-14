<?php
  if(!empty($_POST['tablename'])) {
    //Check if the file is JPEG image and it's size is less than 350Kb
    $tablename = $_POST['tablename'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $repo = $_POST['repo'];
    
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
  }
  
  $content = "var title = '$tablename' ; $(document).ready(function() { transport.open(); client.initiateModel('$tablename', '$username', '$password', '$repo'); document.getElementById('title').innerHTML = title + ' Recommender'; });";
  file_put_contents($dst . "/required_functions.js", $content, FILE_APPEND);
?>
