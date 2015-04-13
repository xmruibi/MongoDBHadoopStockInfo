package com.mongo.utils;

import org.apache.hadoop.conf.Configuration;

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

	public HadoopConnectorUtil() {
		Configuration conf = new Configuration();
		MongoConfig config = new MongoConfig(conf);
		setConf(conf);

		if (MongoTool.isMapRedV1()) {
			MapredMongoConfigUtil.setInputFormat(getConf(),
					com.mongodb.hadoop.mapred.MongoInputFormat.class);
			MapredMongoConfigUtil.setOutputFormat(getConf(),
					com.mongodb.hadoop.mapred.MongoOutputFormat.class);
		} else {
			MongoConfigUtil.setInputFormat(getConf(), MongoInputFormat.class);
			MongoConfigUtil.setOutputFormat(getConf(), MongoOutputFormat.class);
		}

		MongoClientURIBuilder inputURL = new MongoClientURIBuilder();
		inputURL.addHost(host1, 27017);
		inputURL.addHost(host2, 27017);
		inputURL.addHost(host3, 27017);
		inputURL.collection("stock", "symbols");

		config.setOutputURI(inputURL.build());
	}
}
