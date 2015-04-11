package com.model;
import java.util.List;
import java.util.Random;

import yahoofinance.histquotes.HistoricalQuote;

public class Quote {

	private final int key;
	private final String symbol;
	private List<HistoricalQuote> quotes;

	
	
	/**
	 * Cache hashCode for string
	 */
	private int hash;

	// reference table for hashing
	private int[] cryptTable;

	// a randomized value applied to MPQ hash
	// value falls between 0 to 3
	private float hashSeed;
	
	
	
	public Quote(String symbol) {
		initTable();
		this.symbol = symbol;
		this.key = hashCode();
	}
	

	

	public void setQuotes(List<HistoricalQuote> quotes) {
		this.quotes = quotes;
	}

	public String getSymbol() {
		return symbol;
	}

	public List<HistoricalQuote> getQuotes() {
		return quotes;
	}

	
	
	/*************************Blizzard MPQ Hash Code Generate Algorithm **************************/
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
		for (int i = 0; i < symbol.length(); i++) {
			int ch = symbol.charAt(i);
			seed1 = cryptTable[(int) (hashSeed * (1 << 8)) + ch]
					^ (seed1 + seed2);
			seed2 = ch + seed1 + seed2 + (seed2 << 5) + 3;
		}
		hash = seed1;
		return seed1;
	}
	
	
	
	
	@Override
	public String toString() {
		return "id"+key+", symbol:"+symbol+",  historicalQuotes:"+quotes;
	}
}
