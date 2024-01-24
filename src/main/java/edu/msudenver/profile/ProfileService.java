package edu.msudenver.profile;

import edu.msudenver.account.Account;
import edu.msudenver.account.AccountRepository;
import edu.msudenver.inventory.Inventory;
import edu.msudenver.inventory.InventoryRepository;
import edu.msudenver.stats.Stats;
import edu.msudenver.stats.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

@Service // business logic
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private StatsRepository statsRepository;
    @PersistenceContext
    protected EntityManager entityManager;
    private Account account;

    public List<Profile> getProfiles() {

        return profileRepository.findAll();
    }

    public Profile getProfile(Long profileId) {
        try {
            return profileRepository.findById(profileId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Profile createProfileForZone(Long profileId) {
        try {
            Profile zoneProfile = profileRepository.findById(profileId).get();
            String checkAccountOnline = zoneProfile.getAccount().getStatus();
//            Stats zoneProfileStat = statsRepository.getStatByProfileId(zoneProfile.getProfileId());
//            zoneProfileStat.setCurrentCell(1);
            if (checkAccountOnline.matches("Online")){
                return zoneProfile;
            }else {
                throw new IllegalArgumentException("Account is not logged in. Log in to account first.");
            }
        } catch(NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Profile saveProfile(Profile profile) {
        String classType = profile.getClassType().toLowerCase();
        profile = profileRepository.saveAndFlush(profile);
        entityManager.refresh(profile);
        Long afterPId = profile.getProfileId();
        // create starting inventory
        Stats startingStats = new Stats();
        startingStats.setProfileId(afterPId);
        startingStats.setXp(0);

        //create starting inventory
        Inventory startingInventory = new Inventory();
        startingInventory.setProfileId(afterPId);

        try{
            if (classType.matches("warrior")){
                startingStats.setAttack(6);
                startingStats.setHp(10);
                startingStats.setDefense(4);
                statsRepository.saveAndFlush(startingStats);
                entityManager.refresh(startingStats);

                startingInventory.setName("Fists");
                startingInventory.setEquipped(true);
                startingInventory.setQuantity(1);
                startingInventory.setType("Weapon");
                inventoryRepository.saveAndFlush(startingInventory);
                entityManager.refresh(startingStats);


            } else if (classType.matches("tank")){
                startingStats.setAttack(5);
                startingStats.setHp(10);
                startingStats.setDefense(8);
                statsRepository.saveAndFlush(startingStats);
                entityManager.refresh(startingStats);

                startingInventory.setName("Fists");
                startingInventory.setEquipped(true);
                startingInventory.setQuantity(1);
                startingInventory.setType("Weapon");
                inventoryRepository.saveAndFlush(startingInventory);
                entityManager.refresh(startingStats);


            } else if (classType.matches("paladin")){
                startingStats.setAttack(4);
                startingStats.setHp(14);
                startingStats.setDefense(4);
                statsRepository.saveAndFlush(startingStats);
                entityManager.refresh(startingStats);

                startingInventory.setName("Fists");
                startingInventory.setEquipped(true);
                startingInventory.setQuantity(1);
                startingInventory.setType("Weapon");
                inventoryRepository.saveAndFlush(startingInventory);
                entityManager.refresh(startingStats);


            }else {
                System.out.println("Valid class types are warrior, paladin or tank. Choose wisely.");
                throw new java.lang.Error(classType + " is not accepted");
            }
            return profile;
        }catch (Exception e){
            System.out.println("Catch exception");
            e.printStackTrace();
        }
        return profile;
    }

    public boolean deleteProfile(Long profileId) {
        try {
            if(profileRepository.existsById(profileId)) {
                Stats retrievedStats = statsRepository.getStatByProfileId(profileId);
                statsRepository.deleteById(retrievedStats.getStatsId());
                profileRepository.deleteById(profileId);
                return true;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

}
