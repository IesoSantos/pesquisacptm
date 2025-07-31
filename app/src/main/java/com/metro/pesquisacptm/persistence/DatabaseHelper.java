package com.metro.pesquisacptm.persistence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Handler;
import android.os.Looper;

import com.metro.pesquisacptm.model.Estacao;
import com.metro.pesquisacptm.model.Linha;
import com.metro.pesquisacptm.model.Pesquisa;


public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "pesquisa_od_gestoras.db";
    private static final int DATABASE_VERSION = 1; // Atualizado para refletir a nova estrutura
    private final Context context;
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DatabaseHelper - OnCreate", "Entrou no OnCreate");
        this.db = db;
        criaTabelas();

        // Inserir dados iniciais nas tabelas
        inserirLinhas();
        Log.i("DatabaseHelper - OnCreate", "Inseriu as Linhas com Sucesso");
        inserirEstacoes();
        Log.i("DatabaseHelper - OnCreate", "Inseriu as Estacoes com Sucesso");
        inserirGestoras();
        Log.i("DatabaseHelper - OnCreate", "Inseriu as Gestoras com Sucesso");


        // Adiar a execução da leitura das linhas e estações para após a criação
        //postCreationOperations();
        //Log.i("DatabaseHelper - OnCreate", "Inseriu as Areas Comuns com Sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void postCreationOperations() {
        // Esse método será chamado após a criação do banco, utilizando um Handler para garantir a execução
        Log.i("DatabaseHelper - postCreationOperations", "Entrou no postCreationOperations");
        new Handler(Looper.getMainLooper()).post(() -> {
            Log.i("DatabaseHelper - postCreationOperations", "Entrou no Handler postCreationOperations");
            ArrayList<Linha> linhaArrayList = getLinhasComEstacaoSemAreas();

            for (Linha linha : linhaArrayList) {
                for (Estacao estacao : linha.getEstacoes()) {
                    inserirAreasComuns(estacao.getId());
                }
            }
        });
    }

    private void criaTabelas(){
        //Log.i("DatabaseHelper - Criacao de Tabela", "onCreate: Linhas");
        db.execSQL("PRAGMA foreign_keys=ON;");

        // Criação da tabela Linhas
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Linhas (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "nm_linha TEXT NOT NULL, " +
                    "sg_linha TEXT NOT NULL, " +
                    "nm_gestora TEXT NOT NULL)");
        } catch (Exception e) {
            Log.e("Erro - DatabaseHelper - OnCreate", "Erro na criação da tabela Linhas: " + e.getMessage());
        }

        // Criação da tabela Estacoes com chave estrangeira para Linhas
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Estacoes (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "nm_estacao TEXT NOT NULL, " +
                    "sg_estacao TEXT NOT NULL, " +
                    "id_linha INTEGER, " +
                    "isTransferencia INTEGER NOT NULL, " +
                    "FOREIGN KEY (id_linha) REFERENCES Linhas(id))");
            //Log.i("Criacao de Tabela", "onCreate: Estacoes");
        } catch (Exception e) {
            Log.e("Erro - DatabaseHelper - OnCreate", "Erro na criação da tabela Estacoes: " + e.getMessage());
        }

        // Criação da tabela Area com chave estrangeira para Estacoes
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Areas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "nm_area TEXT NOT NULL, " +
                    "id_estacao INTEGER, " +
                    "FOREIGN KEY (id_estacao) REFERENCES Estacoes(id))");

        } catch (Exception e) {
            Log.e("Erro - DatabaseHelper - OnCreate", "Erro na criação da tabela Area: " + e.getMessage());
        }

        // Criação da tabela Area com chave estrangeira para Estacoes
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Gestoras (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "nm_gestora TEXT NOT NULL)");

        } catch (Exception e) {
            Log.e("Erro - DatabaseHelper - OnCreate", "Erro na criação da tabela Area: " + e.getMessage());
        }

        // Criação da tabela Pesquisas com chave estrangeira para Estacoes e Linhas
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS Pesquisas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "nm_pesquisador TEXT NOT NULL," +
                    "ds_tipo_pesquisa TEXT NOT NULL," +
                    "dt_pesquisa TEXT NOT NULL, " +
                    "hr_pesquisa TEXT NOT NULL, " +
                    "id_linha_pesquisa INTEGER NOT NULL, " +
                    "id_estacao_pesquisa INTEGER NOT NULL, " +
                    "nm_area_pesquisa TEXT NOT NULL, " +
                    "nm_origem TEXT, " +
                    "nm_destino TEXT, " +
                    "FOREIGN KEY (id_estacao_pesquisa) REFERENCES Estacoes(id), " +
                    "FOREIGN KEY (id_linha_pesquisa) REFERENCES Linhas(id))");
            //Log.i("Criacao de Tabela", "onCreate: Pesquisa");
        } catch (Exception e) {
            Log.e("Erro - DatabaseHelper - OnCreate" , "Erro Criacao da Tabela pesquisa: "+e.getMessage());
        }

    }

    public void inserirLinhas() {
        // Implementação do método para inserir dados iniciais de Linhas
        //Log.i("DatabaseHelper - InsereLinhas", "Inserindo Linhas");
        //SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Inserir dados na tabela Linhas
            db.execSQL("INSERT INTO Linhas (id, nm_linha, sg_linha, nm_gestora) VALUES (7, 'Linha 7 - Rubi', 'L7', 'CPTM')");
            db.execSQL("INSERT INTO Linhas (id, nm_linha, sg_linha, nm_gestora) VALUES (10, 'Linha 10 - Turquesa', 'L10', 'CPTM')");
            db.execSQL("INSERT INTO Linhas (id, nm_linha, sg_linha, nm_gestora) VALUES (11,'Linha 11 - Coral', 'L11', 'CPTM')");
            db.execSQL("INSERT INTO Linhas (id, nm_linha, sg_linha, nm_gestora) VALUES (12,'Linha 12 - Safira', 'L12', 'CPTM')");
            db.execSQL("INSERT INTO Linhas (id, nm_linha, sg_linha, nm_gestora) VALUES (13,'Linha 13 - Jade', 'L13', 'CPTM')");
            db.setTransactionSuccessful();
            //Log.i("DatabaseHelper - inserirLinhas","Sucesso ao Criar Linhas");
        }catch (Exception e){
            Log.e("DatabaseHelper - inserirLinhas Erro", "Erro ao inserirLinhas: "+e.getMessage() );
        }finally {
            db.endTransaction();
        }
    }

    public void inserirGestoras() {
        // Implementação do método para inserir dados iniciais de Linhas
        //Log.i("DatabaseHelper - InsereLinhas", "Inserindo Linhas");
        //SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Inserir dados na tabela Linhas
            db.execSQL("INSERT INTO Gestoras (id, nm_gestora) VALUES (1,'Metrô SP')");
            db.execSQL("INSERT INTO Gestoras (id, nm_gestora) VALUES (2,'ViaQuatro')");
            db.execSQL("INSERT INTO Gestoras (id, nm_gestora) VALUES (3,'ViaMobilidade')");
            db.execSQL("INSERT INTO Gestoras (id, nm_gestora) VALUES (4, 'CPTM')");
            db.setTransactionSuccessful();
            //Log.i("DatabaseHelper - inserirLinhas","Sucesso ao Criar Linhas");
        }catch (Exception e){
            Log.e("DatabaseHelper - inserirLinhas Erro", "Erro ao inserirLinhas: "+e.getMessage() );
        }finally {
            db.endTransaction();
        }
    }

    public void inserirEstacoes() {
        //Log.i("DatabaseHelper - InserirEstacao", "Inserindo Estacao");

        db.beginTransaction();

        try {
            // Inserir dados na tabela Estações


            //Linha 7 - Rubi (CPTM)
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (84, 7, 'Brás','BAS',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (85, 7, 'Luz','LUZ',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (86, 7, 'Barra Funda','BFU',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (87, 7, 'Água Branca','ABR',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (88, 7, 'Lapa', 'LPA',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (89, 7, 'Piqueri' ,'PQR',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (90, 7, 'Pirituba' ,'PRT',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (91, 7, 'Vila Clarice','VCL',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (92, 7, 'Jaraguá','JRG',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (93, 7, 'Vila Aurora','VAU',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (94, 7, 'Perus','PRU',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (95, 7, 'Caieiras','CAI',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (96, 7, 'Franco da Rocha','FDR',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (97, 7, 'Baltazar Fidélis','BFI',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (98, 7, 'Francisco Morato','FMO',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (99, 7, 'Botujuru','BTJ',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (100, 7, 'Campo Limpo Paulista' ,'CLP',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (101, 7, 'Várzea Paulista','VPL',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (102, 7, 'Jundiaí','JUN',0)");
            //Linha 10 - Turquesa
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (144, 10,'Rio Grande da Serra','RGS',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (145, 10,'Ribeirão Pires','RPI',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (146, 10,'Guapituba','GPT',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (147, 10,'Mauá','MAU',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (148, 10,'Capuava','CPV',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (149, 10,'Santo André','SAN',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (150, 10,'Prefeito Saladino','PSA',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (151, 10,'Utinga','UTG',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (152, 10,'São Caetano do Sul','SCS',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (153, 10,'Tamanduateí','TMD',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (154, 10,'Ipiranga','IPG',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (155, 10,'Juventus–Mooca','MOC',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (156, 10,'Brás','BAS',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (157, 10,'Luz','LUZ',1)");
            // Linha 11 - Coral CPTM
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (158, 11,'Luz','LUZ',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (159, 11,'Brás','BAS',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (160, 11,'Tatuapé','TAT',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (161, 11,'Itaquera','ITQ',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (162, 11,'Dom Bosco','DBO',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (163, 11,'José Bonifácio','JBO',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (164, 11,'Guaianases','GUA',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (165, 11,'Antônio Gianetti Neto','AGN',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (166, 11,'Ferraz de Vasconcelos','FVC',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (167, 11,'Poá','POÁ',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (168, 11,'Calmon Viana','CVN',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (169, 11,'Suzano','SUZ',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (170, 11,'Jundiapeba','JPB',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (171, 11,'Braz Cubas','BCB',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (172, 11,'Mogi das Cruzes','MDC',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (173, 11,'Estudantes','EST',0)");
            //Linha 12 - Safira - CPTM
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (174, 12,'Brás','BAS',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (175, 12,'Tatuapé','TAT',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (176, 12,'Engenheiro Goulart','EGO',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (177, 12,'USP Leste','USL',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (178, 12,'Comendador Ermelino','ERM',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (179, 12,'São Miguel Paulista','SMP',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (180, 12,'Jardim Helena–Vila Mara','JHE',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (181, 12,'Itaim Paulista','ITI',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (182, 12,'Jardim Romano','JRO',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (183, 12,'Engenheiro Manoel Feio','EMF',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (184, 12,'Itaquaquecetuba','IQC',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (185, 12,'Aracaré','ARC',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (186, 12,'Calmon Viana','CVN',0)");
            //Linha 13 - Jade - CPTM

            /*
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (187, 13,'Barra Funda','BFU',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (188, 13,'Luz','LUZ',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (189, 13,'Brás','BAS',0)");
             */
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (187, 13,'Engenheiro Goulart','EGO',1)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (188, 13,'Guarulhos–Cecap','GCE',0)");
            db.execSQL("INSERT INTO Estacoes (id,id_linha, nm_estacao, sg_estacao, isTransferencia) VALUES (189, 13,'Aeroporto-Guarulhos','AGU',0)");

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void inserirAreasComuns(int idEstacao) {
        //Log.i("DatabaseHelper - InsereAreaComum", "INSERT INTO Areas (id_estacao, nome) VALUES ("+idEstacao+", 'Mezanino')");
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Inserir áreas comuns associadas à estação

            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Linha de Bloqueios 1')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Linha de Bloqueios 2')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Linha de Bloqueios 3')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Linha de Bloqueios 4')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Linha de Bloqueios 5')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Linha de Bloqueios 6')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Transferência')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Mezanino Área Paga')", new Object[]{idEstacao});
            db.execSQL("INSERT INTO Areas (id_estacao, nm_area) VALUES (?, 'Mezanino Área Livre')", new Object[]{idEstacao});

            db.setTransactionSuccessful();
            //Log.i("DatabaseHelper - InserirAreasComuns", "inserirAreasComuns: Sucesso na Inserção da área comum");
        } catch (Exception e){
            Log.e("DataBaseHelper - InserirAreasComuns", "Erro ao inserir areas comuns: "+e.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    public ArrayList<Linha> getLinhas() {
        // Log.i("DatabaseHelper - getLinhas", "Entrou no GetLinhas");
        ArrayList<Linha> linhas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Carregar todas as linhas
        Cursor cursor = db.rawQuery("SELECT id, nm_linha, sg_linha, nm_gestora FROM Linhas", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nome = cursor.getString(1);
            String sigla = cursor.getString(2);
            String gestora = cursor.getString(3);
            linhas.add(new Linha(id, nome, sigla, gestora));
            /*
            Log.i("Linha Carregada", "Linhas: id="+id+", Nome: "+nome+", Sigla: "+sigla+
                    ", Gestora: "+gestora);

             */
        }
        cursor.close();

        // Teste de carregamento de estação
        for (Linha linha : linhas) {
            //Log.i("DatabaseHelper - Carregando Estacoes", "Carregando estações para a linha id: " + linha.getId());
            linha.setEstacoes(getEstacoesPorLinha(linha.getId()));
        }
        return linhas;
    }

    public ArrayList<Linha> getLinhasComEstacaoSemAreas() {
        //Log.i("DatabaseHelper - LinhaComEstacaoSemArea", "Entrou em Linhas sem area");
        ArrayList<Linha> linhas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Carregar todas as linhas
        Cursor cursor = db.rawQuery("SELECT id, nm_linha, sg_linha, nm_gestora FROM Linhas", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nome = cursor.getString(1);
            String sigla = cursor.getString(2);
            String gestora = cursor.getString(3);
            linhas.add(new Linha(id, nome, sigla, gestora));
        }
        cursor.close();

        // Teste de carregamento de estação
        for (Linha linha : linhas) {
            // Carregar as estações associadas à linha
            linha.setEstacoes(getEstacoesPorLinhaSemArea(linha.getId()));
        }
        return linhas;
    }

    public ArrayList<Estacao> getEstacoesPorLinhaSemArea(int idLinha) {
        //Log.i("DatabaseHelper - getEstacoesPorLinhaSemArea", "Entrou em estacoes sem area");
        ArrayList<Estacao> estacoes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // A consulta SQL busca as colunas: id, nome, sigla, isTransferencia
        Cursor cursor = db.rawQuery(
                "SELECT id, nm_estacao, sg_estacao, isTransferencia FROM Estacoes " +
                        "WHERE id_linha = ?",
                new String[]{String.valueOf(idLinha)});

        while (cursor.moveToNext()) {
            // Obter os dados do cursor
            int id = cursor.getInt(0);
            String nome = cursor.getString(1);
            String sigla = cursor.getString(2);

            // Corrigir o valor de isTransferencia
            boolean isTransferencia = cursor.getInt(3) == 1; // 1 -> true (transferência), 0 -> false (não é transferência)

            // Adicionar a estação à lista
            estacoes.add(new Estacao(id, nome, sigla, isTransferencia));
        }
        cursor.close();
        return estacoes;
    }

    public ArrayList<Estacao> getEstacoesPorLinha(int idLinha) {
        //Log.i("getEstacoesPorLinha", "Entrou no getEstacoesPorLinha ");
        ArrayList<Estacao> estacoes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // A consulta SQL busca as colunas: id, nome, sigla, isTransferencia
        Cursor cursor = db.rawQuery(
                "SELECT id, nm_estacao, sg_estacao, isTransferencia FROM " +
                        "Estacoes WHERE id_linha = ?",
                new String[]{String.valueOf(idLinha)});

        while (cursor.moveToNext()) {
            // Obter os dados do cursor
            int id = cursor.getInt(0);
            String nome = cursor.getString(1);
            String sigla = cursor.getString(2);

            // Corrigir o valor de isTransferencia
            boolean isTransferencia = cursor.getInt(3) == 1; // 1 -> true (transferência), 0 -> false (não é transferência)

            // Adicionar a estação à lista
            //estacoes.add(new Estacao(id, nome, sigla, isTransferencia, getAreasPorEstacao(id)));
            estacoes.add(new Estacao(id, nome, sigla, isTransferencia));
        }
        cursor.close();

        //Log.i("DatabaseHelper - getEstacoesPorLinha", "Carregou as Estações com sucesso ");

        for(Estacao estacao:estacoes){
            //Log.i("DatabaseHelper - getEstacoesPorLinha", "Tentando Carregar Area da Estacao "+estacao.getNome());
            estacao.setAreas(getAreasPorEstacao(estacao.getId()));
        }
        return estacoes;
    }

    public ArrayList<String> getAreasPorEstacao(int idEstacao){
        //Log.i("DatabaseHelper - getAreasPorEstacao", "Carregando Areas: id: " + idEstacao);
        ArrayList<String> areas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // A consulta SQL busca as colunas: nome da área, onde o id_estacao é igual ao fornecido
            cursor = db.rawQuery(
                    "SELECT nm_area FROM Areas WHERE id_estacao = ?",
                    new String[]{String.valueOf(idEstacao)});

            if (cursor != null && cursor.moveToFirst()) { // Move para o primeiro registro
                do {
                    areas.add(cursor.getString(0)); // Adiciona o nome da área
                } while (cursor.moveToNext()); // Avança para o próximo registro
            } else {
                // Caso não haja resultados, insere áreas comuns
                inserirAreasComuns(idEstacao);
                // Refaça a consulta para garantir que as áreas comuns foram inseridas
                cursor = db.rawQuery(
                        "SELECT nm_area FROM Areas WHERE id_estacao = ?",
                        new String[]{String.valueOf(idEstacao)});
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        areas.add(cursor.getString(0)); // Adiciona o nome da área
                    } while (cursor.moveToNext()); // Avança para o próximo registro
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper - getAreaPorEstacao", "Erro ao consultar áreas da estação: " + idEstacao, e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Fecha o cursor para evitar vazamento de memória
            }
            db.close(); // Fecha o banco de dados
        }
        //Log.i("DatabaseHelper - getAreaPorEstacao", "Tamanho do retorno getAreasPorEstacao "+areas.size());
        return areas;
    }
/*
    public void salvarPesquisa(Pesquisa pesquisa){
        SQLiteDatabase sqLiteDatabase = null;

        try {
            sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.beginTransaction();  // Inicia a transação

            String dataPesquisa = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            String horaPesquisa = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            pesquisa.setDataPesquisa(dataPesquisa);
            pesquisa.setHoraPesquisa(horaPesquisa);
            Log.i("DatabaseHelper", "Pesquisa Salvada: Pesquisador:"+pesquisa.getPesquisador()+
                    ", TipoPesquisa:"+pesquisa.getTipoPesquisa()+
                    ", Data:"+pesquisa.getDataPesquisa()+
                    ", Hora: "+pesquisa.getHoraPesquisa()+
                    ", LinhaPesq:"+ pesquisa.getLinhaPesquisa().getId()+
                    ", EstacaoPesqID: "+pesquisa.getEstacaoPesquisa().getId()+
                    ", AreaPesq: "+pesquisa.getAreaPesquisa()+
                    ", Origem:"+pesquisa.getOrigem()+
                    ", Destino:"+pesquisa.getDestino());
            // Salva a pesquisa
            ContentValues values = new ContentValues();
            values.put("nm_pesquisador", pesquisa.getPesquisador());
            values.put("ds_tipo_pesquisa", pesquisa.getTipoPesquisa());
            values.put("dt_pesquisa", pesquisa.getDataPesquisa());
            values.put("hr_pesquisa", pesquisa.getHoraPesquisa());
            values.put("id_linha_pesquisa", pesquisa.getLinhaPesquisa().getId());
            values.put("id_estacao_pesquisa", pesquisa.getEstacaoPesquisa().getId());
            values.put("nm_area_pesquisa", pesquisa.getAreaPesquisa());
            values.put("nm_origem", pesquisa.getOrigem());
            values.put("nm_destino", pesquisa.getDestino());

            long pesquisaId = sqLiteDatabase.insert("Pesquisas", null, values);  // Insere a pesquisa e pega o id gerado

            // Verifica se a pesquisa foi inserida com sucesso
            if (pesquisaId == -1) {
                String erro = values.toString();
                throw new Exception("DatabaseHelper - Erro ao inserir pesquisa");
            }

            sqLiteDatabase.setTransactionSuccessful();  // Marca a transação como bem-sucedida
            Log.i("DatabaseHelper - SalvarPesquisa","Pesquisa Salva");

        }
        catch (Exception e) {
            throw new RuntimeException("DatabaseHelper - Erro no SQL Salvar Pesquisa"+ e.getMessage());
        }
        finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.endTransaction();  // Finaliza a transação
                sqLiteDatabase.close();  // Fecha o banco de dados
            }
        }
    }
 */

    public void salvarPesquisa(Pesquisa pesquisa) throws Exception{
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();  // Inicia a transação

        String dataPesquisa = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String horaPesquisa = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        pesquisa.setDataPesquisa(dataPesquisa);
        pesquisa.setHoraPesquisa(horaPesquisa);
        // Salva a pesquisa
        ContentValues values = new ContentValues();

        if(pesquisa.getPesquisador()!=null){
            values.put("nm_pesquisador", pesquisa.getPesquisador());
        }else{
            throw new Exception("Pesquisador Nulo!");
        }

        if (pesquisa.getTipoPesquisa() != null) {
            values.put("ds_tipo_pesquisa", pesquisa.getTipoPesquisa());
        }else{
            throw new Exception("Tipo de Pesquisa Nulo!");
        }
        if(pesquisa.getDataPesquisa()!=null){
            values.put("dt_pesquisa", pesquisa.getDataPesquisa());
        }else{
            throw new Exception("Data da Pesquisa Nula!");
        }
        if(pesquisa.getHoraPesquisa()!=null){
            values.put("hr_pesquisa", pesquisa.getHoraPesquisa());
        }else{
            throw new Exception("Hora da Pesquisa Nula!");
        }

        if(pesquisa.getLinhaPesquisa()==null ||
                pesquisa.getLinhaPesquisa().getNome().isEmpty()){
            throw new Exception("Linha Nula");
        }else{
            values.put("id_linha_pesquisa", pesquisa.getLinhaPesquisa().getId());
        }

        if(pesquisa.getEstacaoPesquisa()==null ||
                pesquisa.getEstacaoPesquisa().getNome().isEmpty()){
            throw new Exception("Estação Nula");
        }else{
            values.put("id_estacao_pesquisa", pesquisa.getEstacaoPesquisa().getId());
        }

        if(pesquisa.getAreaPesquisa()==null ||
                pesquisa.getAreaPesquisa().isEmpty()){
            throw new Exception("Área da Pesquisa Nula");
        }else{
            values.put("nm_area_pesquisa", pesquisa.getAreaPesquisa());
        }

        if(pesquisa.getOrigem()==null &&
                pesquisa.getDestino()==null){
            throw new Exception("Pesquisa Inválida! Origem e Destino Nulos");
        }

        if(pesquisa.getOrigem()==null ||
                pesquisa.getOrigem().isEmpty()){
            values.put("nm_origem", "");
        }else{
            values.put("nm_origem", pesquisa.getOrigem());
        }
        if(pesquisa.getDestino()==null ||
                pesquisa.getDestino().isEmpty()){
            values.put("nm_destino", "");
        }else{
            values.put("nm_destino", pesquisa.getDestino());
        }


        long pesquisaId = sqLiteDatabase.insert("Pesquisas", null, values);  // Insere a pesquisa e pega o id gerado

        // Verifica se a pesquisa foi inserida com sucesso
        if (pesquisaId == -1) {
            String erro = values.toString();
            throw new Exception("DatabaseHelper - Erro ao inserir pesquisa");
        }

        sqLiteDatabase.setTransactionSuccessful();  // Marca a transação como bem-sucedida

        if (sqLiteDatabase != null) {
            sqLiteDatabase.endTransaction();  // Finaliza a transação
            sqLiteDatabase.close();  // Fecha o banco de dados
        }

    }

    public ArrayList<Pesquisa> getPesquisas() {
        Utilidades utilidades = Utilidades.getInstance(context);
        ArrayList<Pesquisa> pesquisas = new ArrayList<>();
        //SQLiteDatabase db = dbHelper.getReadableDatabase();  // Usando um SQLiteOpenHelper para acessar o banco de dados
        db = this.getReadableDatabase();
        // Consulta SQL para pegar todas as informações das pesquisas, incluindo transferências
        String query = "SELECT * FROM Pesquisas";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Pesquisa pesquisa = new Pesquisa();

                // Preencher os campos básicos da pesquisa
                pesquisa.setId(cursor.getInt(0));
                pesquisa.setPesquisador(cursor.getString(1));
                pesquisa.setTipoPesquisa(cursor.getString(2));
                pesquisa.setAreaPesquisa(cursor.getString(7));
                // Preencher a origem
                pesquisa.setOrigem(cursor.getString(8));
                // Preencher o destino
                pesquisa.setDestino(cursor.getString(9));


                // Preencher a estação da pesquisa
                pesquisa.setEstacaoPesquisa(utilidades.getEstacaoPorId(cursor.getInt(6)));
                // Preencher a linha de Pesquisa
                pesquisa.setLinhaPesquisa(utilidades.getLinhaPorId(cursor.getInt(5)));
                pesquisa.setDataPesquisa(cursor.getString(3));
                pesquisa.setHoraPesquisa(cursor.getString(4));



                // Adicionar a pesquisa à lista
                pesquisas.add(pesquisa);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return pesquisas;
    }

    public Estacao getEstacaoById(int idEstacao) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Estacoes WHERE id = ?",
                new String[]{String.valueOf(idEstacao)});

        if (cursor.moveToFirst()) {
            Estacao estacao = new Estacao();
            estacao.setId(idEstacao);
            estacao.setNome(cursor.getString(1));
            estacao.setSigla(cursor.getString(2));
            cursor.close();
            return estacao;
        }

        cursor.close();
        return null;
    }

    public Estacao getEstacaoPorNome(String siglaLinha, String nomeEstacao){
        for(Estacao resultado: getLinhaPorSigla(siglaLinha).getEstacoes()){
            if(resultado.getNome().equals(nomeEstacao)){
                return resultado;
            }
        }
        return null;
    }

    public Linha getLinhaPorSigla(String siglaLinha){

        for(Linha resultado: getLinhas()){
            if(resultado.getNome().equals(siglaLinha)){
                return resultado;
            }
        }
        return null;
    }

    public Linha getLinhaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Linhas WHERE id = ?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            Linha linha = new Linha();
            linha.setId(id);
            linha.setNome(cursor.getString(1));
            linha.setSigla(cursor.getString(2));
            linha.setGestora(cursor.getString(3));
            cursor.close();
            return linha;
        }

        cursor.close();
        return null;
    }

}