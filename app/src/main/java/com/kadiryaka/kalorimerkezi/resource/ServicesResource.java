package com.kadiryaka.kalorimerkezi.resource;

import com.kadiryaka.kalorimerkezi.service.ServicesService;
import com.kadiryaka.kalorimerkezi.util.KaloriMerkeziRestAdapter;

import retrofit.Callback;

public class ServicesResource {
    private ServicesService servicesService;

    public ServicesResource() {
        servicesService = new KaloriMerkeziRestAdapter().getRestAdapter().create(ServicesService.class);
    }

    public void getAllExcersize(String token, Callback callback) {
        servicesService.getAllExcersize(token, callback);
    }
}
