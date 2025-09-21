package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Product> findPaginatedProducts(@Param("limit") int size, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM products", nativeQuery = true)
    int getTotalProductCount();

    List<Product> findProductByCategoryContainingIgnoreCase(String keyword);
}
