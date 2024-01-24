package edu.msudenver.inventory;


import edu.msudenver.account.Account;
import edu.msudenver.profile.Profile;
import edu.msudenver.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class InventoryService {
        @Autowired
        private InventoryRepository inventoryRepository;

        @Autowired
        private ProfileRepository profileRepository;

        @PersistenceContext
        protected EntityManager entityManager;
        private Profile profile;

        public List<Inventory> getInventory() { return inventoryRepository.findAll();}

        public Inventory getInventorySlot(Long inventoryId) {
                try {
                        return inventoryRepository.findById(inventoryId).get();
                } catch(NoSuchElementException | IllegalArgumentException e) {
                        e.printStackTrace();
                        return null;
                }
        }

        public Inventory equipItem (Inventory retrievedInventory, List <Inventory> inventoryWeapons, List <Inventory> inventoryArmor, List <Inventory> inventoryConsumable){ //can add List <Inventory> inventoryCurrency to paramaters later

                if (retrievedInventory.getType().matches("Weapon")) {
                        for (int i = 0; i < inventoryWeapons.size(); i++) {
                                if (inventoryWeapons.get(i).isEquipped()) {
                                        inventoryWeapons.get(i).setEquipped(false);
                                }
                        }
                        retrievedInventory.setEquipped(true);
                }

                if (retrievedInventory.getType().matches("Armor")) {
                        for (int i = 0; i < inventoryArmor.size(); i++) {
                                if (inventoryArmor.get(i).isEquipped()) {
                                        inventoryArmor.get(i).setEquipped(false);
                                }
                        }
                        retrievedInventory.setEquipped(true);
                        System.out.println("After for loop");
                }

                if (retrievedInventory.getType().matches("Consumable")) {
                        for (int i = 0; i < inventoryConsumable.size(); i++) {
                                if (inventoryConsumable.get(i).isEquipped()) {
                                        inventoryConsumable.get(i).setEquipped(false);

                                }
                        }
                        retrievedInventory.setEquipped(true);
                }

//                if (retrievedInventory.getType().matches("Currency")) {
//                        System.out.println("type: " + retrievedInventory.getType());
//                        for (int i = 0; i < inventoryCurrency.size(); i++) {
//                                System.out.println("Boolean is equipped: " + inventoryCurrency.get(i).isEquipped());
//                                if (inventoryCurrency.get(i).isEquipped()) {
//                                        inventoryCurrency.get(i).setEquipped(false);
//
//                                        System.out.println("Element at " + i + ": " +inventoryCurrency.get(i).isEquipped());
//                                }
//                        }
//                        retrievedInventory.setEquipped(true);
//                        System.out.println("After for loop");
//                }
                return retrievedInventory;
        }

        @Transactional
        public Inventory saveInventory(Inventory inventory) {
                inventory = inventoryRepository.saveAndFlush(inventory);
                entityManager.refresh(inventory);
                return inventory;
        }
        public boolean deleteInventory(Long catalogId) {
                try {
                        if(inventoryRepository.existsById(catalogId) ){
                                inventoryRepository.deleteById(catalogId);
                                return true;
                        }
                } catch(IllegalArgumentException e) {
                        e.printStackTrace();
                }
                return false;
        }
        public Profile getProfile(Long profileId) {
                try {
                        return profileRepository.findById(profileId).get();
                } catch(NoSuchElementException | IllegalArgumentException e) {
                        e.printStackTrace();
                        return null;
                }
        }
}
