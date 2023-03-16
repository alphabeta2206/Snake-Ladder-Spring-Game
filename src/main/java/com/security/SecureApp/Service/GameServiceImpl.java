package com.security.SecureApp.Service;

import com.security.SecureApp.modal.Games;

public interface GameServiceImpl {
    public Games findbyId(long id);
    public Games createGame(Games game);
    public Object getAllGames();

    public void deleteGame(Games game);


    public Games saveGame(Games game);


    public Games cancelGame(Games game);
}
