package br.com.calculafeira.calculafeira.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.ProductData;

/**
 * Created by DPGE on 22/06/2017.
 */

public class ProductDataDAO {

    public static final String TABLE = "DADOS_PRODUTO";
    public static final String _ID = "_ID";
    public static final String FK_PRODUCT = "FK_PRODUCT";
    public static final String PURCHASE_DATE = "PURCHASE_DATE";
    public static final String QUANTITY = "QUANTITY";
    public static final String PRICE = "PRICE";

    private final SQLiteDatabase database;
    private final Context context;

    public ProductDataDAO(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public Long save(ProductData productData) {
        Long id = productData.getIdProductData();
        if (id != null) {
            update(productData);
        } else {
            id = insert(productData);
        }
        return id;
    }

    private Long insert(ProductData productData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FK_PRODUCT, productData.getFkProduct());
        //contentValues.put(PURCHASE_DATE, String.valueOf(productData.getPurchaseDate()));
        contentValues.put(QUANTITY, productData.getQuantity());
        contentValues.put(PRICE, productData.getPrice());
        Long id = insert(contentValues);
        return id;
    }

    private Long insert(ContentValues contentValues) {
        Long id = database.insert(TABLE, "", contentValues);
        return id;
    }

    private int update(ProductData productData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FK_PRODUCT, productData.getFkProduct());
        //contentValues.put(PURCHASE_DATE, String.valueOf(productData.getPurchaseDate()));
        contentValues.put(QUANTITY, productData.getQuantity());
        contentValues.put(PRICE, productData.getPrice());
        String _id = String.valueOf(productData.getIdProductData());
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = update(contentValues, where, whereArgs);
        return count;
    }

    private int update(ContentValues contentValues, String where, String[] whereArgs) {
        int count = database.update(TABLE, contentValues, where, whereArgs);
        return count;
    }

    public int delete(ProductData productData) {
        String _id = String.valueOf(productData.getIdProductData());
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

    public ProductData getIdProductData(Long id) {
        String _id = String.valueOf(id);
        String where = _ID + "=?";
        String[] whereArgs = new String[]{_id};
        ProductData productData = getIdProductData(where, whereArgs);
        return productData;
    }

    private ProductData getIdProductData(String where, String[] whereArgs) {
        Cursor c = database.rawQuery("SELECT * FROM " + TABLE + " WHERE " + where, whereArgs);
        ProductData productData = new ProductData();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxFkProduct = c.getColumnIndex(FK_PRODUCT);
            int idxPurchaseDate = c.getColumnIndex(PURCHASE_DATE);
            int idxQuantity = c.getColumnIndex(QUANTITY);
            int idxPrice = c.getColumnIndex(PRICE);
            do {
                productData.setIdProductData(c.getLong(idxId));
                productData.setFkProduct(c.getLong(idxFkProduct));
                productData.setPurchaseDate(c.getString(idxPurchaseDate));
                productData.setQuantity(c.getInt(idxQuantity));
                productData.setPrice(c.getDouble(idxPrice));
            } while (c.moveToNext());
        }
        return productData;
    }

    private Cursor getCursor() throws SQLException {
        String[] colunas = new String[]{_ID, FK_PRODUCT, PURCHASE_DATE, QUANTITY, PRICE};
        return database.query(TABLE, colunas, null, null, null, null, null, null);
    }

    public ArrayList<ProductData> getListProductDatas() {
        Cursor c;
        try {
            c = getCursor();
        } catch (SQLException e) {
            Log.e("ProductDataDAO", "Erro ao buscar a lista de Dados Product: " + e.toString());
            return null;
        }
        ArrayList<ProductData> productDatas = new ArrayList<ProductData>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(_ID);
            int idxFkProduct = c.getColumnIndex(FK_PRODUCT);
            int idxPurchaseDate = c.getColumnIndex(PURCHASE_DATE);
            int idxQuantity = c.getColumnIndex(QUANTITY);
            int idxPrice = c.getColumnIndex(PRICE);
            do {
                ProductData productData = new ProductData();
                productData.setIdProductData(c.getLong(idxId));
                productData.setFkProduct(c.getLong(idxFkProduct));
                productData.setPurchaseDate(c.getString(idxPurchaseDate));
                productData.setQuantity(c.getInt(idxQuantity));
                productData.setPrice(c.getDouble(idxPrice));
                productDatas.add(productData);
            } while (c.moveToNext());
        }
        return productDatas;
    }
    
    public Double getTotalPrice(){
        ArrayList<ProductData> productDatas = getListProductDatas();
        Double totalPrice = Double.parseDouble("0");
        for (ProductData product : productDatas) {
            totalPrice =+ product.getPrice();
        }
        return totalPrice;
    }

}
