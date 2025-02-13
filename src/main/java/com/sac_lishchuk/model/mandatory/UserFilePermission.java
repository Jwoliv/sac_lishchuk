package com.sac_lishchuk.model.mandatory;

import com.sac_lishchuk.enums.MandatoryLevel;
import com.sac_lishchuk.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user_file_permissions")
public class UserFilePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private MandatoryFilePermission permission;
    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false)
    private MandatoryLevel accessLevel;
    @CreationTimestamp
    private LocalDateTime grantedAt;
}
