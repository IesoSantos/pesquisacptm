package com.metro.pesquisacptm.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.metro.pesquisacptm.controller.PesquisaController;
import com.metro.pesquisacptm.model.Pesquisa;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportaCSV {

    private File diretorio;
    private File arquivoCSV;
    //private Utilidades utilidades;
    private final ArrayList<Pesquisa> pesquisas;
    private final Context context;

    public ExportaCSV(Context context) {
        //utilidades = Utilidades.getInstance(context);
        //pesquisas = utilidades.getPesquisas();
        PesquisaController controller = new PesquisaController(context);
        pesquisas = controller.getPesquisas();
        diretorio = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        this.context = context;
    }

    public ExportaCSV(Context context, ArrayList<Pesquisa> pesquisas) {
        //utilidades = Utilidades.getInstance(context);
        //pesquisas = utilidades.getPesquisas();
        PesquisaController controller = new PesquisaController(context);
        this.pesquisas = pesquisas;
        diretorio = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        this.context = context;
    }
/*
    public void exportarCSV() throws IOException {
        BufferedWriter writer = null;

        //try {
            // Verifica se o arquivo existe e exclui se necessário
            String dataFormatada=pesquisas.get(pesquisas.size()-1).getDataPesquisa().replace("/","-");
            String nomePesquisador = pesquisas.get(pesquisas.size()-1).getPesquisador().replace(" ","_").toUpperCase();
            String estacaoPesquisa = pesquisas.get(pesquisas.size()-1).getEstacaoPesquisa().getNome().replace(" ","_").toUpperCase();
            String nomeArquivo = dataFormatada+"_"+estacaoPesquisa+"_"+nomePesquisador+".csv";
            arquivoCSV = new File(diretorio, nomeArquivo);
            if (arquivoCSV.exists()) {
                arquivoCSV.delete();  // Exclui o arquivo existente
            }

            // Cria um FileOutputStream para o arquivo
            FileOutputStream fileOutputStream = new FileOutputStream(arquivoCSV);

            // Cria o OutputStreamWriter com a codificação UTF-8
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

            // Adiciona o BOM para garantir que o arquivo seja reconhecido como UTF-8
            fileOutputStream.write(0xEF);
            fileOutputStream.write(0xBB);
            fileOutputStream.write(0xBF);

            // Cria o BufferedWriter com o OutputStreamWriter
            writer = new BufferedWriter(outputStreamWriter);

            // Escreve o cabeçalho
            writer.append(
                    "Id, Pesquisador,Tipo de Pesquisa,Data da Pesquisa,Hora da Pesquisa," +
                            "Linha da Pesquisa, Gestora da Linha da Pesquisa,Estação da Pesquisa," +
                            "Área de Pesquisa," +
                            "Origem,Destino\n");

            // Escreve as informações de cada pesquisa
            for (Pesquisa pesquisa : pesquisas) {
                writer.append(pesquisa.getId() + ",")
                        .append(pesquisa.getPesquisador() + ",")
                        .append(pesquisa.getTipoPesquisa()+ ",")
                        .append(pesquisa.getDataPesquisa() + ",")
                        .append(pesquisa.getHoraPesquisa() + ",")
                        .append(pesquisa.getLinhaPesquisa().getNome() + ",")
                        .append(pesquisa.getLinhaPesquisa().getGestora() + ",")
                        .append(pesquisa.getEstacaoPesquisa().getNome() + ",")
                        .append(pesquisa.getAreaPesquisa() + ",");
                if(pesquisa.getOrigem() != null){
                    writer.append(pesquisa.getOrigem() + ",");
                }else{
                    writer.append(" ,");
                }

                if(pesquisa.getDestino() != null){
                    writer.append(pesquisa.getDestino() + ",");
                }else{
                    writer.append(" ,");
                }

                // Adiciona uma nova linha após a pesquisa
                writer.append("\n");
            }
        //} catch (IOException e) {
        //    throw new IOException("Erro ao exportar o CSV: " + e.getMessage());
        //} finally {
            if (writer != null) {
                writer.close();
            }
        //}
    }
*/
public void exportarCSV() throws IOException {
    String dataExportacao = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(new Date());
    String horaExportacao = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
    String dataHora = dataExportacao+"_"+horaExportacao;
    String dataFormatada = pesquisas.get(pesquisas.size() - 1).getDataPesquisa().replace("/", "-");
    String nomePesquisador = pesquisas.get(pesquisas.size() - 1).getPesquisador().replace(" ", "_").toUpperCase();
    String estacaoPesquisa = pesquisas.get(pesquisas.size() - 1).getEstacaoPesquisa().getNome().replace(" ", "_").toUpperCase();
    String nomeArquivo = dataFormatada + "_" + estacaoPesquisa + "_" + nomePesquisador +"_"+dataHora+ ".csv";

    ContentValues values = new ContentValues();
    values.put(MediaStore.MediaColumns.DISPLAY_NAME, nomeArquivo);
    values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);

    ContentResolver resolver = context.getContentResolver();
    Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);

    if (uri == null) {
        throw new IOException("Erro ao criar URI do arquivo");
    }

    OutputStream outputStream = resolver.openOutputStream(uri);
    if (outputStream == null) {
        throw new IOException("Erro ao abrir o OutputStream");
    }

    // BOM UTF-8
    outputStream.write(0xEF);
    outputStream.write(0xBB);
    outputStream.write(0xBF);

    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

    writer.append("Id;Pesquisador;Tipo de Pesquisa;Data da Pesquisa;" +
            "Hora da Pesquisa;" +
            "Linha da Pesquisa;Estação da Pesquisa;" +
            "Área de Pesquisa;" +
            "Origem;Destino\n");

    for (Pesquisa pesquisa : pesquisas) {
        writer.append((char) pesquisa.getId()).append(";")
                .append(pesquisa.getPesquisador()).append(";")
                .append(pesquisa.getTipoPesquisa()).append(";")
                .append(pesquisa.getDataPesquisa()).append(";")
                .append(pesquisa.getHoraPesquisa()).append(";")
                .append(pesquisa.getLinhaPesquisa().getNome()).append(";")
                //.append(pesquisa.getLinhaPesquisa().getGestora()).append(",")
                .append(pesquisa.getEstacaoPesquisa().getNome()).append(";")
                .append(pesquisa.getAreaPesquisa()).append(";");

            if (pesquisa.getOrigem() != null) {
                writer.append(pesquisa.getOrigem()).append(";");
            } else {
                writer.append(" ;");
            }

            if (pesquisa.getDestino() != null) {
                writer.append(pesquisa.getDestino()).append(";");
            } else {
                writer.append(" ;");
            }

            writer.append("\n");
        }

        writer.flush();
        writer.close();
    }


}

