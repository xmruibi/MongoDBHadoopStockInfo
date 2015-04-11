package com.crawler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SymbolReader {

	private final static String FILE = "symbolList";

	public static List<String> getLocalSymbol() {

		List<String> stockList = new ArrayList();
		Document doc;
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE));
			String symbol = null;
			while ((symbol = br.readLine()) != null) {
				stockList.add(symbol);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return stockList;
	}

	public static List<String> getLocalSymbol(int count) {

		List<String> stockList = new ArrayList();

		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE));
			String symbol = null;
			int i = 0;
			while (i < count && (symbol = br.readLine()) != null) {
				stockList.add(symbol);
				i++;
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return stockList;
	}

}
