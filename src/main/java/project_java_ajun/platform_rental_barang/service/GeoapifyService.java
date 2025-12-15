package project_java_ajun.platform_rental_barang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeoapifyService {

    @Value("${geoapify.api.key}")
    private String apiKey;

    public Map<String, Object> getCoordinates(String alamat) {
        Map<String, Object> result = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        try {
            // URL Geoapify
            String url = "https://api.geoapify.com/v1/geocode/search" +
                    "?text=" + URLEncoder.encode(alamat, StandardCharsets.UTF_8) +
                    "&limit=1" +
                    "&filter=countrycode:ID" +
                    "&apiKey=" + apiKey;

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> features = (List<Map<String, Object>>) response.getBody().get("features");

                if (features != null && !features.isEmpty()) {
                    Map<String, Object> geometry = (Map<String, Object>) features.get(0).get("geometry");
                    List<Double> coordinates = (List<Double>) geometry.get("coordinates");

                    double lon = coordinates.get(0);
                    double lat = coordinates.get(1);

                    result.put("latitude", lat);
                    result.put("longitude", lon);
                    result.put("googleMapsUrl", "https://www.google.com/maps?q=" + lat + "," + lon);
                }
            }

        } catch (Exception e) {
            // simple fallback
            result.put("latitude", null);
            result.put("longitude", null);
            result.put("googleMapsUrl", "https://www.google.com/maps/search/?api=1&query=" +
                    URLEncoder.encode(alamat, StandardCharsets.UTF_8));
        }

        return result;
    }
}
