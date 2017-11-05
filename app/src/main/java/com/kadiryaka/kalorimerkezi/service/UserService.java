package com.kadiryaka.kalorimerkezi.service;

import com.kadiryaka.kalorimerkezi.dto.DateListAndEgzersizListDTO;
import com.kadiryaka.kalorimerkezi.dto.DietListDTO;
import com.kadiryaka.kalorimerkezi.dto.DoListDTO;
import com.kadiryaka.kalorimerkezi.dto.ExcersizeListDTO;
import com.kadiryaka.kalorimerkezi.dto.SaveEgzDTO;
import com.kadiryaka.kalorimerkezi.dto.SaveExcersizeDTO;
import com.kadiryaka.kalorimerkezi.dto.SizeListDTO;
import com.kadiryaka.kalorimerkezi.dto.SubExcersizeListDTO;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

public interface UserService {
    @GET("/api/user/getUserSize")
    void getUserSize(@Header("kalori_token") String token, Callback<SizeListDTO> callback);

    @GET("/api/user/getDiyetListByUser")
    void getDiyetListByUser(@Header("kalori_token") String token, Callback<DietListDTO> callback);

    @GET("/api/user/getExcersizeListByUser")
    void getExcersizeListByUser(@Header("kalori_token") String token, Callback<ExcersizeListDTO> callback);

    @GET("/api/user/getExcersizeByExcersizeTemplate")
    void getExcersizeByExcersizeTemplate(@Header("kalori_token") String token, @Header("temp_id") String temp_id, Callback<SubExcersizeListDTO> callback);

    @GET("/api/user/getExcersizeByExcersizeTemplateAndDayId")
    void getExcersizeByExcersizeTemplateAndDayId(@Header("kalori_token") String token, @Header("temp_id") String temp_id,@Header("day_id") String day_id, Callback<SubExcersizeListDTO> callback);

    @POST("/api/user/save/excercize")
    void saveExcercize(@Header("kalori_token") String token, @Body SaveExcersizeDTO saveExcersizeDTO, Callback<Response> callback);

    @POST("/api/user/save/other/excercize")
    void saveOtherExcercize(@Header("kalori_token") String token, @Body SaveEgzDTO saveEgzDTO, Callback<Response> callback);

    @GET("/api/user/dateListAndEgzersizList")
    void dateListAndEgzersizList(@Header("kalori_token") String token, Callback<DateListAndEgzersizListDTO> callback);

    @GET("/api/user/excersizListByDate")
    void excersizListByDate(@Header("kalori_token") String token, @Header("date") String date, Callback<DoListDTO> callback);
}
