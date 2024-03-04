package uz.pdp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String commentText;

    private LocalDateTime commentedTime;

    private boolean isDeleted;

    @ManyToOne
    private Article post;

    @ManyToOne
    private User user;

}
