package com.sac_lishchuk.model.mandatory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_mandatory_files")
public class MandatoryFile {
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
