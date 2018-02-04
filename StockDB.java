/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*
*	Class defined to store the information about stock symbol, name, price
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StockDB implements CheckStock {

    private HashMap<String, StockInfo> stockMap = new HashMap<>();

    public StockDB(String csvFile){
        // populate the database with the original Values
        try {

            FileReader fileReader = new FileReader(csvFile);
            BufferedReader reader = new BufferedReader(fileReader);

            String header = reader.readLine();		// raed the header pf the csv file

            String stockInfo[];  // array to hold [symbol, securityNum, originalPrice]
            for (String line = reader.readLine(); line!= null; line = reader.readLine()){
                stockInfo = line.split(",");
				// check wether the price is there (valid) or not using a try-catch
                try {
                    float price = Float.valueOf(stockInfo[6]);
                    stockMap.put(stockInfo[0], new StockInfo(stockInfo[1], price));
                }catch (NumberFormatException e){
                    System.out.println("Stock Price for : " + stockInfo[1] + " is N/A");
                }

            }

            if (fileReader != null) fileReader.close();
            if (reader != null) reader.close();

        }catch (IOException e){
            System.out.println("Err while reading : " + e);
        }
    }
	
	// Define the functions we got from the interface
	
    @Override
    public synchronized void updateStockPrice(String symbol, String user, Float price){
        ArrayList<BidInfo> history = stockMap.get(symbol).getBidInfoList();
        BidInfo bid = new BidInfo(user, price);
        history.add(bid);
        stockMap.get(symbol).setBidInfoList(history);
    }

    @Override
    public ArrayList<BidInfo> getBidHistory(String symbol) {
        return stockMap.get(symbol).getBidInfoList();
    }

    @Override
    public boolean isAvailable(String symbol) {
        return stockMap.containsKey(symbol);
    }

    @Override
    public String getSecurityName(String symbol) {
        return stockMap.get(symbol).getStockName();
    }

    @Override
    public Float getStockPrice(String symbol) {
        return stockMap.get(symbol).getCurrentPrice();
    }
}
