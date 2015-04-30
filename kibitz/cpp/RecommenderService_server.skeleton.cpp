// This autogenerated skeleton file illustrates how to build a server.
// You should copy it to another filename to avoid overwriting it.

#include "RecommenderService.h"
#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/server/TSimpleServer.h>
#include <thrift/transport/TServerSocket.h>
#include <thrift/transport/TBufferTransports.h>

using namespace ::apache::thrift;
using namespace ::apache::thrift::protocol;
using namespace ::apache::thrift::transport;
using namespace ::apache::thrift::server;

using boost::shared_ptr;

using namespace  ::kibitz;

class RecommenderServiceHandler : virtual public RecommenderServiceIf {
 public:
  RecommenderServiceHandler() {
    // Your initialization goes here
  }

  void createNewIndividualServer(const std::string& key) {
    // Your implementation goes here
    printf("createNewIndividualServer\n");
  }

  void terminateSession(const std::string& key) {
    // Your implementation goes here
    printf("terminateSession\n");
  }

  void makeRecommendation(std::vector<Item> & _return, const std::string& key, const int64_t userId, const int64_t numRecs, const bool isBoolean, const std::vector<std::string> & displayColumns) {
    // Your implementation goes here
    printf("makeRecommendation\n");
  }

  void makeItemBasedRecommendations(std::vector<Item> & _return, const std::string& key, const int64_t userId, const int64_t numRecs, const std::vector<std::string> & displayColumns) {
    // Your implementation goes here
    printf("makeItemBasedRecommendations\n");
  }

  void getPageItems(std::vector<Item> & _return, const std::string& key, const int64_t page, const int64_t numPerPage, const std::vector<std::string> & displayColumns) {
    // Your implementation goes here
    printf("getPageItems\n");
  }

  int64_t getItemCount(const std::string& key) {
    // Your implementation goes here
    printf("getItemCount\n");
  }

  void recordRatings(const std::string& key, const int64_t userId, const int64_t itemId, const int64_t rating) {
    // Your implementation goes here
    printf("recordRatings\n");
  }

  void deleteRatings(const std::string& key, const int64_t userId, const int64_t itemId) {
    // Your implementation goes here
    printf("deleteRatings\n");
  }

  void createNewUser(std::string& _return, const std::string& key, const std::string& username, const bool iskibitzuser) {
    // Your implementation goes here
    printf("createNewUser\n");
  }

  bool checkUsername(const std::string& key, const std::string& username, const bool iskibitzuser) {
    // Your implementation goes here
    printf("checkUsername\n");
  }

  bool checkLogin(const std::string& key, const std::string& username, const std::string& password, const bool iskibitzuser) {
    // Your implementation goes here
    printf("checkLogin\n");
  }

  int64_t retrieveUserId(const std::string& key, const std::string& username) {
    // Your implementation goes here
    printf("retrieveUserId\n");
  }

  bool createNewRecommender(const std::string& username, const std::string& primaryKey, const std::string& database, const std::string& table, const std::string& title, const std::string& description, const std::string& image, const std::string& ratings_column, const std::string& clientKey) {
    // Your implementation goes here
    printf("createNewRecommender\n");
  }

  void getUserRatedItems(std::vector<Item> & _return, const std::string& key, const int64_t userId, const std::vector<std::string> & displayColumns) {
    // Your implementation goes here
    printf("getUserRatedItems\n");
  }

  void initiateModel(const std::string& key, const std::string& table, const std::string& username, const std::string& database) {
    // Your implementation goes here
    printf("initiateModel\n");
  }

  void getSearchItems(std::vector<Item> & _return, const std::string& table, const std::string& query, const std::vector<std::string> & columnsToSearch, const std::vector<std::string> & displayColumns) {
    // Your implementation goes here
    printf("getSearchItems\n");
  }

  void makeOverallRatingBasedOrRandomRecommendation(std::vector<Item> & _return, const std::string& key, const std::string& ratingColumnName, const int64_t numRecs, const std::vector<std::string> & displayColumns) {
    // Your implementation goes here
    printf("makeOverallRatingBasedOrRandomRecommendation\n");
  }

  void addKibitzUser(const std::string& email, const std::string& password) {
    // Your implementation goes here
    printf("addKibitzUser\n");
  }

  bool checkCorrectDatahubLogin(const std::string& username, const std::string& repository, const std::string& table, const std::string& primary_key, const std::string& title, const std::string& description, const std::string& image) {
    // Your implementation goes here
    printf("checkCorrectDatahubLogin\n");
  }

  bool checkRatingsColumn(const std::string& username, const std::string& table, const std::string& repository, const std::string& ratings_column) {
    // Your implementation goes here
    printf("checkRatingsColumn\n");
  }

  void getRecommenders(std::vector<Recommender> & _return, const std::string& username) {
    // Your implementation goes here
    printf("getRecommenders\n");
  }

  void getProfilePicture(std::string& _return, const std::string& username) {
    // Your implementation goes here
    printf("getProfilePicture\n");
  }

  void saveFBProfilePic(const std::string& username, const std::string& fbUsername) {
    // Your implementation goes here
    printf("saveFBProfilePic\n");
  }

  void deleteRecommender(const std::string& clientKey) {
    // Your implementation goes here
    printf("deleteRecommender\n");
  }

  void updateTemplate(const std::string& username, const std::string& primaryKey, const std::string& title, const std::string& description, const std::string& image, const std::string& video, const std::map<std::string, std::string> & itemTypes, const std::vector<std::string> & displayItems, const int64_t maxRatingVal, const int64_t numRecs, const std::string& recommenderName, const std::string& clientKey, const std::string& homepage, const std::string& creatorName, const std::string& repoName, const std::string& tableName, const std::string& ratingsColumn) {
    // Your implementation goes here
    printf("updateTemplate\n");
  }

  void configurePrefilledUserRatings(const std::string& username, const std::string& repoName, const std::string& primaryKey, const std::string& itemTable, const std::string& tableName, const std::string& userIdCol, const std::string& itemIdCol, const std::string& userRatingCol) {
    // Your implementation goes here
    printf("configurePrefilledUserRatings\n");
  }

  void getTables(std::vector<std::string> & _return, const std::string& username, const std::string& repo) {
    // Your implementation goes here
    printf("getTables\n");
  }

  void getColumns(std::vector<std::string> & _return, const std::string& username, const std::string& repo, const std::string& table) {
    // Your implementation goes here
    printf("getColumns\n");
  }

};

int main(int argc, char **argv) {
  int port = 9090;
  shared_ptr<RecommenderServiceHandler> handler(new RecommenderServiceHandler());
  shared_ptr<TProcessor> processor(new RecommenderServiceProcessor(handler));
  shared_ptr<TServerTransport> serverTransport(new TServerSocket(port));
  shared_ptr<TTransportFactory> transportFactory(new TBufferedTransportFactory());
  shared_ptr<TProtocolFactory> protocolFactory(new TBinaryProtocolFactory());

  TSimpleServer server(processor, serverTransport, transportFactory, protocolFactory);
  server.serve();
  return 0;
}

