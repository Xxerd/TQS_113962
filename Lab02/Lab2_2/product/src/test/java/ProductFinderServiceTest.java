
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqs.product.Product;
import tqs.product.ProductFinderService;
import tqs.product.ISimpleHttpClient;

@ExtendWith(MockitoExtension.class)
public class ProductFinderServiceTest {

    @Mock
    ISimpleHttpClient httpClient;

    @InjectMocks
    ProductFinderService productFinderService;

    @Test
    public void testFindProductDetail() {

        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn(
                "{\"id\":3,\"title\":\"Mens Cotton Jacket\",\"price\":1.0,\"description\":\"test\",\"category\":\"test\",\"image\":\"test\"}");
        ProductFinderService productFinderService = new ProductFinderService(httpClient);
        Optional<Product> product = productFinderService.findProductDetail(3);
        assertTrue(product.isPresent());
        assertEquals(3, product.get().getId());
        assertEquals("Mens Cotton Jacket", product.get().getTitle());

        Optional<Product> product2 = productFinderService.findProductDetail(300);
        assertTrue(product2.isEmpty());

    }
}
