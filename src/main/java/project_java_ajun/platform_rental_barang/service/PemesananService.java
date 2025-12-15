package project_java_ajun.platform_rental_barang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project_java_ajun.platform_rental_barang.entity.*;
import project_java_ajun.platform_rental_barang.repository.BarangRepository;
import project_java_ajun.platform_rental_barang.repository.PemesananRepository;
import project_java_ajun.platform_rental_barang.repository.UserRepository;
import project_java_ajun.platform_rental_barang.request.PemesananRequest;
import project_java_ajun.platform_rental_barang.response.BarangShortResponse;
import project_java_ajun.platform_rental_barang.response.PemesananResponse;
import project_java_ajun.platform_rental_barang.response.RenterShortResponse;
import project_java_ajun.platform_rental_barang.security.CustomUserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PemesananService {

    private final PemesananRepository pemesananRepository;
    private final UserRepository userRepository;
    private final BarangRepository barangRepository;

    public PemesananResponse create(PemesananRequest request) throws Exception {

        //Ambil user yang Login
        CustomUserDetails userDetails = getCurrentUserDetails();

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User tidak ditemukan"));

        Barang barang = barangRepository.findById(request.getBarangId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Barang tidak ditemukan"));

        //Validasi Role
        if (!userDetails.getRole().equals(Role.RENTER)){
            throw new AccessDeniedException("Hanya Renter yang bisa membuat pemesanan");
        }

        //Validasi renter(owner) tidak boleh memesan barang miliknya sendiri
        if (barang.getOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tidak dapat memesan Barang milik sendiri");
        }

        //Validasi barang tidak boleh sedang di pinjam atau Rented
        boolean barangDisewa =
                pemesananRepository.existsByBarangIdAndStatusPemesanan(barang.getId(), StatusPemesanan.RENTED);

        if (barangDisewa){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Barang sedang disewa orang lain");
        }

        //Buat Pemesanan Baru
        Pemesanan pemesanan = Pemesanan.builder()
                .id(UUID.randomUUID().toString())
                .renter(user)
                .barang(barang)
                .tanggalRental(request.getTanggalRental())
                .durasiHari(request.getDurasiHari())
                .statusPemesanan(StatusPemesanan.PENDING)
                .build();

        pemesananRepository.save(pemesanan);
        return toResponse(pemesanan);

    }

    //Get All Pemesanan (Admin)
    public List<PemesananResponse> getAllPemesanan() {
        List<Pemesanan> pesan = pemesananRepository.findAll();
        return pesan.stream()
                .map(this::toResponse)
                .toList();
    }

    //Get Pemesanan By Id (Renter)
    public PemesananResponse getPemesananById(String id){
        Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pesanan Tidak Ditemukan"));
        return toResponse(pemesanan);
    }

    // Approve Pemesanan (Owner)
    public PemesananResponse approve(String id) throws Exception {

        CustomUserDetails userDetails = getCurrentUserDetails();

        Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pemesanan tidak ditemukan"));

        // Hanya owner barang
        if (!pemesanan.getBarang().getOwner().getId().equals(userDetails.getId())) {
            throw new AccessDeniedException("Anda bukan owner dari barang ini");
        }

        // Hanya status PENDING yang bisa approve
        if (pemesanan.getStatusPemesanan() != StatusPemesanan.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pemesanan ini tidak bisa di-approve");
        }

        pemesanan.setStatusPemesanan(StatusPemesanan.APPROVED);
        pemesananRepository.save(pemesanan);

        return toResponse(pemesanan);
    }

    // Cancel Pemesanan Oleh Renter
    public PemesananResponse cancel(String id) throws Exception {

        CustomUserDetails userDetails = getCurrentUserDetails();

        Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pemesanan tidak ditemukan"));

        // hanya renter pemilik pesanan
        if (!pemesanan.getRenter().getId().equals(userDetails.getId())) {
            throw new AccessDeniedException("Bukan pemilik pemesanan ini");
        }

        // hanya status PENDING yang boleh cancel
        if (pemesanan.getStatusPemesanan() != StatusPemesanan.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Pemesanan tidak bisa dibatalkan");
        }

        pemesanan.setStatusPemesanan(StatusPemesanan.CANCELED);
        pemesananRepository.save(pemesanan);

        return toResponse(pemesanan);
    }


    // Owner Set Returned
    public PemesananResponse returned(String id) throws Exception {

        CustomUserDetails userDetails = getCurrentUserDetails();

        Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pemesanan tidak ditemukan"));

        // hanya owner
        if (!pemesanan.getBarang().getOwner().getId().equals(userDetails.getId())) {
            throw new AccessDeniedException("Anda bukan owner dari barang ini");
        }

        // hanya status RENTED yang bisa dikembalikan
        if (pemesanan.getStatusPemesanan() != StatusPemesanan.RENTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status tidak valid untuk dikembalikan");
        }

        pemesanan.setStatusPemesanan(StatusPemesanan.RETURNED);
        pemesananRepository.save(pemesanan);

        return toResponse(pemesanan);
    }

    private PemesananResponse toResponse(Pemesanan pemesanan){
        return PemesananResponse.builder()
                .id(pemesanan.getId())
                .renter(
                        RenterShortResponse.builder()
                                .id(pemesanan.getRenter().getId())
                                .nama(pemesanan.getRenter().getNama())
                                .email(pemesanan.getRenter().getEmail())
                                .noTelp(pemesanan.getRenter().getNoTelp())
                                .build()
                )
                .barang(
                        BarangShortResponse.builder()
                                .id(pemesanan.getBarang().getId())
                                .nama(pemesanan.getBarang().getNama())
                                .hargaPerHari(pemesanan.getBarang().getHargaPerHari())
                                .gambarBarang(pemesanan.getBarang().getGambarBarang())
                                .build()
                )
                .tanggalRental(pemesanan.getTanggalRental())
                .durasiHari(pemesanan.getDurasiHari())
                .tanggalKembali(pemesanan.getTanggalRental().plusDays(pemesanan.getDurasiHari()))
                .statusPemesanan(pemesanan.getStatusPemesanan())
                .createdAt(pemesanan.getCreatedAt())
                .updatedAt(pemesanan.getUpdatedAt())
                .build();
    }

    private CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
