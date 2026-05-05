package com.java.vibecraft.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "plans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(unique = true)
    String stripePriceId;

    Integer maxProjects;
    Integer maxTokensPerDay;
    Integer maxPreviews;
    Boolean unlimitedAi;

    Boolean active;
}
