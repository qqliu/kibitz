var username, userId;
$(document).ready(function() {
  username = getCookie("username");
  userId = getCookie("userId");
  
  if (username === "" || username === undefined || username === null) {
	username = sessionStorage.getItem("username");
	userId = sessionStorage.getItem("userId");
  }
  if (username !== "" && userId !== "" && username !== undefined && userId !== undefined && username !== null && userId !== null) {
    swap("login_message", "login_panel");
    document.getElementById('login_message').innerHTML = "<p>You are now logged in as " + username + ". <a href='javascript:logout();'>Logout</a></p>";

    if (window.location.hash === null || window.location.hash === '') {
      window.location.hash = 0;
    }

    var items = client.getUserRatedItems(client_key, userId, display_items);
    display_database_items(items);
  } else {
    document.getElementById('listofitems').innerHTML = "<p>Please <a href='javascript:show_signup_form();'>login</a> to see and rate items.</p>";
  }
});