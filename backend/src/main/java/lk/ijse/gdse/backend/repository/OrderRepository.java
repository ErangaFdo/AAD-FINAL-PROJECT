package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Object> findTopByUser_UserNameOrderByOrderDatetimeDesc(String username);

    @Query(value = "SELECT * FROM orders LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> findByOrderPaginated(int size, int offset);

    @Query(value = "SELECT COUNT(*) FROM orders", nativeQuery = true)
    int getTotalOrderCount();

    @Modifying
    @Query(value = "UPDATE orders SET status='complete' WHERE order_id = :id", nativeQuery = true)
    void updateStatus(Long id);
}

