namespace php kibitz
namespace py kibitz
namespace csharp kibitz
namespace cpp kibitz
namespace perl kibitz
namespace d kibitz
namespace java kibitz
namespace js kibitz

struct Item {
    1: required i64 id
    2: required string title
    3: required string description
    4: required string image
    5: i32 rating
}
service RecommenderService {
    void createNewIndividualServer(1:string key)
    void terminateSession(1:string key)
    list<Item> makeRecommendation(1:string key, 2:i32 userId, 3:i32 numRecs)
    list<Item> getItems(1:string key)
    void recordRatings(1:string key, 2:i32 userId, 3:i32 itemId, 4:i32 rating)
    void deleteRatings(1:string key, 2:i32 userId, 3:i32 itemId)
    string createNewUser(1:string key, 2:string username, 3:string email, 4:string password, 5:bool iskibitzuser)
    bool checkUsername(1:string key, 2:string username, 3:bool iskibitzuser)
    bool checkLogin(1:string key, 2:string username, 3:string password, 4:bool iskibitzuser)
    i64 retrieveUserId(1:string key, 2:string username, 3:string password)
    bool createNewRecommender(1:string username, 2:string password, 3:string database, 4:string table)
    list<Item> getUserRatedItems(1:string key, 2:i32 userId)
    void initiateModel(1:string key 2:string table, 3:string username, 4:string password, 5:string database)
}
