package edu.msudenver.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @SpyBean
    private ProfileService profileService;

    @BeforeEach
    public void setup() {
        profileService.entityManager = entityManager;
    }

    @Test
    void testGetProfiles() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/profile/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Profile testProfile = new Profile();
        testProfile.setProfileId(Long.valueOf("10"));
        testProfile.setProfileName("Kung Fu Panda");
        testProfile.setClassType("Warrior");
        testProfile.setOrigins("A small village in a Chinese mountain range");
        testProfile.setIsActive(true);

        Mockito.when(profileRepository.findAll()).thenReturn(Collections.singletonList(testProfile));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Kung Fu Panda"));
    }

    @Test
    void testGetProfile() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/profile/10")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Profile testProfile = new Profile();
        testProfile.setProfileId(Long.valueOf("10"));
        testProfile.setProfileName("Kung Fu Panda");
        testProfile.setClassType("Warrior");
        testProfile.setOrigins("A small village in a Chinese mountain range");
        testProfile.setIsActive(true);

        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.of(testProfile));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Kung Fu Panda"));
    }

    @Test
    void testGetProfileNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/profile/10/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void testCreateProfile() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/profile/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"profileId\":\"10\", \"profileName\": \"Kung Fu Panda\",\"classType\":  \"Warrior\", \"gender\": \"male\", \"origins\": \"A small village in a Chinese mountain range\", \"isActive\": \"true\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Profile testProfile = new Profile();
        testProfile.setProfileId(Long.valueOf("10"));
        testProfile.setProfileName("Kung Fu Panda");
        testProfile.setClassType("Warrior");
        testProfile.setOrigins("A small village in a Chinese mountain range");
        testProfile.setIsActive(true);

        Mockito.when(profileRepository.saveAndFlush(Mockito.any())).thenReturn(testProfile);
        Mockito.when(profileRepository.save(Mockito.any())).thenReturn(testProfile);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Kung Fu Panda"));
    }

    @Test
    void testCreateProfileBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/profile/")
                .accept(MediaType.APPLICATION_JSON)
                //.content("{\"classType\": \"testGamerTa\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(profileRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(profileRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void testUpdateProfile() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/profile/10")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"profileId\":\"10\", \"profileName\": \"Kung Fu Panda\",\"classType\": \"Warrior\", \"gender\": \"male\", \"origins\": \"A small village in a Chinese mountain range\", \"isActive\": \"false\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Profile testProfile = new Profile();
        testProfile.setProfileId(Long.valueOf("10"));
        testProfile.setProfileName("Kung Fu Panda");
        testProfile.setClassType("Warrior");
        testProfile.setOrigins("A small village in a Chinese mountain range");
        testProfile.setIsActive(true);
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.of(testProfile));

        Profile testProfileUpdated = new Profile();
        testProfileUpdated.setProfileId(Long.valueOf("10"));
        testProfileUpdated.setProfileName("Kung Fu Panda Updated");
        testProfileUpdated.setClassType("Warrior Updated");
        testProfileUpdated.setOrigins("A small village in a Chinese mountain range Updated");
        testProfileUpdated.setIsActive(false);

        Mockito.when(profileRepository.save(Mockito.any())).thenReturn(testProfileUpdated);
        Mockito.when(profileRepository.saveAndFlush(Mockito.any())).thenReturn(testProfileUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Kung Fu Panda Updated"));
    }

    @Test
    void testUpdateProfileNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/profile/10/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"profileId\":\"notfound\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void testUpdateProfileBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/profile/9")
                .accept(MediaType.APPLICATION_JSON)
                //.content("{\"profileId\":\"9\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Profile testProfile = new Profile();
        testProfile.setProfileId(Long.valueOf("11"));
        testProfile.setProfileName("Kung Fu Panda");
        testProfile.setClassType("Warrior");
        testProfile.setOrigins("A small village in a Chinese mountain range");
        testProfile.setIsActive(true);
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.of(testProfile));

        Mockito.when(profileRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(profileRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void testDeleteProfile() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/profile/10")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Profile testProfile = new Profile();
        testProfile.setProfileId(Long.valueOf("10"));
        testProfile.setProfileName("Kung Fu Panda");
        testProfile.setClassType("Warrior");
        testProfile.setOrigins("A small village in a Chinese mountain range");
        testProfile.setIsActive(true);
        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.of(testProfile));
        Mockito.when(profileRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void testDeleteProfileNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/profile/10/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(profileRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(profileRepository.existsById(Mockito.any())).thenReturn(false);
//        Mockito.doThrow(IllegalArgumentException.class)
//                .when(profileRepository)
//                .deleteById(Mockito.any());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
}
