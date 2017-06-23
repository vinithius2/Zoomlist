package br.com.calculafeira.calculafeira.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.DadosProduto;

/**
 * Created by DPGE on 22/06/2017.
 */

public class DadosProdutoDAO {

    public static final String TABELA = "DADOS_PRODUTO";
    public static final String _ID = "_ID";
    public static final String FK_PRODUTO = "FK_PRODUTO";
    public static final String DATA_AQUISICAO = "DATA_AQUISICAO";
    public static final String QUANTIDADE = "QUANTIDADE";
    public static final String PRECO = "PRECO";

    private final SQLiteDatabase database;
    private final Context context;

    public DadosProdutoDAO(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public Long save(DadosProduto dadosProduto) {
        Long id = dadosProduto.getIdDadosProduto();
        if (id != null) {
            update(dadosProduto);
        } else {
            id = insert(dadosProduto);
        }
        return id;
    }

    private Long insert(DadosProduto dadosProduto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FK_PRODUTO, dadosProduto.getFkProduto());
        contentValues.put(DATA_AQUISICAO, String.valueOf(dadosProduto.getDataAquisicao()));
        contentValues.put(QUANTIDADE, dadosProduto.getQuantidade());
        contentValues.put(PRECO, dadosProduto.getPreco());
        Long id = insert(contentValues);
        return id;
    }

    private Long insert(ContentValues contentValues) {
        Long id = database.insert(TABELA, "", contentValues);
        return id;
    }

    private int update(DadosProduto dadosProduto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FK_PRODUTO, dadosProduto.getFkProduto());
        contentValues.put(DATA_AQUISICAO, String.valueOf(dadosProduto.getDataAquisicao()));
        contentValues.put(QUANTIDADE, dadosProduto.getQuantidade());
        contentValues.put(PRECO, dadosProduto.getPreco());
        String _id = String.valueOf(dadosProduto.getIdDadosProduto());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = update(contentValues, where, whereArgs);
        return count;
    }

    private int update(ContentValues contentValues, String where, String[] whereArgs) {
        int count = database.update(TABELA, contentValues, where, whereArgs);
        return count;
    }

    public int delete(DadosProduto dadosProduto) {
        String _id = String.valueOf(dadosProduto.getIdDadosProduto());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = delete(where, whereArgs);
        return count;
    }

    private int delete(String where, String[] whereArgs) {
        int count = database.delete(TABELA, where, whereArgs);
        return count;
    }

    public int getCount() {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ProdutoDAO.TABELA, null);
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public DadosProduto getIdDadosProduto(Long id) {
        String _id = String.valueOf(id);
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        DadosProduto dadosProduto = getIdDadosProduto(where, whereArgs);
        return dadosProduto;
    }

    private DadosProduto getIdDadosProduto(String where, String[] whereArgs) {
        Cursor c = database.rawQuery("SELECT * FROM " + TABELA + " WHERE " + where, whereArgs);
        DadosProduto dadosProduto = new DadosProduto();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxFkProduto = c.getColumnIndex(FK_PRODUTO);
            int idxDataAquisicao = c.getColumnIndex(DATA_AQUISICAO);
            int idxQuantidade = c.getColumnIndex(QUANTIDADE);
            int idxPreco = c.getColumnIndex(PRECO);
            do {
                dadosProduto.setIdDadosProduto(c.getLong(idxId));
                dadosProduto.setFkProduto(c.getLong(idxFkProduto));
                dadosProduto.setDataAquisicao(Long.parseLong(String.valueOf(idxDataAquisicao)));
                dadosProduto.setQuantidade(c.getInt(idxQuantidade));
                dadosProduto.setPreco(c.getDouble(idxPreco));
            } while (c.moveToNext());
        }
        return dadosProduto;
    }

    private Cursor getCursor() throws SQLException {
        String[] colunas = new String[]{_ID, FK_PRODUTO, DATA_AQUISICAO, QUANTIDADE, PRECO};
        return database.query(TABELA, colunas, null, null, null, null, null, null);
    }

    public ArrayList<DadosProduto> getListaCategorias() {
        Cursor c;
        try {
            c = getCursor();
        } catch (SQLException e) {
            Log.e("DadosProdutoDAO", "Erro ao buscar a lista de Dados Produto: " + e.toString());
            return null;
        }
        ArrayList<DadosProduto> dadosProdutos = new ArrayList<DadosProduto>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxFkProduto = c.getColumnIndex(FK_PRODUTO);
            int idxDataAquisicao = c.getColumnIndex(DATA_AQUISICAO);
            int idxQuantidade = c.getColumnIndex(QUANTIDADE);
            int idxPreco = c.getColumnIndex(PRECO);
            do {
                DadosProduto dadosProduto = new DadosProduto();
                dadosProduto.setIdDadosProduto(c.getLong(idxId));
                dadosProduto.setFkProduto(c.getLong(idxFkProduto));
                dadosProduto.setDataAquisicao(Long.parseLong(String.valueOf(idxDataAquisicao)));
                dadosProduto.setQuantidade(c.getInt(idxQuantidade));
                dadosProduto.setPreco(c.getDouble(idxPreco));
                dadosProdutos.add(dadosProduto);
            } while (c.moveToNext());
        }
        return dadosProdutos;
    }

}
