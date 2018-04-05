package cafe.adriel.androidaudiorecorder.rest;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import cafe.adriel.androidaudiorecorder.activity.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    public static final String BASE_URL = "https://203.113.152.90:8088/voice_test/public/";
    public static final String SUFFIX_URL_AUTHEN = "auth";
    public static final String SUFFIX_URL_GETCONTENT = "getContent";
    public static final String SUFFIX_URL_SAVE_AUDIO = "saveAudio";
    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream cert = context.getResources().openRawResource(R.raw.voice);
        Certificate ca;
        try {
            ca = cf.generateCertificate(cert);
        } finally { cert.close(); }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);



        // creating an OkHttpClient that uses our SSLSocketFactory
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
