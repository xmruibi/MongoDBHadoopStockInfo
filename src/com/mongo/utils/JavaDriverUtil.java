package com.mongo.utils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.model.Quote;
import com.model.Symbol;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;


/**
 * This class is utility for connecting remote distributed sharding MongoDB
 * @author birui
 *
 */
public class JavaDriverUtil {
	// list of remote mongodb servers
	private final List<ServerAddress> serverSeeds;
	
	private MongoClient mongoClient = null;
	private DB database = null;
	private DBCollection collection = null;

	private static final Logger log = LogManager
			.getLogger(JavaDriverUtil.class);

	public JavaDriverUtil() {
		serverSeeds = new ArrayList<ServerAddress>();
		try {
			serverSeeds.add(new ServerAddress("45.55.188.234", 27017));
			serverSeeds.add(new ServerAddress("45.55.186.238", 27017));
			serverSeeds.add(new ServerAddress("104.131.106.22", 27017));
			mongoClient = new MongoClient(serverSeeds);
		} catch (UnknownHostException e) {
			log.error(e + "" + e.getCause());
		}
	}

	/**
	 * Input database name and collection name
	 * @param dbName
	 * @param clctName
	 */
	public void connDB(String dbName, String clctName) {
		database = mongoClient.getDB(dbName);
		collection = database.getCollection(clctName);
	}


	/**
	 * Insert Database
	 * @param object
	 */
	public void insertDB(Object object) {
		if(object==null||((object instanceof Collection<?>&&((Collection) object).size()==0)))
			return;
		Gson gson = new Gson();
		String json = gson.toJson(object);
		
		BasicDBObject obj = (BasicDBObject) JSON.parse(json);
		collection.insert(obj);
	}
//	/**
//	 * Write Quote object and convert to JSON
//	 * @param quote
//	 */
//	public void writeHistoricalQuotes(Quote quote) {
//		if (quote == null||quote.getQuotes().size()==0)
//			return;
//			BasicDBObject obj = (BasicDBObject) JSON.parse(GsonUtil.QuotetoJson(quote));
//			collection.insert(obj);
//
//	}

	/**
	 * DB connection close method
	 */
	public void close() {
		mongoClient.close();
	}
}
