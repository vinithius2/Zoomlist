package br.com.calculafeira.calculafeira.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.Produto;

/**
 * Created by DPGE on 22/06/2017.
 */

public class ProdutoDAO {

    public static final String TABELA = "PRODUTO";
    public static final String _ID = "_ID";
    public static final String NOME_PRODUTO = "NOME_PRODUTO";
    public static final String FK_CATEGORIA = "FK_CATEGORIA";

    private final SQLiteDatabase database;
    private final Context context;

    public ProdutoDAO(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public Long save(Produto produto) {
        Long id = produto.getIdProduto();
        if (id != null) {
            update(produto);
        } else {
            id = insert(produto);
        }
        return id;
    }

    private Long insert(Produto produto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_PRODUTO, produto.getNomeProduto());
        contentValues.put(FK_CATEGORIA, produto.getFkCategoria());
        Long id = insert(contentValues);
        return id;
    }

    private Long insert(ContentValues contentValues) {
        Long id = database.insert(TABELA, "", contentValues);
        return id;
    }

    private int update(Produto produto) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_PRODUTO, produto.getNomeProduto());
        contentValues.put(FK_CATEGORIA, produto.getFkCategoria());
        String _id = String.valueOf(produto.getIdProduto());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = update(contentValues, where, whereArgs);
        return count;
    }

    private int update(ContentValues contentValues, String where, String[] whereArgs) {
        int count = database.update(TABELA, contentValues, where, whereArgs);
        return count;
    }

    public int delete(Produto produto) {
        String _id = String.valueOf(produto.getIdProduto());
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

    public Produto getIdProduto(Long id) {
        String _id = String.valueOf(id);
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        Produto produto = getIdProduto(where, whereArgs);
        return produto;
    }

    private Produto getIdProduto(String where, String[] whereArgs) {
        Cursor c = database.rawQuery("SELECT * FROM " + TABELA + " WHERE " + where, whereArgs);
        Produto produto = new Produto();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNomeProduto = c.getColumnIndex(NOME_PRODUTO);
            int idxFkCategoria = c.getColumnIndex(FK_CATEGORIA);
            do {
                produto.setIdProduto(c.getLong(idxId));
                produto.setNomeProduto(c.getString(idxNomeProduto));
                produto.setFkCategoria(c.getLong(idxFkCategoria));
            } while (c.moveToNext());
        }
        return produto;
    }

    private Cursor getCursor() throws SQLException {
        String[] colunas = new String[]{_ID, NOME_PRODUTO, FK_CATEGORIA};
        return database.query(TABELA, colunas, null, null, null, null, null, null);
    }

    public ArrayList<Produto> getListaProdutos() {
        Cursor c;
        try {
            c = getCursor();
        } catch (SQLException e) {
            Log.e("ProdutoDAO", "Erro ao buscar a lista de produtos: " + e.toString());
            return null;
        }
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNomeProduto = c.getColumnIndex(NOME_PRODUTO);
            int idxFkCategoria = c.getColumnIndex(FK_CATEGORIA);
            do {
                Produto produto = new Produto();
                produto.setIdProduto(c.getLong(idxId));
                produto.setNomeProduto(c.getString(idxNomeProduto));
                produto.setFkCategoria(c.getLong(idxFkCategoria));
                produtos.add(produto);
            } while (c.moveToNext());
        }
        return produtos;
    }

}
