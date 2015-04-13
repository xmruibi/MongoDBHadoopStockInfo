package com.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Create Quote Object for setting each document in MongoDB as one key, One
 * Symbol, and its historical quotes The key is built by hashCode which is using
 * the fast MPQ hashCode generation algorithm and it also benefit for sharding;
 * 
 * Thing is still need to be improved since current code has some duplicate in
 * quotes list, like symbol name appended every quotes list item; Have tried to
 * set the null but it cost O(n) time for iterate.
 * 
 * @author birui
 *
 */
public class Quote {

	private transient final int key;
	private final String symbolName;
	private final ArrayList<Object> historical_quotes;
	// reference table for hashing
	private transient int[] cryptTable;

	// a randomized value applied to MPQ hash
	// value falls between 0 to 3
	private transient float hashSeed;

	public Quote(String symbolName) {
		initTable();
		this.symbolName = symbolName;
		this.key = hashCode();
		this.historical_quotes = new ArrayList<Object>();
	}

	public void setQuotes(List<HistoricalQuote> quotes) {
		if (quotes == null || quotes.size() == 0) {
			DBObject historical_quote = new BasicDBObject();
			historical_quote.put("date", "N/A");
			historical_quote.put("open", "N/A");
			historical_quote.put("low", "N/A");
			historical_quote.put("high", "N/A");
			historical_quote.put("close", "N/A");
			historical_quote.put("adjClose", "N/A");
			historical_quote.put("volume", "N/A");
			historical_quotes.add(historical_quote);
		} else {
			for (HistoricalQuote quote : quotes) {
				DBObject historical_quote = new BasicDBObject();
				historical_quote.put("date", DateSerializer(quote.getDate()
						.getTime()));
				historical_quote.put("open", quote.getOpen().toString());
				historical_quote.put("low", quote.getLow().toString());
				historical_quote.put("high", quote.getHigh().toString());
				historical_quote.put("close", quote.getClose().toString());
				historical_quote
						.put("adjClose", quote.getAdjClose().toString());
				historical_quote.put("volume", quote.getVolume());
				historical_quotes.add(historical_quote);
			}
		}
	}

	private String DateSerializer(Date d) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		return format.format(d);
	}

	public int getKey() {
		return key;
	}

	public String getSymbolName() {
		return symbolName;
	}

	public ArrayList<Object> getHistorical_quotes() {
		return historical_quotes;
	}

	/************************* Blizzard MPQ Hash Code Generate Algorithm **************************/
	private float getRandom() {
		Random ran = new Random();
		return ran.nextFloat() * 3.f;
	}

	/**
	 * initialize hash seed and cryptTable
	 */
	private void initTable() {
		this.hashSeed = getRandom();
		initCryptTable();
	}

	private void initCryptTable() {
		this.cryptTable = new int[0x500];
		int seed = 0x00100001;
		int index1 = 0, index2 = 0, i;
		for (index1 = 0; index1 < 0x100; index1++) {
			for (index2 = index1, i = 0; i < 5; i++, index2 += 0x100) {
				int temp1, temp2;
				seed = (seed * 125 + 3) % 0x2AAAAB;
				temp1 = (seed & 0xFFFF) << 0x10;
				seed = (seed * 125 + 3) % 0x2AAAAB;
				temp2 = (seed & 0xFFFF);
				cryptTable[index2] = (temp1 | temp2);
			}
		}
	}

	@Override
	/**
	 * Uses MPQ hash algorithm to generate a long type variable as hashed value.
	 * @return hashCode
	 */
	public int hashCode() {
		int seed1 = 0x7FED7FED, seed2 = 0xEEEEEEEE;
		for (int i = 0; i < symbolName.length(); i++) {
			int ch = symbolName.charAt(i);
			seed1 = cryptTable[(int) (hashSeed * (1 << 8)) + ch]
					^ (seed1 + seed2);
			seed2 = ch + seed1 + seed2 + (seed2 << 5) + 3;
		}
		return seed1;
	}
}
