package project_java_ajun.platform_rental_barang.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project_java_ajun.platform_rental_barang.request.PembayaranRequest;
import project_java_ajun.platform_rental_barang.response.PembayaranResponse;
import project_java_ajun.platform_rental_barang.response.WebResponse;
import project_java_ajun.platform_rental_barang.service.PembayaranService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pembayaran")
public class PembayaranController {

    private final PembayaranService pembayaranService;

    //Create Pembayaran
    @PostMapping
    @PreAuthorize("hasRole('RENTER')")
    public ResponseEntity<WebResponse<PembayaranResponse>> create(@Valid @RequestBody PembayaranRequest request) throws Exception {
        PembayaranResponse response = pembayaranService.create(request);
        return ResponseEntity.ok(WebResponse.success("Success Create Pembayaran",response));
    }

    //Midtrans Webhook
    @PostMapping("/webhook")
    public ResponseEntity<WebResponse<String>> midtransWebhook(
            @RequestBody Map<String, Object> notification) {
        pembayaranService.handleMidtransWebhook(notification);
        return ResponseEntity.ok(WebResponse.success("Webhook processed", null));
    }

    //Get By Id (Renter)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RENTER','OWNER')")
    public ResponseEntity<WebResponse<PembayaranResponse>> getById(@PathVariable String id) throws Exception {
        PembayaranResponse response = pembayaranService.getPembayaranById(id);
        return ResponseEntity.ok(WebResponse.success(response));
    }

    // Get All (Admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WebResponse<List<PembayaranResponse>>> getAllPembayaran(){
        List<PembayaranResponse> pembayaranResponses = pembayaranService.getAllPembayaran();
        return ResponseEntity.ok(WebResponse.success("Success Retrieving data",pembayaranResponses));
    }
}
