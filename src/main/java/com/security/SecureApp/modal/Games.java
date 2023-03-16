package com.security.SecureApp.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "games")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Games {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String game_name;
    private String game_typeid;
    private String creation_time;
    private String status;
    private String cancel_reason;
    @Column(nullable = false)
    private long bet_amount;
    private long gamecreatorid;
    @OneToMany(
            cascade = CascadeType.PERSIST
    )
    private List<User> players;


}
