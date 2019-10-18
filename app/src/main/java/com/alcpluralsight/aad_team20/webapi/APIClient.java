package com.alcpluralsight.aad_team20.webapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.alcpluralsight.aad_team20.webapi.Constant.BASE_URL;

public class APIClient {

    private static Retrofit retrofit = null;
    private static APIClient instance;

    public APIClient() {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static synchronized APIClient getInstance(){
        if (instance == null)
        {
            instance = new APIClient();
        }
        return instance;
    }

    public static APIService getApiService(){
        return retrofit.create(APIService.class);
    }
}

