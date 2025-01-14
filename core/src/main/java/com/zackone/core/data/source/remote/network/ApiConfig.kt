package com.zackone.core.data.source.remote.network

import android.content.Context
import com.zackone.core.util.SSLCertificateConfigurator
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Arrays
import javax.net.ssl.X509TrustManager

class ApiConfig {
    companion object {
        fun getApiService(context: Context): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val trustManagerFactory = SSLCertificateConfigurator.getTrustManager(context)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }
            val trustManager = trustManagers[0] as X509TrustManager

            val hostname = "newsapi.org"
            val certificatePinner = CertificatePinner.Builder()
                .add(hostname, "sha256/DWxiTEZOOmdHH3X/fYzlD7Jny9/CZ8IFmaLQQHT+PmA=")
                .add(hostname, "sha256/Q/Dhu9FBOaTRCRL38yOpIxCg3wvkc5pnQhC7NVqnZoo=")
                .build()

            val client = OkHttpClient.Builder()
                .sslSocketFactory(SSLCertificateConfigurator.getSSLConfiguration(context).socketFactory, trustManager)
                .certificatePinner(certificatePinner)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}