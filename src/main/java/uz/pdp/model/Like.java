package uz.pdp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue
    private Long Id;

    private boolean isLiked;

    @ManyToOne
    private Article post;
    @ManyToOne
    private  User user;
}
