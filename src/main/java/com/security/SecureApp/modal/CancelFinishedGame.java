package com.security.SecureApp.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "cancelled_finished_games")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CancelFinishedGame {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String game_name;
    @Column(nullable = false)
    private String game_typeid;
    @Column(nullable = false)
    private String creation_time;
    private String status;
    @Column(nullable = false)
    private String cancel_reason;
    @Column(nullable = false)
    private long bet_amount;
    @OneToMany(
            cascade = CascadeType.PERSIST
    )
    private List<User> players;
    @Column(nullable = false)
    private long gamecreatorid;
    private String winorder;

}
