var items = client.getItems(client_key);
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

document.getElementById('kibitz-items-list').innerHTML = itemslist;
var ratingVal = $("#kibitz-items-list").getAttribute("max-rating-value");
var pageLimit = $("#kibitz-items-list").getAttribute("items-per-page");

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
      $(el).rating('', {maxvalue: ratingVal, curvalue: rating});
    } else {
      $(el).rating('', {maxvalue: ratingVal});
    }
});