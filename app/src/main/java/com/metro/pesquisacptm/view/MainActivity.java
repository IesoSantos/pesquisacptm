package com.metro.pesquisacptm.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.metro.pesquisacptm.R;
import com.metro.pesquisacptm.model.Estacao;
import com.metro.pesquisacptm.model.Linha;
import com.metro.pesquisacptm.model.Pesquisa;
import com.metro.pesquisacptm.persistence.ExportaCSV;
import com.metro.pesquisacptm.persistence.Utilidades;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Utilidades utilidades;
    private EditText txtNomePesquisador;
    private Spinner spinnerTipoPesquisa,spinnerLinha, spinnerEstacao, spinnerLocal;
    private Button btnEntrar, btnExportar, btnSair;
    private static ArrayList<Linha> linhas;
    private static ArrayList<Estacao> estacoes;
    private ArrayAdapter<Linha> arrayAdapterLinha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        utilidades = Utilidades.getInstance(this);

        txtNomePesquisador = findViewById(R.id.nomePesquisador);
        spinnerTipoPesquisa = findViewById(R.id.spinnerTipoPesquisa);
        spinnerLinha = findViewById(R.id.spinnerLinha);
        spinnerEstacao = findViewById(R.id.spinnerEstacao);
        spinnerLocal = findViewById(R.id.spinnerLocal);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnExportar = findViewById(R.id.btnExportar);
        btnSair = findViewById(R.id.btnSair);

        carregarLinhas();
        configurarSpinners();

        btnEntrar.setOnClickListener(v -> iniciarPesquisa());
        btnExportar.setOnClickListener(v -> exportarPesquisa());
        btnSair.setOnClickListener(v -> sairApp());
    }

    private void carregarLinhas() {
        //comentado para teste utilizar linhas
        //linhas = databaseHelper.getLinhas();
        linhas = utilidades.getLinhas();
    }

    private void configurarSpinners() {

        // Criando a lista de opções
        String[] options = {"Destino", "Estimativa"};

        // Criando o ArrayAdapter para popular o Spinner
        ArrayAdapter<String> adapterTipoPesquisa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapterTipoPesquisa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPesquisa.setAdapter(adapterTipoPesquisa);

        // Adicionando um listener para capturar a seleção
        spinnerTipoPesquisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Exibindo um Toast com a opção selecionada
                String tipoDePesquisa = options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Caso nada seja selecionado
            }
        });

        arrayAdapterLinha = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, linhas);
        arrayAdapterLinha.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLinha.setAdapter(arrayAdapterLinha);

        // Configurar evento de seleção do Spinner Linha
        spinnerLinha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obter a Linha selecionada
                Linha linhaSelecionada = (Linha) parentView.getItemAtPosition(position);
                // Atualizar o Spinner Estacao com base na Linha
                atualizarEstacoesSpinner(linhaSelecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Configurar evento de seleção do Spinner Estacao
        spinnerEstacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obter a Estacao selecionada
                Estacao estacaoSelecionada = (Estacao) parentView.getItemAtPosition(position);
                // Atualizar o Spinner Local com base na Estacao
                atualizarLocaisSpinner(estacaoSelecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void atualizarEstacoesSpinner(Linha linha) {
        // Atualiza as estações baseadas na linha selecionada
        estacoes = linha.getEstacoes();
        ArrayAdapter<Estacao> estacaoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estacoes);
        estacaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstacao.setAdapter(estacaoAdapter);
    }

    private void atualizarLocaisSpinner(Estacao estacao) {

        // Configura o adapter para o Spinner de Local
        ArrayAdapter<String> localAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estacao.getAreas());
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(localAdapter);
    }

    private void iniciarPesquisa() {
        String pesquisador = txtNomePesquisador.getText().toString().trim();

        Linha linha = (Linha) spinnerLinha.getSelectedItem();
        Estacao estacaoOrigem = (Estacao) spinnerEstacao.getSelectedItem();
        String local = (String) spinnerLocal.getSelectedItem();

        if (pesquisador.isEmpty()) {
            Toast.makeText(this, "Por favor, insira seu nome", Toast.LENGTH_SHORT).show();
            return;
        }

        Pesquisa pesquisa = new Pesquisa();
        pesquisa.setPesquisador(pesquisador);
        pesquisa.setLinhaPesquisa(linha);
        pesquisa.setEstacaoPesquisa(estacaoOrigem);
        pesquisa.setAreaPesquisa(local);
        pesquisa.setTipoPesquisa((String)spinnerTipoPesquisa.getSelectedItem());

/*
        new AlertDialog.Builder(this)
                .setTitle("Resultado")
                .setMessage(pesquisa+"")
                .setPositiveButton("Sim", (dialog, which) -> Log.i("Teste",pesquisa+""))
                .show();
 */

        Intent intent = null;
        if(pesquisa.getTipoPesquisa().equals("Estimativa e Destino")){
            intent = new Intent(this, PesquisaCompostaActivity.class);
        }else{
            intent = new Intent(this, PesquisaSimplesActivity.class);
        }
        intent.putExtra("pesquisa", pesquisa);
        intent.putExtra("quantidadePesquisa", 0);
        startActivity(intent);

    }

    private void exportarPesquisa() {
        ExportaCSV exportadorCSV = new ExportaCSV(this);
        new AlertDialog.Builder(this)
                .setTitle("Sucesso")
                .setMessage("Sucesso ao Exportar o Arquivo: ")
                .setPositiveButton("Ok", null)
                .show();
        try {
            exportadorCSV.exportarCSV();
        } catch (IOException e) {
            Log.e("MainActivity", "exportarPesquisa: "+e.getMessage());
            new AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Erro ao Exportar o Arquivo: "+e.getMessage())
                    .setPositiveButton("Ok", null)
                    .show();

        }
    }

    private void sairApp() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair?")
                //.setPositiveButton("Sim", (dialog, which) -> finish())
                .setPositiveButton("Sim", (dialog, which) -> finishAffinity())
                .setNegativeButton("Não", null)
                .show();
    }
}