package com.kadiryaka.kalorimerkezi.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class KaloriMerkeziRestAdapter {
    private RestAdapter restAdapter;

    public KaloriMerkeziRestAdapter() {
        Gson gson = new GsonBuilder()
                // .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(Constants.service_url)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    public RestAdapter getRestAdapter() {
        return this.restAdapter;
    }
}
