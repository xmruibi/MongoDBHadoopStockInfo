package com.mongo.utils;


import org.apache.hadoop.conf.Configuration;

import com.mongodb.hadoop.MongoConfig;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MapredMongoConfigUtil;
import com.mongodb.hadoop.util.MongoClientURIBuilder;
import com.mongodb.hadoop.util.MongoConfigUtil;
import com.mongodb.hadoop.util.MongoTool;

public class HadoopConnectorUtil extends MongoTool{

	public HadoopConnectorUtil() {
        Configuration conf = new Configuration();
        MongoConfig config = new MongoConfig(conf);
        setConf(conf);
        
        if (MongoTool.isMapRedV1()) {
            MapredMongoConfigUtil.setInputFormat(getConf(), com.mongodb.hadoop.mapred.MongoInputFormat.class);
            MapredMongoConfigUtil.setOutputFormat(getConf(), com.mongodb.hadoop.mapred.MongoOutputFormat.class);
        } else {
            MongoConfigUtil.setInputFormat(getConf(), MongoInputFormat.class);
            MongoConfigUtil.setOutputFormat(getConf(), MongoOutputFormat.class);
        }
        
        
        MongoClientURIBuilder urlbuiler = new MongoClientURIBuilder();
        urlbuiler.addHost("45.55.188.234" , 20000);
        urlbuiler.addHost("45.55.186.238 " , 20000);
        urlbuiler.addHost("104.131.106.22" , 20000);
        config.setInputURI(urlbuiler.build());
	}
}
