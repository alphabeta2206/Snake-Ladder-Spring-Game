package com.security.SecureApp.Service;

import com.security.SecureApp.repository.CancelFinishedRepository;
import com.security.SecureApp.modal.CancelFinishedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelFinishedGameServiceImpl implements CancelFinishedGameService {

    @Autowired
    CancelFinishedRepository cancelFinishedRepository;
    private CancelFinishedGame cancelFinishedGame;


    @Override
    public CancelFinishedGame saveGame(CancelFinishedGame cancelFinishedGame) {
        return cancelFinishedRepository.save(cancelFinishedGame);
    }

    @Override
    public CancelFinishedGame saveGame() {
        return cancelFinishedRepository.save(cancelFinishedGame);
    }
}
