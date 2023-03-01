package com.Task1.Task.model;

import com.Task1.Task.enums.CancelReason;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gid;
    private Date createdTime;
    private int playerOneID;
    private int playerTwoID;
    private int playerThreeID;
    private int playerFourID;
    private Long bet_amt;
    private Enum<CancelReason> cancelledReason;
}
