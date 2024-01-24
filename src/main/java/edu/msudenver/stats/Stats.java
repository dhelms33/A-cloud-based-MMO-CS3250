package edu.msudenver.stats;


import javax.persistence.*;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@RequiredArgsConstructor // EMPTY CONSTRUCTOR
@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @Column(name = "stats_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long statsId;

    //@JsonIgnore
    @OneToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id", insertable = false, updatable = false)
    private Profile profile;

    @Column(name = "profile_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long profileId;


    @Column(name = "attack")
    private int attack;

    @Column(name = "defense")
    private int defense;

    @Column(name="hp")
    private int hp;

    @Column(name="xp")
    private int xp;

    @Column(name = "current_level") //big int
    private int currentLevel;

    // would probably be a zone object?
    @Column(name = "current_zone")
    private int currentZone;

    // would probably be a zone object?
    @Column(name = "current_cell")
    private int currentCell;

    // constructor
    public Stats(Long statsId, Profile profile, Long profileId, int attack, int defense, int hp, int xp, int currentLevel, int currentZone, int currentCell) {
        this.statsId = statsId;
        this.profileId = profileId;
        this.profile = profile;
        this.attack = attack;
        this.defense = defense;
        this.hp = hp;
        this.xp = xp;
        this.currentLevel = currentLevel;
        this.currentZone = currentZone;
        this.currentCell = currentCell;
    }


}