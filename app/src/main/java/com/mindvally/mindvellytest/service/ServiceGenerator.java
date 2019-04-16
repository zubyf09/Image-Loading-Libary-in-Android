package com.mindvally.mindvellytest.service;


import android.util.Base64;


import com.mindvally.mindvellytest.utils.C;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {


    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Retrofit retrofit = new Retrofit.Builder().baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }

    public static <S> S createLoginService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic).method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            }).addInterceptor(logging);
        }

        OkHttpClient client = httpClient.build();
        //httpClient.addInterceptor(logging);

        Retrofit.Builder  builder =new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());


        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

}






