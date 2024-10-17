package com.example.organizaiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.organizaiapp.dto.LoginRequest;
import com.example.organizaiapp.dto.UserDataDto;
import com.example.organizaiapp.manager.UserSessionManager;
import com.example.organizaiapp.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

//    private SqLiteHelper dbHelper;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

//        dbHelper = new SqLiteHelper(this);
//        criaCategoriasReceita();
//        criaCategoriasDespesa();
//        criaUsuarioAdmin();

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
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
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
                        UserDataDto userData = gson.fromJson(responseBody, UserDataDto.class);

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


    //    private void criaCategoriasReceita() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Verificar se as categorias já existem para evitar duplicação
//        String query = "SELECT CategoriaId FROM categoria WHERE tipo = 1";
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor.getCount() == 0) { // Se não houver categorias ainda, insira
//            ContentValues values = new ContentValues();
//
//            // Inserir categoria "Salário"
//            values.put("CategoriaId", R.drawable.icone_salario); // ID do ícone
//            values.put("nomeCat", "Salário");
//            values.put("tipo", 1); // 1 para receitas
//            db.insert("categoria", null, values);
//
//            // Inserir categoria "Aluguel"
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_aluguel); // ID do ícone
//            values.put("nomeCat", "Aluguel");
//            values.put("tipo", 1); // 1 para receitas
//            db.insert("categoria", null, values);
//
//            // Inserir categoria "Presente"
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_presente); // ID do ícone
//            values.put("nomeCat", "Presente");
//            values.put("tipo", 1); // 1 para receitas
//            db.insert("categoria", null, values);
//
//            // Inserir categoria "Outros"
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_outros); // ID do ícone
//            values.put("nomeCat", "Outros");
//            values.put("tipo", 1); // 1 para receitas
//            db.insert("categoria", null, values);
//
//            Toast.makeText(this, "Categorias de receita criadas com sucesso", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Categorias de receita já existem", Toast.LENGTH_LONG).show();
//        }
//
//        cursor.close();
//    }
//
//    private void criaCategoriasDespesa() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Verificar se as categorias já existem para evitar duplicação
//        String query = "SELECT CategoriaId FROM categoria WHERE tipo = 2";
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor.getCount() == 0) { // Se não houver categorias ainda, insira
//            ContentValues values = new ContentValues();
//
//            // Inserir categoria "Comida"
//            values.put("CategoriaId", R.drawable.icone_comida); // ID do ícone
//            values.put("nomeCat", "Comida");
//            values.put("tipo", 2);
//            db.insert("categoria", null, values);
//
//            // Inserir categoria "Transporte"
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_transporte); // ID do ícone
//            values.put("nomeCat", "Transporte");
//            values.put("tipo", 2);
//            db.insert("categoria", null, values);
//
//            // Inserir categoria "Gasolina"
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_gasolina); // ID do ícone
//            values.put("nomeCat", "Gasolina");
//            values.put("tipo", 2);
//            db.insert("categoria", null, values);
//
//            // Inserir categoria "Lazer"
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_lazer); // ID do ícone
//            values.put("nomeCat", "Lazer");
//            values.put("tipo", 2);
//            db.insert("categoria", null, values);
//
//            values.clear();
//            values.put("CategoriaId", R.drawable.icone_casa); // ID do ícone
//            values.put("nomeCat", "Casa");
//            values.put("tipo", 2);
//            db.insert("categoria", null, values);
//
//            Toast.makeText(this, "Categorias de despesa criadas com sucesso", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Categorias de despesa já existem", Toast.LENGTH_LONG).show();
//        }
//
//        cursor.close();
//    }


//    private void criaUsuarioAdmin() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Verificar se o usuário já existe para não inserir duplicado
//        String query = "SELECT UserId FROM user WHERE email = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{"admin@gmail.com"});
//
//        if (cursor.getCount() == 0) {
//            ContentValues values = new ContentValues();
//            values.put("UserId", 1); // Definindo o ID manualmente como 1
//            values.put("nome", "Admin");
//            values.put("sobrenome", "User");
//            values.put("cpf", "00000000000");
//            values.put("email", "admin@gmail.com");
//            values.put("telefone", "0000000000");
//            values.put("senha", "admin");
//
//            long newRowId = db.insert("user", null, values);
//
//            if (newRowId == -1) {
//                Toast.makeText(this, "Erro ao inserir usuário admin", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Usuário admin criado com sucesso", Toast.LENGTH_LONG).show();
//
//                // Agora, associar as categorias ao usuário admin
//                associaCategoriasAoUsuario(Long.parseLong(values.get("UserId").toString())); // ID 1 para o usuário admin
//            }
//        }
//
//        cursor.close();
//    }

//    private void associaCategoriasAoUsuario(Long userId) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Categorias a serem associadas (IDs dos ícones)
//        List<Integer> categoriaIds = findAllCategorias();
//
//        for (int categoriaId : categoriaIds) {
//            ContentValues values = new ContentValues();
//            values.put("UserId", userId);
//            values.put("CategoriaId", categoriaId);
//            db.insert("UsuarioCategoria", null, values);
//        }
//
//        Toast.makeText(this, "Categorias associadas ao usuário admin", Toast.LENGTH_LONG).show();
//    }

//    private List<Integer> findAllCategorias() {
//        List<Integer> categoriaIds = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        // Selecionar todos os IDs de categoria
//        String query = "SELECT CategoriaId FROM categoria";
//        Cursor cursor = db.rawQuery(query, null);
//
//        // Iterar pelo cursor e adicionar IDs à lista
//        while (cursor.moveToNext()) {
//            @SuppressLint("Range") int categoriaId = cursor.getInt(cursor.getColumnIndex("CategoriaId"));
//            categoriaIds.add(categoriaId);
//        }
//
//        cursor.close();
//        return categoriaIds;
//    }
}