package com.poly.app.domain.repository;

import com.poly.app.domain.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Integer> {
    List<Announcement> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
