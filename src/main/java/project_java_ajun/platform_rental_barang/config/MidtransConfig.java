package project_java_ajun.platform_rental_barang.config;

import com.midtrans.Midtrans;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidtransConfig {

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Value("${midtrans.client-key}")
    private String clientKey;

    @Value("${midtrans.is-production:false}")
    private boolean isProduction;

    @PostConstruct
    public void init(){
        // Set secara global
        Midtrans.serverKey = serverKey;
        Midtrans.clientKey = clientKey;
        Midtrans.isProduction = isProduction;

        Midtrans.setConnectTimeout(10_000);  // 10 detik
        Midtrans.setReadTimeout(30_000);     // 30 detik
        Midtrans.setWriteTimeout(10_000);    // 10 detik
    }
}
