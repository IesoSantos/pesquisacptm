package com.metro.pesquisacptm.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.metro.pesquisacptm.R;
import com.metro.pesquisacptm.model.Pesquisa;
import com.metro.pesquisacptm.persistence.Utilidades;

public class PesquisaCompostaActivity extends AppCompatActivity {

    private int quantidadePesquisa;
    private CheckBox checkboxCPTMOrigem,checkboxViaMobilidadeOrigem,checkboxViaQuatroOrigem,
            checkboxMetroOrigem, checkboxRuaOrigem, checkboxCPTMDestino,checkboxViaMobilidadeDestino,
            checkboxViaQuatroDestino, checkboxMetroDestino,checkboxRuaDestino;
    private Button btnSalvar, btnEncerrar;
    TextView lblQuantidadePesquisa;
    private Pesquisa pesquisa;
    private Utilidades utilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa_composta);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        utilidades = Utilidades.getInstance(this);
        pesquisa = (Pesquisa) getIntent().getSerializableExtra("pesquisa", Pesquisa.class);
        quantidadePesquisa = getIntent().getIntExtra("quantidadePesquisa",0);
        lblQuantidadePesquisa = findViewById(R.id.lblQuantidadePesquisaComposta);

        iniciarCheckboxOrigem();
        iniciarCheckboxDestino();
        iniciarBotoes();
    }

    private void iniciarCheckboxOrigem(){
        checkboxCPTMOrigem=findViewById(R.id.checkboxCPTMCompostaOrigem);
        checkboxCPTMOrigem.setOnClickListener(v -> {
            checkboxMetroOrigem.setChecked(false);
            checkboxViaQuatroOrigem.setChecked(false);
            checkboxViaMobilidadeOrigem.setChecked(false);
            checkboxRuaOrigem.setChecked(false);
        });

        checkboxViaMobilidadeOrigem=findViewById(R.id.checkboxViaMobilidadeCompostaOrigem);
        checkboxViaMobilidadeOrigem.setOnClickListener(v -> {
            checkboxCPTMOrigem.setChecked(false);
            checkboxMetroOrigem.setChecked(false);
            checkboxViaQuatroOrigem.setChecked(false);
            checkboxRuaOrigem.setChecked(false);
        });

        checkboxViaQuatroOrigem=findViewById(R.id.checkboxViaQuatroCompostaOrigem);
        checkboxViaQuatroOrigem.setOnClickListener(v -> {
            checkboxCPTMOrigem.setChecked(false);
            checkboxMetroOrigem.setChecked(false);
            checkboxViaMobilidadeOrigem.setChecked(false);
            checkboxRuaOrigem.setChecked(false);
        });

        checkboxMetroOrigem=findViewById(R.id.checkboxMetroCompostaOrigem);
        checkboxMetroOrigem.setOnClickListener(v -> {
            checkboxCPTMOrigem.setChecked(false);
            checkboxViaQuatroOrigem.setChecked(false);
            checkboxViaMobilidadeOrigem.setChecked(false);
            checkboxRuaOrigem.setChecked(false);
        });

        checkboxRuaOrigem=findViewById(R.id.checkboxRuaCompostaOrigem);
        checkboxRuaOrigem.setOnClickListener(v -> {
            checkboxCPTMOrigem.setChecked(false);
            checkboxMetroOrigem.setChecked(false);
            checkboxViaQuatroOrigem.setChecked(false);
            checkboxViaMobilidadeOrigem.setChecked(false);
        });
    }

    private void iniciarCheckboxDestino(){
        checkboxCPTMDestino=findViewById(R.id.checkboxCPTMCompostaDestino);
        checkboxCPTMDestino.setOnClickListener(v -> {
            checkboxMetroDestino.setChecked(false);
            checkboxViaQuatroDestino.setChecked(false);
            checkboxViaMobilidadeDestino.setChecked(false);
            checkboxRuaDestino.setChecked(false);
        });

        checkboxViaMobilidadeDestino=findViewById(R.id.checkboxViaMobilidadeCompostaDestino);
        checkboxViaMobilidadeDestino.setOnClickListener(v -> {
            checkboxCPTMDestino.setChecked(false);
            checkboxMetroDestino.setChecked(false);
            checkboxViaQuatroDestino.setChecked(false);
            checkboxRuaDestino.setChecked(false);
        });

        checkboxViaQuatroDestino=findViewById(R.id.checkboxViaQuatroCompostaDestino);
        checkboxViaQuatroDestino.setOnClickListener(v -> {
            checkboxCPTMDestino.setChecked(false);
            checkboxMetroDestino.setChecked(false);
            checkboxViaMobilidadeDestino.setChecked(false);
            checkboxRuaDestino.setChecked(false);
        });

        checkboxMetroDestino=findViewById(R.id.checkboxMetroCompostaDestino);
        checkboxMetroDestino.setOnClickListener(v -> {
            checkboxCPTMDestino.setChecked(false);
            checkboxViaQuatroDestino.setChecked(false);
            checkboxViaMobilidadeDestino.setChecked(false);
            checkboxRuaDestino.setChecked(false);
        });

        checkboxRuaDestino=findViewById(R.id.checkboxRuaCompostaDestino);
        checkboxRuaDestino.setOnClickListener(v -> {
            checkboxCPTMDestino.setChecked(false);
            checkboxMetroDestino.setChecked(false);
            checkboxViaQuatroDestino.setChecked(false);
            checkboxViaMobilidadeDestino.setChecked(false);
        });
    }

    private void iniciarBotoes(){
        btnSalvar = findViewById(R.id.btnSalvarPesquisaComposta);
        btnSalvar.setOnClickListener(view2 -> {
            boolean selecionouOrigem = false;
            boolean selecionouDestino = false;
            if(checkboxCPTMOrigem.isChecked()){
                pesquisa.setOrigem("CPTM");
                selecionouOrigem=true;
            }
            if(checkboxCPTMDestino.isChecked()) {
                    pesquisa.setDestino("CPTM");
                    selecionouDestino = true;
                }
            if (checkboxMetroOrigem.isChecked()) {
                    pesquisa.setOrigem("Metrô SP");
                    selecionouOrigem = true;
                }
            if (checkboxMetroDestino.isChecked()) {
                    pesquisa.setDestino("Metrô SP");
                    selecionouDestino = true;
                }
            if (checkboxViaQuatroOrigem.isChecked()) {
                    pesquisa.setOrigem("ViaQuatro");
                    selecionouOrigem = true;
                }
            if (checkboxViaQuatroDestino.isChecked()) {
                    pesquisa.setDestino("ViaQuatro");
                    selecionouDestino = true;
                }
            if (checkboxViaMobilidadeOrigem.isChecked()) {
                    pesquisa.setOrigem("ViaMobilidade");
                    selecionouOrigem = true;
                }
            if (checkboxViaMobilidadeDestino.isChecked()) {
                    pesquisa.setDestino("ViaMobilidade");
                    selecionouDestino = true;
                }
            if (checkboxRuaOrigem.isChecked()) {
                    pesquisa.setOrigem("Rua");
                    selecionouOrigem = true;
                }
            if (checkboxRuaDestino.isChecked()) {
                    pesquisa.setDestino("Rua");
                    selecionouDestino = true;
                }

            Log.i("Pesquisa Composta Botoes", "iniciarBotoes: selecionouOrigem:"+selecionouOrigem+
                    ", selecionouDestino:"+selecionouDestino);
                if (selecionouOrigem && selecionouDestino) {
                    salvar();
                }
                else if (selecionouOrigem && !selecionouDestino) {
                    Toast.makeText(this, "Por favor, Selecione o Destino", Toast.LENGTH_LONG).show();
                }
                else if (!selecionouOrigem && selecionouDestino) {
                    Toast.makeText(this, "Por favor, Selecione a Origem", Toast.LENGTH_LONG).show();
                }
                else if (!selecionouOrigem && !selecionouDestino) {
                    Toast.makeText(this, "Por favor, Selecione a Origem e o Destino", Toast.LENGTH_LONG).show();
                }
        });

        btnEncerrar = findViewById(R.id.btnEncerrarPesquisaComposta);
        btnEncerrar.setOnClickListener(view2 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Encerrar Pesquisa")
                    .setMessage("Deseja Realmente Encerrar Esta Pesquisa? ")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        this.pesquisa = null;
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    private void salvar(){
        utilidades.salvarPesquisa(pesquisa);
        quantidadePesquisa++;
        checkboxCPTMOrigem.setChecked(false);
        checkboxMetroOrigem.setChecked(false);
        checkboxViaQuatroOrigem.setChecked(false);
        checkboxViaMobilidadeOrigem.setChecked(false);
        checkboxRuaOrigem.setChecked(false);
        checkboxCPTMDestino.setChecked(false);
        checkboxMetroDestino.setChecked(false);
        checkboxViaQuatroDestino.setChecked(false);
        checkboxViaMobilidadeDestino.setChecked(false);
        checkboxRuaDestino.setChecked(false);
        lblQuantidadePesquisa.setText("Quantidade de Pesquisas Realizadas: "+quantidadePesquisa);
    }

}