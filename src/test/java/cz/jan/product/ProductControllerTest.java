package cz.jan.product;

import cz.jan.AbstractEshopIntegrationTest;
import cz.jan.product.model.CreateProductRequest;
import cz.jan.product.model.Product;
import cz.jan.product.model.UpdateProductRequest;
import cz.jan.product.repository.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends AbstractEshopIntegrationTest {

    @Test
    void createProduct() throws Exception {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("chleba a salam")
                .quantity(0L)
                .pricePerUnit(BigDecimal.valueOf(55))
                .build();
        Product product = callPostWithBody("/api/product", request, Product.class);
        assertEquals("chleba a salam", product.name());

        assertTrue(productRepository.findById(product.id()).isPresent());
    }

    @Test
    void updateProduct() throws Exception {
        long productId = 2000005;

        Optional<ProductEntity> alreadySavedProduct = productRepository.findById(productId);
        assertTrue(alreadySavedProduct.isPresent());
        assertEquals(20, alreadySavedProduct.get().getQuantity());


        UpdateProductRequest request = UpdateProductRequest.builder()
                .quantity(1000L)
                .build();
        mockMvc.perform(put("/api/product/" + productId)
                        .content(jsonAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<ProductEntity> updatedProduct = productRepository.findById(productId);
        assertTrue(updatedProduct.isPresent());
        assertEquals(1000L, updatedProduct.get().getQuantity());
    }

    @Test
    void deleteProduct() throws Exception {
        long productId = 2000006;

        Optional<ProductEntity> alreadySavedProduct = productRepository.findById(productId);
        assertTrue(alreadySavedProduct.isPresent());
        assertTrue(alreadySavedProduct.get().getActive());

        mockMvc.perform(delete("/api/product/" + productId))
                .andExpect(status().isOk());

        Optional<ProductEntity> updatedProduct = productRepository.findById(productId);
        assertTrue(updatedProduct.isPresent());
        assertFalse(updatedProduct.get().getActive());
    }
}