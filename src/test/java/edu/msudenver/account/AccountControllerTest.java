package edu.msudenver.account;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @SpyBean
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        accountService.entityManager = entityManager;
    }

    @Test
    public void testGetAccounts() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/accounts/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf("10"));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");

        Mockito.when(accountRepository.findAll()).thenReturn(Arrays.asList(testAccount));

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("testGamerTag"));
    }

    @Test
    public void testGetAccount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/accounts/10")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf("10"));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccount));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("testGamerTag"));
    }

    @Test
    public void testGetAccountNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/accounts/10/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateAccount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/accounts/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"10\", \"email\": \"A.Gamer@gmail.com\",\"gamerTag\": \"testGamerTag\", \"password\": \"********\", \"status\": \"Active\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf("10"));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");

        Mockito.when(accountRepository.saveAndFlush(Mockito.any())).thenReturn(testAccount);
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(testAccount);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("testGamerTag"));
    }

    @Test
    public void testCreateAccountBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/accounts/")
                .accept(MediaType.APPLICATION_JSON)
                //.content("{\"gamerTag\": \"testGamerTa\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(accountRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(accountRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateAccount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/accounts/10")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"10\", \"email\": \"A.Gamer@gmail.com\",\"gamerTag\": \"testGamerTagUpdated\", \"password\": \"********\", \"status\": \"Active\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf("10"));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccount));

        Account testAccountUpdated = new Account();
        testAccountUpdated.setAccountId(Long.valueOf("10"));
        testAccountUpdated.setEmail("A.Gamer@gmail.updated");
        testAccountUpdated.setGamerTag("testGamerTagUpdated");
        testAccountUpdated.setPassword("****");
        testAccountUpdated.setStatus("Banned");

        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(testAccountUpdated);
        Mockito.when(accountRepository.saveAndFlush(Mockito.any())).thenReturn(testAccountUpdated);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("testGamerTagUpdated"));
    }

    @Test
    public void testUpdateAccountNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/accounts/10/notfound")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":\"notfound\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateAccountBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/accounts/9")
                .accept(MediaType.APPLICATION_JSON)
                //.content("{\"accountId\":\"9\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf("11"));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccount));

        Mockito.when(accountRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(accountRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }
}
