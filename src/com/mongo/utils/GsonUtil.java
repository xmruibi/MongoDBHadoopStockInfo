package com.mongo.utils;

import org.apache.commons.codec.net.QuotedPrintableCodec;

import yahoofinance.histquotes.HistoricalQuote;

import com.google.gson.Gson;
import com.model.Quote;


public class GsonUtil {
	public static String HistoricalQuotetoJson(HistoricalQuote quote) {
		Gson gson = new Gson();
		String json = gson.toJson(quote);
		return json;
	}
	
	public static HistoricalQuote JsontoQuote(String json) {
		Gson gson = new Gson();
		HistoricalQuote quote = gson.fromJson(json, HistoricalQuote.class);
		return quote;
	}
	
	public static String QuotetoJson(Quote quote) {
		Gson gson = new Gson();
		String json = gson.toJson(quote);
		return json;
	}

}
