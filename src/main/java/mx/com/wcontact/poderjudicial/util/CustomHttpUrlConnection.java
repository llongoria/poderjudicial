package mx.com.wcontact.poderjudicial.util;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CustomHttpUrlConnection {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(CustomHttpUrlConnection.class.getName());

    private static final String USER_AGENT = "Mozilla/5.0";

    // https://contingencia2.tvg.mx:15672/api/connections
    private static final String GET_URL = "https://api.cjj.gob.mx/bulletin";

    private String tempUrl;

    public String getUrl(){
        return tempUrl;
    }

    public String sendGET(String path) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        this.tempUrl = GET_URL + "/" + path;
        URL obj = URI.create(tempUrl).toURL();
        HttpsURLConnection https = (HttpsURLConnection) obj.openConnection();
        try {
            String auth = "scjn" + ":" + "12345678901234567890";
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);

            TrustAllCertificates trustAllCerts = new TrustAllCertificates();

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustAllCerts}, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory( sslContext.getSocketFactory() );

            HttpsURLConnection.setDefaultHostnameVerifier( trustAllCerts );

            https.setRequestMethod("GET");
            https.setRequestProperty("User-Agent", USER_AGENT);
            https.setRequestProperty("Authorization", authHeaderValue);
            https.setHostnameVerifier(trustAllCerts);
            https.setSSLSocketFactory(sslContext.getSocketFactory());

            int responseCode = https.getResponseCode();
            // System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(https.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                //System.out.println(response.toString());
                return response.toString();
            } else {
                log.warn("GET request did not work.");
            }
        } catch(Exception ex) {
            log.error(ex);
        } finally {
            if(https != null) { https.disconnect(); }
        }
        return null;
    }

    public void close(){

    }


}
