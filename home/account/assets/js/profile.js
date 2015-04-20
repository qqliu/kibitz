$(document).ready(function() {
    var user = $("#username_display").html();
    var profilePic = client.getProfilePicture(user);
    $("#username").val(username);
    $("#fb_username").val(profilePic);
});