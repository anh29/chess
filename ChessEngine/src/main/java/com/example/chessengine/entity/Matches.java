package com.example.chessengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "matches")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Matches {
    @Id
    @Column(name = "match_id")
    private String matchId;

    @Column(name = "moves")
    private String moves;

    @Column(name = "score")
    private String score;

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

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private Set<AccountsMatches> accountsMatchesSet;

    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Rooms room;
}
