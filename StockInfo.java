/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*
*	Class to store a list of changes done to a certain stock
*/

import java.util.ArrayList;

public class StockInfo {

    private String stockName;
    private Float price;
    private ArrayList<BidInfo> bidInfoList;

    public StockInfo(String stockName, Float price){
        this.stockName = stockName;
        bidInfoList = new ArrayList<>();
		// We call this only once at the stage we read the data from the csv
        bidInfoList.add(new BidInfo("Original", price));
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public ArrayList<BidInfo> getBidInfoList() {
        return bidInfoList;
    }

    public void setBidInfoList(ArrayList<BidInfo> bidInfoList) {
        this.bidInfoList = bidInfoList;
    }
	
	// Need to get the latest price. ie, the last element of the BidInfo arraylist
    public Float getCurrentPrice(){
        return this.bidInfoList.get(this.bidInfoList.size()-1).getPrice();
    }
}
