package br.fecap.pi.organizai.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.transition.TransitionManager;

import br.fecap.pi.organizai.R;
import br.fecap.pi.organizai.domain.CategoriaCard;
import br.fecap.pi.organizai.dto.CategoriaDto;
import br.fecap.pi.organizai.dto.CategoriasAndTransacaoDto;
import br.fecap.pi.organizai.dto.TransacaoDto;
import br.fecap.pi.organizai.dto.UserDataDto;
import br.fecap.pi.organizai.manager.UserSessionManager;
import br.fecap.pi.organizai.service.ApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    ImageView btnBeneficios;

    UserDataDto user;

    private List<CategoriasAndTransacaoDto> listdataCatTransacao;

    private ApiService apiService;

    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    TextView nome;
    private int categoriaAtual = 1;
    @Override
    protected void onResume() {
        super.onResume();
        buscaPorUsuarioByEmail();
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



        buscaPorUsuarioByEmail();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        criaBarraDosMeses();
        mudaTiposCategoria();

       nome = findViewById(R.id.txt_nome_usuario);
        Button btnMeusBeneficios = findViewById(R.id.btn_meus_beneficios);
        btnMeusBeneficios.setOnClickListener(v -> {
            Intent i = new Intent(this, MeusBeneficiosActivity.class);
            i.putExtra("nome", user.getNome());
            startActivity(i);
        });

        ImageView btnconfiguracoes = findViewById(R.id.btn_config);
        btnconfiguracoes.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConfiguracoesActivity.class);
            startActivity(intent);
        });

        ImageView btnIa = findViewById(R.id.btn_ia);
        btnIa.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConstrucaoActivity.class);
            startActivity(intent);
        });

        ImageView btnBeneficios = findViewById(R.id.btn_beneficios);
        btnBeneficios.setOnClickListener(view -> {
            Intent i = new Intent(this, MeusBeneficiosActivity.class);
            i.putExtra("nome", user.getNome());
            startActivity(i);
        });

        //Implementacao do BottomSheetDialog para adicionar Receita e Despesa
        btnAdicionar = findViewById(R.id.btn_adicionar);
        btnAdicionar.setOnClickListener(v -> {
            showDialog();
        });
    }

    private void mudaTiposCategoria() {
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
                setTipoCategoriaAtual(1);
                buscarCategoriasByParam(user.getUserId(), 1);
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
                setTipoCategoriaAtual(2);
                buscarCategoriasByParam(user.getUserId(), 2);
            }
        });
    }


    private void calculaBalanco() {
        List<CategoriaDto> categoriaDtos = new ArrayList<>();

        // Itera sobre cada categoria e suas transações para acumular o total por categoria
        for (CategoriasAndTransacaoDto cat : listdataCatTransacao) {
            CategoriaDto categoria = cat.getCategoria();
            double valorTotalDaCategoria = 0;

            // Calcula o total para cada categoria a partir das suas transações
            for (TransacaoDto tranDto : cat.getTransacoes()) {
                valorTotalDaCategoria += tranDto.getValor();
            }

            // Define o total calculado na categoria
            categoria.setTotal(valorTotalDaCategoria);
            categoriaDtos.add(categoria);
        }

        // Filtra categorias de receita e despesa
        List<CategoriaDto> categoriaReceita = categoriaDtos.stream()
                .filter(categoriaDto -> categoriaDto.getTipo() == 1)
                .collect(Collectors.toList());

        List<CategoriaDto> categoriaDespesa = categoriaDtos.stream()
                .filter(categoriaDto -> categoriaDto.getTipo() == 2)
                .collect(Collectors.toList());

        // Calcula o total geral para receitas e despesas
        double totalCatReceita = categoriaReceita.stream().mapToDouble(CategoriaDto::getTotal).sum();
        double totalCatDespesa = categoriaDespesa.stream().mapToDouble(CategoriaDto::getTotal).sum();

        // Formatação com duas casas decimais
        TextView txt_receita_valor = findViewById(R.id.txt_receita_valor);
        txt_receita_valor.setText("R$ " + decimalFormat.format(totalCatReceita));

        TextView txt_despesa_valor = findViewById(R.id.txt_despesa_valor);
        txt_despesa_valor.setText("R$ " + decimalFormat.format(totalCatDespesa));

        TextView txt_numBalanco = findViewById(R.id.txt_numBalanco);
        txt_numBalanco.setText("R$ " + decimalFormat.format(totalCatReceita - totalCatDespesa));
    }


    private void montaRegistros() {
        ScrollView verticalScrollView = findViewById(R.id.vertical_scroll_view);
        LinearLayout linearRegistros = findViewById(R.id.linear_registros);
        TextView txtRegistroVazio = findViewById(R.id.txt_registro_vazio);

        // Limpa os registros anteriores
        linearRegistros.removeAllViews();

        // Mapeia as categorias e calcula o total de transações para cada uma
        List<CategoriaCard> registros = new ArrayList<>();
        for (CategoriasAndTransacaoDto cat : listdataCatTransacao) {
            CategoriaDto categoria = cat.getCategoria();
            double valorTotalDaCategoria = 0;

            // Soma os valores das transações para a categoria atual
            for (TransacaoDto tranDto : cat.getTransacoes()) {
                valorTotalDaCategoria += tranDto.getValor();
            }

            // Cria um registro com o total da categoria
            registros.add(new CategoriaCard(categoria.getCategoriaId(), categoria.getNomeCat(), valorTotalDaCategoria));
        }

        // Se houver registros, mostra a lista e oculta a mensagem de registro vazio
        if (!registros.isEmpty()) {
            txtRegistroVazio.setVisibility(View.GONE); // Oculta o texto de registro vazio
            verticalScrollView.setVisibility(View.VISIBLE); // Exibe o ScrollView

            // Adiciona cada registro como um card
            for (CategoriaCard registro : registros) {
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout card = (LinearLayout) inflater.inflate(R.layout.categoria_card, linearRegistros, false);

                ImageView imgCat = card.findViewById(R.id.categoria_icon_categoria);
                TextView nomeCategoria = card.findViewById(R.id.categoria_nome_categoria);
                TextView valorGasto = card.findViewById(R.id.categoria_valor_gasto);

                imgCat.setImageResource(registro.getCategoriaId());
                nomeCategoria.setText(registro.getCategoria());
                valorGasto.setText("R$ " + decimalFormat.format(registro.getValor()));

                // Adiciona o card ao LinearLayout
                linearRegistros.addView(card);

                // Define o comportamento de clique
                card.setOnClickListener(v -> {

                    // Filtra as transações para a categoria específica clicada
                    ArrayList<TransacaoDto> transacoesCategoria = new ArrayList<>();
                    for (CategoriasAndTransacaoDto cat : listdataCatTransacao) {
                        if (cat.getCategoria().getCategoriaId() == registro.getCategoriaId()) {
                            transacoesCategoria.addAll(cat.getTransacoes());
                            break; // Sai do loop ao encontrar a categoria correspondente
                        }
                    }

                    Intent i = new Intent(this, CategoriaEspecificaActivity.class);
                    i.putExtra("nomeCategoria", registro.getCategoria());

                    i.putParcelableArrayListExtra("especificos", transacoesCategoria);

                    startActivity(i);
                });
            }
        } else {
            // Se a lista de registros estiver vazia, exibe a mensagem de registro vazio
            txtRegistroVazio.setVisibility(View.VISIBLE);
            verticalScrollView.setVisibility(View.GONE); // Oculta o ScrollView
        }

        buscarCategoriasByParam(user.getUserId(), 0);
    }

    private void criaBarraDosMeses() {
        final HorizontalScrollView[] horizontalScrollView = {findViewById(R.id.horizontal_scroll_view)};
        LinearLayout linearLayout = findViewById(R.id.linear_meses);
        Drawable connersRounded = ContextCompat.getDrawable(this, R.drawable.banner_conners_rounded);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.saira);

        // Obtém a data atual
        Calendar calendar = Calendar.getInstance();
        int mesAtual = calendar.get(Calendar.MONTH); // 0-11
        int anoAtual = calendar.get(Calendar.YEAR);

        // Calcula os anos para exibição (ano anterior, atual e próximo)
        int anoAnterior = anoAtual - 1;
        int proximoAno = anoAtual + 1;

        // Array para armazenar os nomes dos meses abreviados
        String[] nomesMeses = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};

        // Lista para armazenar todos os meses que serão exibidos
        List<String> listaMeses = new ArrayList<>();
        List<Integer> listaAnos = new ArrayList<>();
        List<Integer> listaMesNumeros = new ArrayList<>();

        // Adiciona meses do ano anterior
        for (int i = 0; i < 12; i++) {
            listaMeses.add(nomesMeses[i] + "/" + String.format("%02d", anoAnterior % 100));
            listaAnos.add(anoAnterior);
            listaMesNumeros.add(i + 1); // Mês como índice 1-12
        }

        // Adiciona meses do ano atual
        for (int i = 0; i < 12; i++) {
            listaMeses.add(nomesMeses[i] + "/" + String.format("%02d", anoAtual % 100));
            listaAnos.add(anoAtual);
            listaMesNumeros.add(i + 1); // Mês como índice 1-12
        }

        // Adiciona meses do próximo ano
        for (int i = 0; i < 12; i++) {
            listaMeses.add(nomesMeses[i] + "/" + String.format("%02d", proximoAno % 100));
            listaAnos.add(proximoAno);
            listaMesNumeros.add(i + 1); // Mês como índice 1-12
        }

        final TextView[] selectedTextView = {null}; // Variável para armazenar o TextView selecionado

        // Cria os TextViews para cada mês
        for (int i = 0; i < listaMeses.size(); i++) {
            TextView textView = new TextView(this);

            // Obter o ano e mês atual da iteração
            int ano = listaAnos.get(i);
            int mes = listaMesNumeros.get(i);

            // Define o ID como ano * 100 + mes (por exemplo: 202502 para fevereiro de 2025)
            int id = ano * 100 + mes;
            textView.setId(id);

            // Configuração de layout com margens
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            // Adiciona margens laterais (esquerda, topo, direita, baixo)
            params.setMargins(-5, 0, 2, 0);
            textView.setLayoutParams(params);

            textView.setTag(listaMeses.get(i)); // Armazena o nome do mês como uma tag
            textView.setText(listaMeses.get(i));
            textView.setTextSize(16);
            textView.setPadding(45, 0, 45, 0);
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(typeface, Typeface.BOLD);

            // Verificar se este é o mês atual
            boolean isCurrentMonth = (ano == anoAtual && mes == (mesAtual + 1));

            if (isCurrentMonth) {
                textView.setBackground(connersRounded);
                textView.setTextColor(Color.WHITE);
                selectedTextView[0] = textView; // Define o mês atual como selecionado inicialmente

                // Scroll para o mês atual
                final int index = i; // Precisa ser final para usar em lambda
                textView.post(() -> {
                    // Calcular a posição aproximada no scrollview
                    int position = index * textView.getWidth();
                    horizontalScrollView[0].smoothScrollTo(position, 0);
                });

                setDataSelecionada(id);
                Log.d("DataSelecionada", "Mês atual selecionado: " + id);
            }

            // Adicionar OnClickListener para alterar o background quando a data for clicada
            textView.setOnClickListener(v -> {
                if (selectedTextView[0] != null) {
                    // Reseta o background da data previamente selecionada
                    selectedTextView[0].setBackground(null);
                    selectedTextView[0].setTextColor(Color.BLACK);
                }
                // Define o novo TextView como selecionado
                textView.setBackground(connersRounded);
                textView.setTextColor(Color.WHITE);
                selectedTextView[0] = textView; // Atualiza a variável para a nova seleção
                setDataSelecionada(textView.getId());
                Log.d("DataSelecionada", "Novo mês selecionado: " + textView.getId());
                buscarCategoriasByParam(user.getUserId(), getTipoCategoriaAtual());
            });

            linearLayout.addView(textView);
        }
    }



    private void buscarCategoriasByParam(final int userId, final int tipoCat) {
        String dataSelecionada = String.valueOf(getDataSelecionada()); // Exemplo: "202410"
        int ano = Integer.parseInt(dataSelecionada.substring(0, 4)); // Ano
        int mes = Integer.parseInt(dataSelecionada.substring(4, 6)); // Mês

        Call<ResponseBody> call = apiService.buscaTransacoesUserBy(String.valueOf(userId), String.valueOf(tipoCat), String.valueOf(mes), String.valueOf(ano));
        // Executa a chamada da API
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Lê o corpo da resposta como uma string
                        String responseBody = response.body().string(); // Lê o corpo da resposta
                        Gson gson = new Gson();

                        // Usando TypeToken para converter a string JSON em uma lista de CategoriasAndTransacaoDto
                        Type listType = new TypeToken<List<CategoriasAndTransacaoDto>>(){}.getType();
                        listdataCatTransacao = gson.fromJson(responseBody, listType);

                        Log.d("CatLog", "Resposta da API: " + responseBody);
                        if (tipoCat == 0){
                            calculaBalanco();
                        } else {
                            montaRegistros();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("CatLog", "Erro ao processar a resposta: " + e.getMessage());
                    }
                } else {
                    Log.e("CatLog", "Erro na resposta da API: " + response.message());
                    // Pode-se adicionar aqui uma lógica para lidar com falhas (exibir mensagem ao usuário, etc.)
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CatLog", "Erro interno: " + t.getMessage());
                // Também é possível tratar falhas de rede ou erros da API
            }
        });
    }



    private void buscaPorUsuarioByEmail() {

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        String email = sessionManager.getUserEmail();
        Call<ResponseBody> call = apiService.findUserByEmail(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        user = gson.fromJson(responseBody, UserDataDto.class);

                        nome.setText("Olá, " + user.getNome());
                        buscarCategoriasByParam(user.getUserId(), 1);
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

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

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

    }

    public int getDataSelecionada() {
        Log.d("getDataSelecionada", "ID da TextView: " + dataSelecionada);
        return dataSelecionada;
    }

    public void setDataSelecionada(int dataSelecionada) {
        this.dataSelecionada = dataSelecionada;
    }

    private int getTipoCategoriaAtual() {
        return categoriaAtual;
    }

    public void setTipoCategoriaAtual(int categoriaAtual) {
        this.categoriaAtual = categoriaAtual;
    }
}