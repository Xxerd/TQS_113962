package tqs.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public class ProductFinderService {
    private String API_PRODUCTS;
    private ISimpleHttpClient httpClient;

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.API_PRODUCTS = "https://fakestoreapi.com/products";
    }

    public Optional<Product> findProductDetail(int id) {
        String response = httpClient.doHttpGet(API_PRODUCTS + "/" + id);
        if (response == null) {
            return Optional.empty();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product product = objectMapper.readValue(response, Product.class);
            return Optional.of(product);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
