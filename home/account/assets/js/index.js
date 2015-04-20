$(document).ready(function() {
  if (!(sessionStorage.getItem("username") === null || sessionStorage.getItem("username") === "null" ||
	 sessionStorage.getItem("username") === "undefined" || sessionStorage.getItem("username") === undefined))
    username = sessionStorage.getItem("username")
  else if (!(getCookie("username") === null || getCookie("username") === "null" ||
	    getCookie("username") === "undefined" || getCookie("username") === undefined))
    username = getCookie("username");
  
  recommendersInfo = client.getRecommenders(username);
  recommenders = {};
  var trash_button_1 = '<button class="close" aria-hidden="true" data-dismiss="alert" type="button" onclick="process_delete_rec(\'' 
  var trash_button_2 = '\');"><i class="fa fa-trash-o"></i></button>';
  
  $("#todo tbody").empty();
  
  for (i in recommendersInfo) {
    recommenders[recommendersInfo[i].clientKey] = recommendersInfo[i];
    
    var entry = '<tr><td><a href=\'javascript:process_recommender_info("' + recommendersInfo[i].clientKey + '");\'>' + recommendersInfo[i].recommenderName + '</a></span>' + trash_button_1
                + recommendersInfo[i].clientKey + trash_button_2 + '</tr></td>';
    $("#todo tbody").append(entry);
  }
});