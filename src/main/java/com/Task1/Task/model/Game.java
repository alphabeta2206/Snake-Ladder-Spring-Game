package com.Task1.Task.model;

import com.Task1.Task.enums.CancelReason;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {
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
