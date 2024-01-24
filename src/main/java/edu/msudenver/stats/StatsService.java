package edu.msudenver.stats;


import edu.msudenver.profile.Profile;
import edu.msudenver.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatsService {

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    public List<Stats> getAllProfileStats() {
        return statsRepository.findAll();
    }

    public Stats getProfileStats(Long statsId) {
        try {
            return statsRepository.findById(statsId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stats createStatProfileForZone(Long statsId) {
        try {
            Stats statsProfile = statsRepository.findById(statsId).get();
            String checkAccountOnline = statsProfile.getProfile().getAccount().getStatus();

            if (checkAccountOnline.matches("Online")){
                return statsProfile;
            }else {
                throw new IllegalArgumentException("Account is not logged in. Log in to account first.");
            }
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public Stats gainXP(Long statsId, int xpToAdd) {
//        try {
//            Stats statsProfile = statsRepository.findById(statsId).get();
//            levelUp(statsProfile, xpToAdd);
//
//            if (checkAccountOnline.matches("Online")){
//                return statsProfile;
//            }else {
//                throw new IllegalArgumentException("Account is not logged in. Log in to account first.");
//            }
//        } catch(NoSuchElementException | IllegalArgumentException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    public void levelUp(Stats statsProfile, int xpToAdd){
//        int currXP = statsProfile.getXp();
//        int level = statsProfile.getCurrentLevel();
//
//
//        if ( (level >= 0) && (level <= 10) ) {
//            int maxXP = 100;
//            int total = currXP + xpToAdd;
//            int difference;
//            if (total >= maxXP) {
//                difference = total - maxXP;
//                statsProfile.setCurrentLevel(level + 1);
//                statsProfile.setXp(0 + difference);
//                statsProfile.setAttack(statsProfile.getAttack() + 2);
//                statsProfile.setDefense(statsProfile.getDefense() + 2);
//                statsProfile.setHp(statsProfile.getHp() + 5);
//            }
//
//        } else if ( (level >= 11) && (level <= 20) ) {
//            int maxXP = 150;
//            int total = currXP + xpToAdd;
//            int difference;
//            if (total >= maxXP) {
//                difference = total - maxXP;
//                statsProfile.setCurrentLevel(level + 1);
//                statsProfile.setXp(0 + difference);
//                statsProfile.setAttack(statsProfile.getAttack() + 4);
//                statsProfile.setDefense(statsProfile.getDefense() + 4);
//                statsProfile.setHp(statsProfile.getHp() + 10);
//            }
//
//        }else if ( (level >= 21) && (level <= 35) ) {
//            int maxXP = 200;
//            int total = currXP + xpToAdd;
//            int difference;
//            if (total >= maxXP) {
//                difference = total - maxXP;
//                statsProfile.setCurrentLevel(level + 1);
//                statsProfile.setXp(0 + difference);
//                statsProfile.setAttack(statsProfile.getAttack() + 8);
//                statsProfile.setDefense(statsProfile.getDefense() + 8);
//                statsProfile.setHp(statsProfile.getHp() + 15);
//            }
//        }else if ( (level >= 36) && (level <= 50 ) ){
//            int maxXP = 250;
//            int total = currXP + xpToAdd;
//            int difference;
//            if (total >= maxXP) {
//                difference = total - maxXP;
//                statsProfile.setCurrentLevel(level + 1);
//                statsProfile.setXp(0 + difference);
//                statsProfile.setAttack(statsProfile.getAttack() + 12);
//                statsProfile.setDefense(statsProfile.getDefense() + 12);
//                statsProfile.setHp(statsProfile.getHp() + 18);
//            }
//        }else if ( (level >= 50) && (level <= 60 ) ) {
//            int maxXP = 300;
//            int total = currXP + xpToAdd;
//            int difference;
//            if (total >= maxXP) {
//                difference = total - maxXP;
//                statsProfile.setCurrentLevel(level + 1);
//                statsProfile.setXp(0 + difference);
//                statsProfile.setAttack(statsProfile.getAttack() + 18);
//                statsProfile.setDefense(statsProfile.getDefense() + 18);
//                statsProfile.setHp(statsProfile.getHp() + 27);
//            }
//        }else{
//            statsProfile.setCurrentLevel(60);
//
//        }
//    }

    public Profile getProfileForStats(Long profileId) {
        try {
            return profileRepository.findById(profileId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional

    public Stats saveProfileStats(Stats stats) {
        stats = statsRepository.saveAndFlush(stats);
        entityManager.refresh(stats);
        return stats;
    }

    public boolean deleteProfileStats(Long statsId) {
        try {
            if(statsRepository.existsById(statsId)) {
                statsRepository.deleteById(statsId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Stats patchProfileStats(Stats stats) { //we need a patch method, I could not test if this works due to naming conventions
        return statsRepository.save(stats);
    }
    // TODO: Adjust stats for equipped item
    // TODO: Add level up method
    // TODO: Adjust stats for selected class


}