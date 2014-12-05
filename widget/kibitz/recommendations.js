$(document).ready(function() {
    username = getCookie("username");
    userId = parseInt(getCookie("userId"));
    stars = $("#kibitz-listofitems").attr("max-rating");

    if (username != "" && userId != "" && username != undefined && userId != undefined && username != null && userId != null) {
        stars = $("#kibitz-listofitems").attr("max-rating");
        swap("kibitz-login-message", "kibitz-login-panel");
        document.getElementById('kibitz-login-message').innerHTML = "<p>You are now logged in as " + username + ". <a href='javascript:logout();'>Logout</a></p>";

        if (userId != null) {
        var items = client.makeRecommendation(client_key, userId, 10);
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
            document.getElementById('kibitz-listofitems').innerHTML = itemslist;
        }

        if(userId != null) {
          var my_rated_items = client.getUserRatedItems(client_key, userId);
          for (i = 0; i < my_rated_items.length; i++) {
            item = my_rated_items[i];
            var r = document.getElementById("rate" + item.id);
            if (r != null) {
                r.setAttribute("value", item.rating ? item.rating: -1);
            }
          }
        }

        var ratings = $(".rating");
        ratings.each(function (i, el) {
            var rating = parseInt($(el).attr('value'));
            if (rating > -1) {
              $(el).rating('http://localhost:9800/kibitz/rate', {maxvalue: parseInt(stars), curvalue: rating});
            } else {
              $(el).rating('http://localhost:9800/kibitz/rate', {maxvalue: parseInt(stars)});
            }
        });
    } else {
        document.getElementById('kibitz-listofitems').innerHTML = "<p>Please login or <a href='register.html'>signup</a> to see recommendations.</p>";
    }
});