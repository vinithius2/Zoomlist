package br.com.calculafeira.calculafeira.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.Category;

/**
 * Created by DPGE on 22/06/2017.
 */

public class CategoryDAO {

    public static final String TABLE = "CATEGORIA";
    public static final String _ID = "_ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    public static final String FOOD = "Alimento";
    public static final String DRINK = "Bebida";
    public static final String CLEANING = "Limpeza";
    public static final String HYGIENE = "Higiene";

    private final SQLiteDatabase database;
    private final Context context;

    public CategoryDAO(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public Long save(Category category) {
        Long id = category.getIdCategory();
        if (id != null) {
            update(category);
        } else {
            id = insert(category);
        }
        return id;
    }

    private Long insert(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, category.getNameCategory());
        Long id = insert(contentValues);
        return id;
    }

    private Long insert(ContentValues contentValues) {
        Long id = database.insert(TABLE, "", contentValues);
        return id;
    }

    private int update(Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, category.getNameCategory());
        String _id = String.valueOf(category.getIdCategory());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = update(contentValues, where, whereArgs);
        return count;
    }

    private int update(ContentValues contentValues, String where, String[] whereArgs) {
        int count = database.update(TABLE, contentValues, where, whereArgs);
        return count;
    }

    public int delete(Category category) {
        String _id = String.valueOf(category.getIdCategory());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = delete(where, whereArgs);
        return count;
    }

    private int delete(String where, String[] whereArgs) {
        int count = database.delete(TABLE, where, whereArgs);
        return count;
    }

    public int getCount() {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + ProductDAO.TABLE, null);
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public Category getIdCategory(Long id) {
        String _id = String.valueOf(id);
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        Category category = getIdCategory(where, whereArgs);
        return category;
    }

    private Category getIdCategory(String where, String[] whereArgs) {
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE + " WHERE " + where, whereArgs);
        Category category = new Category();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNameCategory = c.getColumnIndex(CATEGORY_NAME);
            do {
                category.setIdCategory(c.getLong(idxId));
                category.setNameCategory(c.getString(idxNameCategory));
            } while (c.moveToNext());
        }
        return category;
    }


    private Cursor getCursor() throws SQLException {
        String[] colunas = new String[]{_ID, CATEGORY_NAME};
        return database.query(TABLE, colunas, null, null, null, null, null, null);
    }

    public ArrayList<Category> getListCategorys() {
        Cursor c;
        try {
            c = getCursor();
        } catch (SQLException e) {
            Log.e("CategoryDAO", "Erro ao buscar a lista de categories: " + e.toString());
            return null;
        }
        ArrayList<Category> categories = new ArrayList<Category>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNameCategory = c.getColumnIndex(CATEGORY_NAME);
            do {
                Category category = new Category();
                category.setIdCategory(c.getLong(idxId));
                category.setNameCategory(c.getString(idxNameCategory));
                categories.add(category);
            } while (c.moveToNext());
        }
        return categories;
    }
}
