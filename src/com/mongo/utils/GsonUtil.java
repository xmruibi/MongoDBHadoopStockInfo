package com.mongo.utils;


import yahoofinance.histquotes.HistoricalQuote;

import com.google.gson.Gson;
import com.model.Quote;

/**
 * This class is for Conversions between JSON and Java Object
 * @author birui
 *
 */
public class GsonUtil {
	public static String HistoricalQuotetoJson(HistoricalQuote quote) {
		Gson gson = new Gson();
		quote.setSymbol(null);
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
