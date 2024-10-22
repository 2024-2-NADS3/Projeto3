package com.example.organizaiapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.transition.TransitionManager;

import com.example.organizaiapp.R;
import com.example.organizaiapp.domain.CategoriaCard;
import com.example.organizaiapp.dto.CategoriaDto;
import com.example.organizaiapp.dto.UserDataDto;
import com.example.organizaiapp.manager.UserSessionManager;
import com.example.organizaiapp.service.ApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int dataSelecionada;
    ImageView btnAdicionar;
    UserDataDto user;
    private ApiService apiService;

    @Override
    protected void onResume() {
        super.onResume();
        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        String email = sessionManager.getUserEmail();
        buscaPorUsuarioByEmail(email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        //Implementacao de mudança de constraint entre receita e despesa
        ConstraintLayout constraintLayout = findViewById(R.id.balanco_constraint);
        View bgSelected = findViewById(R.id.bg_selected);
        TextView receitaTitulo = findViewById(R.id.txt_receita_titulo);
        TextView despesaTitulo = findViewById(R.id.txt_despesa_titulo);

        //animacação da barra de receita ao clicar
        receitaTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realiza a animação
                TransitionManager.beginDelayedTransition(constraintLayout);
                // Modifica as constraints para vincular o bg_selected ao título Receitas
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.TOP, R.id.txt_receita_titulo, ConstraintSet.TOP);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.BOTTOM, R.id.txt_receita_titulo, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.START, R.id.txt_receita_titulo, ConstraintSet.START);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.END, R.id.txt_receita_titulo, ConstraintSet.END);
                constraintSet.applyTo(constraintLayout);
                int colorVerdeForte = ContextCompat.getColor(v.getContext(), R.color.verdeForte);
                bgSelected.setBackgroundTintList(ColorStateList.valueOf(colorVerdeForte));
            }
        });

        //animacação da barra de despesa ao clicar
        despesaTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Realiza a animação
                TransitionManager.beginDelayedTransition(constraintLayout);
                // Modifica as constraints para vincular o bg_selected ao título Despesas
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.TOP, R.id.txt_despesa_titulo, ConstraintSet.TOP);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.BOTTOM, R.id.txt_despesa_titulo, ConstraintSet.BOTTOM);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.START, R.id.txt_despesa_titulo, ConstraintSet.START);
                constraintSet.connect(R.id.bg_selected, ConstraintSet.END, R.id.txt_despesa_titulo, ConstraintSet.END);
                constraintSet.applyTo(constraintLayout);
                bgSelected.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
        });

        Button btnMeusBeneficios = findViewById(R.id.btn_meus_beneficios);
        btnMeusBeneficios.setOnClickListener(v -> {
            Intent i = new Intent(this, MeusBeneficiosActivity.class);
            startActivity(i);
        });

        //Implementacao do BottomSheetDialog para adicionar Receita e Despesa
        btnAdicionar = findViewById(R.id.btn_adicionar);
        btnAdicionar.setOnClickListener(v -> {
            showDialog();
        });
    }

    private void montaInterfacePrincipal() {
        //criacao a barra de rolagem para meses do ano
        final HorizontalScrollView[] horizontalScrollView = {findViewById(R.id.horizontal_scroll_view)};
        LinearLayout linearLayout = findViewById(R.id.linear_meses);
        Drawable connersRounded = ContextCompat.getDrawable(this, R.drawable.banner_conners_rounded);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.saira);
        String[] meses = {"JAN/24", "FEV/24", "MAR/24","ABR/24","MAI/24","JUN/24", "JUL/24","AGO/24", "SET/24", "OUT/24", "NOV/24", "DEZ/24",
                "JAN/25", "FEV/25", "MAR/25","ABR/25","MAI/25", "JUN/25","JUL/25","AGO/25", "SET/25", "OUT/25", "NOV/25", "DEZ/25"};
        Calendar calendar = Calendar.getInstance();
        int mesAtual = calendar.get(Calendar.MONTH);
        int anoAtual = calendar.get(Calendar.YEAR);
        final TextView[] selectedTextView = {null}; // Variável para armazenar a data selecionada
        for (int i = 0; i < meses.length; i++) {
            TextView textView = new TextView(this);
            textView.setId(i < 12 ? (anoAtual * 100 + i + 1) : ((anoAtual + 1) * 100 + (i - 11))); // Gera um ID automático
            textView.setTag(meses[i]); // Armazena o nome do mês como uma tag
            textView.setText(meses[i]);
            textView.setTextSize(16);
            textView.setPadding(35, 0, 35, 0);
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(typeface, Typeface.BOLD);

            // Verificar se o TextView corresponde ao mês atual
            if ((i % 12) == mesAtual && (i < 12 && anoAtual == 2024 || i >= 12 && anoAtual == 2025)) {
                textView.setBackground(connersRounded);
                textView.setTextColor(Color.WHITE);
                selectedTextView[0] = textView; // Define o mês atual como selecionado inicialmente
                textView.post(() -> horizontalScrollView[0].smoothScrollTo(textView.getLeft(), 0));
                setDataSelecionada(textView.getId());
            }

            // Adicionar OnClickListener para alterar o background quando a data for clicada
            textView.setOnClickListener(v -> {
                if (selectedTextView[0] != null) {
                    // Reseta o background da data previamente selecionada
                    selectedTextView[0].setBackground(null); // Remove o background ou substitua por outro estilo
                    selectedTextView[0].setTextColor(Color.BLACK); // Restaura a cor do texto original
                }
                // Define o novo TextView como selecionado
                textView.setBackground(connersRounded);
                textView.setTextColor(Color.WHITE);
                selectedTextView[0] = textView; // Atualiza a variável para a nova seleção
                setDataSelecionada(textView.getId());
            });
            linearLayout.addView(textView);
        }
        // -----------------------------------------------------------------------------------------


        // Criação dos registros
        ScrollView verticalScrollView = findViewById(R.id.vertical_scroll_view);
        LinearLayout linearRegistros = findViewById(R.id.linear_registros);
        TextView txtRegistroVazio = findViewById(R.id.txt_registro_vazio);

        linearRegistros.removeAllViews();
        List<CategoriaCard> registros = user.getCategorias().stream()
                .filter(cat -> cat.getTipo() == 1 && cat.getTotal() != 0.00) // Filtra categorias do tipo 1 com total diferente de 0.00
                .map(cat -> new CategoriaCard(cat.getCategoriaId(), cat.getNomeCat(), cat.getTotal())) // Mapeia para CategoriaCard
                .sorted(Comparator.comparingDouble(CategoriaCard::getValor).reversed()) // Ordena por total em ordem decrescente
                .collect(Collectors.toList());

        if(!registros.isEmpty()){
            txtRegistroVazio.setVisibility(View.INVISIBLE);
            verticalScrollView.setVisibility(View.VISIBLE);
            for (CategoriaCard registro : registros){
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout card = (LinearLayout) inflater.inflate(R.layout.categoria_card, linearRegistros, false);

                ImageView imgCat = card.findViewById(R.id.categoria_icon_categoria);
                TextView nomeCategoria = card.findViewById(R.id.categoria_nome_categoria);
                TextView valorGasto = card.findViewById(R.id.categoria_valor_gasto);

                imgCat.setImageResource(registro.getCategoriaId());
                nomeCategoria.setText(registro.getCategoria());
                valorGasto.setText("R$ " + registro.getValor());

                // Adicione o card ao LinearLayout
                linearRegistros.addView(card);

                card.setOnClickListener(v -> {
                    Intent i = new Intent(this, CategoriaEspecificaActivity.class);
                    i.putExtra("nomeCategoria", registro.getCategoria());
                    startActivity(i);
                });
            }
        }
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
                        montaInterfacePrincipal();
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

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetadd);

        LinearLayout linearReceita = dialog.findViewById(R.id.linear_receita);
        LinearLayout linearDespesa = dialog.findViewById(R.id.linear_despesa);

        linearReceita.setOnClickListener(v->{
            Intent i = new Intent(this, ReceitaActivity.class);
            startActivity(i);
            dialog.cancel();
        });

        linearDespesa.setOnClickListener(v->{
            Intent i = new Intent(this, DespesaActivity.class);
            startActivity(i);
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public int getDataSelecionada() {
        Log.d("getDataSelecionada", "ID da TextView: " + dataSelecionada);
        return dataSelecionada;
    }

    public void setDataSelecionada(int dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }
}