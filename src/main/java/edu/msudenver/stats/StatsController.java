//package edu.msudenver.stats;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//public class StatsController {
//    @Autowired
//    private StatsRepository statsRepository;
//    //get all character stats
//    @GetMapping(produces = "application/json")
//    public ResponseEntity<List<Stats>> getAllStats() {
//        return ResponseEntity.ok(statsRepository.findAll());
//    }
//
//    //get character stats by character id
//    @GetMapping(value = "/{characterId}", produces = "application/json")
//    public ResponseEntity<Stats> getStatsByCharacterId(@PathVariable String characterId) {
//        Stats stats = statsRepository.findById(characterId).orElse(null);
//        return new ResponseEntity<>(stats, stats == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
//    }
//
//    //create a character stats
//    public Stats createStats(Stats characterStats) {
//        return statsRepository.save(characterStats);
//    }
//
//    //delete a character stats
//    public void deleteStats(String characterName) {
//        statsRepository.deleteById(characterName);
//    }
//    //patch a character stats
//    public Stats patchCharacterStats(Stats characterStats) {
//        return statsRepository.save(characterStats);
//    }
//
//}

package edu.msudenver.stats;

import edu.msudenver.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/stats")
public class StatsController {
    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private StatsService statsService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Stats>> getAllProfileStats() {
        return ResponseEntity.ok(statsService.getAllProfileStats());
    }

//    @GetMapping(value = "/{statsId}", produces = "application/json")
//    public ResponseEntity<Stats> getProfileStats(@PathVariable Long statsId) {
//        Stats stats = statsService.getProfileStats(statsId);
//        return new ResponseEntity<>(stats, stats == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
//    }
    @GetMapping(value = "/{statsId}", produces = "application/json")
    public ResponseEntity<Stats> getStatProfileForZone(@PathVariable Long statsId) {
        Stats stats = statsService.createStatProfileForZone(statsId);
        return new ResponseEntity<>(stats, stats == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Stats> createProfileStats(@RequestBody Stats statsId) {
        try {
            return new ResponseEntity<>(statsService.saveProfileStats(statsId), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{statsId}/profile/{profileId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Stats> assignStatsToProfile(@PathVariable String statsId,@PathVariable Long profileId, @RequestBody Stats updateStats) {
        Stats retrievedStats = statsService.getProfileStats(Long.valueOf(statsId)); // statsId is Long in service not sure why converted from string
        Profile profile = statsService.getProfileForStats(profileId);
        if (retrievedStats != null && profile != null) {
            retrievedStats.setProfile(profile);
            try {
                return ResponseEntity.ok(statsService.saveProfileStats(retrievedStats));
            } catch(Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // update all stats once selected class type is set?
    @PutMapping(path = "/{statsId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Stats> updateProfile(@PathVariable Long statsId, @RequestBody Stats updatedStats) {
        Stats retrievedStats = statsService.getProfileStats(statsId);
        if (retrievedStats != null) {
            retrievedStats.setAttack(updatedStats.getAttack());
            retrievedStats.setDefense(updatedStats.getDefense());
            retrievedStats.setXp(updatedStats.getXp());
            retrievedStats.setHp(updatedStats.getHp());

            retrievedStats.setCurrentLevel(updatedStats.getCurrentLevel());
            retrievedStats.setCurrentZone(updatedStats.getCurrentZone());
            retrievedStats.setCurrentCell(updatedStats.getCurrentCell());

            try {
                return ResponseEntity.ok(statsService.saveProfileStats(retrievedStats));
            } catch(Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{statsId}")
    public ResponseEntity<Stats> deleteProfileStats(@PathVariable Long statsId) {
        return new ResponseEntity<>(statsService.deleteProfileStats(statsId) ?
                HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    // partial update (PATCH) stats
    // link to resource: https://www.baeldung.com/spring-rest-json-patch
    // link to resource: https://www.baeldung.com/spring-rest-patch
//    public Stats patchProfileStats(Stats stats) {
//        return statsRepository.save(stats);
//    }

//    @PatchMapping(path = "/{contentId}",
//            consumes = "application/json", produces = "application/json")
//    public ResponseEntity<Stats> patchStats(@RequestBody StatsPatchRequest statsPatchRequest,
//                                                @PathVariable Long zoneId, Long cellId, Long contentId) {
//        Stats response = null;
//        String patchType = statsPatchRequest.getPatchType();
//        if (patchType != null) patchType = patchType.toLowerCase();
//        switch (patchType) {
//            case "attack":
//                //TODO Implement
//                break;
//            case "defense":
//                //TODO Implement
//                break;
//            case "hp":
//                //TODO Implement
//                break;
//            case "xp":
//                //TODO Implement
//                break;
//            case "currentLevel":
//                //TODO Implement
//                break;
//            case "currentZone":
//                //TODO Implement
//                break;
//            case "currentCell":
//                //TODO Implement
//                break;
//            default:
//        }
//        if(response != null) {
//            return ResponseEntity.ok(response);
//        }
//        else {
//            return new ResponseEntity("Invalid Request", HttpStatus.BAD_REQUEST);
//        }
//    }
}
