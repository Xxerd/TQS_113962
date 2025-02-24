package tqs.stock;

import java.util.ArrayList;
import java.util.List;

public class StocksPortfolio implements IStockmarketService {
    IStockmarketService stockMarket;
    List<Stock> stocks = new ArrayList<Stock>();

    @Override
    public Double lookUpPrice(String symbol) {
        return 100.0;
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public Double TotalValue() {
        double total = 0.0;
        for (Stock s : stocks) {
            total += stockMarket.lookUpPrice(s.getLabel()) * s.getQuantity();
        }
        return total;
    }

    /**
     * @param topN the number of most valuable stocks to return
     * @return a list with the topN most valuable stocks in the portfolio
     */
    public List<Stock> mostValuableStocks(int topN) {
        List<Stock> mostValuable = new ArrayList<Stock>();
        List<Stock> copy = new ArrayList<Stock>(stocks);
        if (topN > copy.size()) {
            throw new IllegalArgumentException("topN is greater than the number of stocks in the portfolio");
        }
        for (int i = 0; i < topN; i++) {
            Stock mostValuableStock = copy.get(0);
            for (Stock s : copy) {
                if (stockMarket.lookUpPrice(s.getLabel()) * s.getQuantity() > stockMarket
                        .lookUpPrice(mostValuableStock.getLabel()) * mostValuableStock.getQuantity()) {
                    mostValuableStock = s;
                }
            }
            mostValuable.add(mostValuableStock);
            copy.remove(mostValuableStock);
        }
        return mostValuable;
    }
}
