package br.fecap.pi.organizai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.organizai.R;
import br.fecap.pi.organizai.dto.CadastroRequest;
import br.fecap.pi.organizai.dto.CategoriaDto;
import br.fecap.pi.organizai.manager.UserSessionManager;
import br.fecap.pi.organizai.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroAcitivity extends AppCompatActivity {
    private ApiService apiService;
    private UserSessionManager sessionManager;
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
        fetchAndSaveAESKey();

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
        ImageButton btnVoltar = findViewById(R.id.btnVoltar);

        btnCadastrar.setOnClickListener(v -> {
            String nome = Objects.requireNonNull(inputNome.getText()).toString();
            String sobrenome = Objects.requireNonNull(inputSobrenome.getText()).toString();
            String email = Objects.requireNonNull(inputEmail.getText()).toString();
            String senha = Objects.requireNonNull(inputSenha.getText()).toString();
            String tel = Objects.requireNonNull(inputTel.getText()).toString();
            cadastrarUser(nome, sobrenome,email,senha,tel);
        });

        btnVoltar.setOnClickListener(v ->{
            Intent i = new Intent(CadastroAcitivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        });
    }

    private void cadastrarUser(String nome, String sobrenome, String email, String senha, String tel) {


        String aeskey = sessionManager.getAesKey();

        List<CategoriaDto> categorias = gerarListaCategorias();
        CadastroRequest cadatroRequest = new CadastroRequest(nome, sobrenome,email,tel, senha, aeskey, categorias);

        Call<ResponseBody> call = apiService.cadastroUser(cadatroRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CadastroAcitivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Chame o método para buscar e salvar a chave AES

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

    private void fetchAndSaveAESKey() {
        Call<ResponseBody> keyCall = apiService.getAESKey();
        keyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String aesKey = response.body().string();
                        JSONObject jsonObject = new JSONObject(aesKey);
                        String keyValue = jsonObject.getString("key"); // Extrai o valor da chave "key"
                        saveAESKeyToPreferences(keyValue);
                        Log.d("AES_KEY", "Chave AES salva com sucesso");
                    } catch (IOException e) {
                        Log.e("AES_KEY", "Erro ao processar a resposta da chave AES", e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("AES_KEY", "Falha ao buscar chave AES: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AES_KEY", "Erro ao conectar com o servidor para buscar chave AES", t);
            }
        });
    }

    private void saveAESKeyToPreferences(String aesKey) {
        sessionManager = new UserSessionManager(getApplicationContext());
        sessionManager.saveAESKeyToPreferences(aesKey);
    }

}
