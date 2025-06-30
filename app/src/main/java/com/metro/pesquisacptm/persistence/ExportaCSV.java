package com.metro.pesquisacptm.persistence;

import android.content.Context;
import android.os.Environment;
import com.metro.pesquisacptm.model.Pesquisa;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExportaCSV {

    private File diretorio;
    private File arquivoCSV;
    private Utilidades utilidades;
    private ArrayList<Pesquisa> pesquisas;

    public ExportaCSV(Context context) {
        utilidades = Utilidades.getInstance(context);
        pesquisas = utilidades.getPesquisas();
        diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    }

    public void exportarCSV() throws IOException {
        BufferedWriter writer = null;

        try {
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
        } catch (IOException e) {
            throw new IOException("Erro ao exportar o CSV: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
