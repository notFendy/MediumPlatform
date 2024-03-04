package uz.pdp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String gender;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Article> posts;

}