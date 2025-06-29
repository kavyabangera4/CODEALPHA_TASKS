import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

class Stock {
    String name;
    double price;
    int availableQty;

    Stock(String name, double price, int availableQty) {
        this.name = name;
        this.price = price;
        this.availableQty = availableQty;
    }
}

class PortfolioItem {
    String stockName;
    int quantity;
    double totalValue;

    PortfolioItem(String stockName, int quantity, double totalValue) {
        this.stockName = stockName;
        this.quantity = quantity;
        this.totalValue = totalValue;
    }
}

public class StockTradingApp extends JFrame {
    private JTable stockTable;
    private DefaultTableModel stockModel;
    private JTextArea portfolioArea;
    private JTextField quantityField;
    private java.util.List<Stock> stockList = new ArrayList<>();
    private java.util.List<PortfolioItem> portfolio = new ArrayList<>();

    public StockTradingApp() {
        setTitle("Stock Trading Platform");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Stock Table
        stockModel = new DefaultTableModel(new Object[]{"Stock", "Price", "Available Qty"}, 0);
        stockTable = new JTable(stockModel);
        add(new JScrollPane(stockTable), BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        bottomPanel.add(quantityField);

        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");
        bottomPanel.add(buyButton);
        bottomPanel.add(sellButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Right panel for portfolio
        portfolioArea = new JTextArea(20, 25);
        portfolioArea.setEditable(false);
        add(new JScrollPane(portfolioArea), BorderLayout.EAST);

        // Initialize stock list
        stockList.add(new Stock("TCS", 3500, 100));
        stockList.add(new Stock("Infosys", 1500, 200));
        stockList.add(new Stock("Reliance", 2800, 150));
        refreshStockTable();

        // Buy Button Action
        buyButton.addActionListener(e -> {
            int row = stockTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a stock to buy.");
                return;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }
            Stock selectedStock = stockList.get(row);
            if (quantity > selectedStock.availableQty) {
                JOptionPane.showMessageDialog(this, "Not enough stock available.");
                return;
            }
            selectedStock.availableQty -= quantity;
            boolean found = false;
            for (PortfolioItem item : portfolio) {
                if (item.stockName.equals(selectedStock.name)) {
                    item.quantity += quantity;
                    item.totalValue += quantity * selectedStock.price;
                    found = true;
                    break;
                }
            }
            if (!found) {
                portfolio.add(new PortfolioItem(selectedStock.name, quantity, quantity * selectedStock.price));
            }
            refreshStockTable();
            updatePortfolioArea();
            quantityField.setText("");
        });

        // Sell Button Action
        sellButton.addActionListener(e -> {
            int row = stockTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a stock to sell.");
                return;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }
            Stock selectedStock = stockList.get(row);
            Iterator<PortfolioItem> it = portfolio.iterator();
            while (it.hasNext()) {
                PortfolioItem item = it.next();
                if (item.stockName.equals(selectedStock.name)) {
                    if (quantity > item.quantity) {
                        JOptionPane.showMessageDialog(this, "You don’t own that much to sell.");
                        return;
                    }
                    item.quantity -= quantity;
                    item.totalValue -= quantity * selectedStock.price;
                    selectedStock.availableQty += quantity;
                    if (item.quantity == 0) it.remove();
                    refreshStockTable();
                    updatePortfolioArea();
                    quantityField.setText("");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "You don’t own this stock.");
        });
    }

    private void refreshStockTable() {
        stockModel.setRowCount(0);
        for (Stock s : stockList) {
            stockModel.addRow(new Object[]{s.name, s.price, s.availableQty});
        }
    }

    private void updatePortfolioArea() {
        portfolioArea.setText("User Portfolio:\n-----------------\n");
        for (PortfolioItem item : portfolio) {
            portfolioArea.append(item.stockName + ": Qty = " + item.quantity + ", Value = ₹" + item.totalValue + "\n");
        }
    }

    public static void main(String[] args) {
        new StockTradingApp().setVisible(true);
    }
}
