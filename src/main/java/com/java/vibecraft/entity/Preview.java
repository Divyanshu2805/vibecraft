package com.java.vibecraft.entity;

import com.java.vibecraft.enums.PreviewStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "previews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    String namespace;
    String podName;
    String previewUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PreviewStatus status;

    Instant startedAt;
    Instant terminatedAt;

    @CreationTimestamp
    Instant createdAt;

}
