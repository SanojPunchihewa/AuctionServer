# CO225 Project 2 AuctionServer

The users can connect to the server using 'telnet' command and bid on the stocks. The program use threads and sockets to deal with the connections. The GUI shows the details(name, current price, history) of the stocks

### HOW TO USE

#### Step 1:
Run the AuctionServer >$java AuctionServer

#### Step 2:
Use nc to connect to the Server
>$nc localhost 2000

#### Step 3:
Server will request to input the userName, give your username here
>Welcome to Auction Server !
>Input UserName : <type your userName here> and Press Enter

#### Step 4:
Next it will request the stock symbol
Give the symbol and Press Enter

#### Step 5:
if we have that symbol in our system the server will give you the current price of the stock,
Now you can place your bid and press Enter.
If the requested symbol is not there server will print -1,
now you can again input a valid symbol

#### Step 6:
If the stock was updated the server will display it to you.
Now you can bid again to the same symbol (stock) or type "quit" to exit


#### Note

if we need to check the changes happend to the stocks,
type the symbol in the textbox in the AuctionServer GUI and press "Search".
This will display all the changes
