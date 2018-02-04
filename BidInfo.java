/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*	
*	Class to store the userInformation (name and the bid price)
*/

public class BidInfo {

    private String user;
    private Float price;

    public BidInfo(String user, Float price){
        this.user = user;
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
