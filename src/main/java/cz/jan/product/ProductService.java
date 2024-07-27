package cz.jan.product;

import cz.jan.exception.ResourceDoesNotExist;
import cz.jan.product.model.CreateProductRequest;
import cz.jan.product.model.Product;
import cz.jan.product.model.UpdateProductRequest;
import cz.jan.product.repository.ProductEntity;
import cz.jan.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProduct)
                .toList();
    }

    public Optional<Product> getProduct(long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProduct);
    }

    public void createProduct(CreateProductRequest createProductRequest) {
        ProductEntity productEntity = productMapper.toProductEntity(createProductRequest);
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        log.info("Created product {}", savedProductEntity);
    }

    public void updateProduct(long productId, UpdateProductRequest createProductRequest) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceDoesNotExist(String.format(
                        "Product %d does not exist", productId)));

        Optional.ofNullable(createProductRequest.name()).ifPresent(productEntity::setName);
        Optional.ofNullable(createProductRequest.quantity()).ifPresent(productEntity::setQuantity);
        Optional.ofNullable(createProductRequest.pricePerUnit()).ifPresent(productEntity::setPricePerUnit);

        ProductEntity savedProductEntity = productRepository.save(productEntity);
        log.info("Updated product {}", savedProductEntity);
    }

    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }
}
