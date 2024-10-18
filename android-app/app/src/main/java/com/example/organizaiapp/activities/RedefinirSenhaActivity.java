package com.example.organizaiapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.organizaiapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class RedefinirSenhaActivity extends AppCompatActivity {

    // Referências aos campos de texto e botões
    private TextInputEditText TextInputEditNovaSenha;
    private TextInputEditText TextInputEditConfirmarNovaSenha;  // Ajustado para corresponder ao ID no XML
    private Button buttonEnviar;
    private Button buttonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinicaodesenha);

        // Referências aos campos de texto e botões
        TextInputEditNovaSenha = findViewById(R.id.TextInputEditTextNovaSenha);
        TextInputEditConfirmarNovaSenha = findViewById(R.id.TextInputEditConfirmarNovaSenha);  // Corrigido aqui

        buttonEnviar = findViewById(R.id.buttonEnviar);
        buttonCancelar = findViewById(R.id.buttonCancelar);

        // Lógica do botão "Enviar"
        buttonEnviar.setOnClickListener(view -> {
            String novaSenha = TextInputEditNovaSenha.getText().toString().trim();
            String confirmarSenha = TextInputEditConfirmarNovaSenha.getText().toString().trim();

            // Verifica se as senhas coincidem
            if (novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(RedefinirSenhaActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else if (!novaSenha.equals(confirmarSenha)) {
                // Senhas não coincidem
                Toast.makeText(RedefinirSenhaActivity.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            } else {
                // Senhas coincidem: proceder com a redefinição
                Toast.makeText(RedefinirSenhaActivity.this, "Redefinição de senha bem-sucedida!", Toast.LENGTH_SHORT).show();

                // Aqui você pode enviar a nova senha para a API quando ela estiver disponível
            }
        });

        // Lógica do botão "Cancelar"
        buttonCancelar.setOnClickListener(view -> {
            // Fecha a tela e retorna para a anterior
            finish();
        });
    }
}


