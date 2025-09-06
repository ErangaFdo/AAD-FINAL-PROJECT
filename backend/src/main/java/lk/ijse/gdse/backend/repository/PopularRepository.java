package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.Popular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularRepository extends JpaRepository<Popular, Long> {
}
