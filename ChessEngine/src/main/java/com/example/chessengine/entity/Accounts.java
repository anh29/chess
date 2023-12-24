package com.example.chessengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Column(name = "gmail")
    private String gmail;

    @Column(name = "password")
    private String password;

    @Column(name = "elo")
    private Integer elo;

    @Column(name = "username12")
    private String username12;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "images")
    private String image;

    @Column(name = "status")
    private Integer status;

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

    public String getGmail() {
        return gmail;
    }

    public String getPassword() {
        return password;
    }

    public Integer getElo() {
        return elo;
    }

    public String getUsername12() {
        return username12;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername12(String username12) {
        this.username12 = username12;
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

    public String getDescription() {
        return description;
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
    }

}
