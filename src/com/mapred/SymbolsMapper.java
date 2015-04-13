	package com.mapred;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.bson.BSONObject;
import org.bson.types.ObjectId;

public class SymbolsMapper extends Mapper<Object, BSONObject, Text, IntWritable>{
    @Override
    public void map(Object key, BSONObject val, final Context context) 
        throws IOException, InterruptedException {
    	System.out.println(val.get("symbol")+"  Mapper Getted!!");
        context.write(new Text((val.get("symbol")).toString()), 
        		new IntWritable(1));
    }
}
