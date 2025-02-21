package com.example.spring_Yunhyeok_01023567215.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
@Table(name = "Users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    private Integer fakerId;

    @Column(nullable = false)
    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Builder.Default
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at= LocalDateTime.now();

    @Builder.Default
    @UpdateTimestamp
    private LocalDateTime updated_at = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRooms;


    public enum UserStatus {
        WAIT, ACTIVE, NON_ACTIVE
    }
}
