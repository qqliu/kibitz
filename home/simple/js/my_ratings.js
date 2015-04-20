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
    document.getElementById('login_message').innerHTML = "<a href='javascript:logout();'>Logout</a>";

    if (window.location.hash === null || window.location.hash === '') {
      window.location.hash = 0;
    }

    var items = client.getUserRatedItems(client_key, userId, display_items);
    var itemslist ="";
    for (var i =0; i < items.length; i++) {
      var item = items[i];
      if (item.attributes[title] !== null && item.attributes[title] !== undefined && item.kibitz_generated_id !== null) {
	currItem = '<tr><tr><div class="relative">';
	if (image && item.attributes[image] !== null && item.attributes[image] !== undefined && item.attributes.image.indexOf("http") > -1) {
		currItem += '<object id="profile_image' + item.kibitz_generated_id + '" data="' + item.attributes[image] + '"></object>';
		var imageUrl = item.attributes[image];
      
		imageExists(imageUrl, function(id, prefix) {
			//Delete the object
			$("#" + prefix + id).remove();
		}, item.kibitz_generated_id, "profile_image");
	}
	
	if (video && item.attributes[video] !== null && item.attributes[video] !== undefined) {
		currItem += '<object id="profile_image' + item.kibitz_generated_id + '" width="300px" height="300px" data="' + item.attributes[video] + '"></object>';
	}
	currItem += '<div class="inline-block user-info"><h2>' + item.attributes[title] + '</h2>';
	if (description && item.attributes[description] !== null && item.attributes[description] !== undefined){
	  currItem += '<div class="icons"><ul class="list-inline"><li>' + item.attributes[description] + '</li>';
	}

	var object_keys = Object.keys(item_types);
	for (var j in object_keys) {
		var type = item_types[object_keys[j]];
		if (item.attributes[object_keys[j]] !== undefined && item.attributes[object_keys[j]] !== null && item.attributes[object_keys[j]] !== "undefined" && item.attributes[object_keys[j]] !== "null") {
			if (type === "text") {
				currItem += "<div>" + item.attributes[object_keys[j]] + "</div>";
			} else if (type === "image") {
				currItem += "<div><object id='"+ object_keys[j] + item.kibitz_generated_id + "' data='" + item.attributes[object_keys[j]] + "'></object></div>";
				var extra_imageUrl = item.attributes[object_keys[j]];
				imageExists(extra_imageUrl, function(id, prefix) {
					//Delete the object
					$("#" + prefix + id).remove();
				}, item.kibitz_generated_id, object_keys[j]);
			} else if (type === "video") {
				currItem += "<div><object id='"+ object_keys[j] + item.kibitz_generated_id + "' width='300px' height='300px' data='" + item.attributes[object_keys[j]] + "'></object></div>";
			} else if (type === "html") {
				currItem += "<div>" + item.attributes[object_keys[j]] + "</div>";
			}
		}
	}
	
	/*if (rating_type == "like/dislike")
	  currItem += '<div id="rate' + item.kibitz_generated_id + '" class="like-btn">Like</div><div id="rate' + item.kibitz_generated_id + 'dislike" class="dislike-btn"></div>';
	else if (rating_type == "thumbs")
	  currItem += '<div id="rate' + item.kibitz_generated_id + '" class="rating_thumbs">&nbsp;</div><div class="implementation_thumbs"></div>';
	else if (rating_type == "dropdown")
	  currItem += '<div id="rate' + item.kibitz_generated_id + '" class="rating_dropdown">&nbsp;</div><div class="implementation_dropdown"></div>';
	else if (rating_type == "plus/minus")
	  currItem += '<div id="rate' + item.kibitz_generated_id + '" class="rating_plus">&nbsp;</div><div class="implementation_plus"></div>';
	else*/
	currItem += '<div id="rate' + item.kibitz_generated_id + '" class="rating">&nbsp;</div><div class="implementation"></div>';
	if (item.attributes.description !== null) {
	  currItem += '</ul></div>';
	}
	currItem += '</div></div></td></tr>';
	itemslist += currItem;
      }
    }
    document.getElementById('listofitems').innerHTML = itemslist;

    if(userId !== null) {
      var my_rated_items = client.getUserRatedItems(client_key, userId, display_items);
      for (i = 0; i < my_rated_items.length; i++) {
	item = my_rated_items[i];
	var r = document.getElementById("rate" + item.kibitz_generated_id);
	if (r !== null) {
	    r.setAttribute("value", item.attributes.rating ? item.attributes.rating: -1);
	}
	var r_dislike = document.getElementById("rate" + item.kibitz_generated_id+"dislike");
	if (r_dislike !== null) {
	    r_dislike.setAttribute("value", item.attributes.rating ? item.attributes.rating: -1);
	}
      }
    }

    /*if (rating_type == "like/dislike") {
      var ratings = $(".like-btn");
      ratings.each(function (i, el) {
	  var rating = parseInt($(el).attr('value'));
	  if (rating > -1) {
	    $(el).rating('', {maxvalue: parseInt(maxRatingVal), curvalue: rating});
	  } else {
	    // Get rid of like/dislike
	  }
      });
      
      ratings = $(".dislike-btn");
      ratings.each(function (i, el) {
	  var rating = parseInt($(el).attr('value'));
	  if (rating > -1) {
	    $(el).rating('', {maxvalue: parseInt(maxRatingVal), curvalue: rating});
	  } else {
	    $(el).rating('', {maxvalue: parseInt(maxRatingVal)});
	  }
      });      
      
      $('.like-btn').click(function(){
	  $('.dislike-btn').removeClass('dislike-h');    
	  $(this).addClass('like-h');
      });
      
      $('.dislike-btn').click(function(){
	  $('.like-btn').removeClass('like-h');    
	  $(this).addClass('dislike-h');
      });
    } else if (rating_type == "thumbs") {
      
    } else if (rating_type == "plus/minus") {
      
    } else if (rating_type == "dropdown") {
      
    } else {*/
    var ratings = $(".rating");
    ratings.each(function (i, el) {
	var rating = parseInt($(el).attr('value'));
	if (rating > -1) {
	  $(el).rating('', {maxvalue: parseInt(maxRatingVal), curvalue: rating});
	} else {
	  $(el).rating('', {maxvalue: parseInt(maxRatingVal)});
	}
    });
    //} 

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
    document.getElementById('listofitems').innerHTML = "<p>Please <a href='javascript:show_signup_form();'>login</a> to see and rate items.</p>";
  }
});