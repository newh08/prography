package com.example.spring_Yunhyeok_01023567215.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "host", referencedColumnName = "user_id")
    private User host;

    @Enumerated(EnumType.STRING)
    private RoomType room_type;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Builder.Default
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at= LocalDateTime.now();

    @Builder.Default
    @UpdateTimestamp
    private LocalDateTime updated_at = LocalDateTime.now();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRooms;

    public int getMaxTeamSize() {
        return this.room_type.getMaxPlayers() / 2;
    }

    public int getMaxCapacity() {
        return this.room_type.getMaxPlayers();
    }

    public enum RoomStatus {
        WAIT, PROGRESS, FINISH;

        public boolean isFinish(){
            return this.equals(FINISH);
        }
    }

    public boolean isRoomFull(int currentMemberCount) {
        int maxMemberCount = this.getMaxCapacity();
        return currentMemberCount >= maxMemberCount;
    }

    public boolean isHost(int userId){
        return this.host.getId() == userId;
    }

    public enum RoomType {
        SINGLE(2),
        DOUBLE(4);

        private final int maxPlayers;

        RoomType(int maxPlayers) {
            this.maxPlayers = maxPlayers;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }
    }


}

