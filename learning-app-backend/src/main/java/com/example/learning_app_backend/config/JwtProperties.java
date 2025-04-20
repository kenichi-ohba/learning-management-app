package com.example.learning_app_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component // Spring の Bean として登録
@ConfigurationProperties(prefix = "jwt") // "jwt" で始まるプロパティをマッピング
@Data
public class JwtProperties {
    private String secret;     // jwt.secret に対応
    private long expirationMs; // jwt.expiration-ms に対応 (ケバブケース -> キャメルケース)
}
