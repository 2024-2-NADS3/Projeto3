package com.example.organizaiapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.organizaiapp.dto.TransacaoRequest;
import com.example.organizaiapp.dto.UserDataDto;
import com.example.organizaiapp.manager.UserSessionManager;
import com.example.organizaiapp.service.ApiService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DespesaActivity extends AppCompatActivity {

    private static final int TIPO_DESPESA = 2;

    private ApiService apiService;

    private Long categoriaIdSelected;

    UserDataDto user;

    TextInputEditText categoriaInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_despesa);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://organizai-api-aghjahgkaucjddde.brazilsouth-01.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        String email = sessionManager.getUserEmail();
        buscaPorUsuarioByEmail(email);

        TextView btnVoltar = findViewById(R.id.btn_voltar_despesa);
        btnVoltar.setOnClickListener(v -> finish());

        categoriaInput = findViewById(R.id.edit_input_categoria);
        categoriaInput.setOnClickListener(v -> {
            // Mostrar o BottomSheetDialog
            showBottomSheetDialog();
        });

        TextInputEditText valorInput = findViewById(R.id.edit_input_valor);
        TextInputEditText descInput = findViewById(R.id.edit_input_desc);

        TextInputEditText dataInput = findViewById(R.id.edit_input_data);
        dataInput.setOnClickListener(v -> showDatePicker(dataInput));

        Button btnAddDespesa = findViewById(R.id.btn_add_despesa);

        btnAddDespesa.setOnClickListener(v -> {
            String valorStr = valorInput.getText().toString();

            // Remove os separadores de milhar e formatação de moeda, se houver
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            try {
                Number number = format.parse(valorStr);

                assert number != null;
                BigDecimal valor = BigDecimal.valueOf(number.doubleValue()).setScale(2, RoundingMode.HALF_UP);
                String descricao = descInput.getText().toString();
                Long categoria = categoriaIdSelected;
                String data = dataInput.getText().toString();

                if (valor == null || descricao.isEmpty() || categoria == null || data.isEmpty()) {
                    Toast.makeText(this, "Todos os campos precisam ser preenchidos", Toast.LENGTH_LONG).show();
                } else if (valor.equals(new BigDecimal("0.00"))) {
                    Toast.makeText(this, "Valor não pode ser R$ 0.00", Toast.LENGTH_LONG).show();
                } else {
                    TransacaoRequest tr = new TransacaoRequest(user.getUserId(),categoria,false, valor,descricao,data);
                    inserirTransacao(tr);
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }
            } catch (ParseException e) {
                Log.e("Format Error", "Erro ao formatar valor " + e);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void inserirTransacao(TransacaoRequest tr) {
        Call<ResponseBody> call = apiService.inserirTransacao(tr);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(DespesaActivity.this, "Novo registro de despesa!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DespesaActivity.this, "Erro ao inserir despesa!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DespesaActivity.this, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
            }
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

    private void showDatePicker(TextInputEditText dataInput) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String date =  selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay; // Formato yyyy/MM/dd
                        dataInput.setText(date);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_categorias, null);
        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.recycler_categories);

        // Filtra as categorias de tipo 2
        List<CategoriaDto> categoriasTipo2 = user.getCategorias().stream()
                .filter(categoria -> categoria.getTipo() == TIPO_DESPESA)
                .collect(Collectors.toList());

        // Cria o adapter e passa as categorias filtradas
        CategoriaAdapter adapter = new CategoriaAdapter(categoriasTipo2, categoriaDto -> {
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

//    private List<CategoriaDto> getCategorias(Long userId) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        // Consulta para buscar as categorias do usuário do tipo 1
//        String query = "SELECT c.CategoriaId, c.nomeCat FROM UsuarioCategoria uc " +
//                "JOIN categoria c ON uc.CategoriaId = c.CategoriaId " +
//                "WHERE uc.UserId = ? AND c.tipo = 2 ORDER BY c.nomeCat asc";
//
//        // Execute a consulta
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
//
//        // Retorna uma lista de categorias com ícones e nomes
//        List<CategoriaDto> categoriaDtos = new ArrayList<>();
//        // Iterar sobre o cursor e preencher a lista de CategoriaDto
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") int categoriaId = cursor.getInt(cursor.getColumnIndex("CategoriaId"));
//                @SuppressLint("Range") String nomeCat = cursor.getString(cursor.getColumnIndex("nomeCat"));
//                // Criar um CategoriaDto e adicionar à lista
//                categoriaDtos.add(new CategoriaDto(categoriaId, nomeCat));
//            } while (cursor.moveToNext());
//        }
//        // Fechar o cursor após a utilização
//        if (cursor != null) {
//            cursor.close();
//        }
//        return categoriaDtos;
//    }
}