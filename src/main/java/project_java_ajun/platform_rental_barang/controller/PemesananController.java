package project_java_ajun.platform_rental_barang.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project_java_ajun.platform_rental_barang.request.PemesananRequest;
import project_java_ajun.platform_rental_barang.response.PemesananResponse;
import project_java_ajun.platform_rental_barang.response.WebResponse;
import project_java_ajun.platform_rental_barang.service.PemesananService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pemesanan")
public class PemesananController {

    private final PemesananService pemesananService;

    //Create Pemesanan (Renter)
    @PostMapping
    @PreAuthorize("hasRole('RENTER')")
    public ResponseEntity<WebResponse<PemesananResponse>> create(@Valid @RequestBody PemesananRequest request) throws Exception {
        PemesananResponse response  = pemesananService.create(request);
        return ResponseEntity.ok(WebResponse.success("Succes Create Pemesanan", response));
    }

    // Cancel Pemesanan (Renter)
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('RENTER')")
    public ResponseEntity<WebResponse<PemesananResponse>> cancelPemesanan(@PathVariable String id) throws Exception {
        PemesananResponse response = pemesananService.cancel(id);
        return ResponseEntity.ok(WebResponse.success("Pemesanan Berhasil Dibatalkan",response));
    }

    // Approved Pemesanan (Owner)
    @PutMapping("/{id}/approved")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<WebResponse<PemesananResponse>> approvedPemesanan(@PathVariable String id) throws Exception {
        PemesananResponse response = pemesananService.approve(id);
        return ResponseEntity.ok(WebResponse.success("Pemesanan Disetujui", response));
    }

    // Returned Pemesanan (Owner)
    @PutMapping("/{id}/returned")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<WebResponse<PemesananResponse>> returnPemesanan(@PathVariable String id) throws Exception {
        PemesananResponse response = pemesananService.returned(id);
        return ResponseEntity.ok(WebResponse.success("Pemesanan Diselesaikan", response));
    }

    // Get All (Admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<List<PemesananResponse>>> getAllPemesanan(){
        List<PemesananResponse> pesanan = pemesananService.getAllPemesanan();
        return ResponseEntity.ok(WebResponse.success("Success Retrieving data",pesanan));
    }

    // Get By Id (Renter)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RENTER')")
    public ResponseEntity<WebResponse<PemesananResponse>> getById(@PathVariable String id){
        PemesananResponse response = pemesananService.getPemesananById(id);
        return ResponseEntity.ok(WebResponse.success(response));
    }
}
