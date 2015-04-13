package com.mapred;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongo.utils.JavaDriverUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.hadoop.util.MongoTool;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MapredMongoConfigUtil;
import com.mongodb.hadoop.util.MongoClientURIBuilder;
import com.mongodb.hadoop.util.MongoConfigUtil;
import com.mongodb.hadoop.util.MongoTool;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MongoMapredStockCrawler extends MongoTool {

	private static final Logger log = LogManager
			.getLogger(MongoMapredStockCrawler.class);

	public MongoMapredStockCrawler() {
		setConf(new Configuration());

		if (MongoTool.isMapRedV1()) {
			MapredMongoConfigUtil.setInputFormat(getConf(),
					com.mongodb.hadoop.mapred.MongoInputFormat.class);
			MapredMongoConfigUtil.setOutputFormat(getConf(),
					com.mongodb.hadoop.mapred.MongoOutputFormat.class);
		} else {
			MongoConfigUtil.setInputFormat(getConf(), MongoInputFormat.class);
			MongoConfigUtil.setOutputFormat(getConf(), MongoOutputFormat.class);
		}

		MongoClientURIBuilder uriBuilder = ShardedDBURIBuilder();
		uriBuilder.collection("stock", "symbols");
		MongoClientURI inputURI = uriBuilder.build();
		uriBuilder.collection("stock", "quotes");
		MongoClientURI outputURI = uriBuilder.build();

		MongoConfigUtil.setInputURI(getConf(), inputURI);
		MongoConfigUtil.setOutputURI(getConf(), outputURI);

		MongoConfigUtil.setMapper(getConf(), SymbolsMapper.class);
		MongoConfigUtil.setReducer(getConf(), SymbolsReducer.class);

		MongoConfigUtil.setMapperOutputKey(getConf(), Text.class);
		MongoConfigUtil.setMapperOutputValue(getConf(), IntWritable.class);

		MongoConfigUtil.setOutputKey(getConf(), IntWritable.class);
		MongoConfigUtil.setOutputValue(getConf(), BSONWritable.class);
	}

	private MongoClientURIBuilder ShardedDBURIBuilder() {
		List<ServerAddress> serverSeeds;
		MongoClient mongoClient = null;
		serverSeeds = new ArrayList<ServerAddress>();
		try {
			serverSeeds.add(new ServerAddress("45.55.186.238", 27017));
			serverSeeds.add(new ServerAddress("104.131.106.22", 27017));
			mongoClient = new MongoClient(serverSeeds);
			mongoClient.getMongoClientOptions();
		} catch (UnknownHostException e) {
			log.error(e + "" + e.getCause());
		}
		String mainHost = "45.55.188.234";
		MongoClientURIBuilder uriBuilder = new MongoClientURIBuilder();
		uriBuilder.addHost(mainHost, 27017);
		uriBuilder.options(mongoClient.getMongoClientOptions());
		return uriBuilder;
	}

	public static void main(final String[] args) throws Exception {
		System.exit(ToolRunner.run(new MongoMapredStockCrawler(), args));
	}
}
