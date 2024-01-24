package edu.msudenver.stats;

import edu.msudenver.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    public Stats getStatByProfileId(Long profileId);
    public Stats getAccountByProfileId (Long profileId);
}
