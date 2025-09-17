package lk.ijse.gdse.backend.repository;

import lk.ijse.gdse.backend.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    @Query(value = "SELECT * FROM feed_back LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<FeedBack> findByFeedbackPaginated(@Param("size") int size, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM feed_back", nativeQuery = true)
    int getTotalFeedbackCount();
}
