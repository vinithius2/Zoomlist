package br.com.calculafeira.calculafeira.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.Product;

/**
 * Created by DPGE on 22/06/2017.
 */

public class ProductDAO {

    public static final String TABLE = "PRODUTO";
    public static final String _ID = "_ID";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String FK_CATEGORY = "FK_CATEGORY";

    private final SQLiteDatabase database;
    private final Context context;

    public ProductDAO(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public Long save(Product product) {
        Long id = product.getIdProduct();
        if (id != null) {
            update(product);
        } else {
            id = insert(product);
        }
        return id;
    }

    private Long insert(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_NAME, product.getNameProduct());
        contentValues.put(FK_CATEGORY, product.getFkCategory());
        Long id = insert(contentValues);
        return id;
    }

    private Long insert(ContentValues contentValues) {
        Long id = database.insert(TABLE, "", contentValues);
        return id;
    }

    private int update(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_NAME, product.getNameProduct());
        contentValues.put(FK_CATEGORY, product.getFkCategory());
        String _id = String.valueOf(product.getIdProduct());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = update(contentValues, where, whereArgs);
        return count;
    }

    private int update(ContentValues contentValues, String where, String[] whereArgs) {
        int count = database.update(TABLE, contentValues, where, whereArgs);
        return count;
    }

    public int delete(Product product) {
        String _id = String.valueOf(product.getIdProduct());
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

    public Product getIdProduct(Long id) {
        String _id = String.valueOf(id);
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        Product product = getIdProduct(where, whereArgs);
        return product;
    }

    private Product getIdProduct(String where, String[] whereArgs) {
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE + " WHERE " + where, whereArgs);
        Product product = new Product();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNameProduct = c.getColumnIndex(PRODUCT_NAME);
            int idxFkCategory = c.getColumnIndex(FK_CATEGORY);
            do {
                product.setIdProduct(c.getLong(idxId));
                product.setNameProduct(c.getString(idxNameProduct));
                product.setFkCategory(c.getLong(idxFkCategory));
            } while (c.moveToNext());
        }
        return product;
    }

    private Cursor getCursor() throws SQLException {
        String[] colunas = new String[]{_ID, PRODUCT_NAME, FK_CATEGORY};
        return database.query(TABLE, colunas, null, null, null, null, null, null);
    }

    public ArrayList<Product> getListProducts() {
        Cursor c;
        try {
            c = getCursor();
        } catch (SQLException e) {
            Log.e("ProductDAO", "Erro ao buscar a lista de products: " + e.toString());
            return null;
        }
        ArrayList<Product> products = new ArrayList<Product>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxNameProduct = c.getColumnIndex(PRODUCT_NAME);
            int idxFkCategory = c.getColumnIndex(FK_CATEGORY);
            do {
                Product product = new Product();
                product.setIdProduct(c.getLong(idxId));
                product.setNameProduct(c.getString(idxNameProduct));
                product.setFkCategory(c.getLong(idxFkCategory));
                products.add(product);
            } while (c.moveToNext());
        }
        return products;
    }

}
