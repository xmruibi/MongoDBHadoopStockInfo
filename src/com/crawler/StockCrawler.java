package com.crawler;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.model.Quote;
import com.mongo.utils.JavaDriverUtil;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockCrawler {
	private static final Logger log = LogManager.getLogger(StockCrawler.class);
	private final Calendar from;
	private final Calendar to;

	public StockCrawler() {
		from = Calendar.getInstance();
		to = Calendar.getInstance();
		from.add(Calendar.YEAR, -10); // from 10 year ago
	}

	public Quote getHistQuotesBySymbol(String symbol) {
		try {
			Stock stock = YahooFinance.get(symbol);
			if (stock != null) {
				List<HistoricalQuote> quotes = stock.getHistory(from, to,
						Interval.DAILY);
				Quote quote = new Quote(symbol);
				quote.setQuotes(quotes);
				return quote;
			}
		} catch (Exception e) {
			log.error(e + " " + e.getCause());
		} 
		return null;
	}	
}
