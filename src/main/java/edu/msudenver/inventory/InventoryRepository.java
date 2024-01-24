package edu.msudenver.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{
    public List<Inventory> getInventoryByProfileId(Long profileId);
    public List<Inventory> getInventoryByProfileIdAndType(Long profileId, String type);
}