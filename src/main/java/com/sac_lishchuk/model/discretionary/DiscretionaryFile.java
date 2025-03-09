package com.sac_lishchuk.model.discretionary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_discretionary_files")
public class DiscretionaryFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name", unique = true)
    private String fileName;
    @CreationTimestamp(source = SourceType.DB)
    private String createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    private String updatedAt;
}
