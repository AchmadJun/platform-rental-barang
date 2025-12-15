package project_java_ajun.platform_rental_barang.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "barangs")
public class Barang {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "kondisi")
    private KondisiBarang kondisi;

    @Lob
    private String deskripsi;

    @Column(name = "harga_per_hari")
    private Long hargaPerHari;

    @Column(name = "foto_url")
    private String gambarBarang;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
