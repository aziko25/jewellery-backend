package jewellery.jewellery.CARATO_ONLY;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirestoreConfig {

    private static final String DATABASE_URL = "https://carato-shopping-app-default-rtdb.firebaseio.com";

    @PostConstruct
    public void initialize() {

        try {
            Resource resource = new ClassPathResource("carato-shopping-app-firebase-adminsdk-mm14l-9e0ac1c77a.json");
            InputStream serviceAccount = resource.getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
