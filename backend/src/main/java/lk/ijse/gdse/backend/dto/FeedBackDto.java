package lk.ijse.gdse.backend.dto;

import jakarta.persistence.*;
import lk.ijse.gdse.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDto {
    private Long feedBackId;
    private String fullName;
    private String email;
    private String services;
    private String ratings;
    private String message;
}

