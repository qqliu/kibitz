<?php
  if(!empty($_POST['tablename'])) {
    //Check if the file is JPEG image and it's size is less than 350Kb
    $tablename = $_POST['tablename'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $repo = $_POST['repo'];

    $src = getcwd();
    $dst = $src . "/" . $tablename;
    $src = $src . "/widget";
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

  $characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  $client_key = '';
  for ($i = 0; $i < 20; $i++) {
    $client_key .= $characters[rand(0, strlen($characters) - 1)];
  }

  $content = "var title = '$tablename' ; var client_key = '$client_key'; $(document).ready(function() { transport.open(); client.createNewIndividualServer(client_key); client.initiateModel(client_key, '$tablename', '$username', '$password', '$repo'); document.getElementById('title').innerHTML = title + ' Recommender'; });";
  file_put_contents($dst . "/required_functions.js", $content, FILE_APPEND);
?>
