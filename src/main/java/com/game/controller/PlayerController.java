package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    @Autowired
    PlayerServiceImpl playerService;

    @GetMapping()
    public @ResponseBody ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> params) {
        List<Player> playersList = playerService.getPlayersList(params);
        return new ResponseEntity<>(playersList, HttpStatus.OK);
    }

    @GetMapping("/count")
    public @ResponseBody Integer getPlayersCount(@RequestParam Map<String, String> params) {
        return  playerService.getPlayersCount(params);
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Player> createPlayer(@RequestBody  Map<String, String> params) {
        Player newPlayer = playerService.createPlayer(params);
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    public @ResponseBody ResponseEntity<Player> getPlayerById (@PathVariable (name = "id") Long id) {
        Player player = playerService.getPlayer(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping ("/{id}")
    public @ResponseBody ResponseEntity<Player> updatePlayerById (@PathVariable (name = "id") Long id, @RequestBody  Map<String, String> params) {
        Player player = playerService.updatePlayer(id, params);

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping ("/{id}")
    //@ResponseStatus( value = HttpStatus.BAD_REQUEST)
    public HttpStatus deletePlayerById (@PathVariable (name = "id") Long id) {
        playerService.deletePlayer(id);
        return HttpStatus.OK;
    }

}
