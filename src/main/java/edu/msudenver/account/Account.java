package edu.msudenver.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "accounts")

public class Account {
    @Id
    @Column(name = "account_id", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountId;

    @Column(name = "email")
    @NotNull(message = "email cannot be null")
    private String email;

    @Column(name = "gamer_tag")
    @NotNull(message = "gamerTag cannot be null")
    private String gamerTag;


    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    public Account(Long accountId, String email, String gamerTag, String password, String status) {
        this.accountId = accountId;
        this.email = email;
        this.gamerTag = gamerTag;
        this.password = password;
        this.status = status;
    }

    public Account(String email) {
        this.email = email;
    }

    public Account() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getGamerTag() {
        return gamerTag;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
