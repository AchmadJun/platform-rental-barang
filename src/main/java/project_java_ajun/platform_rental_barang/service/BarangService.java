package project_java_ajun.platform_rental_barang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import project_java_ajun.platform_rental_barang.entity.*;
import project_java_ajun.platform_rental_barang.repository.BarangRepository;
import project_java_ajun.platform_rental_barang.repository.UserRepository;
import project_java_ajun.platform_rental_barang.request.BarangRequest;
import project_java_ajun.platform_rental_barang.response.BarangResponse;
import project_java_ajun.platform_rental_barang.response.OwnerShortResponse;
import project_java_ajun.platform_rental_barang.response.PagingResponse;
import project_java_ajun.platform_rental_barang.security.CustomUserDetails;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarangService {

    private final BarangRepository barangRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    //Create Barang
    public BarangResponse create(BarangRequest request, MultipartFile file) throws IOException{

        //Ambil user yang Login
        CustomUserDetails userDetails = getCurrentUserDetails();

        //Validasi Role
        if (!userDetails.getRole().equals(Role.OWNER)){
            throw new AccessDeniedException("Hanya Owner yang bisa menambah barang");
        }

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Tidak Ditemukan"));

        String publicId = "barang/" + UUID.randomUUID().toString();
        String imgUrl = cloudinaryService.uploadFile(file,publicId);

        Barang barang = Barang.builder()
                .id(UUID.randomUUID().toString())
                .owner(user)
                .nama(request.getNama())
                .kondisi(request.getKondisi())
                .deskripsi(request.getDeskripsi())
                .hargaPerHari(request.getHargaPerHari())
                .gambarBarang(imgUrl)
                .build();

        barangRepository.save(barang);
        return toResponse(barang);
    }

    //Get All Barang
    public List<BarangResponse> getAllBarang() {
        List<Barang> barangs = barangRepository.findAll();
        return barangs.stream()
                .map(this::toResponse)
                .toList();
    }

    //Get Barang By Id
    public BarangResponse getBarangById(String id){
        Barang barang = barangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Barang Tidak Ditemukan"));
        return toResponse(barang);
    }

    //Filter nama barang atau deskripsi dan Harga
    public PagingResponse<BarangResponse> filter(String keyword, Double min, Double max,String kondisi, int page,int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<Barang> result;

        // Filter nama atau deskripsi
        if (keyword != null && !keyword.isEmpty()){
            result = barangRepository.findByNamaContainingIgnoreCaseOrDeskripsiContainingIgnoreCase(keyword,keyword,pageable);
        }

        // Filter Rentang Harga
        else if (min != null && max != null){
            result = barangRepository.findByHargaPerHariBetween(min, max, pageable);
        }

        // Filter Kondisi Barang
        else if (kondisi != null && !kondisi.isEmpty()) {
            try {
                KondisiBarang kondisiEnum = KondisiBarang.valueOf(kondisi.toUpperCase());
                result = barangRepository.findByKondisi(kondisiEnum, pageable);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Kondisi tidak valid. Gunakan huruf kapital"
                );
            }
        }

        else {
            result = barangRepository.findAll(pageable);
        }

        List<BarangResponse> data = result.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PagingResponse<>(
                data,
                result.getNumber(),
                result.getTotalPages(),
                result.getSize()
        );
    }

    public BarangResponse update(String id, BarangRequest request, MultipartFile file) throws IOException{
        //Ambil user yang Login
        CustomUserDetails userDetails = getCurrentUserDetails();

        //Validasi Role
        if (!userDetails.getRole().equals(Role.OWNER)){
            throw new AccessDeniedException("Hanya Owner yang bisa mengupdate barang");
        }

        //Cari Barang
        Barang barang = barangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Barang Tidak Ditemukan"));

        //Memastikan Barang milik user yang login
        if (!barang.getOwner().getId().equals(userDetails.getId())){
            throw new AccessDeniedException("Tidak memiliki akses untuk mengupdate barang ini");
        }

        //Set update barang (Partial)
        if (request.getNama() != null){
            barang.setNama(request.getNama());
        }

        if (request.getKondisi() != null) {
            barang.setKondisi(request.getKondisi());
        }

        if (request.getDeskripsi() != null){
            barang.setDeskripsi(request.getDeskripsi());
        }

        if (request.getHargaPerHari() != null){
            barang.setHargaPerHari(request.getHargaPerHari());
        }

        // Jika ada file baru upload ke Cloudinary
        if (file != null && !file.isEmpty()) {
            String publicId = "barang/" + barang.getId();
            String imgUrl = cloudinaryService.uploadFile(file,publicId);
            barang.setGambarBarang(imgUrl);
        }

        barangRepository.save(barang);
        return toResponse(barang);
    }

    public void delete(String id) throws IOException {
        //Ambil user yang Login
        CustomUserDetails userDetails = getCurrentUserDetails();

        //Cari Barang
        Barang barang = barangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Barang Tidak Ditemukan"));

        // Validasi Role & Akses
        if (userDetails.getRole().equals(Role.OWNER)) {
            // Owner hanya bisa hapus barang miliknya sendiri
            if (!barang.getOwner().getId().equals(userDetails.getId())) {
                throw new AccessDeniedException("Tidak memiliki akses untuk menghapus barang ini");
            }
        } else if (!userDetails.getRole().equals(Role.ADMIN)) {
            // Yang bukan Owner dan bukan Admin ditolak
            throw new AccessDeniedException("Hanya Owner atau Admin yang bisa menghapus barang");
        }
        // Hapus file dari Cloudinary
        if (barang.getGambarBarang() != null && !barang.getGambarBarang().isEmpty()) {
            String publicId = "barang/" + barang.getId(); // pastikan sama dengan yang digunakan saat upload
            cloudinaryService.deleteFile(publicId);
        }

        barangRepository.delete(barang);
    }

    private BarangResponse toResponse(Barang barang){
        return BarangResponse.builder()
                .id(barang.getId())
                .owner(
                        OwnerShortResponse.builder()
                                .id(barang.getOwner().getId())
                                .nama(barang.getOwner().getNama())
                                .noTelp(barang.getOwner().getNoTelp())
                                .build()
                )
                .nama(barang.getNama())
                .kondisi(barang.getKondisi())
                .deskripsi(barang.getDeskripsi())
                .hargaPerHari(barang.getHargaPerHari())
                .gambarBarang(barang.getGambarBarang())
                .createdAt(barang.getCreatedAt())
                .updatedAt(barang.getUpdatedAt())
                .build();
    }

    private CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
