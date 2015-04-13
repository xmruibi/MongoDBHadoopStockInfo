package com.mapred;

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

public class MongoMapredStockCrawler extends MongoTool{
	private static final String host1 = "45.55.188.234";
	private static final String host2 = "45.55.186.238";
	private static final String host3 = "104.131.106.22";
	public MongoMapredStockCrawler() {
		 setConf(new Configuration());

	        if (MongoTool.isMapRedV1()) {
	            MapredMongoConfigUtil.setInputFormat(getConf(), com.mongodb.hadoop.mapred.MongoInputFormat.class);
	            MapredMongoConfigUtil.setOutputFormat(getConf(), com.mongodb.hadoop.mapred.MongoOutputFormat.class);
	        } else {
	            MongoConfigUtil.setInputFormat(getConf(), MongoInputFormat.class);
	            MongoConfigUtil.setOutputFormat(getConf(), MongoOutputFormat.class);
	        }
	        
			MongoClientURIBuilder inputURL = new MongoClientURIBuilder();
			inputURL.addHost(host1, 27017);
			inputURL.addHost(host2, 27017);
			inputURL.addHost(host3, 27017);
			inputURL.collection("stock", "symbols");
			MongoClientURIBuilder outputURL = new MongoClientURIBuilder();
			outputURL.addHost(host1, 27017);
			outputURL.addHost(host2, 27017);
			outputURL.addHost(host3, 27017);
			outputURL.collection("stock", "quotes");
	        MongoConfigUtil.setInputURI(getConf(), "mongodb://45.55.186.238:27017/stock.symbols");
	        MongoConfigUtil.setOutputURI(getConf(), "mongodb://45.55.188.234:27017/stock.quotes");

	        MongoConfigUtil.setMapper(getConf(), SymbolsMapper.class);
	        MongoConfigUtil.setReducer(getConf(), SymbolsReducer.class);
	        
	        MongoConfigUtil.setMapperOutputKey(getConf(), Text.class);
	        MongoConfigUtil.setMapperOutputValue(getConf(), IntWritable.class);
	        
	        MongoConfigUtil.setOutputKey(getConf(), IntWritable.class);
	        MongoConfigUtil.setOutputValue(getConf(), BSONWritable.class);
	}
	
	public static void main(final String[] args) throws Exception{
		System.exit(ToolRunner.run(new MongoMapredStockCrawler(), args));
	}
}
