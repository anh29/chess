package com.example.chessengine.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AccountsChatsId implements Serializable {
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "chat_id")
    private Integer chatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountsChatsId that = (AccountsChatsId) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, chatId);
    }
}
