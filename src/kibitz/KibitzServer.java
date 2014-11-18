package kibitz;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kibitz.RecommenderService.Iface;

import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class KibitzServer implements Iface {
	
	public static Map<String, IndividualRecommender> SESSIONS = new HashMap<String, IndividualRecommender>();
	public static Map<String, AbstractRecommender> RECOMMENDERS = new HashMap<String, AbstractRecommender>();
	public static boolean RUNNING = true;
	
	private MysqlDataSource dataSource;
	private DatahubDataModel dataModel = null;
	private Thread loop = null;
	
	public KibitzServer(MysqlDataSource dataSource) {
		this.dataSource = dataSource;
		
		// Start thread to continuously train recommenders
		if (this.loop == null) {
			this.loop = new Thread(new RecommenderRunnable());
			this.loop.start();
		}
	}
	
	@Override
	public void createNewIndividualServer(String key) {
		if (key != null) {
			if (SESSIONS.get(key) == null) {
				SESSIONS.put(key, IndividualRecommender.createNewIndividualServer(this.dataSource));
			}
		}
	}
	
	@Override
	public List<Item> makeRecommendation(String key, int userId, int numRecs) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).makeRecommendation(userId, numRecs);
			}
		}
		return null;
	}
	
	@Override
	public void initiateModel(String key, String table, String username, String password, String database) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.get(key).initiateModel(table, username, password, database);
			}
		}
	}
	
	@Override
    public boolean createNewRecommender(String username, String password, String database, String table) {
		try {
			this.dataModel = new DatahubDataModel(this.dataSource.getServerName(), database, 
				username,
				password,
				null);
			return this.dataModel.createNewRecommender(table);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
}
	
	@Override
	public List<Item> getUserRatedItems(String key, int userId) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getUserRatedItems(userId);
			}
		}
		return null;
    }
	
	@Override
	public boolean checkLogin(String key, String username, String password, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).checkLogin(username, password, isKibitzUser);
			}
		}
		return false;
	}
	
	@Override
	public boolean checkUsername(String key, String username, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).checkUsername(username, isKibitzUser);
			}
		}
		return false;
	}
	
	@Override
	public String createNewUser(String key, String username, String email, String password, boolean isKibitzUser) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).createNewUser(username, email, password, isKibitzUser);
			}
		}
		return null;
	}
	
	@Override
	public long retrieveUserId(String key, String username, String password) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).retrieveUserId(username, password);
			}
		}
		return 0;
	}
	
	@Override
	public List<Item> getItems(String key) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getItems();
			}
		}
		return null;
	}
	
	@Override
	public void recordRatings(String key, int userId, int itemId, int rating) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.get(key).recordRatings(userId, itemId, rating);;
			}
		}
	}
	
	@Override
	public void deleteRatings(String key, int userId, int itemId) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.get(key).deleteRatings(userId, itemId);
			}
		}
	}
	
	@Override
	public List<Item> getPageItems(String key, int page, int numPerPage) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getPageItems(page, numPerPage);
			}
		}
		return null;
	}

	@Override
	public int getItemCount(String key) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				return SESSIONS.get(key).getItemCount();
			}
		}
		return 0;
	}
	
	@Override
	public void terminateSession(String key) {
		if (key != null) {
			if (SESSIONS.get(key) != null) {
				SESSIONS.remove(key);
			}
		}
	}
	
	public class RecommenderRunnable implements Runnable {	
		public void run() {
			while (RUNNING) {
				if (RECOMMENDERS.size() != 0) {
					for(String cur_key: RECOMMENDERS.keySet()) {
						updateRecommender(RECOMMENDERS.get(cur_key));
					}
				}
			}
		}
		
		private void updateRecommender(AbstractRecommender recommender) {
			//recommender.clear();
			recommender.refresh(null);
		}
	}
}
