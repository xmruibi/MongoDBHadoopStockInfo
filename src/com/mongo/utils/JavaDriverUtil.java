package com.mongo.utils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import yahoofinance.histquotes.HistoricalQuote;

import com.model.Quote;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class JavaDriverUtil {
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
	 * Input database name
	 * 
	 * @param dbName
	 */
	public void connDB(String dbName, String clctName) {
		database = mongoClient.getDB(dbName);
		collection = database.getCollection(clctName);
	}

	public void writeHistoricalQuotes(Quote quote) {
		if (quote == null||quote.getQuotes().size()==0)
			return;
			BasicDBObject obj = (BasicDBObject) JSON.parse(GsonUtil
					.QuotetoJson(quote));
			collection.insert(obj);

	}

	public void close() {
		mongoClient.close();
	}
}
