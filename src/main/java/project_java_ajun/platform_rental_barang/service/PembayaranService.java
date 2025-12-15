package project_java_ajun.platform_rental_barang.service;

import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project_java_ajun.platform_rental_barang.entity.*;
import project_java_ajun.platform_rental_barang.repository.PembayaranRepository;
import project_java_ajun.platform_rental_barang.repository.PemesananRepository;
import project_java_ajun.platform_rental_barang.request.PembayaranRequest;
import project_java_ajun.platform_rental_barang.response.PembayaranResponse;
import project_java_ajun.platform_rental_barang.response.PemesananShortResponse;
import project_java_ajun.platform_rental_barang.security.CustomUserDetails;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PembayaranService {

    private final PembayaranRepository pembayaranRepository;
    private final PemesananRepository pemesananRepository;

    //Create Pembayaran
    public PembayaranResponse create(PembayaranRequest request) throws Exception {

        //Ambil user yang Login
        CustomUserDetails userDetails = getCurrentUserDetails();

        //Validasi Role
        if (!userDetails.getRole().equals(Role.RENTER)){
            throw new AccessDeniedException("Hanya Renter yang bisa membuat pemesanan");
        }

        Pemesanan pemesanan = pemesananRepository.findById(request.getPemesananId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pemesanan Tidak Ditemukan"));

        // Validasi bahwa pemesanan ini milik renter yang login
        if (!pemesanan.getRenter().getId().equals(userDetails.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tidak dapat membayar pesanan milik user lain");
        }

        //Cek apakah sudah ada pembayaran sebelumnya
        pembayaranRepository.findByPesanan_Id(pemesanan.getId())
                .ifPresent(pembayaran -> { throw new RuntimeException("Pembayaran untuk pesanan ini sudah dibuat"); });

        String orderId = "ORDER-" + pemesanan.getId();

        // hitung total bayar
        long totalBayar = pemesanan.getBarang().getHargaPerHari() * pemesanan.getDurasiHari();

        //Panggil Midtrans untuk mendapatkan snapToken
        String redirectUrl = createMidtransTransaction(orderId, totalBayar, pemesanan);


        //Hitung tanggal kembali = tanggalRental + durasiHari
        LocalDate tgglKembali = pemesanan.getTanggalRental()
                .plusDays(pemesanan.getDurasiHari());

        //Buat pembayaran
        Pembayaran pembayaran = Pembayaran.builder()
                .id(UUID.randomUUID().toString())
                .pesanan(pemesanan)
                .totalBayar(totalBayar)
                .metodeBayar(request.getMetodeBayar())
                .orderId(orderId)
                .status(StatusTransaksi.PENDING)
                .tanggalPembayaran(null)
                .tanggalKembali(tgglKembali)
                .build();

        pembayaranRepository.save(pembayaran);

        return toResponse(pembayaran, redirectUrl);

    }

    // Create Midtrans
    private String createMidtransTransaction(String orderId, long totalBayar, Pemesanan pemesanan) {

        //buat objek SnapApi
        SnapApi snapApi = new SnapApi();

        //transaction details
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", orderId);
        transactionDetails.put("gross_amount", totalBayar);

        //item details
        Map<String, Object> itemDetail = new HashMap<>();
        itemDetail.put("id", pemesanan.getBarang().getId());
        itemDetail.put("price", pemesanan.getBarang().getHargaPerHari());
        itemDetail.put("quantity", pemesanan.getDurasiHari());
        itemDetail.put("name", pemesanan.getBarang().getNama());

        //customer details
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", pemesanan.getRenter().getNama());
        customerDetails.put("email", pemesanan.getRenter().getEmail());
        customerDetails.put("phone", pemesanan.getRenter().getNoTelp());


        Map<String, Object> params = new HashMap<>();
        params.put("transaction_details", transactionDetails);
        params.put("customer_details", customerDetails);
        params.put("item_details", List.of(itemDetail));

        try {
            JSONObject response = snapApi.createTransaction(params);
            return response.getString("redirect_url");
        } catch (MidtransError e) {
            throw new RuntimeException("Gagal membuat transaksi Midtrans", e);
        }
    }

    //WebHook
    public void handleMidtransWebhook(Map<String, Object> notification) {

        String orderId = (String) notification.get("order_id");
        String transactionStatus = (String) notification.get("transaction_status");

        // Ambil pembayaran berdasarkan pemesananId
        Pembayaran pembayaran = pembayaranRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Pembayaran tidak ditemukan"));

        Pemesanan pemesanan = pembayaran.getPesanan();

        switch (transactionStatus.toLowerCase()) {  // convert ke lowerCase

            case "settlement":
                pembayaran.setStatus(StatusTransaksi.SUCCESS);
                pembayaran.setTanggalPembayaran(LocalDateTime.now());
                pemesanan.setStatusPemesanan(StatusPemesanan.RENTED);
                break;

            case "pending":
                pembayaran.setStatus(StatusTransaksi.PENDING);
                break;

            case "cancel":
                pembayaran.setStatus(StatusTransaksi.CANCEL);
                pemesanan.setStatusPemesanan(StatusPemesanan.CANCELED);
                break;

            case "expire":
                pembayaran.setStatus(StatusTransaksi.EXPIRED);
                pemesanan.setStatusPemesanan(StatusPemesanan.CANCELED);
                break;

            case "deny":
                pembayaran.setStatus(StatusTransaksi.FAILED);
                pemesanan.setStatusPemesanan(StatusPemesanan.CANCELED);
                break;

            default:
                System.out.println("Status tidak dikenal: " + transactionStatus);
        }

        pembayaranRepository.save(pembayaran);
        pemesananRepository.save(pemesanan);
    }

    //Get By Id
    public PembayaranResponse getPembayaranById(String id) throws Exception {
        CustomUserDetails userDetails = getCurrentUserDetails();

        Pembayaran pembayaran = pembayaranRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pembayaran Tidak Ditemukan"));

        Pemesanan pemesanan = pembayaran.getPesanan();

        //Renter hanya boleh lihat pembayaran miliknya sendiri
        if (userDetails.getRole().equals(Role.RENTER)) {
            if (!pemesanan.getRenter().getId().equals(userDetails.getId())) {
                throw new AccessDeniedException("Anda tidak berhak melihat pembayaran ini");
            }
        }

        //Owner hanya boleh lihat pembayaran untuk barang milik owner tersebut
        if (userDetails.getRole().equals(Role.OWNER)) {
            if (!pemesanan.getBarang().getOwner().getId().equals(userDetails.getId())) {
                throw new AccessDeniedException("Anda tidak berhak melihat pembayaran ini");
            }
        }

        return toResponse(pembayaran, null);
    }

    //Get All Pembayaran (Admin)
    public List<PembayaranResponse> getAllPembayaran() {
        List<Pembayaran> pembayarans = pembayaranRepository.findAll();
        return pembayarans.stream()
                .map(pembayaran -> toResponse(pembayaran,null))
                .toList();
    }

    //Mapper Response
    private PembayaranResponse toResponse(Pembayaran pembayaran, String redirectUrl){
        return PembayaranResponse.builder()
                .id(pembayaran.getId())
                .pemesanan(
                        PemesananShortResponse.builder()
                                .id(pembayaran.getPesanan().getId())
                                .tanggalRental(pembayaran.getPesanan().getTanggalRental())
                                .durasiHari(pembayaran.getPesanan().getDurasiHari())
                                .statusPemesanan(pembayaran.getPesanan().getStatusPemesanan())
                                .build()
                )
                .totalBayar(pembayaran.getTotalBayar())
                .metodeBayar(pembayaran.getMetodeBayar())
                .orderId(pembayaran.getOrderId())
                .redirectUrl(redirectUrl)
                .status(pembayaran.getStatus())
                .tanggalPembayaran(pembayaran.getTanggalPembayaran())
                .tanggalKembali(pembayaran.getTanggalKembali())
                .createdAt(pembayaran.getCreatedAt())
                .updatedAt(pembayaran.getUpdatedAt())
                .build();
    }
    private CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

}