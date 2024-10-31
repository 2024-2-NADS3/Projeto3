package com.example.organizaiapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.organizaiapp.R;
import com.example.organizaiapp.service.ApiService;
import com.example.organizaiapp.manager.UserSessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageView btnvoltar;
    private TextView TextViewResetarSenha;
    private TextView TextViewLogout;
    private TextView TextViewExcluirConta;
    private ApiService apiService;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        // Referências aos elementos de UI
        btnvoltar = findViewById(R.id.imageBtnVoltar);
        TextViewLogout = findViewById(R.id.TextViewLogout);
        TextViewResetarSenha = findViewById(R.id.TextViewResetarSenha);
        TextViewExcluirConta = findViewById(R.id.TextViewExcluirConta);

        // Configuração do Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Inicializa a sessão do usuário
        sessionManager = new UserSessionManager(this);

        btnvoltar.setOnClickListener(view -> {
            Intent intent = new Intent(ConfiguracoesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        TextViewLogout.setOnClickListener(view -> {
            sessionManager.clearSession();  // Limpa a sessão
            Intent intent = new Intent(ConfiguracoesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        TextViewResetarSenha.setOnClickListener(view -> {
            Intent intent = new Intent(ConfiguracoesActivity.this, ResetActivity.class);
            startActivity(intent);
        });

        TextViewExcluirConta.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir Conta")
                    .setMessage("Tem certeza de que deseja excluir sua conta?")
                    .setPositiveButton("Sim", (dialog, which) -> excluirConta())
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    // Método para excluir a conta
    private void excluirConta() {
        int userId = sessionManager.getUserId();

        Call<ResponseBody> call = apiService.deleteUser(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ConfiguracoesActivity.this, "Conta excluída com sucesso", Toast.LENGTH_SHORT).show();
                    sessionManager.clearSession();

                    Intent intent = new Intent(ConfiguracoesActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ConfiguracoesActivity.this, "Erro ao excluir conta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ConfiguracoesActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}