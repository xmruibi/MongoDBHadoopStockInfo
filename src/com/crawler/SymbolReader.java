package com.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.model.Symbol;
import com.mongo.utils.JavaDriverUtil;


/**
 * Symbols reader for stock info crawler
 * 
 * @author birui
 *
 */
public class SymbolReader {

	private final static String FILE = "symbolList";

	public static List<String> getLocalSymbol() {
		List<String> stockList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE));
			String symbol = null;
			while ((symbol = br.readLine()) != null) {
				stockList.add(symbol);
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return stockList;
	}

	public static List<String> getLocalSymbol(int count) {
		List<String> stockList = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE));
			String symbol = null;
			int i = 0;
			while (i < count && (symbol = br.readLine()) != null) {
				stockList.add(symbol);
				i++;
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return stockList;
	}

//	// insert symbol list into mongodb
//	public static void main(String[] args) {
//		JavaDriverUtil mongodriver = new JavaDriverUtil();
//		mongodriver.connDB("stock", "symbols");
//		for(String symbol:getLocalSymbol(100)){
//			mongodriver.insertDB(new Symbol(symbol));
//			System.out.println(symbol+" inserted!");
//		}
//		mongodriver.close();
//	}
}
