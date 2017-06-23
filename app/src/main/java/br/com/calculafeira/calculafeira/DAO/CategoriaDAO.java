package br.com.calculafeira.calculafeira.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.Categoria;

/**
 * Created by DPGE on 22/06/2017.
 */

public class CategoriaDAO {

    public static final String TABELA = "CATEGORIA";
    public static final String _ID = "_ID";
    public static final String NOME_CATEGORIA = "NOME_CATEGORIA";

    public static final String ALIMENTO = "Alimento";
    public static final String BEBIDA = "Bebida";
    public static final String LIMPEZA = "Limpeza";
    public static final String HIGIENE = "Higiene";

    private final SQLiteDatabase database;
    private final Context context;

    public CategoriaDAO(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public Long save(Categoria categoria) {
        Long id = categoria.getIdCategoria();
        if (id != null) {
            update(categoria);
        } else {
            id = insert(categoria);
        }
        return id;
    }

    private Long insert(Categoria categoria) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_CATEGORIA, categoria.getNomeCategoria());
        Long id = insert(contentValues);
        return id;
    }

    private Long insert(ContentValues contentValues) {
        Long id = database.insert(TABELA, "", contentValues);
        return id;
    }

    private int update(Categoria categoria) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOME_CATEGORIA, categoria.getNomeCategoria());
        String _id = String.valueOf(categoria.getIdCategoria());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = update(contentValues, where, whereArgs);
        return count;
    }

    private int update(ContentValues contentValues, String where, String[] whereArgs) {
        int count = database.update(TABELA, contentValues, where, whereArgs);
        return count;
    }

    public int delete(Categoria categoria) {
        String _id = String.valueOf(categoria.getIdCategoria());
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

    public Categoria getIdCategoria(Long id) {
        String _id = String.valueOf(id);
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        Categoria categoria = getIdCategoria(where, whereArgs);
        return categoria;
    }

    private Categoria getIdCategoria(String where, String[] whereArgs) {
        Cursor c = database.rawQuery("SELECT * FROM " + TABELA + " WHERE " + where, whereArgs);
        Categoria categoria = new Categoria();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNomeCategoria = c.getColumnIndex(NOME_CATEGORIA);
            do {
                categoria.setIdCategoria(c.getLong(idxId));
                categoria.setNomeCategoria(c.getString(idxNomeCategoria));
            } while (c.moveToNext());
        }
        return categoria;
    }


    private Cursor getCursor() throws SQLException {
        String[] colunas = new String[]{_ID, NOME_CATEGORIA};
        return database.query(TABELA, colunas, null, null, null, null, null, null);
    }

    public ArrayList<Categoria> getListaCategorias() {
        Cursor c;
        try {
            c = getCursor();
        } catch (SQLException e) {
            Log.e("CategoriaDAO", "Erro ao buscar a lista de categorias: " + e.toString());
            return null;
        }
        ArrayList<Categoria> categorias = new ArrayList<Categoria>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNomeCategoria = c.getColumnIndex(NOME_CATEGORIA);
            do {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(c.getLong(idxId));
                categoria.setNomeCategoria(c.getString(idxNomeCategoria));
                categorias.add(categoria);
            } while (c.moveToNext());
        }
        return categorias;
    }

}
