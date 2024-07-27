package cz.jan.product;

import cz.jan.product.model.CreateProductRequest;
import cz.jan.product.model.Product;
import cz.jan.product.repository.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductEntity productEntity);

    @Mapping(target = "id", ignore = true)
    ProductEntity toProductEntity(CreateProductRequest createProductRequest);

}
