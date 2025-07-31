package com.metro.pesquisacptm.persistence;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogErros {

    private File diretorio;
    private File arquivoCSV;;

    public LogErros(String mensagem) throws Exception{
        this.diretorio = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        gravarErro(mensagem);

    }

    public void gravarErro(String mensagem) throws Exception {
        BufferedWriter writer = null;

        String dataFormatada = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String nomeArquivo = "LogErro" + dataFormatada + ".csv";
        // Verifica se o arquivo existe e exclui se necessário
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

        // Escreve o Erro
        writer.append(mensagem);

        if (writer != null) {
            writer.close();
        }

    }
}

