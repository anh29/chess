package com.example.chessengine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accounts implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Getter
    @Column(name = "gmail")
    private String gmail;

    @Getter
    @Column(name = "password")
    private String password;

    @Getter
    @Column(name = "elo")
    private Integer elo;

    @Getter
    @Column(name = "username12")
    private String username12;

    @Getter
    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Getter
    @Column(name = "gender")
    private Boolean gender;

    @Getter
    @Column(name = "images")
    private String image;

    @Column(name = "status")
    private Integer status;

    @Getter
    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccountsChats> accountsChatsSet;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccountsMatches> accountsMatchesSet;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccountsPuzzles> accountsPuzzlesSet;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Roles role;

    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername12(String username12) {
        if (Objects.equals(username12, "")) {
            String[] parts = this.gmail.split("@");

            this.username12 = parts[0];
        } else {
            this.username12 = username12;
        }
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public String getUsername() {
        return this.gmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Accounts(String gmail, String password, Roles role, int elo, int status) {
        this.gmail = gmail;
        this.password = password;
        this.role = role;
        this.elo = elo;
        this.status = status;
        setUsername12("");
    }

}
