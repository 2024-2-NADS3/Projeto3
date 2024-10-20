package com.example.organizaiapp.service;

import com.example.organizaiapp.dto.CadastroRequest;
import com.example.organizaiapp.dto.LoginRequest;
import com.example.organizaiapp.dto.QuizDto;
import com.example.organizaiapp.dto.QuizRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("users/isUsuario")
    Call<ResponseBody> isUsuario(@Body LoginRequest loginRequest);

    @POST("users/")
    Call<ResponseBody> cadastroUser(@Body CadastroRequest cadastroRequest);

    @GET("users/{email}")
    Call<ResponseBody> findUserByEmail(@Path("email") String email);

    @POST("quiz/")
    Call<ResponseBody> criaQuizWithIsAnsweredFalse(@Body QuizRequest cadastroRequest);
}

