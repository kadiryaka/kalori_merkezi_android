package com.kadiryaka.kalorimerkezi.resource;

import com.kadiryaka.kalorimerkezi.dto.SaveEgzDTO;
import com.kadiryaka.kalorimerkezi.dto.SaveExcersizeDTO;
import com.kadiryaka.kalorimerkezi.service.UserService;
import com.kadiryaka.kalorimerkezi.util.KaloriMerkeziRestAdapter;

import retrofit.Callback;

public class UserResource {
    private UserService userService;

    public UserResource() {
        userService = new KaloriMerkeziRestAdapter().getRestAdapter().create(UserService.class);
    }

    public void getUserSize(String token, Callback callback) {
        userService.getUserSize(token, callback);
    }

    public void getDiyetListByUser(String token, Callback callback) {
        userService.getDiyetListByUser(token, callback);
    }

    public void getExcersizeListByUser(String token, Callback callback) {
        userService.getExcersizeListByUser(token, callback);
    }

    public void getExcersizeByExcersizeTemplate(String token, String temp_id, Callback callback) {
        userService.getExcersizeByExcersizeTemplate(token, temp_id, callback);
    }

    public void getExcersizeByExcersizeTemplateAndDayId(String token, String temp_id, String dayId, Callback callback) {
        userService.getExcersizeByExcersizeTemplateAndDayId(token, temp_id, dayId, callback);
    }

    public void saveExcercize(String token, SaveExcersizeDTO saveExcersizeDTO, Callback callback) {
        userService.saveExcercize(token, saveExcersizeDTO, callback);
    }

    public void saveOtherExcercize(String token, SaveEgzDTO saveEgzDTO, Callback callback) {
        userService.saveOtherExcercize(token, saveEgzDTO, callback);
    }

    public void dateListAndEgzersizList(String token, Callback callback) {
        userService.dateListAndEgzersizList(token, callback);
    }

    public void excersizListByDate(String token, String date, Callback callback) {
        userService.excersizListByDate(token, date, callback);
    }
}
