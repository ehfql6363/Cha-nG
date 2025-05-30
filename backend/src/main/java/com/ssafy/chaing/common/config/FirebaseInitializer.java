package com.ssafy.chaing.common.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FirebaseInitializer {

    private final FirebaseProperties properties;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        if (properties.getServiceAccountBase64() == null) {
            throw new IllegalStateException("Firebase service account is null!");
        }

        String decodedServiceAccount = new String(
                Base64.getDecoder().decode(properties.getServiceAccountBase64())
        );

        try (InputStream serviceAccountStream = new ByteArrayInputStream(decodedServiceAccount.getBytes())) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setProjectId(properties.getProjectName())
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            }
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

