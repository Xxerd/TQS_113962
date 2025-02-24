import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import tqs.stock.IStockmarketService;
import tqs.stock.Stock;
import tqs.stock.StocksPortfolio;

@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {

    @Mock
    IStockmarketService stockMarket;

    @InjectMocks
    StocksPortfolio portfolio;

    @Test
    void getTotalValue() {
        when(stockMarket.lookUpPrice("AAPL")).thenReturn(100.0);
        when(stockMarket.lookUpPrice("TSLA")).thenReturn(200.0);
        when(stockMarket.lookUpPrice("GOOGL")).thenReturn(300.0);

        portfolio.addStock(new Stock("AAPL", 2));
        portfolio.addStock(new Stock("TSLA", 3));

        double result = portfolio.TotalValue();
        assertEquals(800.0, result);
        verify(stockMarket, times(2)).lookUpPrice(anyString());
    }

    @Test
    void getTotalValueWithHamcrest() {
        when(stockMarket.lookUpPrice("AAPL")).thenReturn(100.0);
        when(stockMarket.lookUpPrice("TSLA")).thenReturn(200.0);

        portfolio.addStock(new Stock("AAPL", 2));
        portfolio.addStock(new Stock("TSLA", 3));

        double result = portfolio.TotalValue();
        assertThat(800.0, equalTo(result));
    }

    @Test
    void getMostValuableStocks() {
        when(stockMarket.lookUpPrice("AAPL")).thenReturn(100.0);
        when(stockMarket.lookUpPrice("TSLA")).thenReturn(200.0);
        when(stockMarket.lookUpPrice("GOOGL")).thenReturn(300.0);

        portfolio.addStock(new Stock("AAPL", 2));
        portfolio.addStock(new Stock("TSLA", 3));
        portfolio.addStock(new Stock("GOOGL", 1));

        var result = portfolio.mostValuableStocks(2);
        assertEquals(2, result.size());
        assertEquals("TSLA", result.get(0).getLabel());
        assertEquals("GOOGL", result.get(1).getLabel());

        assertThrows(IllegalArgumentException.class, () -> portfolio.mostValuableStocks(5));
    }

}
