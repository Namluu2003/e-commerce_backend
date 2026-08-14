package com.poly.app.domain.repository;

import com.poly.app.domain.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    Optional<Conversation> findByCustomerIdAndStaffId(Integer customerId, Integer staffId);
    List<Conversation> findByStaffId(Long id);
}