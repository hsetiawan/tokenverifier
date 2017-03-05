package hu.cray.tokenverifier;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;


public class GoogleVerifier implements TokenVerifier {

    final Logger logger = LoggerFactory.getLogger(GoogleVerifier.class);

    private static GoogleVerifier instance = null;

    private String privAppId;
    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;

    private GoogleVerifier() {
    }


    private static GoogleVerifier getInstance() {
        if (instance == null) {
            instance = new GoogleVerifier();
        }

        return instance;
    }


    public static void setAppId(String appId) {
        getInstance().setPrivAppId(appId);
        getInstance().setHttpTransport(new NetHttpTransport());
        getInstance().setJsonFactory(new JacksonFactory());
    }

    public static int verify(String token, String uid) {
        return getInstance().verifyPrivate(token, uid);
    }

    public int verifyPrivate(String token, String uid) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(token);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(privAppId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                if (payload.getSubject().equals(uid)) {
                    logger.debug("Matching google id: " + uid);
                    return TOKEN_VALID;
                } else {
                    logger.debug("Mismatching google id: " + uid);
                    return TOKEN_INVALID;
                }
            } else {
                logger.debug("Invalid id token: " + token);
                return TOKEN_INVALID;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return TOKEN_INVALID;
        }
    }

    private void setPrivAppId(String appId) {
        this.privAppId = appId;
    }

    private void setHttpTransport(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    private void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
}
