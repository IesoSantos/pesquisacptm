package com.metro.pesquisacptm.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import com.metro.pesquisacptm.controller.PesquisaController;
import com.metro.pesquisacptm.model.Pesquisa;
import com.metro.pesquisacptm.persistence.LogErros;
import com.metro.pesquisacptm.persistence.Utilidades;

public class PesquisaSimplesActivity extends AppCompatActivity {
    private int quantidadePesquisa;
    private CheckBox checkboxCPTM,checkboxViaMobilidade,checkboxViaQuatro,
            checkboxMetro, checkboxRua;
    private Button btnSalvar, btnEncerrar;
    TextView lblQuantidadePesquisa, lblIntrucao;
    private Pesquisa pesquisa;
    //private Utilidades utilidades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa_simples);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //utilidades = Utilidades.getInstance(this);
        pesquisa = (Pesquisa) getIntent().getSerializableExtra("pesquisa", Pesquisa.class);
        quantidadePesquisa = getIntent().getIntExtra("quantidadePesquisa",0);
        lblQuantidadePesquisa = findViewById(R.id.lblQuantidadePesquisaSimples);
        lblIntrucao = findViewById(R.id.txtInstrucaoPesquisaSimples);
        if(pesquisa.getTipoPesquisa().equals("Destino")){
            lblIntrucao.setText("Selecione o Destino:");
        }
        iniciarCheckbox();
        iniciarBotoes();
    }

    private void iniciarCheckbox(){
        checkboxCPTM=findViewById(R.id.checkboxCPTM);
        checkboxCPTM.setOnClickListener(v -> {
            checkboxMetro.setChecked(false);
            checkboxViaQuatro.setChecked(false);
            checkboxViaMobilidade.setChecked(false);
            checkboxRua.setChecked(false);
        });

        checkboxViaMobilidade=findViewById(R.id.checkboxViaMobilidade);
        checkboxViaMobilidade.setOnClickListener(v -> {
            checkboxCPTM.setChecked(false);
            checkboxMetro.setChecked(false);
            checkboxViaQuatro.setChecked(false);
            checkboxRua.setChecked(false);
        });

        checkboxViaQuatro=findViewById(R.id.checkboxViaQuatro);
        checkboxViaQuatro.setOnClickListener(v -> {
            checkboxCPTM.setChecked(false);
            checkboxMetro.setChecked(false);
            checkboxViaMobilidade.setChecked(false);
            checkboxRua.setChecked(false);
        });

        checkboxMetro=findViewById(R.id.checkboxMetro);
        checkboxMetro.setOnClickListener(v -> {
            checkboxCPTM.setChecked(false);
            checkboxViaQuatro.setChecked(false);
            checkboxViaMobilidade.setChecked(false);
            checkboxRua.setChecked(false);
        });

        checkboxRua=findViewById(R.id.checkboxRua);
        checkboxRua.setOnClickListener(v -> {
            checkboxCPTM.setChecked(false);
            checkboxMetro.setChecked(false);
            checkboxViaQuatro.setChecked(false);
            checkboxViaMobilidade.setChecked(false);
        });
    }

    private void iniciarBotoes(){
        btnSalvar = findViewById(R.id.btnSalvarPesquisaSimples);
        btnSalvar.setOnClickListener(view2 -> {
            if(checkboxCPTM.isChecked()){

                if(pesquisa.getTipoPesquisa().equals("Estimativa")){
                    pesquisa.setOrigem("CPTM");
                }else{
                    pesquisa.setDestino("CPTM");
                }
                salvar();
            }else if(checkboxMetro.isChecked()){
                if(pesquisa.getTipoPesquisa().equals("Estimativa")){
                    pesquisa.setOrigem("Metrô SP");
                }else{
                    pesquisa.setDestino("Metrô SP");
                }
                salvar();
            }else if(checkboxViaQuatro.isChecked()){
                if(pesquisa.getTipoPesquisa().equals("Estimativa")){
                    pesquisa.setOrigem("ViaQuatro");
                }else{
                    pesquisa.setDestino("ViaQuatro");
                }

                salvar();
            }else if(checkboxViaMobilidade.isChecked()){
                    if(pesquisa.getTipoPesquisa().equals("Estimativa")){
                        pesquisa.setOrigem("ViaMobilidade");
                        }else{
                        pesquisa.setDestino("ViaMobilidade");
                    }

                salvar();
            }else if(checkboxRua.isChecked()){
                    if(pesquisa.getTipoPesquisa().equals("Estimativa")){
                        pesquisa.setOrigem("Rua");
                        }else{
                        pesquisa.setDestino("Rua");
                    }

                salvar();
            }else {
                //origem = null;
                Toast.makeText(this, "Por favor, Selecione Uma Opção!", Toast.LENGTH_LONG).show();
            }

        });

        btnEncerrar = findViewById(R.id.btnEncerrarPesquisaSimples);
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
        if(selecionou()){

            //utilidades.salvarPesquisa(pesquisa);
            PesquisaController controller = new PesquisaController(this);
            try {
                controller.salvarPesquisa(pesquisa);
                quantidadePesquisa++;
                checkboxCPTM.setChecked(false);
                checkboxMetro.setChecked(false);
                checkboxViaQuatro.setChecked(false);
                checkboxViaMobilidade.setChecked(false);
                checkboxRua.setChecked(false);
                lblQuantidadePesquisa.setText("Quantidade de Pesquisas Realizadas: "+quantidadePesquisa);

            } catch (Exception e) {
                caixaDeDialogo(e.getMessage());
            }

            /*
            String dataPesquisa = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String horaPesquisa = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            pesquisa.setDataPesquisa(dataPesquisa);
            pesquisa.setHoraPesquisa(horaPesquisa);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Resultado")
                    .setMessage("Pesquisa = "+pesquisa.toString())
                    .setPositiveButton("Sim", (dialog, which) -> {
                        checkboxCPTM.setChecked(false);
                        checkboxMetro.setChecked(false);
                        checkboxViaQuatro.setChecked(false);
                        checkboxViaMobilidade.setChecked(false);
                        checkboxRua.setChecked(false);
                        this.pesquisa.setOrigem(null);
                        //origem = null;
                    })
                    .setNegativeButton("Não", null)
                    .show();

             */
        }
    }

    private boolean selecionou(){
        boolean selecionou = false;
        if(checkboxCPTM.isChecked()){
            selecionou = true;
        }else if(checkboxMetro.isChecked()){
            selecionou = true;
        } else if (checkboxViaQuatro.isChecked()) {
            selecionou = true;
        } else if (checkboxViaMobilidade.isChecked()) {
            selecionou = true;
        } else if (checkboxRua.isChecked()) {
            selecionou = true;
        }
        return selecionou;
    }

    private void caixaDeDialogo(String mensagem){
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage(mensagem)
                .setPositiveButton("Sim", (dialog, which) -> {
                    try {
                        new LogErros(mensagem);
                    } catch (Exception e) {
                        caixaDeDialogo(e.getMessage());
                    }
                })
                .show();
    }
}