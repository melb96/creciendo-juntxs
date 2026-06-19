package com.unlar.guarderia.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.unlar.guarderia.Entitites.PushSubscription;

import jakarta.transaction.Transactional;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {

    List<PushSubscription> findByTutorId(Long tutorId);

    @Transactional
    @Modifying
    void deleteByTutorId(Long tutorId);
}