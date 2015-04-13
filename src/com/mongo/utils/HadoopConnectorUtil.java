package com.mongo.utils;

import org.apache.hadoop.conf.Configuration;

import com.mongodb.MongoClientURI;
import com.mongodb.hadoop.MongoConfig;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MapredMongoConfigUtil;
import com.mongodb.hadoop.util.MongoClientURIBuilder;
import com.mongodb.hadoop.util.MongoConfigUtil;
import com.mongodb.hadoop.util.MongoTool;

public class HadoopConnectorUtil extends MongoTool {

	private static final String host1 = "45.55.188.234";
	private static final String host2 = "45.55.186.238";
	private static final String host3 = "104.131.106.22";
	private static Configuration conf;
	private static MongoConfig config;

	public HadoopConnectorUtil() {
		conf = new Configuration();
		setConf(conf);
		config = new MongoConfig(conf);


		MongoClientURIBuilder uriBuilder = new MongoClientURIBuilder();
		uriBuilder.addHost(host1, 27017);
		uriBuilder.addHost(host2, 27017);
		uriBuilder.addHost(host3, 27017);
		uriBuilder.collection("stock", "symbols");
		MongoClientURI inputURI = uriBuilder.build();
		uriBuilder.collection("stock", "quotes");
		MongoClientURI outputURI = uriBuilder.build();

		config.setInputURI(inputURI);
		config.setOutputURI(outputURI);
	}
}
