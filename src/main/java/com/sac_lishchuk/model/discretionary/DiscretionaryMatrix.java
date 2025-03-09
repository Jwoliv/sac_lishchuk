package com.sac_lishchuk.model.discretionary;

import com.sac_lishchuk.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_discretionary_matrix")
public class DiscretionaryMatrix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private DiscretionaryFile file;
    @Column(name = "since_access")
    private LocalDateTime sinceAccess;
    @Column(name = "to_access")
    private LocalDateTime toAccess;
    @Builder.Default
    private Boolean readable = false;
    @Builder.Default
    private Boolean writeable = false;
    @Builder.Default
    private Boolean executable = false;
}
