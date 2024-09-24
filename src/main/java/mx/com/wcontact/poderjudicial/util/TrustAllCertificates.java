package mx.com.wcontact.poderjudicial.util;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
public class TrustAllCertificates implements X509TrustManager, HostnameVerifier {

    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }

    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException { }

    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException { }

    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    public static void trustAllHttpsCertificates() throws Exception {
        TrustAllCertificates trustAllCerts = new TrustAllCertificates();

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trustAllCerts}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(trustAllCerts);
    }

    public static void main(String[] args) throws Exception {
        trustAllHttpsCertificates();
        // Your HTTPS request code here.
    }
}

