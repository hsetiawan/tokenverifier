package hu.cray.tokenverifier;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.RawAPIResponse;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FacebookVerifier implements TokenVerifier {

    final Logger logger = LoggerFactory.getLogger(FacebookVerifier.class);
    private static FacebookVerifier faceBookVerifier = null;

    FacebookFactory ff;
    String appId;
    String appSecret;

    private FacebookVerifier() {
    }

    private static FacebookVerifier getInstance() {
        if (faceBookVerifier == null) {
            faceBookVerifier = new FacebookVerifier();
        }
        return faceBookVerifier;
    }

    public static void setAppId(String appId, String appSecret) {
        getInstance().setAppId(appId);
        getInstance().setAppSecret(appSecret);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthAppId(appId)
                .setOAuthAppSecret(appSecret);

        getInstance().setFf(new FacebookFactory(cb.build()));
    }



    /**
     * Verifies a given TokenInfo object against Facebook's API.
     * Compares the response, returns a skeleton User object if matches, null if it doesn't or an error occurs.
     *
     * @param tokenInfo TokenInfo to check
     * @return skeleton User object (for registration form) or null
     */
    public static int verify(String token, String uid) {
        return getInstance().verifyPrivate(token, uid);
    }

    private int verifyPrivate(String token, String uid) {

        logger.error("xxx");
        Facebook f = ff.getInstance(new AccessToken(token));

        try {
            Map params = new HashMap();
            params.put("input_token", token);

            RawAPIResponse res = f.callGetAPI("debug_token", params);

            JSONObject o = res.asJSONObject();

            res = f.callGetAPI("me");
            JSONObject me = res.asJSONObject();

            res = f.callGetAPI(o.getJSONObject("data").getString("user_id") + "?fields=id,gender,age_range,locale,picture,timezone");
            JSONObject profile  = res.asJSONObject();


            if ((o.getJSONObject("data").get("user_id").equals(uid)) &&
                    (o.getJSONObject("data").get("app_id").equals(appId))) {
                logger.debug("Valid facebook user: " + uid);
                return TOKEN_VALID;
            }
            logger.debug("Invalid facebook user: " + uid);
            return TOKEN_INVALID;
        }
        catch (FacebookException e) {
            logger.error(e.getMessage());
            return TOKEN_INVALID;
        }
        catch (JSONException e) {
            logger.error(e.getMessage());
            return TOKEN_INVALID;
        }
    }

    private void setFf(FacebookFactory ff) {
        this.ff = ff;
    }

    private void setAppId(String appId) {
        this.appId = appId;
    }

    private void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}

