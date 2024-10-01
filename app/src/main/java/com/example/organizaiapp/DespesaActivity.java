package com.example.organizaiapp;

import android.app.DatePickerDialog;
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

import com.example.organizaiapp.adpater.CategoriaAdapter;
import com.example.organizaiapp.dto.Categoria;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DespesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_despesa);

        TextInputEditText valorInput = findViewById(R.id.edit_input_valor);
        TextInputEditText descInput = findViewById(R.id.edit_input_desc);

        TextView btnVoltar = findViewById(R.id.btn_voltar_despesa);
        btnVoltar.setOnClickListener(v -> finish());

        TextInputEditText categoriaInput = findViewById(R.id.edit_input_categoria);
        categoriaInput.setOnClickListener(v -> {
            // Mostrar o BottomSheetDialog
            showBottomSheetDialog();
        });

        TextInputEditText dataInput = findViewById(R.id.edit_input_data);
        dataInput.setOnClickListener(v -> showDatePicker(dataInput));

        Button btnAddReceita = findViewById(R.id.btn_add_despesa);
        btnAddReceita.setOnClickListener(v -> {
            if(valorInput.getText().toString().isEmpty() || descInput.getText().toString().isEmpty()
                    || categoriaInput.getText().toString().isEmpty() || dataInput.getText().toString().isEmpty()){
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

        List<Categoria> categorias = getCategorias();
        CategoriaAdapter adapter = new CategoriaAdapter(categorias, categoria -> {
            // Ação quando o usuário selecionar uma categoria
            ((TextInputEditText) findViewById(R.id.edit_input_categoria)).setText(categoria.getNome());
            bottomSheetDialog.dismiss();
        });

        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private List<Categoria> getCategorias() {
        // Retorna uma lista de categorias com ícones e nomes
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(new Categoria(R.drawable.icone_comida, "Comida"));
        categorias.add(new Categoria(R.drawable.icone_transporte, "Transporte"));
        categorias.add(new Categoria(R.drawable.icone_gasolina, "Gasolina"));
        categorias.add(new Categoria(R.drawable.icone_lazer, "Lazer"));
        categorias.add(new Categoria(R.drawable.icone_casa, "Casa"));
        // Adicione mais categorias conforme necessário
        return categorias;
    }
}