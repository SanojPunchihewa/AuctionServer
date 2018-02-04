/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*
*	Creates an object of this class when a new user is connecting
*/

import java.io.*;
import java.net.Socket;

public class CommunicationServer extends Thread {

    private Socket commSocket;

    private CheckStock checkStock;
	
	// Declare a state so we can track the stage the user is at
    private int state;
    private static int login = 0;
    private static int requestStock = 1;
    private static int bidStock = 2;
    private static int endSession = 3;

    private static String welcomeMsg = "Welcome to Auction Server ! \nInput UserName : ";
    private static String requestSymbol = "Input the Symbol of the Stock\n";
    private static String requestNewBid = "Input your new Bid\n";

    private String userName;
    private String stockSymbol;

    public CommunicationServer(Socket socket, CheckStock checkStock){
        this.commSocket = socket;
        this.checkStock = checkStock;
        this.state = login;
    }

    public void run(){

        try {
			// Read the input given through the terminal
            BufferedReader reader = new BufferedReader(new InputStreamReader(commSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(commSocket.getOutputStream()));

            writer.write(welcomeMsg);	// Print welcome Message
            writer.flush();

            String line;

            for(line = reader.readLine(); line != null && !line.equals("quit"); line = reader.readLine()) {
                if(state == login) {
					// Initial state where user input name and log into the server
                    userName = line;
                    state = requestStock;
                    System.out.println("New User : " + userName);
                    line = requestSymbol;
                }else if(state == requestStock) {
					// request for a symbol and check availabilty
                    System.out.print("Check stock... : ");
                    if(checkStock.isAvailable(line)){
                        state = bidStock;
                        stockSymbol = line;
						float stockPrice = checkStock.getStockPrice(stockSymbol);
                        line = "Current Price = " + stockPrice + "\n" + requestNewBid;
                        System.out.println("Found " + stockSymbol + " | " + checkStock.getStockPrice(stockSymbol));
                    }else {
                        line = "-1\n";
                        System.out.println("N/A " + stockSymbol);
                    }
                }else if(state == bidStock){
					// final state where use bid for the stock
                    //state = endSession;
                    float price = Float.valueOf(line);					
					// check wether the bid is valid
					if(price < checkStock.getStockPrice(stockSymbol)){
						line = "Place a bid higher than Current Price\n";
					}else{
						checkStock.updateStockPrice(stockSymbol, userName, price);
						System.out.println("Stock updated... | user = " + userName  + " | stock = " + stockSymbol + " | price = " + price);
						line = "Stock updated...\nInput a new Bid or type quit to exit\n";
					}
					
                }

                writer.write(line);
                writer.flush();

                //if(state == endSession) break;

            }

        }catch (IOException e){
            System.out.println(e);
        }
        try {
            this.commSocket.close();
        }catch (IOException e){
            System.out.println("Err Closing Connection" + e);
        }
    }

}
