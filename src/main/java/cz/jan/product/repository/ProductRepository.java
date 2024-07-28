package cz.jan.product.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM ProductEntity p WHERE p.id in ?1 AND active = true")
    List<ProductEntity> findAllActiveWithLock(List<Long> ids);

    List<ProductEntity> findByActiveTrue();

    Optional<ProductEntity> findByIdAndActiveTrue(Long id);
}
