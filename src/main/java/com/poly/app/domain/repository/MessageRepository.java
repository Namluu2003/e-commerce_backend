package com.poly.app.domain.repository;

import com.poly.app.domain.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByConversationId(Long conversationId);

    @Modifying
    @Query(value = "update  messages set status = \"SEEN\" where conversation_id =:conversation", nativeQuery = true)
    public void read(@Param("conversation") Integer id);
}