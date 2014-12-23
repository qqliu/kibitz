package updates;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.HOURS;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	private static final String KIBITZ_LOCAL_STORAGE_ADDR = "/Users/qliu/Documents/kibitz/BX-CSV-Dump/";
	
	public static void main(String[] args) throws Exception {
		class GetDataTask extends TimerTask {
			public void run() {
				THttpClient transport;
				try {
					transport = new THttpClient(DatahubDataModel.getDefaultDatahubHost());
					TBinaryProtocol protocol = new  TBinaryProtocol(transport);
					DataHub.Client clnt = new DataHub.Client(protocol);
	
					ConnectionParams params = new ConnectionParams();
					params.setUser(DatahubDataModel.getDefaultDatahubUsername());
					params.setPassword(DatahubDataModel.getDefaultDatahubPassword());
					Connection connection = clnt.open_connection(params);
					
					ResultSet result = clnt.execute_sql(connection, "SELECT * FROM kibitz_users.users", null);
					
					if(result != null) {
						for (Tuple t : result.getTuples()) {
							List<ByteBuffer> cells = t.getCells();
							
							String database = new String(cells.get(0).array());
							String username = new String(cells.get(1).array());
							String password = new String(cells.get(2).array());
							String ratings_table = new String(cells.get(3).array());
							
							THttpClient transport_user = new THttpClient(DatahubDataModel.getDefaultDatahubHost());
							TBinaryProtocol protocol_user = new  TBinaryProtocol(transport_user);
							DataHub.Client client = new DataHub.Client(protocol_user);
							
							ConnectionParams kibitzUserParams = new ConnectionParams();
							kibitzUserParams.setUser(username);
							kibitzUserParams.setPassword(password);
							Connection client_con = client.open_connection(kibitzUserParams);
							
							FileWriter writer = new FileWriter(KIBITZ_LOCAL_STORAGE_ADDR + database + "/" + ratings_table + ".csv"); 
							
							ResultSet count = client.execute_sql(client_con, "select count(*) from " + ratings_table, null);;
							int numItems = Integer.parseInt(new String(count.getTuples().get(0).getCells().get(0).array()));
									
							for (int i = 0; i < numItems; i += 10000) {
								ResultSet res = client.execute_sql(client_con, "SELECT * FROM " + database + "." + ratings_table + 
										" LIMIT " + 10000 + " OFFSET " + i, null);
								for (Tuple tt : res.getTuples()) {
									List<ByteBuffer> c = tt.getCells();
									writer.write(new String(c.get(0).array()) + "," + new String(c.get(1).array()) + "," + new String(c.get(2).array()) + "\n");
								}
							}
						}
					}
				} catch (DBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		Timer timer = new Timer();
		timer.schedule(new GetDataTask(), MILLISECONDS.convert(3, HOURS), MILLISECONDS.convert(1, DAYS));
	}
}
