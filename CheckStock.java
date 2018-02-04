/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*
*	Interface to use in our database
*/

import java.util.ArrayList;

interface CheckStock {

    public boolean isAvailable(String symbol);
    public String getSecurityName(String symbol);
    public Float getStockPrice(String symbol);
    public void updateStockPrice(String symbol, String user, Float price);
    public ArrayList<BidInfo> getBidHistory(String symbol);
}
