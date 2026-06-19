package com.unlar.guarderia.Entitites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PushSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String endpoint;
    @Column(columnDefinition = "TEXT")
    private String p256dh;
    @Column(columnDefinition = "TEXT")
    private String auth;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;
}
