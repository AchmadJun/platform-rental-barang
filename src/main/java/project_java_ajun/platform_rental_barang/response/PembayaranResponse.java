package project_java_ajun.platform_rental_barang.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_java_ajun.platform_rental_barang.entity.StatusTransaksi;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PembayaranResponse {

    private String id;

    private PemesananShortResponse pemesanan;

    private Long totalBayar;

    private String metodeBayar;

    private String orderId;

    private String redirectUrl;

    private StatusTransaksi status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime tanggalPembayaran;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate tanggalKembali;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
