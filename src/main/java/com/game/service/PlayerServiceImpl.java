package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.google.protobuf.MapEntry;
import javassist.tools.web.BadHttpRequest;
import org.hibernate.criterion.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService{

    final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> getPlayersList(Map<String, String> params) {
        if(params.isEmpty()) {
            Pageable pageable = PageRequest.of(0, 3);
            return playerRepository.findAll(pageable).get().collect(Collectors.toList());
        } else {
            String name = params.getOrDefault("name", null);
            String title = params.getOrDefault("title", null);
            Race race = params.containsKey("race")
                    ? Race.valueOf(params.get("race"))
                    : null;
            Profession profession = params.containsKey("profession")
                    ? Profession.valueOf(params.get("profession"))
                    : null;
            Date after = params.containsKey("after")
                    ? new Date(Long.parseLong(params.get("after")))
                    : null;
            Date before = params.containsKey("before")
                    ? new Date(Long.parseLong(params.get("before")))
                    : null;
            Boolean banned = params.containsKey("banned")
                    ? Boolean.parseBoolean(params.get("banned"))
                    : null;
            Integer minExperience = params.containsKey("minExperience")
                    ? Integer.parseInt(params.get("minExperience"))
                    : null;
            Integer maxExperience = params.containsKey("maxExperience")
                    ? Integer.parseInt(params.get("maxExperience"))
                    : null;
            Integer minLevel = params.containsKey("minLevel")
                    ? Integer.parseInt(params.get("minLevel"))
                    : null;
            Integer maxLevel = params.containsKey("maxLevel")
                    ? Integer.parseInt(params.get("maxLevel"))
                    : null;

            int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
            int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

            Pageable pageable = params.containsKey("order")
                    ? PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, PlayerOrder.valueOf(params.get("order")).getFieldName())
                    : PageRequest.of(pageNumber, pageSize);

            return playerRepository.getAllByParams(name, title, race, profession, after,
                    before, banned, minExperience, maxExperience, minLevel, maxLevel,
                    pageable).stream().collect(Collectors.toList());
        }

        /*int pageN = (params.containsKey("pageNumber") ? Integer.valueOf(params.get ("pageNumber")): 0);
        int pageS = (params.containsKey("pageSize") ? Integer.valueOf(params.get ("pageSize")): 0);
             pageable = PageRequest.of(pageN,pageS);
        return playerRepository.findAll(pageable).get().collect(Collectors.toList());
         */
    }

    @Override
    public Player createPlayer(Map<String, String> params) {
        if (params==null||params.isEmpty()||!isCorrectParam(params))
            throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        Boolean banned = (params.containsKey("banned")? Boolean.valueOf(params.get("banned")): Boolean.FALSE);
        Player player = new Player(params.get("name"),
                params.get("title"),
                Race.valueOf(params.get("race")),
                Profession.valueOf(params.get("profession")),
                Integer.valueOf(params.get("experience")),
                new Date(Long.valueOf(params.get("birthday"))),
                banned);
        playerRepository.save(player);
        return player;
    }

    @Override
    public boolean isCorrectParam(Map<String, String> params) {
        String name = params.get("name");
        if (name == null || name.isEmpty() || name.length() > 12)
            return false;
        String title = params.get("title");
        if (title == null || title.isEmpty() || title.length() > 30)
            return false;
        String race = params.get("race");
        if (race == null || race.isEmpty())
            return false;
        String profession = params.get("profession");
        if (profession == null || profession.isEmpty())
            return false;
        String birthday = params.get("birthday");
        if (birthday == null || birthday.isEmpty())
            return false;
        long lo = Long.valueOf(birthday.replaceAll("L", ""));
        GregorianCalendar cal =new GregorianCalendar();
        cal.setTime(new Date(lo));
        int y = cal.get(Calendar.YEAR);
        if (y < 2000 || y>3000) {
            return false;
        }
        String experience = params.get("experience");
        if (experience == null || experience.isEmpty())
            return false;
        int experienceInt = Integer.valueOf(experience);
        if(experienceInt<0|| experienceInt>10_000_000)
            return false;
        return true;
    }

    @Override
    public Integer getPlayersCount(Map<String, String> params) {
        if (params.isEmpty())
            return (int) playerRepository.count();
        else {
            String name = params.getOrDefault("name", null);
            String title = params.getOrDefault("title", null);
            Race race = params.containsKey("race")
                    ? Race.valueOf(params.get("race"))
                    : null;
            Profession profession = params.containsKey("profession")
                    ? Profession.valueOf(params.get("profession"))
                    : null;
            Date after = params.containsKey("after")
                    ? new Date(Long.parseLong(params.get("after")))
                    : null;
            Date before = params.containsKey("before")
                    ? new Date(Long.parseLong(params.get("before")))
                    : null;
            Boolean banned = params.containsKey("banned")
                    ? Boolean.parseBoolean(params.get("banned"))
                    : null;
            Integer minExperience = params.containsKey("minExperience")
                    ? Integer.parseInt(params.get("minExperience"))
                    : null;
            Integer maxExperience = params.containsKey("maxExperience")
                    ? Integer.parseInt(params.get("maxExperience"))
                    : null;
            Integer minLevel = params.containsKey("minLevel")
                    ? Integer.parseInt(params.get("minLevel"))
                    : null;
            Integer maxLevel = params.containsKey("maxLevel")
                    ? Integer.parseInt(params.get("maxLevel"))
                    : null;

            int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
            int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

            Pageable pageable = params.containsKey("order")
                    ? PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, PlayerOrder.valueOf(params.get("order")).getFieldName())
                    : PageRequest.of(pageNumber, pageSize);

            return playerRepository.countByParams(name, title, race, profession, after,
                    before, banned, minExperience, maxExperience, minLevel, maxLevel);
        }
    }

    @Override
    public Player getPlayer(Long id) {
        if(id==null||id<1)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        if(playerRepository.existsById(id)) {
            return playerRepository.findById(id).get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Player updatePlayer(Long id, Map<String, String> params) {
        Player player =getPlayer(id);
        System.out.println(player.toString());
        if(player!=null)  {
            String name = params.get("name");
            if (name !=null) {
                if (!(name.isEmpty() || name.length() > 12)) {
                    player.setName(name);
                } else {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }
            }

            String banned =  params.get("banned");
            if(! (banned== null ||banned.isEmpty() )) {
                player.setBanned(Boolean.valueOf(banned));
            }
            String title = params.get("title");
            if (!(title==null|| title.isEmpty() || title.length() > 30))
                player.setTitle(title);
            String race = params.get("race") ;
            if (!(race == null ||race.isEmpty()))
                player.setRace(Race.valueOf(race));
            String profession = params.get("profession");
            if (!(profession == null || profession.isEmpty()))
                player.setProfession(Profession.valueOf(profession));

            String birthStr =params.get("birthday");
            if (birthStr!=null) {
                if (!(birthStr.isEmpty())) {
                    Long birthday = Long.valueOf(birthStr.replaceAll("L", ""));
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(new Date(birthday));
                    int y = cal.get(Calendar.YEAR);
                    if (!(y < 2000 || y > 3000)) {
                        player.setBirthday(new Date(birthday));
                    } else {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                    }
                }else {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }
            }
            String experience = params.get("experience");
            if (experience!=null) {
                if (!experience.isEmpty()) {
                    Integer exp = Integer.valueOf(experience);
                    if (exp > 0 && exp <= 10_000_000) {
                        player.setExperience(exp);
                    }else {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }
            }
            playerRepository.saveAndFlush(player);
            System.out.println("Save player " + player.toString());
        }
        return player;
    }

    @Override
    public void deletePlayer(Long id) {
        if(id==null||id<1)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        if(playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
