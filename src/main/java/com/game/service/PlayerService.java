package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    List<Player> getPlayersList(Map<String, String> params);
    Player createPlayer(Map<String, String> params);
    Integer getPlayersCount(Map<String, String>params);
    Player getPlayer (Long id);
    Player updatePlayer (Long id, Map<String, String> params);
    void deletePlayer (Long id);
    boolean isCorrectParam(Map<String, String> params);
}
