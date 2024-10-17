package com.example.organizaiapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReceitaActivity extends AppCompatActivity {

//    private SqLiteHelper dbHelper;

    private Long categoriaIdSelected;

    private final Long userId = 1L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receita);

//        dbHelper = new SqLiteHelper(this);

        TextInputEditText valorInput = findViewById(R.id.edit_input_valor);
        TextInputEditText descInput = findViewById(R.id.edit_input_desc);

        TextView btnVoltar = findViewById(R.id.btn_voltar_receita);
        btnVoltar.setOnClickListener(v -> finish());

        TextInputEditText categoriaInput = findViewById(R.id.edit_input_categoria);
        categoriaInput.setOnClickListener(v -> {
            // Mostrar o BottomSheetDialog
             AtomicInteger categoriaIdSelected = showBottomSheetDialog();
        });


        TextInputEditText dataInput = findViewById(R.id.edit_input_data);
//        dataInput.setOnClickListener(v -> showDatePicker(dataInput));

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
//    private void showDatePicker(TextInputEditText dataInput) {
//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
//                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear; // Formato dd/MM/yyyy
//                        dataInput.setText(date);
//                    }
//                }, year, month, day);
//
//        datePickerDialog.show();
//    }

    private AtomicInteger showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_categorias, null);
        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.recycler_categories);


        AtomicInteger categoriaId = new AtomicInteger();
        List<CategoriaDto> categoriaDtos = null;
        CategoriaAdapter adapter = new CategoriaAdapter(categoriaDtos, categoriaDto -> {
            // Ação quando o usuário selecionar uma categoria
            ((TextInputEditText) findViewById(R.id.edit_input_categoria)).setText(categoriaDto.getNomeCat());
            categoriaIdSelected = (long) categoriaDto.getCategoriaId();
            bottomSheetDialog.dismiss();

        });

        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        return categoriaId;
    }

//    private List<CategoriaDto> getCategorias(Long userId) {
//
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        // Consulta para buscar as categorias do usuário do tipo 1
//        String query = "SELECT c.CategoriaId, c.nomeCat FROM UsuarioCategoria uc " +
//                "JOIN categoria c ON uc.CategoriaId = c.CategoriaId " +
//                "WHERE uc.UserId = ? AND c.tipo = 1 ORDER BY c.nomeCat asc";
//
//        // Execute a consulta
//        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(userId) });
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