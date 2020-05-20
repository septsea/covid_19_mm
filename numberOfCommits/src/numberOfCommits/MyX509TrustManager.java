package numberOfCommits                                                                                                                         ;

import java.security.cert.CertificateException                                                                                                  ;
import java.security.cert.X509Certificate                                                                                                       ;

import javax.net.ssl.X509TrustManager                                                                                                           ;

/**
 * This is a trust manager doing nothing but enabling HTTPS connections.
 */
public class MyX509TrustManager implements X509TrustManager                                                                                     {

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException                            {}

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException                            {}

    @Override
    public X509Certificate[] getAcceptedIssuers()                                                                                               {
        return null                                                                                                                             ;}}
