package edu.msudenver.profile;

import edu.msudenver.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
    public List<Profile> getProfileByAccountId(Long accountId);
}
