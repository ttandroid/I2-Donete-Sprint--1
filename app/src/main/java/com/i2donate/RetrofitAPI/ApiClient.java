package com.i2donate.RetrofitAPI;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    //Local
  // public static final String BASE_URL = "http://192.168.1.244/customscript/i2donate/api/newwebservice/";
   // public static final String BASE_URL = "http://check.i2-donate.com/api/newwebservice/";
    //public static final String BASE_URL = "http://project975.website/i2-donate/api/";
//    public static final String BASE_URL = "https://i2donate.quickiz.com/api/newwebservice/";
   public static final String BASE_URL = "https://check.i2-donate.com/api/newwebservice/";

    //old
    //public static final String BASE_URL = "https://check.i2-donate.com/webservice/";
    // public static final String BASE_URL = "https://admin.i2-donate.com/webservice/";
    //public static final String BASE_URL ="http://project975.website/i2donate/webservice/";

    private static Retrofit retrofit = null;



    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS);

   /* public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return builder.create(serviceClass);
    }*/



    public static Retrofit getClient() {

        Gson gson = new GsonBuilder().setLenient().create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }











}