package com.kadiryaka.kalorimerkezi.service;

import com.kadiryaka.kalorimerkezi.dto.EgzListDTO;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;

public interface ServicesService {
    @GET("/api/services/getAllExcersize")
    void getAllExcersize(@Header("kalori_token") String token, Callback<EgzListDTO> callback);
}
