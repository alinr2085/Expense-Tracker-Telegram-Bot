package ir.spring.expensetracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.IdGeneratorType;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    @Column(unique = true, nullable = false)
    private Long telegramId;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
