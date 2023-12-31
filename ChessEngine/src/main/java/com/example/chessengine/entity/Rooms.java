package com.example.chessengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "Rooms")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rooms {
    @Id
    @Column(name = "match_id")
    private String matchId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "match_id")
    private Matches match;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Chats> chat;
}
