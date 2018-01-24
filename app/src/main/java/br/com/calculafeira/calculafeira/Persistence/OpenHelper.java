package br.com.calculafeira.calculafeira.Persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.calculafeira.calculafeira.DAO.ProductDAO;
import br.com.calculafeira.calculafeira.DAO.ProductDataDAO;

/**
 * Created by DPGE on 22/06/2017.
 */

public class OpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CALCULA_FEIRA_APP";
    private static final int MAX_REGISTROS_TRANSACAO = 10; // máximo de
    private static int REGISTROS_TRANSACAO;
    private static String[] DATABASE_TABLES;
    private static String[] DATABASE_TABLES_REMOVIDAS = {};
    private static String[] DATABASE_CREATE;
    private final String sufixoTabelaTemp = "_TEMP";
    private boolean chaveCategory = true;
    private Context context;

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.InicializarVariaveis();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            Cursor c = db.rawQuery("PRAGMA foreign_keys", null);
            Log.i(OpenHelper.class.getName(), "PRAGMA foreign_keys=ON");
            if (c.moveToFirst()) {
                int result = c.getInt(0);
            }
            if (!c.isClosed()) {
                c.close();
            }
        }
    }

    public static int getMaxRegistrosTransacao() {
        if (REGISTROS_TRANSACAO == 0) {
            Runtime rt = Runtime.getRuntime();
            long maxMemory = rt.maxMemory() / 1024 / 1024;
            if (maxMemory <= 16) {
                REGISTROS_TRANSACAO = MAX_REGISTROS_TRANSACAO / 8;
            } else if (maxMemory <= 24) {
                REGISTROS_TRANSACAO = MAX_REGISTROS_TRANSACAO / 4;
            }
            REGISTROS_TRANSACAO = MAX_REGISTROS_TRANSACAO;
        }
        return REGISTROS_TRANSACAO;
    }

    private void InicializarVariaveis() {
        DATABASE_TABLES = new String[]{
                ProductDAO.TABLE,
                ProductDataDAO.TABLE,

        };

        DATABASE_CREATE = new String[]{
                createTableProduct(),
                createTableProductData(),
        };
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(OpenHelper.class.getName(), "Criando banco de dados " + DATABASE_NAME);
        Log.i("DB", "onCreate - Chave: " + chaveCategory);
        // apaga as tabelas que forma removidas do banco
        for (String tabelaRemovida : DATABASE_TABLES_REMOVIDAS) {
            apagaTabela(db, tabelaRemovida);
        }
        // cria as tabelas
        for (String aDATABASE_CREATE : DATABASE_CREATE) {
            db.execSQL(aDATABASE_CREATE);
        }
    }

    private void apagaTabela(SQLiteDatabase db, String tabela) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", tabela));
        Log.i(OpenHelper.class.getName(), "DROP TABLE IF EXISTS %s" + tabela);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(OpenHelper.class.getName(), "Atualizando banco de dados da versão " + oldVersion + " para " + newVersion);
        //dropTables(db);
        recriaBanco(db, oldVersion, newVersion);
    }

    private void recriaBanco(SQLiteDatabase db, int oldVersion, int newVersion) {
        // cria tabelas temp e apaga tabelas
        for (String tabela : DATABASE_TABLES) {
            if (tabelaExists(db, tabela)) {
                criaTabelaTemp(db, tabela);
                apagaTabela(db, tabela);
                Log.i(OpenHelper.class.getName(), "recriaBanco");
            }
        }
        onCreate(db);

        // repopula tabelas
        for (String tabela : DATABASE_TABLES) {
            if (tabelaExists(db, tabela + sufixoTabelaTemp)) {
                try {
                    repopula(db, tabela);
                } catch (SQLiteException e) {
                    apagaRegistrosTabela(db, tabela);
                }
                apagaTabelaTemp(db, tabela);
            }
        }
    }

    private void apagaTabelaTemp(SQLiteDatabase db, String tabela) {
        apagaTabela(db, tabela + sufixoTabelaTemp);
    }

    private void apagaRegistrosTabela(SQLiteDatabase db, String tabela) {
        db.execSQL(String.format("DELETE FROM %s", tabela));
        Log.i(OpenHelper.class.getName(), "DELETE FROM %s" + tabela);
    }

    private void repopula(SQLiteDatabase db, String tabela)
            throws SQLiteException {
        Cursor cursorDadosTabelaTemp = null;
        Cursor cursorTabelaNova = null;
        try {
            cursorDadosTabelaTemp = db.rawQuery(String.format(
                    "SELECT * FROM %s%s", tabela, sufixoTabelaTemp), null);
            cursorTabelaNova = db.rawQuery(
                    String.format("SELECT * FROM %s", tabela), null);

            int[] indices = getIndicesColunas(cursorDadosTabelaTemp);

            // itera pelos valores da tabela e adiciona-os ao List
            if (cursorDadosTabelaTemp.moveToFirst()) {
                // cria a string para insert (INSERT INTO tabela(col1, col2)
                // values(?, ?))
                String queryInsert = createInsertString(indices, tabela,
                        cursorDadosTabelaTemp, cursorTabelaNova);
                do {
                    // obt�m os valores das colunas
                    String[] valoresTemp = getValoresCursor(indices,
                            cursorDadosTabelaTemp, cursorTabelaNova);
                    // Insere os dados da tabela temp na nova tabela
                    db.execSQL(queryInsert, valoresTemp);

                } while (cursorDadosTabelaTemp.moveToNext());
            }
        } finally {
            if (cursorTabelaNova != null
                    && !cursorTabelaNova.isClosed())
                cursorTabelaNova.close();

            if (cursorDadosTabelaTemp != null
                    && !cursorDadosTabelaTemp.isClosed())
                cursorDadosTabelaTemp.close();
        }
    }

    private String[] getValoresCursor(int[] indices,
                                      Cursor cursorDadosTabelaTemp, Cursor cursorTabelaNova) {
        List<String> valoresTemp = new ArrayList<String>(); // List de String


        for (int indice : indices) {
            String valor = cursorDadosTabelaTemp.getString(indice); // obt�m

            String coluna = cursorDadosTabelaTemp.getColumnName(indice);

            if (cursorTabelaNova.getColumnIndex(coluna) != -1) {
                valoresTemp.add(valor);
            }
        }

        return valoresTemp.toArray(valoresTemp.toArray(new String[valoresTemp
                .size()]));
    }

    private String createInsertString(int[] indices, String tabela,
                                      Cursor cursorDadosTabelaTemp, Cursor cursorTabelaNova) {
        StringBuilder colunasTemp = new StringBuilder(); // StringBuilder com os

        StringBuilder values = new StringBuilder(); // StringBuilder com valores
        // das colunas (?, ?, ?...)

        for (int indice : indices) {
            String coluna = cursorDadosTabelaTemp.getColumnName(indice);

            if (cursorTabelaNova.getColumnIndex(coluna) != -1) {
                colunasTemp.append(coluna).append(",");
                values.append("?,");
            }
        }

        // Apaga a ultima virgula
        colunasTemp.deleteCharAt(colunasTemp.length() - 1);
        values.deleteCharAt(values.length() - 1);

        return String.format("INSERT INTO %s(%s) values(%s)",
                tabela, colunasTemp, values);
    }

    private int[] getIndicesColunas(Cursor cursorSelect) {
        String[] nameColunas = cursorSelect.getColumnNames();
        int[] indices = new int[nameColunas.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = cursorSelect.getColumnIndex(nameColunas[i]);
        }
        return indices;
    }

    private void criaTabelaTemp(SQLiteDatabase db, String tabela) {

        String tabelaTemp = String.format("%s%s", tabela, sufixoTabelaTemp);
        String queryCriaTabelaTemp = String.format(
                "CREATE TABLE %s AS SELECT * FROM %s", tabelaTemp, tabela);
        db.execSQL(queryCriaTabelaTemp);
    }

    private boolean tabelaExists(SQLiteDatabase db, String tabela) {
        boolean tabelaExiste = false;
        try {
            @SuppressWarnings("unused")
            Cursor cursor = db
                    .query(tabela, null, null, null, null, null, null);
            tabelaExiste = true;
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                tabelaExiste = false;
            }
        }
        return tabelaExiste;
    }

    private String createTableProduct() {
        Log.i(OpenHelper.class.getName(), "Criando tabela: " + ProductDAO.TABLE);
        String CREATE_TABLE_PRODUTO = "CREATE TABLE IF NOT EXISTS " + ProductDAO.TABLE + "("
                + ProductDAO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductDAO.PRODUCT_NAME + " TEXT NOT NULL,"
                + ProductDAO.CATEGORY_NAME + " TEXT NOT NULL"
                + ")";
        Log.i("CREATE TABLE", CREATE_TABLE_PRODUTO);
        return CREATE_TABLE_PRODUTO;
    }

    private String createTableProductData() {
        Log.i(OpenHelper.class.getName(), "Criando tabela: " + ProductDataDAO.TABLE);
        String CREATE_TABLE_DADOS_PRODUTO = "CREATE TABLE IF NOT EXISTS " + ProductDataDAO.TABLE + "("
                + ProductDataDAO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductDataDAO.PURCHASE_DATE + " DATETIME DEFAULT CURRENT_DATETIME,"
                + ProductDataDAO.PRICE + " REAL NOT NULL,"
                + ProductDataDAO.QUANTITY + " INTEGER NOT NULL,"
                + ProductDataDAO.FK_PRODUCT + " INTEGER NOT NULL,"
                + "FOREIGN KEY(" + ProductDataDAO.FK_PRODUCT + ") REFERENCES " + ProductDAO.TABLE + "(" + ProductDAO._ID + ") ON DELETE CASCADE"
                + ")";
        Log.i("CREATE TABLE", CREATE_TABLE_DADOS_PRODUTO);
        return CREATE_TABLE_DADOS_PRODUTO;
    }

}
