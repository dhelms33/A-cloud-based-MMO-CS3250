package edu.msudenver.account;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AccountService.class)
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        accountService.entityManager = entityManager;
    }

    @Test
    public void testGetAccounts() throws Exception {
        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf(10L));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");

        Mockito.when(accountRepository.findAll()).thenReturn(Arrays.asList(testAccount));

        List<Account> accounts = accountService.getAccounts();
        assertEquals(1, accounts.size());
        assertEquals("testGamerTag", accounts
                .get(0)
                .getGamerTag());
    }

    @Test
    public void testGetAccount() throws Exception {
        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf(10L));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testAccount));
        Account account = accountService.getAccount(10L);
        assertEquals("testGamerTag", account.getGamerTag());
    }

    @Test
    public void testGetAccountNotFound() throws Exception {
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertEquals(null,
                accountService.getAccount(null));
    }

    @Test
    public void testSaveAccount() throws Exception {
        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf(10L));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");

        Mockito.when(accountRepository.saveAndFlush(Mockito.any())).thenReturn(testAccount);
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(testAccount);

        assertEquals(testAccount, accountService.saveAccount(testAccount));
    }

    @Test
    public void testSaveAccountBadRequest() throws Exception {
        Account badTestAccount = new Account();
        badTestAccount.setAccountId(Long.valueOf(10L));
        badTestAccount.setEmail("A.Gamer@gmail.com");
        badTestAccount.setGamerTag("testGamerTag");
        badTestAccount.setPassword("********");
        badTestAccount.setStatus("Active");

        Mockito.when(accountRepository.save(Mockito.any())).thenThrow(IllegalArgumentException.class);
        Mockito.when(accountRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.saveAccount(badTestAccount);
        });
    }

    @Test
    public void testDeleteAccount() throws Exception {
        Account testAccount = new Account();
        testAccount.setAccountId(Long.valueOf(10L));
        testAccount.setEmail("A.Gamer@gmail.com");
        testAccount.setGamerTag("testGamerTag");
        testAccount.setPassword("********");
        testAccount.setStatus("Active");
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccount));
        Mockito.when(accountRepository.existsById(Mockito.any())).thenReturn(true);

        assertTrue(accountService.deleteAccount(10L));
    }

    @Test
    public void testDeleteAccountNotFound() throws Exception {
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(accountRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(accountRepository)
                .deleteById(Mockito.any());

        assertFalse(accountService.deleteAccount(null));
    }
}
