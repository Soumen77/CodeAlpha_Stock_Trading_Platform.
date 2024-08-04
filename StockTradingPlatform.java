package com.stock;

import java.util.*;

class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    double cash;

    public Portfolio(double initialCash) {
        this.cash = initialCash;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cost > cash) {
            System.out.println("Insufficient funds to buy " + quantity + " shares of " + stock.symbol);
            return;
        }
        holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
        cash -= cost;
        System.out.println("Bought " + quantity + " shares of " + stock.symbol);
    }

    public void sellStock(Stock stock, int quantity) {
        int currentHoldings = holdings.getOrDefault(stock.symbol, 0);
        if (currentHoldings < quantity) {
            System.out.println("Insufficient shares to sell " + quantity + " of " + stock.symbol);
            return;
        }
        holdings.put(stock.symbol, currentHoldings - quantity);
        cash += stock.price * quantity;
        System.out.println("Sold " + quantity + " shares of " + stock.symbol);
    }

    public double getTotalValue(Map<String, Stock> market) {
        double totalValue = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            double stockPrice = market.get(symbol).price;
            totalValue += stockPrice * quantity;
        }
        return totalValue;
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\nPortfolio:");
        System.out.printf("Cash: $%.2f\n", cash);
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            double stockPrice = market.get(symbol).price;
            System.out.printf("%s: %d shares, Current Value: $%.2f\n", symbol, quantity, stockPrice * quantity);
        }
        System.out.printf("Total Portfolio Value: $%.2f\n", getTotalValue(market));
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 150.0));
        market.put("GOOGL", new Stock("GOOGL", 2800.0));
        market.put("MSFT", new Stock("MSFT", 300.0));

        Portfolio portfolio = new Portfolio(100000.0);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCurrent Market Prices:");
            for (Stock stock : market.values()) {
                System.out.printf("%s: $%.2f\n", stock.symbol, stock.price);
            }

            portfolio.displayPortfolio(market);

            System.out.println("\nOptions:");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. Update Market Prices");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    System.out.print("Enter quantity to buy: ");
                    int buyQuantity = scanner.nextInt();
                    if (market.containsKey(buySymbol)) {
                        portfolio.buyStock(market.get(buySymbol), buyQuantity);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    System.out.print("Enter quantity to sell: ");
                    int sellQuantity = scanner.nextInt();
                    if (market.containsKey(sellSymbol)) {
                        portfolio.sellStock(market.get(sellSymbol), sellQuantity);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;
                case 3:
                    for (Stock stock : market.values()) {
                        double change = (Math.random() - 0.5) * 10;  // Random price change between -5% and 5%
                        stock.price *= (1 + change / 100);
                    }
                    System.out.println("Market prices updated.");
                    break;
                case 4:
                    System.out.println("Thank you for using the Stock Trading Platform. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}