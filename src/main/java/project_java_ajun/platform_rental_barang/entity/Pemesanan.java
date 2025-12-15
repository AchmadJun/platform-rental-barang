package project_java_ajun.platform_rental_barang.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "pemesanan")
public class Pemesanan {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private User renter;

    @ManyToOne
    @JoinColumn(name = "barang_id")
    private Barang barang;

    @Column(name = "tanggal_rental")
    private LocalDate tanggalRental;

    @Column(name = "durasi_hari")
    private Integer durasiHari;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPemesanan statusPemesanan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
