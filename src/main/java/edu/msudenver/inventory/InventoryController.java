package edu.msudenver.inventory;


import edu.msudenver.profile.Profile;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.util.List;


@RestController
@RequestMapping(path = "/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Get all items in inventory
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Inventory>> getInventory() {return ResponseEntity.ok(inventoryService.getInventory()); }

    // Get 1 item in inventory
    @GetMapping(path = "/{inventoryId}", produces = "application/json")
    public ResponseEntity<Inventory> getInventoryItem(@PathVariable Long inventoryId) {
        Inventory inventory = inventoryService.getInventorySlot(inventoryId);
        return new ResponseEntity<>(inventory, inventory == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping(path = "/profile/{profileId}", produces = "application/json")
    public ResponseEntity<Inventory> getProfileInventory(@PathVariable Long profileId) {
        List<Inventory> inventory = new ArrayList<Inventory>();
        inventory = inventoryRepository.getInventoryByProfileId(profileId);
        return new ResponseEntity(inventory, inventory == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        try {
            return new ResponseEntity<>(inventoryService.saveInventory(inventory), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(ExceptionUtils.getStackTrace(e), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/equip/{inventoryId}/profile/{profileId}")
    public ResponseEntity<Inventory> inventoryProfileEquip(@PathVariable Long inventoryId, @PathVariable Long profileId) {
        Inventory retrievedInventory = inventoryService.getInventorySlot(inventoryId);

        Profile profile = retrievedInventory.getProfile();
        //Boolean equipped = inventoryPatchRequest.getEquipped();
        List<Inventory> inventoryWeapons = new ArrayList<Inventory>();
        inventoryWeapons = inventoryRepository.getInventoryByProfileIdAndType(profileId, "Weapon");
        List<Inventory> inventoryArmor = new ArrayList<Inventory>();
        inventoryWeapons = inventoryRepository.getInventoryByProfileIdAndType( profileId, "Armor");
        List<Inventory> inventoryConsumable = new ArrayList<Inventory>();
        inventoryWeapons = inventoryRepository.getInventoryByProfileIdAndType( profileId, "Consumable");
//        List<Inventory> inventoryCurrency = new ArrayList<Inventory>();
//        inventoryWeapons = inventoryRepository.getInventoryByProfileIdAndType( profileId, "Currency");

        if (retrievedInventory != null && profile != null) {
            Inventory updatedInventory = inventoryService.equipItem(retrievedInventory, inventoryWeapons, inventoryArmor, inventoryConsumable);

            try {
                return ResponseEntity.ok(inventoryService.saveInventory(updatedInventory));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping (path = "/{inventoryId}/profile/{profileId}/edit",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Inventory> changeQuantityType(@PathVariable Long inventoryId, @PathVariable Long profileId, @RequestBody Inventory updatedInventory) {
        Inventory retrievedInventory = inventoryService.getInventorySlot(inventoryId);
        Profile profile = inventoryService.getProfile(profileId);
        if (retrievedInventory != null && profile != null) {
            retrievedInventory.setQuantity(updatedInventory.getQuantity());
            retrievedInventory.setType(updatedInventory.getType());
            retrievedInventory.setName(updatedInventory.getName());
            try {
                return ResponseEntity.ok(inventoryService.saveInventory(retrievedInventory));
            } catch(Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Delete item from inventory
    @DeleteMapping(path = "/{inventoryId}/")
    public ResponseEntity<Void> deleteItem(@PathVariable Long inventoryId) {
        return new ResponseEntity<>(inventoryService.deleteInventory(inventoryId) ?
                HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}