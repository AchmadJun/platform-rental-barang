package project_java_ajun.platform_rental_barang.config;

import com.cloudinary.Cloudinary;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project_java_ajun.platform_rental_barang.properties.CloudinaryProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(CloudinaryProperties properties){
        Map<String, String> konfigurasi = new HashMap<>();
        konfigurasi.put("cloud_name", properties.getCloudName());
        konfigurasi.put("api_key", properties.getApiKey());
        konfigurasi.put("api_secret", properties.getApiSecret());
        konfigurasi.put("secure", "true");
        return new Cloudinary(konfigurasi);
    }
}
