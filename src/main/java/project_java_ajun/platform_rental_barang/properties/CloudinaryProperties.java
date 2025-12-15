package project_java_ajun.platform_rental_barang.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryProperties {
    private String cloudName;

    private String apiKey;

    private String apiSecret;
}
