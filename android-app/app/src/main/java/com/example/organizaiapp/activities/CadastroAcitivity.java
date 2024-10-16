package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.R;
import com.example.organizaiapp.dto.CadastroRequest;
import com.example.organizaiapp.dto.LoginRequest;
import com.example.organizaiapp.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroAcitivity extends AppCompatActivity {
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_acitivity);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextInputEditText inputNome = findViewById(R.id.textInputNome);
        TextInputEditText inputSobrenome = findViewById(R.id.textInputSobrenome);
        TextInputEditText inputEmail = findViewById(R.id.textInputEmail);
        TextInputEditText inputSenha = findViewById(R.id.textInputPassword);
        TextInputEditText inputTel = findViewById(R.id.textInputTelefone);
        Button btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            String nome = Objects.requireNonNull(inputNome.getText()).toString();
            String sobrenome = Objects.requireNonNull(inputSobrenome.getText()).toString();
            String email = Objects.requireNonNull(inputEmail.getText()).toString();
            String senha = Objects.requireNonNull(inputSenha.getText()).toString();
            String tel = Objects.requireNonNull(inputTel.getText()).toString();
            cadastrarUser(nome, sobrenome,email,senha,tel);
        });
    }

    private void cadastrarUser(String nome, String sobrenome, String email, String senha, String tel) {

        CadastroRequest cadatroRequest = new CadastroRequest(nome, sobrenome,email,tel, senha);
        Call<ResponseBody> call = apiService.cadastroUser(cadatroRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CadastroAcitivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CadastroAcitivity.this, LoginActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(CadastroAcitivity.this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CadastroAcitivity.this, "Erro de conexão com o servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}
