package com.example.organizaiapp.service;

import com.example.organizaiapp.dto.CadastroRequest;
import com.example.organizaiapp.dto.ElegivelDto;
import com.example.organizaiapp.dto.LoginRequest;
import com.example.organizaiapp.dto.QuizRequest;
import com.example.organizaiapp.dto.ResetRequest;
import com.example.organizaiapp.dto.TransacaoRequest;
import com.example.organizaiapp.dto.UpdateCategoriasUserRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/generate-key")
    Call<ResponseBody> getAESKey();

    @POST("users/isUsuario")
    Call<ResponseBody> isUsuario(@Body LoginRequest loginRequest);

    @POST("users/resetSenha")
    Call<ResponseBody> resetarSenha(@Body ResetRequest resetRequest);

    @POST("users/")
    Call<ResponseBody> cadastroUser(@Body CadastroRequest cadastroRequest);

    @GET("users/transacoes/{userId}/{tipoCategoria}/{mes}/{ano}")
    Call<ResponseBody> buscaTransacoesUserBy(@Path("userId") String userId, @Path("tipoCategoria") String tipoCat, @Path("mes") String mes, @Path("ano") String ano);

    @GET("users/{email}")
    Call<ResponseBody> findUserByEmail(@Path("email") String email);

    @POST("quiz/")
    Call<ResponseBody> criaQuizWithIsAnsweredFalse(@Body QuizRequest cadastroRequest);

    @POST("quiz/")
    Call<ResponseBody> criaQuizCompleto(@Body QuizRequest quizRequest);

    @POST("transacao/")
    Call<ResponseBody> inserirTransacao(@Body TransacaoRequest tr);

    @PUT("categoria/")
    Call<ResponseBody> updateCategoriasUser(@Body UpdateCategoriasUserRequest upRequest);

    @DELETE("users/{id}")
    Call<ResponseBody> deleteUser(@Path("id") int userId);

    @GET("quiz/{userId}/elegibilidade")
    Call<ElegivelDto> getElegibilidade(@Path("userId") int userId);
}
