package com.metro.pesquisacptm.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metro.pesquisacptm.R;
import com.metro.pesquisacptm.controller.PesquisaController;
import com.metro.pesquisacptm.model.Pesquisa;
import com.metro.pesquisacptm.persistence.ExportaCSV;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExportarActivity extends AppCompatActivity {
    private ArrayList<Pesquisa> pesquisas;
    private RecyclerView listaPesquisa;
    //private ImageView btnVoltar, btnExportar, btnFechar;

    //private Button btnExportar;

    private final Calendar calendario = Calendar.getInstance();
    private EditText txtDataInicial, txtDataFinal;
    private PesquisaRecycleAdapter pesquisaRecycleAdapter;
    private PesquisaController pesquisaController;
    private TextView tvCabecalho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exportar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pesquisaController = new PesquisaController(this);
        inicializaArrayPesquisas();
        inicializaListaPesquisa();

        iniciarBotaoExportar();
        /*
        iniciarBotaoFechar();
        */
        iniciarBotaoVoltar();

        inicializarEditText();
        inicializarBtnFiltrar();
        tvCabecalho = findViewById(R.id.tvCabecalhoExportaArquivo);
        tvCabecalho.setText("Exportar Arquivo em CSV - Total de Pesquisas: "+pesquisas.size());
    }


    private void inicializaArrayPesquisas(){
        //Utilidades utilidades = Utilidades.getInstance(this);
        //pesquisas = utilidades.getPesquisas();
        pesquisas = pesquisaController.getPesquisas();
        //ExportaCSV exportadorCSV = new ExportaCSV(this);
    }

    private void inicializaListaPesquisa(){
        listaPesquisa = findViewById(R.id.lstPesquisas);
        GridLayoutManager lm = new GridLayoutManager(this,1);
        listaPesquisa.setLayoutManager(lm);
        pesquisaRecycleAdapter = new PesquisaRecycleAdapter(pesquisas);
        listaPesquisa.setAdapter(pesquisaRecycleAdapter);

    }


    private void iniciarBotaoVoltar(){
        Button btnVoltar = findViewById(R.id.btnVoltarExportar);
        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarBotaoExportar(){
        Button btnExportar = findViewById(R.id.btnExportarPesquisa);
        btnExportar.setOnClickListener(v -> {
            //ExportaCSV exportadorCSV = new ExportaCSV(this);
            ExportaCSV exportadorCSV = new ExportaCSV(this, this.pesquisas);

            try {
                exportadorCSV.exportarCSV();
                new AlertDialog.Builder(this)
                        .setTitle("Sucesso")
                        .setMessage("Sucesso ao Exportar o Arquivo: ")
                        .setPositiveButton("Ok", null)
                        .show();
            } catch (IOException e) {
                Log.e("MainActivity", "exportarPesquisa: "+e.getMessage());
                new AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Erro ao Exportar o Arquivo: "+e.getMessage())
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });
    }

    /*
        private void iniciarBotaoFechar(){

            btnFechar = findViewById(R.id.btnFecharExportar);
            btnFechar.setOnClickListener(view2 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Encerrar Exportação")
                        .setMessage("Deseja Realmente Encerrar a Exportação? ")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton("Não", null)
                        .show();
            });
        }

        private void iniciarBotaoVoltar(){
            btnVoltar = findViewById(R.id.btnVoltarExportar);
            btnVoltar.setOnClickListener(view2 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Encerrar Exportação")
                        .setMessage("Deseja Retornar a Tela Anterior? ")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            Intent intent = new Intent(this, MainActivity.class);

                            startActivity(intent);
                        })
                        .setNegativeButton("Não", null)
                        .show();
            });
        }
         */
    private void inicializarEditText(){
        txtDataInicial = findViewById(R.id.dt_inicio_pesquisa);
        txtDataInicial.setOnClickListener(v -> {

            DatePickerDialog dialogo = new DatePickerDialog(ExportarActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendario.set(Calendar.YEAR,year);
                        calendario.set(Calendar.MONTH,month);
                        calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        String formatacao = "dd/MM/yyyy";
                        SimpleDateFormat dataFormat = new SimpleDateFormat(formatacao, Locale.US);
                        txtDataInicial.setText(dataFormat.format(calendario.getTime()));
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH));
            dialogo.show();
        });
        txtDataFinal = findViewById(R.id.dt_fim_pesquisa);
        txtDataFinal.setOnClickListener(v -> {

            DatePickerDialog dialogo = new DatePickerDialog(ExportarActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendario.set(Calendar.YEAR,year);
                        calendario.set(Calendar.MONTH,month);
                        calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        String formatacao = "dd/MM/yyyy";
                        SimpleDateFormat dataFormat = new SimpleDateFormat(formatacao, Locale.US);
                        txtDataFinal.setText(dataFormat.format(calendario.getTime()));
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH));

            dialogo.show();
        });
    }

    private void inicializarBtnFiltrar(){
        Button btnFiltrar = findViewById(R.id.btnFiltarPesquisa);
        btnFiltrar.setOnClickListener(v -> {

            String dtInicial = txtDataInicial.getText().toString();
            String dtFinal = txtDataFinal.getText().toString();

            if(!dtInicial.isEmpty() && dtFinal.isEmpty()){
                //pesquisas = pesquisaController.getPesquisaPorData(dtInicial);
                pesquisas = pesquisaController.getPesquisaDataInicial(dtInicial);
                pesquisaRecycleAdapter.atualizarLista(pesquisas);
            }else if(dtInicial.isEmpty() && !dtFinal.isEmpty()){
                //pesquisas = pesquisaController.getPesquisaPorData(dtInicial);
                pesquisas = pesquisaController.getPesquisaPorDataFinal(dtFinal);
                pesquisaRecycleAdapter.atualizarLista(pesquisas);
            }else if((!dtInicial.isEmpty() && !dtFinal.isEmpty()) && dtInicial.equals(dtFinal)){
                //pesquisas = pesquisaController.getPesquisaPorData(dtInicial);
                pesquisas = pesquisaController.getPesquisaPorData(dtInicial);
                pesquisaRecycleAdapter.atualizarLista(pesquisas);
            }else if(!dtInicial.isEmpty() && !dtFinal.isEmpty()){
                //pesquisas = pesquisaController.getPesquisaPorData(dtInicial);
                pesquisas = pesquisaController.getPesquisaEntreDatas(dtInicial, dtFinal);
                pesquisaRecycleAdapter.atualizarLista(pesquisas);
            }else{
                pesquisas = pesquisaController.getPesquisas();
                pesquisaRecycleAdapter.atualizarLista(pesquisas);
            }
            txtDataInicial.setText("");
            txtDataFinal.setText("");

            tvCabecalho.setText("Exportar Arquivo em CSV - Total de Pesquisas: "+pesquisas.size());

            //pesquisaRecycleAdapter.atualizarListaTeste(pesquisas);
            /*
            new AlertDialog.Builder(this)
                    .setTitle("Teste")
                    .setMessage("Pressionou o Botão Filtrar ")
                    .setPositiveButton("Ok", null)
                    .show();
             */
        });
    }

}