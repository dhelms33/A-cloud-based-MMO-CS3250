package edu.msudenver.profile;

import edu.msudenver.account.Account;
import edu.msudenver.inventory.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;


    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Profile>> getProfiles() {
        return ResponseEntity.ok(profileService.getProfiles());
    }

//    @GetMapping(path = "/{profileId}", produces = "application/json")
//    public ResponseEntity<Profile> getProfile(@PathVariable Long profileId) {
//        Profile profile = profileService.getProfile(profileId);
//        return new ResponseEntity<>(profile, profile == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
//    }

    @GetMapping(path = "/{profileId}", produces = "application/json")
    public ResponseEntity<Profile> getZoneProfile(@PathVariable Long profileId) {
        Profile profile = profileService.createProfileForZone(profileId);
        return new ResponseEntity<>(profile, profile == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @GetMapping(path = "/account/{accountId}", produces = "application/json")
    public ResponseEntity<Profile> getAccountProfiles(@PathVariable Long accountId) {
        List<Profile> profile = new ArrayList<Profile>();
        profile = profileRepository.getProfileByAccountId(accountId);
        return new ResponseEntity(profile, profile == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {
        try {
            return new ResponseEntity<>(profileService.saveProfile(profile), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping(path = "/{profileId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long profileId, @RequestBody Profile updatedProfile) {
        Profile retrievedProfile = profileService.getProfile(profileId);
        if (retrievedProfile != null) {
            retrievedProfile.setProfileName(updatedProfile.getProfileName());
            retrievedProfile.setGender(updatedProfile.getGender());
            retrievedProfile.setOrigins(updatedProfile.getOrigins());
            retrievedProfile.setIsActive(updatedProfile.getIsActive());
            try {
                return ResponseEntity.ok(profileService.saveProfile(retrievedProfile));
            } catch(Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping(path = "/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long profileId) {
        return new ResponseEntity<>(profileService.deleteProfile(profileId) ?
                HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}