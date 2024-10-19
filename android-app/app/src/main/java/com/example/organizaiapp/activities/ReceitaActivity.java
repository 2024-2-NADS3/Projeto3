package com.example.organizaiapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizaiapp.R;
import com.example.organizaiapp.adpater.CategoriaAdapter;
//import com.example.organizaiapp.db.SqLiteHelper;
import com.example.organizaiapp.dto.CategoriaDto;
import com.example.organizaiapp.dto.UserDataDto;
import com.example.organizaiapp.manager.UserSessionManager;
import com.example.organizaiapp.service.ApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceitaActivity extends AppCompatActivity {

    private static final int TIPO_RECEITA = 1;

    private ApiService apiService;

    private Long categoriaIdSelected;

    UserDataDto user;

    TextInputEditText categoriaInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receita);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);


//        Map<String, Integer> iconeCategorias = new HashMap<>();
//        iconeCategorias.put("Aluguel", R.drawable.icone_aluguel);
//        iconeCategorias.put("Comida", R.drawable.icone_comida);
//        iconeCategorias.put("Casa", R.drawable.icone_casa);
//        iconeCategorias.put("Gasolina", R.drawable.icone_gasolina);
//        iconeCategorias.put("Lazer", R.drawable.icone_lazer);
//        iconeCategorias.put("Outros", R.drawable.icone_outros);
//        iconeCategorias.put("Presente", R.drawable.icone_presente);
//        iconeCategorias.put("Salario", R.drawable.icone_salario);
//        iconeCategorias.put("Transporte", R.drawable.icone_transporte);
//        iconeCategorias.put("Internet", R.drawable.icone_internet);
//
//        for (Map.Entry<String, Integer> entry : iconeCategorias.entrySet()) {
//            String categoria = entry.getKey();
//            Integer id = entry.getValue();
//            Log.d("IconeCategorias", "Categoria: " + categoria + ", ID: " + id);
//        }

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        String email = sessionManager.getUserEmail();
        buscaPorUsuarioByEmail(email);

        TextView btnVoltar = findViewById(R.id.btn_voltar_receita);
        btnVoltar.setOnClickListener(v -> finish());


        categoriaInput = findViewById(R.id.edit_input_categoria);
        categoriaInput.setOnClickListener(v -> {
            showBottomSheetDialog();
        });

        TextInputEditText valorInput = findViewById(R.id.edit_input_valor);
        TextInputEditText descInput = findViewById(R.id.edit_input_desc);

        TextInputEditText dataInput = findViewById(R.id.edit_input_data);
        dataInput.setOnClickListener(v -> showDatePicker(dataInput));

        Button btnAddReceita = findViewById(R.id.btn_add_receita);
        btnAddReceita.setOnClickListener(v -> {
            String valor = valorInput.getText().toString();
            String descricao = descInput.getText().toString();
            Long categoria = categoriaIdSelected;
            String data = dataInput.getText().toString();

            if (valor.isEmpty() || descricao.isEmpty() || categoria == null || data.isEmpty()) {
                Toast.makeText(this, "Todos os campos precisam ser preenchidos", Toast.LENGTH_LONG).show();
            } else if (valor.equals("R$ 0,00")) {
                Toast.makeText(this, "Valor não pode ser R$ 0,00", Toast.LENGTH_LONG).show();
            } else {
                // Insere os dados no banco de dados
//                long id = inserirTransacao(valor, descricao, categoria, data);

//                if (id != -1) {
//                    Toast.makeText(this, "Transação adicionada com sucesso!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(this, "Erro ao adicionar transação", Toast.LENGTH_LONG).show();
//                }
                // Volta para a tela principal
                finish();
            }


          if(valorInput.getText().toString().isEmpty() || descInput.getText().toString().isEmpty()
                  || categoriaInput.getText().toString().isEmpty() || dataInput.getText().toString().isEmpty()){

//              inserirTransacao(valorInput.getText().toString(), descInput.getText().toString(), categoriaIdSelected, dataInput.getText().toString());

              Toast.makeText(this, "Todos os campos precisam ser preenchidos", Toast.LENGTH_LONG).show();
          } else if (valorInput.getText().toString().equals("R$ 0,00")) {
              Toast.makeText(this, "Valor não pode ser R$ 0,00", Toast.LENGTH_LONG).show();
          } else {
              //salva os dados no banco e volta pra tela principal

              finish();
          }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void buscaPorUsuarioByEmail(String email) {
        Call<ResponseBody> call = apiService.findUserByEmail(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        user = gson.fromJson(responseBody, UserDataDto.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Tratar o caso de resposta não bem-sucedida
                    Log.e("API Error", "Erro na resposta da API: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API Fail", "Internal error ");
            }
        });
    }

//    private long inserirTransacao(String valor, String desc, Long categoriaIdSelected, String data) {
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        // Populando os valores para a transação
//        values.put("UsuarioId", userId);
//        values.put("CategoriaId", categoriaIdSelected);
//        values.put("isReceita", 1);
//        values.put("valor", valor);
//        values.put("descricao", desc);
//        values.put("data", data);
//
//        // Inserindo os dados e retornando o ID da nova linha
//        long id = db.insert("transacoes", null, values);
//
//        db.close(); // Fechar o banco de dados após a operação
//        return id;
//    }
//
    private void showDatePicker(TextInputEditText dataInput) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear; // Formato dd/MM/yyyy
                        dataInput.setText(date);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_categorias, null);
        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.recycler_categories);

        // Filtra as categorias de tipo 1
        List<CategoriaDto> categoriasTipo1 = user.getCategorias().stream()
                .filter(categoria -> categoria.getTipo() == TIPO_RECEITA)
                .collect(Collectors.toList());

        // Cria o adapter e passa as categorias filtradas
        CategoriaAdapter adapter = new CategoriaAdapter(categoriasTipo1, categoriaDto -> {
            // Ação quando o usuário selecionar uma categoria
            categoriaInput.setText(categoriaDto.getNomeCat());
            categoriaIdSelected = (long) categoriaDto.getCategoriaId();
            bottomSheetDialog.dismiss();;
        });

        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

}