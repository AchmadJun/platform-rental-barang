package project_java_ajun.platform_rental_barang.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project_java_ajun.platform_rental_barang.request.BarangRequest;
import project_java_ajun.platform_rental_barang.response.BarangResponse;
import project_java_ajun.platform_rental_barang.response.PagingResponse;
import project_java_ajun.platform_rental_barang.response.WebResponse;
import project_java_ajun.platform_rental_barang.service.BarangService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/barangs")
@RequiredArgsConstructor
public class BarangController {

    private final BarangService barangService;

    // Create Barang
    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<WebResponse<BarangResponse>> create(
            @RequestPart("data") @Valid BarangRequest request,
            @RequestPart("file") MultipartFile file
            ) throws IOException {
        BarangResponse response = barangService.create(request, file);
        return ResponseEntity.ok(WebResponse.success("Success Created Barang",response));
    }

    // Update Barang
    @PutMapping(value = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<WebResponse<BarangResponse>> update(
            @PathVariable String id,
            @RequestPart("data") BarangRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
       BarangResponse response = barangService.update(id, request,file);
       return ResponseEntity.ok(WebResponse.success("Barang updated successfully",response));
    }


    //Get All Barang
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RENTER')")
    public ResponseEntity<WebResponse<List<BarangResponse>>> getAll(){
        List<BarangResponse> barangs = barangService.getAllBarang();
        return ResponseEntity.ok(WebResponse.success("Success Retrieving data",barangs));
    }

    //Get By Id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<WebResponse<BarangResponse>> getById(@PathVariable String id){
        BarangResponse response = barangService.getBarangById(id);
        return ResponseEntity.ok(WebResponse.success(response));
    }

    //Filter berdasarkan nama dan rentang harga
    @GetMapping("filter")
    @PreAuthorize("hasAnyRole('RENTER','ADMIN')")
    public ResponseEntity<WebResponse<PagingResponse<BarangResponse>>> filter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(required = false) String kondisi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(WebResponse.success(barangService.filter(
                keyword, min, max,kondisi, page, size)
        ));
    }

    //Delete barang
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable String id){
        try {
            barangService.delete(id); // service akan menghapus barang + file Cloudinary
            return ResponseEntity.ok(WebResponse.success("Barang deleted successfully", null));
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WebResponse.error(e.getMessage()));
        }
    }
}
