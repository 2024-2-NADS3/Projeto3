package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.organizaiapp.R;
//import com.example.organizaiapp.db.SqLiteHelper;
import com.example.organizaiapp.dto.CategoriaDto;
import com.example.organizaiapp.dto.LoginRequest;
import com.example.organizaiapp.dto.UpdateCategoriasUserRequest;
import com.example.organizaiapp.dto.UserDataDto;
import com.example.organizaiapp.manager.UserSessionManager;
import com.example.organizaiapp.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ApiService apiService;

    private UserDataDto userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


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

        TextInputEditText inputEmail = findViewById(R.id.textInputEmail);
        TextInputEditText inputSenha = findViewById(R.id.textInputPassword);
        Button btnEntrar = findViewById(R.id.btnEntrar);
        TextView txtCadastrese = findViewById(R.id.txtCadastrese);
        TextView txtReset = findViewById(R.id.textreset);


        btnEntrar.setOnClickListener(v -> {
            String email = Objects.requireNonNull(inputEmail.getText()).toString();
            String senha = Objects.requireNonNull(inputSenha.getText()).toString();
            verificarLogin(email,senha);
        });

        txtCadastrese.setOnClickListener(v ->{
            Intent i = new Intent(LoginActivity.this, CadastroAcitivity.class);
            startActivity(i);

            finish();
        });

        txtReset.setOnClickListener(v ->{
            Intent i = new Intent(LoginActivity.this, ResetActivity.class);
            startActivity(i);

            finish();
        });
    }

    private void verificarLogin(String email, String senha) {
        LoginRequest loginRequest = new LoginRequest(email, senha);
        Call<ResponseBody> call = apiService.isUsuario(loginRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    isQuizAnswered(email)
                            .thenAccept(isAnswered -> {
                                if (isAnswered) {
                                    verificaCategorias(userData.getUserId(), userData.getCategorias());
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    verificaCategorias(userData.getUserId(), userData.getCategorias());
                                    Intent i = new Intent(LoginActivity.this, IntroActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .exceptionally(throwable -> {
                                Toast.makeText(LoginActivity.this, "Erro ao verificar o quiz", Toast.LENGTH_LONG).show();
                                return null;
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Usuário não autorizado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verificaCategorias(int userId, List<CategoriaDto> categoriasDb) {
        List<CategoriaDto> categoriaOfDevice = gerarListaCategorias();

        // Ordenar categoriasDb em ordem alfabética pelo nomeCat
        Collections.sort(categoriasDb, new Comparator<CategoriaDto>() {
            @Override
            public int compare(CategoriaDto cat1, CategoriaDto cat2) {
                return cat1.getNomeCat().compareTo(cat2.getNomeCat());
            }
        });

        // Ordenar categoriaOfDevice em ordem alfabética pelo nomeCat
        Collections.sort(categoriaOfDevice, new Comparator<CategoriaDto>() {
            @Override
            public int compare(CategoriaDto cat1, CategoriaDto cat2) {
                return cat1.getNomeCat().compareTo(cat2.getNomeCat());
            }
        });

        // Atualizar categoriaOfDevice com os totais de categoriasDb se necessário
        for (CategoriaDto catDb : categoriasDb) {
            for (CategoriaDto catDevice : categoriaOfDevice) {
                // Verifica se os nomes das categorias são iguais
                if (catDb.getNomeCat().equals(catDevice.getNomeCat())) {
                    // Se o total da categoria do banco for diferente de 0.00, atualiza o total na categoria do dispositivo
                    if (catDb.getTotal() != 0.00) {
                        catDevice.setTotal(catDb.getTotal()); // Atualiza o total
                    }
                    break; // Sai do loop interno se a categoria for encontrada
                }
            }
        }

        if (categoriasDb.get(0).getCategoriaId() != categoriaOfDevice.get(0).getCategoriaId()) {
            UpdateCategoriasUserRequest upRequest = new UpdateCategoriasUserRequest(userId, categoriaOfDevice);
            Call<ResponseBody> call = apiService.updateCategoriasUser(upRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "As categorias foram sincronizadas", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "Erro ao sincronizar categorias", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private List<CategoriaDto> gerarListaCategorias() {
        List<CategoriaDto> categorias = new ArrayList<>();

        Map<String, Integer> categoriasReceita = new HashMap<>();
        categoriasReceita.put("Outros", R.drawable.icone_outros);
        categoriasReceita.put("Presente", R.drawable.icone_presente);
        categoriasReceita.put("Salario", R.drawable.icone_salario);

        for (Map.Entry<String, Integer> entry : categoriasReceita.entrySet()) {
            String categoria = entry.getKey();
            Integer id = entry.getValue();
            categorias.add(new CategoriaDto(id,categoria,1,0.00));
        }

        Map<String, Integer> categoriasDespesa = new HashMap<>();
        categoriasDespesa.put("Aluguel", R.drawable.icone_aluguel);
        categoriasDespesa.put("Comida", R.drawable.icone_comida);
        categoriasDespesa.put("Casa", R.drawable.icone_casa);
        categoriasDespesa.put("Gasolina", R.drawable.icone_gasolina);
        categoriasDespesa.put("Lazer", R.drawable.icone_lazer);
        categoriasDespesa.put("Transporte", R.drawable.icone_transporte);
        categoriasDespesa.put("Internet", R.drawable.icone_internet);

        for (Map.Entry<String, Integer> entry : categoriasDespesa.entrySet()) {
            String categoria = entry.getKey();
            Integer id = entry.getValue();
            categorias.add(new CategoriaDto(id,categoria,2,0.00));
        }
        return categorias;
    }

    private CompletableFuture<Boolean> isQuizAnswered(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Call<ResponseBody> call = apiService.findUserByEmail(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        userData = gson.fromJson(responseBody, UserDataDto.class);

                        //salva na userSession o email e o id do usuario logado
                        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
                        sessionManager.saveUserSession(userData.getEmail(), userData.getUserId());

                        boolean isAnswered = userData.getQuiz() != null && userData.getQuiz().getIsAnswered();
                        future.complete(isAnswered);
                    } catch (IOException e) {
                        future.completeExceptionally(e);
                    }
                } else {
                    future.complete(false); // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

}