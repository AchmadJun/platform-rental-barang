package project_java_ajun.platform_rental_barang.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private static final Pattern IMAGE_CONTENT_TYPE_PATTERN = Pattern.compile("^image/(jpeg|jpg|png|bmp)$");

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public String uploadFile(MultipartFile file, String publicId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File Cannot be Empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("The file size exceeds 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !IMAGE_CONTENT_TYPE_PATTERN.matcher(contentType).matches()) {
            throw new IllegalArgumentException("File type not supported. Support File (JPG, JPEG, PNG, or BMP)");
        }

        Map<String, Object> options = new HashMap<>();
        options.put("public_id", publicId);
        options.put("overwrite", true); // otomatis overwrite file lama

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return uploadResult.get("secure_url").toString();
    }

    public void deleteFile(String publicId) throws IOException {
        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }
}
