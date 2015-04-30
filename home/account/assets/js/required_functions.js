var homepageURL = "https://localhost/kibitz-demo/home/";

var process_recommender_info = function(client_key) {
  cur_client_key = client_key;
  sessionStorage.setItem("cur_client_key", cur_client_key);
  sessionStorage.setItem("recommenders", JSON.stringify(recommenders));
  
  localStorage.setItem("cur_client_key", cur_client_key);
  localStorage.setItem("recommenders", JSON.stringify(recommenders));
  window.location = "recommender_info.html";
};

var save_recommender_info = function() {
  var primaryKey, title, description, image, video, item_types, display_items, maxRatingVal, num_recs,
      recommenderName, clientKey, homepage, creatorName, repoName, tableName, ratings_column;
      
  clientKey = $("#client_key").html().trim();
  recommenderName = $("#recommender_name").val().trim();
  repoName = $("#repo").html().trim();
  tableName = $("#table").html().trim();
  homepage = $("#homepage").val().trim();
  primaryKey = $("#primary_key_chosen").find(".chosen-single").find("span").html().trim();
  if (primaryKey === "ID Column")
      primaryKey = "";
  title = $("#title_chosen").find(".chosen-single").find("span").html().trim();
  if (title === "Title Column")
      title = "";
  description = $("#description_chosen").find(".chosen-single").find("span").html().trim();
  if (description === "Description Column")
    description = "";
  image = $("#image_chosen").find(".chosen-single").find("span").html().trim();
  if (image === "Image Column")
    image = "";
  video = $("#video_chosen").find(".chosen-single").find("span").html().trim();
  if (video === "Video Column")
    video = "";
  ratings_column = $("#overall_rating_chosen").find(".chosen-single").find("span").html().trim();
  display_items = [];
  
  var display = $("#display_columns").val().split(",");
  for (i in display) {
    display_items.push(display[i].trim());
  }
  
  item_types = {};
  var types = $("#display_item_types").val().split(",");
  for (i in types) {
    var key_val = types[i].split(":");
    item_types[key_val[0].trim()] = key_val[1].trim();
  }
  
  num_recs = parseInt($("#num_recs").val().trim());
  maxRatingVal = parseInt($("#max_rating_value").val().trim());
  
  client.updateTemplate(username, primaryKey, title, description, image, video, item_types, display_items, maxRatingVal, num_recs,
			recommenderName, clientKey, homepage, creatorName, repoName, tableName, ratings_column);
};

var configure_prefilled_user_ratings_table = function() {
  var tableName, user_id_col, item_id_col, user_rating_col, itemTable, repoName, primaryKey;
  tableName = $("#user_rating_table").val().trim();
  user_id_col = $("#user_id").val().trim();
  item_id_col = $("#item_id").val().trim();
  user_rating_col = $("#user_rating_column").val().trim();
  repoName = $("#repo").html().trim();
  itemTable = $("#table").html().trim();
  primaryKey = $("#primary_key").val().trim();
  client.configurePrefilledUserRatings(username, repoName, primaryKey, itemTable, tableName, user_id_col, item_id_col, user_rating_col);
};

var configure_item_similarity = function() {
  var tableName, first_id_col, second_id_col, item_sim_col;
  tableName = $("#item_similarity_table").val();
  first_id_col = $("#item_id_one").val();
  second_id_col = $("#item_id_two").val();
  item_sim_col = $("#similarity_column").val();
};

var process_delete_rec = function(client_key) {
  client.deleteRecommender(client_key);
};

var save_fb_username = function() {
  var fb_username = $("#fb_username").val().trim(), username = $("#username").val();
  if (fb_username !== "")
    client.saveFBProfilePic(username, fb_username);
};