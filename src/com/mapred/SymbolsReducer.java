package com.mapred;

import com.crawler.StockCrawler;
import com.google.gson.Gson;
import com.model.Quote;
import com.mongodb.DBObject;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.io.MongoUpdateWritable;
import com.mongodb.util.JSON;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;

public class SymbolsReducer extends Reducer<Text, IntWritable, NullWritable, MongoUpdateWritable>{

	
    @Override
    public void reduce(final Text pKey, final Iterable<IntWritable> pValues,
                        final Context pContext )
            throws IOException, InterruptedException{
    	    
        StockCrawler stockCrawler = new StockCrawler();
        
        // get symbol from keyIn 
        Quote quote = stockCrawler.getHistQuotesBySymbol(pKey.toString());
        if(quote==null||quote.getSymbolName()==null||quote.getHistorical_quotes()==null)
        	return;
        System.out.println(quote.getSymbolName()+" reducer getted!!!!!!!!!!");
        // get Quote info, set new id
        BasicBSONObject query = new BasicBSONObject("_id", quote.getKey());
       
        // set symbol name and symbol quotes
        BasicBSONObject stockQuote = new BasicBSONObject();
        stockQuote.put("symbol", quote.getSymbolName());
        ArrayList<Object> historical_quotes =  quote.getHistorical_quotes();
        
        BasicBSONObject update = new BasicBSONObject("$set", stockQuote);
        update.append("$pushAll", new BasicBSONObject("historical_quotes", historical_quotes));
        
        pContext.write(null, new MongoUpdateWritable(query, update, true, false));
    }
}
