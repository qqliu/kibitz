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
    2: required i64 id
}
service RecommenderService {
    void createNewIndividualServer(1:string key)
    void terminateSession(1:string key)
    list<Item> makeRecommendation(1:string key, 2:i64 userId, 3:i64 numRecs, 4:list<string> displayColumns)
    list<Item> makeItemBasedRecommendations(1:string key, 2:i64 userId, 3:i64 numRecs, 4:list<string> displayColumns)
    list<Item> getPageItems(1:string key, 2:i64 page, 3:i64 numPerPage, 4:list<string> displayColumns)
    i64 getItemCount(1:string key)
    void recordRatings(1:string key, 2:i64 userId, 3:i64 itemId, 4:i64 rating)
    void deleteRatings(1:string key, 2:i64 userId, 3:i64 itemId)
    string createNewUser(1:string key, 2:string username, 3:string email, 4:string password, 5:bool iskibitzuser)
    bool checkUsername(1:string key, 2:string username, 3:bool iskibitzuser)
    bool checkLogin(1:string key, 2:string username, 3:string password, 4:bool iskibitzuser)
    i64 retrieveUserId(1:string key, 2:string username, 3:string password)
    bool createNewRecommender(1:string username, 2:string primaryKey, 3:string password, 4:string database, 5:string table, 6:string firstColumnName, 7:string secondColumnName, 8:string thirdColumnName, 9:string firstColumnType, 10:string secondColumnType, 11:string thirdColumnType, 12:list<string> displayColumns)
    list<Item> getUserRatedItems(1:string key, 2:i64 userId, 4:list<string> displayColumns)
    void initiateModel(1:string key 2:string table, 3:string username, 4:string password, 5:string database)
    list<Item> getSearchItems(1:string table, 2:string query, 4:list<string> displayColumns)
    list<Item> makeOverallRatingBasedOrRandomRecommendation(1:string key, 2:string ratingColumnName, 3:i64 numRecs, 4:list<string> displayColumns)
    list<Item> getItemsFromPrimaryKeys(1:string key, 2:string primaryKeys, 3:list<string> itemKey, 4:list<string> displayColumns)
}
