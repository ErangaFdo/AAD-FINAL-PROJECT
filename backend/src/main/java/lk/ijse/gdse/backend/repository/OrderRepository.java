package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Object> findTopByUser_UserNameOrderByOrderDatetimeDesc(String username);
}

