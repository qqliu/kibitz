namespace php kibitz
namespace py kibitz
namespace csharp kibitz
namespace cpp kibitz
namespace perl kibitz
namespace d kibitz
namespace java kibitz
namespace js kibitz

struct Item {
    1: required map<string, string> attributes
    2: required i64 kibitz_generated_id
}

struct Recommender {
    1: required string username
    2: required string recommenderName
    3: required string clientKey
    4: required string homepage
    5: required string repoName
    6: required string title
    7: required string description
    8: required string image
    9: string video
    10: map<string, string> itemTypes
    11: list<string> displayItems
    12: required i64 numRecs
    13: required i64 maxRatingVal
    14: string ratingsColumn
    15: string primaryKey
}
service RecommenderService {
    void createNewIndividualServer(1:string key)
    void terminateSession(1:string key)
    list<Item> makeRecommendation(1:string key, 2:i64 userId, 3:i64 numRecs, 4:bool isBoolean, 5:list<string> displayColumns)
    list<Item> makeItemBasedRecommendations(1:string key, 2:i64 userId, 3:i64 numRecs, 4:list<string> displayColumns)
    list<Item> getPageItems(1:string key, 2:i64 page, 3:i64 numPerPage, 4:list<string> displayColumns)
    i64 getItemCount(1:string key)
    void recordRatings(1:string key, 2:i64 userId, 3:i64 itemId, 4:i64 rating)
    void deleteRatings(1:string key, 2:i64 userId, 3:i64 itemId)
    string createNewUser(1:string key, 2:string username, 3:bool iskibitzuser)
    bool checkUsername(1:string key, 2:string username, 3:bool iskibitzuser)
    bool checkLogin(1:string key, 2:string username, 3:string password, 4:bool iskibitzuser)
    i64 retrieveUserId(1:string key, 2:string username)
    bool createNewRecommender(1:string username, 2:string primaryKey, 3:string database, 5:string table, 6:string title, 7:string description, 8:string image, 9:string ratings_column, 10:string clientKey)
    list<Item> getUserRatedItems(1:string key, 2:i64 userId, 3:list<string> displayColumns)
    void initiateModel(1:string key 2:string table, 3:string username, 4:string database)
    list<Item> getSearchItems(1:string table, 2:string query, 3:list<string> columnsToSearch, 4:list<string> displayColumns)
    list<Item> makeOverallRatingBasedOrRandomRecommendation(1:string key, 2:string ratingColumnName, 3:i64 numRecs, 4:list<string> displayColumns)
    void addKibitzUser(1:string email, 2:string password)
    bool checkCorrectDatahubLogin(1:string username, 2:string repository, 3:string table, 4:string primary_key, 5:string title, 6:string description, 7:string image)
    bool checkRatingsColumn(1:string username, 2:string table, 3:string repository, 4:string ratings_column)
    list<Recommender> getRecommenders(1:string username)
    string getProfilePicture(1:string username)
    void saveFBProfilePic(1:string username, 2:string fbUsername)
    void deleteRecommender(1:string clientKey)
    void updateTemplate(1:string username, 2:string primaryKey, 3:string title, 4:string description, 5:string image, 6:string video, 7:map<string, string> itemTypes,
            8:list<string> displayItems, 9:i64 maxRatingVal, 10:i64 numRecs, 11:string recommenderName, 12:string clientKey, 13:string homepage,
            14:string creatorName, 15:string repoName, 16:string tableName, 17:string ratingsColumn)
    void configurePrefilledUserRatings(1:string username, 2:string repoName, 3:string primaryKey, 4:string itemTable,
            5:string tableName, 6:string userIdCol, 7:string itemIdCol, 8:string userRatingCol)
    list<string> getTables(1:string username, 2:string repo)
    list<string> getColumns(1:string username, 2:string repo, 3:string table)
}
