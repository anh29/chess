package com.example.chessengine.entity;

import com.example.chessengine.entity.embeddable.AccountsChatsId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts_chats")
public class AccountsChats {
    @EmbeddedId
    private AccountsChatsId id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Accounts account;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private Chats chat;

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

}
