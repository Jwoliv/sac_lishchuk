package com.sac_lishchuk.model.role;

import com.sac_lishchuk.enums.Role;
import com.sac_lishchuk.enums.Rule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"role_file_id", "role"})
)
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_file_id", nullable = false)
    private RoleFile roleFile;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "permission_rules", joinColumns = @JoinColumn(name = "permission_id"))
    @Enumerated(EnumType.STRING)
    private Set<Rule> rules;
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
}
