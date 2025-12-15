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
@Table(name = "pembayaran")
public class Pembayaran {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "pemesanan_id",unique = true)
    private Pemesanan pesanan;

    @Column(name = "total_bayar")
    private Long totalBayar;

    @Column(name = "metode_bayar")
    private String metodeBayar;

    @Column(name = "order_id")
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private StatusTransaksi status;

    @Column(name = "tanggal_pembayaran")
    private LocalDateTime tanggalPembayaran;

    @Column(name = "tanggal_kembali")
    private LocalDate tanggalKembali;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
