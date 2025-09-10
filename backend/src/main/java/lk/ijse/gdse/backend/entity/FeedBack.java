package lk.ijse.gdse.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedBackId;
    private String fullName;
    private String email;
    private String services;
    private String ratings;
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
