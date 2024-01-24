/**

 package edu.msudenver.stats;
 ​
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.Mockito;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
 import org.springframework.test.context.junit.jupiter.SpringExtension;
 import javax.persistence.EntityManager;
 import javax.persistence.EntityManagerFactory;
 import java.math.BigInteger;
 import java.util.Arrays;
 import java.util.List;
 import java.util.Optional;
 import static edu.msudenver.stats.StatsService.getProfileStats;
 //import static edu.msudenver.stats.StatsService.saveProfileStats;
 import static org.junit.jupiter.api.Assertions.*;
 @ExtendWith(SpringExtension.class)
 @WebMvcTest(value = StatsService.class)
 public class StatsServiceTest {
 @MockBean
 private StatsRepository statsRepository;
 @MockBean
 private EntityManagerFactory entityManagerFactory;
 @MockBean
 private EntityManager entityManager;
 @Autowired
 private StatsService statsService;
 @BeforeEach
 public void setup() {
 statsService.entityManager = entityManager;
 }
 @Test
 public void testGetStats() throws Exception { //gets multiple
 Stats testStats = new Stats();
 testStats.setName("Dereck Helms");
 testStats.setXp(34); //should I create a method in stats?
 testStats.setCurrentLevel(BigInteger.valueOf(1));
 testStats.setHp(100);
 testStats.setAttack(1);
 //testStats.setEquippedItem; // don't believe we need this and causes an error
 testStats.setProfileId(String.valueOf(5));
 Mockito.when(StatsRepository.findAll()).thenReturn(Arrays.asList(testStats)); //creates an error
 List<Stats> stats = StatsService.getAllStats();
 //StatsRepository.values(); this is what this website recommends
 }
 @Test
 public void testGetStat() throws Exception { //test for getting one stats? in country this only gets
 Stats testStats = new Stats();
 testStats.setName("Dereck Helms"); //is Name the same as CurrentProfile
 testStats.setXp(34); //should I create a method in stats?
 testStats.setCurrentLevel(BigInteger.valueOf(1));
 testStats.setHp(100);
 testStats.setCurrentProfile("001"); //not sure what this value would be
 Mockito.when(StatsRepository.findById(Mockito.anyString())).thenReturn(Optional.of(testStats));
 //Stats stats = statsService.setProfileID("us"); not needed
 assertEquals("Dereck Helms", StatsService.getProfileStats("001").getName());
 }
 ​
 @Test
 public void testGetStatsNotFound() throws Exception {
 Mockito.when(StatsRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
 assertEquals(null,
 getProfileStats("notfound"));
 }
 @Test
 public void testSaveStats() throws Exception {
 Stats Dereck = new Stats();
 Dereck.setName("Dereck Helms"); //is Name the same as CurrentProfile
 Dereck.setXp(34); //should I create a method in stats?
 Dereck.setCurrentLevel(BigInteger.valueOf(1));
 Dereck.setHp(100);
 Dereck.setCurrentProfile("001"); //not sure what this value would be, Idk what other methods we would have here?
 Mockito.when(StatsRepository.saveAndFlush(Mockito.any())).thenReturn(Dereck);
 Mockito.when(StatsRepository.save(Mockito.any())).thenReturn(Dereck);
 assertEquals(Dereck, statsService.saveProfileStats(Dereck)); //not sure why this is not calling the method in StatsService
 }
 @Test
 public void testSaveStatsBadRequest() throws Exception {
 Stats badDereck = new Stats();
 badDereck.setName("Dereck");
 Mockito.when(StatsRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
 Mockito.when(StatsRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);
 Exception exception = assertThrows(IllegalArgumentException.class, () -> {
 statsService.saveProfileStats(badDereck);
 });
 }
 @Test
 public void testDeleteStats() throws Exception {
 Stats Dereck = new Stats();
 Dereck.setName("Canada");
 Dereck.setCurrentProfile("001");
 Mockito.when(StatsRepository.findById(Mockito.any())).thenReturn(Optional.of(Dereck));
 Mockito.when(StatsRepository.existsById(Mockito.any())).thenReturn(true);
 assertTrue(StatsService.deleteStats("001"));
 }
 @Test
 public void testDeleteStatsNotFound() throws Exception {
 Mockito.when(StatsRepository.findById(Mockito.any())).thenReturn(Optional.empty());
 Mockito.when(StatsRepository.existsById(Mockito.any())).thenReturn(false);
 Mockito.doThrow(IllegalArgumentException.class)
 .when(StatsRepository).deleteById(Mockito.any());
 assertFalse(StatsService.deleteStats("notfound"));
 }
 }
 ​
 //}
 **/