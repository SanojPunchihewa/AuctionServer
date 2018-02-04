/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*	use nc to connect to the Server and Bid
*	Main Class
*/


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AuctionServer {

    private static ServerSocket serverSocket;
    private StockDB stockdb;
	private static final int PORT = 2000;					// Port used to connect to the server
	private static String fileName = "stocks.csv";			// file name which contain the stock details

    public AuctionServer(int socket, String file) throws IOException{
        serverSocket = new ServerSocket(socket);
        stockdb = new StockDB(file);
        new GUI(stockdb);
    }

    public void startServer() throws IOException {
        while(true) {
			// create new instance of CommunicationServer when a new client trys to connect
            Socket socket = serverSocket.accept();
            CommunicationServer commServer = new CommunicationServer(socket, stockdb);
            commServer.start();
        }
    }

    public static void main(String [] args) throws IOException {
		// Start our AuctionServer
        AuctionServer server = new AuctionServer(PORT, fileName);
        server.startServer();
    }

}
