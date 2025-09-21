package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query(value = "SELECT * FROM delivery LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Delivery> findDeliveryPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM delivery", nativeQuery = true)
    long getTotalDeliveryCount();
}
