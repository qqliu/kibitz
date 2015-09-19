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

    var items = client.getPageItems(client_key, parseInt(window.location.hash.split("#")[1]), 10, display_items);
    display_database_items(items, true);

    var numPages = parseInt(client.getItemCount(client_key));
    var pages = "";

	for (var i = 0; i < Math.min(numPages, 100); i += 10) {
	    var displayPages = i/10 + 1;
		    pages += "<li><a href='#" + i/10 + "' onclick='processPage(" + i/10 + ")'>" + displayPages + "</a></li>";
	}

        var nextPages = i - 10;
        pages += "<li><a href='#" + nextPages/10 + "' onclick='processNextPages(" + nextPages + ")'>&#10095;</a></li>";

	$("#pages").empty();
	$("#pages").append(pages);
  } else {
    document.getElementById('listofitems').innerHTML = "<p>Please <a href='javascript:popup_register_page();'>sign up</a> or <a href='javascript:show_signup_form();'>login</a> to see and rate items.</p>";
  }
});
