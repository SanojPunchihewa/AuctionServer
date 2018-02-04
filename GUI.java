/*
*	CO225 Project 2
*	AuctionServer	
*	Simple Stock Server Program
*
*	Graphical Interface of the server
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {
	
	// define the Symbols need to be dispalyed
    private static String[] symbols = {"FB" , "VRTU",
            "MSFT", "GOOGL", "YHOO", "XLNX", "TSLA", "TXN"};

    private JTable tableBidInfo;
    private StockDB db;
	
	// define colors
    private Color colorBg = new Color(33, 33, 33);
    private Color colorGridColor = new Color(50, 50, 50);
    private Color colorHeaderText = new Color(18, 186, 129);
    private Color colorBidInfoText = new Color(20, 182, 180);
    private Color colorTitle = new Color(13, 206, 131);

    public GUI(StockDB db){
        super("Stock Server");							// set a name
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 520));		// set width and height of the GUI
        setPreferredSize(new Dimension(1000, 520));
        setLocationRelativeTo(null);
        this.db = db;

        addComponents(this);							// populate the components

        pack();
        setVisible(true);								// display the GUI

    }

    private void addComponents(JFrame frame){

        // Add the Title Panel

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(colorBg);
        JLabel labelHeader = new JLabel("Stock Server");
        labelHeader.setForeground(colorTitle);
        labelHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
        panelHeader.add(labelHeader);
        frame.add(panelHeader, BorderLayout.PAGE_START);


        // Add the Bid Price Table Panel

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(colorBg);

        JLabel labelTable = new JLabel("Current Status of Stocks");
        labelTable.setPreferredSize(new Dimension(100, 50));
        labelTable.setHorizontalAlignment(SwingConstants.CENTER);
        labelTable.setForeground(Color.WHITE);

        panelTable.add(labelTable, BorderLayout.NORTH);

        DefaultTableModel modelBidInfo = new DefaultTableModel(){
            // Remove the editing feature of rows
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableBidInfo = new JTable(modelBidInfo);

        modelBidInfo.addColumn("Symbol");
        modelBidInfo.addColumn("Name");
        modelBidInfo.addColumn("Price");
				
		// populate the table with default values for the first time
        for(int i = 0; i < 8; i++){
            addTableRow(modelBidInfo, symbols[i], db.getSecurityName(symbols[i]), String.valueOf(db.getStockPrice(symbols[i])));
        }
		
		// customize the table
        tableBidInfo.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableBidInfo.getColumnModel().getColumn(1).setPreferredWidth(280);
        tableBidInfo.getColumnModel().getColumn(2).setPreferredWidth(80);

        tableBidInfo.setGridColor(colorBg);
        tableBidInfo.setRowHeight(40);
        tableBidInfo.setBackground(colorBg);
        tableBidInfo.setForeground(colorBidInfoText);
        tableBidInfo.setGridColor(colorGridColor);
        tableBidInfo.setFocusable(false);
        tableBidInfo.setRowSelectionAllowed(false);

        tableBidInfo.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        tableBidInfo.getTableHeader().setForeground(colorHeaderText);
        tableBidInfo.getTableHeader().setBackground(colorBg);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tableBidInfo);
        scrollPane.getViewport().setBackground(colorBg);

        panelTable.add(scrollPane);

        add(panelTable, BorderLayout.CENTER);

        updateTable();		// Timer function which triggers every 500ms to dispaly changes in prices

        // Add a buttom Panel

        JPanel panelButtom = new JPanel(new FlowLayout());
        panelButtom.setBackground(colorBg);
        panelButtom.setPreferredSize(new Dimension(1000, 48));

        frame.add(panelButtom, BorderLayout.PAGE_END);

        // Add Search Panel

        JPanel panelHistory = new JPanel(new BorderLayout());
        panelHistory.setBackground(colorBg);

        JLabel labelHistory = new JLabel("Bid History");
        labelHistory.setForeground(Color.WHITE);
        labelHistory.setHorizontalAlignment(SwingConstants.CENTER);
        labelHistory.setPreferredSize(new Dimension(300, 45));

        panelHistory.add(labelHistory, BorderLayout.NORTH);

        JPanel panelSearch = new JPanel(new FlowLayout());
        panelSearch.setBackground(colorBg);

        JTextField inputSearchName = new JTextField();
        inputSearchName.setPreferredSize(new Dimension(180, 25));
        inputSearchName.setBackground(new Color(58, 58, 58));
        inputSearchName.setBorder(BorderFactory.createEmptyBorder());
        inputSearchName.setForeground(Color.WHITE);
        inputSearchName.setCaretColor(Color.cyan);

        JButton btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(100, 25));
        btnSearch.setBackground(colorHeaderText);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);

        panelSearch.add(inputSearchName);
        panelSearch.add(btnSearch);

        JPanel panelHistoryCenter = new JPanel(new BorderLayout());
        panelHistoryCenter.add(panelSearch, BorderLayout.NORTH);
        panelHistoryCenter.setBackground(colorBg);

        JLabel labelCurrentPrice = new JLabel("");
        labelCurrentPrice.setBackground(colorBg);
        labelCurrentPrice.setForeground(colorBidInfoText);
        labelCurrentPrice.setHorizontalAlignment(SwingConstants.CENTER);

        panelHistoryCenter.add(labelCurrentPrice, BorderLayout.CENTER);

        DefaultTableModel modelHistory = new DefaultTableModel();

        JTable tableHistory = new JTable(modelHistory);

        modelHistory.addColumn("User Name");
        modelHistory.addColumn("Bid Offer");
		
		// display a list of changes took place on stocks when a Search is done
        btnSearch.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search = inputSearchName.getText();
				// if we have the stock display the details
				// else hide the table and display error
                if(db.isAvailable(search)){

                    String currentPrice = String.valueOf(db.getStockPrice(search));

                    labelCurrentPrice.setText("Current Price of " + search + " is " + currentPrice);
                    labelCurrentPrice.setForeground(colorBidInfoText);

                    ArrayList<BidInfo> bidHistory = db.getBidHistory(search);
					
					// get the current no.of rows of the table and add the new ones only
                    int countRows = tableHistory.getRowCount();

                    for(int i = countRows; i < bidHistory.size(); i++){
                        addTableRow(modelHistory, bidHistory.get(i).getUser(), String.valueOf(bidHistory.get(i).getPrice()), "");
                    }

                    tableHistory.setVisible(true);

                }else{
                    labelCurrentPrice.setText("Symbol Not Found !");
                    labelCurrentPrice.setForeground(Color.RED);
                    tableHistory.setVisible(false);
                }
            }
        });
		
		// Customize the table view
        tableHistory.setBackground(colorBg);
        tableHistory.setForeground(new Color(175, 175, 175));
        tableHistory.setGridColor(colorGridColor);
        tableHistory.setRowHeight(25);
        tableHistory.setFocusable(false);
        tableHistory.setRowSelectionAllowed(false);

        tableHistory.getColumnModel().getColumn(0).setPreferredWidth(160);

        tableHistory.getTableHeader().setBackground(colorBg);
        tableHistory.getTableHeader().setForeground(new Color(217, 217, 217));

        JScrollPane scrollPaneHistory = new JScrollPane();
        scrollPaneHistory.setViewportView(tableHistory);
        scrollPaneHistory.setPreferredSize(new Dimension(100, 300));
        scrollPaneHistory.getViewport().setBackground(colorBg);

        panelHistoryCenter.add(scrollPaneHistory, BorderLayout.PAGE_END);

        panelHistory.add(panelHistoryCenter, BorderLayout.CENTER);

        frame.add(panelHistory, BorderLayout.EAST);

    }
	
	// Function which triggers every 500ms to update the Bid price Table
    private void updateTable(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(int i = 0; i < 8; i++){
                    tableBidInfo.getModel().setValueAt(db.getStockPrice(symbols[i]), i, 2);
                }
            }
        }, 0, 500);
    }
	
	// Function to add new rows to our tables 
    private void addTableRow(DefaultTableModel model, String col1, String col2, String col3){
        model.addRow(new Object[]{col1, col2, col3});
    }

}
