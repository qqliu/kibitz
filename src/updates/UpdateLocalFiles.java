package updates;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.util.List;

import kibitz.DatahubDataModel;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.THttpClient;

import datahub.Connection;
import datahub.ConnectionParams;
import datahub.DBException;
import datahub.DataHub;
import datahub.ResultSet;
import datahub.Tuple;

public class UpdateLocalFiles {
	private static final String KIBITZ_LOCAL_STORAGE_ADDR = "/home/ubuntu/";

	public static void main(String[] args) {
		try {
			THttpClient transport = new THttpClient(DatahubDataModel.getDefaultDatahubHost());
			TBinaryProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client clnt = new DataHub.Client(protocol);

			ConnectionParams params = new ConnectionParams();
			params.setApp_id(DatahubDataModel.getKibitzAppName());
			params.setApp_token(DatahubDataModel.getKibitzAppId());
			params.setRepo_base(DatahubDataModel.getDefaultDatahubUsername());
			Connection connection = clnt.open_connection(params);
			while (true) {
				ResultSet result = clnt.execute_sql(connection, "SELECT * FROM kibitz_users.recommenders", null);

				if (result != null) {
					for (Tuple t : result.getTuples()) {
						List<ByteBuffer> cells = t.getCells();

						String database = new String(cells.get(0).array());
						String username = new String(cells.get(1).array());
						String ratings_table = new String(cells.get(3).array());
						boolean wroteRatings = Boolean.parseBoolean(new String (cells.get(9).array()));

						THttpClient transport_user = new THttpClient(DatahubDataModel.getDefaultDatahubHost());
						TBinaryProtocol protocol_user = new  TBinaryProtocol(transport_user);
						DataHub.Client client = new DataHub.Client(protocol_user);

						ConnectionParams kibitzUserParams = new ConnectionParams();
						kibitzUserParams.setApp_id(DatahubDataModel.getKibitzAppName());
						kibitzUserParams.setApp_token(DatahubDataModel.getKibitzAppId());
						kibitzUserParams.setRepo_base(username);
						Connection client_con = client.open_connection(kibitzUserParams);

						if (!wroteRatings) {
							String tableName = ratings_table.split("\\.")[1];
							try {
								BufferedWriter writer = new BufferedWriter(new FileWriter(getKibitzLocalStorageAddr() + username + "/" + database + "/" + tableName + "_ratings.csv"));
								writer.write("1,1,1\n");

								ResultSet count = client.execute_sql(client_con, "select count(*) from " + ratings_table + "_ratings", null);;
								int numItems = Integer.parseInt(new String(count.getTuples().get(0).getCells().get(0).array()));

								for (int i = 0; i < numItems; i += 10000) {
									ResultSet res = client.execute_sql(client_con, "SELECT * FROM " + ratings_table + "_ratings" +
										" LIMIT " + 10000 + " OFFSET " + i, null);
									for (Tuple tt : res.getTuples()) {
										List<ByteBuffer> c = tt.getCells();
										if (Long.parseLong(new String(c.get(0).array())) >= 0 && Long.parseLong(new String(c.get(1).array())) >= 0 && Long.parseLong(new String(c.get(2).array())) >= 0)
											writer.write(new String(c.get(0).array()) + "," + new String(c.get(1).array()) + "," + new String(c.get(2).array()) + "\n");
									}
								}
								writer.close();
								clnt.execute_sql(connection, "update kibitz_users.recommenders set wrote_ratings=true where username = '"
										+ username + "' and database = '" + database + "' and ratings_table = '" + tableName + "';", null);
							} catch (Exception e){
								System.out.println(e);
							}
						}
					}
				}
			}
		}  catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getKibitzLocalStorageAddr() {
		return KIBITZ_LOCAL_STORAGE_ADDR;
	}
}
