var setCookie = function (cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
};

var getCookie = function(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
    }
    return "";
};

var eraseCookie = function (name) {
    setCookie(name,"",-1);
}

var process_login = function() {
    username=document.getElementById('username').value;
    password = document.getElementById('password').value;

    var correct_login = client.checkLogin(client_key, username, password, false);
    swap("login_message", "login_panel");
    if (correct_login) {
       setCookie("username", username);
       userId = client.retrieveUserId(client_key, username, password);
       setCookie("userId", userId);
       document.location = ".";
    } else {
       document.getElementById('login_message').innerHTML = "<p>Wrong login info. <a href='.'>Try again.</a></p>";
    }
};

var swap = function swap(one, two) {
    document.getElementById(one).style.display = 'block';
    document.getElementById(two).style.display = 'none';
};

var logout = function() {
    swap("login_panel", "login_message");
    eraseCookie("username");
    eraseCookie("userId");

    document.location = ".";
};

var processPage = function(page) {
    window.location.hash = page;
    document.getElementById('listofitems').innerHTML = "";

    var items = client.getPageItems(client_key, window.location.hash, 10);
    var itemslist ="";
    for (var i =0; i < items.length; i++) {
      var item = items[i];
      if (item.id != null && item.title != null) {
        currItem = '<tr><tr><div class="relative">';
        if (item.image.indexOf("http") > -1) {
          currItem += '<img src = "' + item.image + '" />';
        }
        currItem += '<div class="inline-block user-info"><h2>' + item.title + '</h2>';
        if (item.description != null){
          currItem += '<div class="icons"><ul class="list-inline"><li>' + item.description + '</li>';
        }
        currItem += '<div id="rate' + item.id + '" class="rating">&nbsp;</div><div class="implementation"></div>';
        if (item.description != null) {
          currItem += '</ul></div>';
        }
        currItem += '</div></div></td></tr>';
        itemslist += currItem;
      }
    }
    document.getElementById('listofitems').innerHTML = itemslist;
};

//var transport = new Thrift.Transport("http://kibitz.csail.mit.edu:9888/kibitz/");
var transport = new Thrift.Transport("http://localhost:9889/kibitz/");
var protocol = new Thrift.Protocol(transport);
var client = new kibitz.RecommenderServiceClient(protocol);

