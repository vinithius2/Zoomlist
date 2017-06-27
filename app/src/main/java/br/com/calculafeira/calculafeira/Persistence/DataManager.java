package br.com.calculafeira.calculafeira.Persistence;

import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NotificationCompat;

import br.com.calculafeira.calculafeira.DAO.CategoryDAO;
import br.com.calculafeira.calculafeira.DAO.ProductDataDAO;
import br.com.calculafeira.calculafeira.DAO.ProductDAO;

/**
 * Created by DPGE on 22/06/2017.
 */

public class DataManager {

    private static DataManager instance;
    private Context context;
    private SQLiteDatabase database;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    // --- DAOs --- //
    private CategoryDAO categoriaDAO;
    private ProductDataDAO dadosProductDAO;
    private ProductDAO produtoDAO;


    public DataManager(Context contx) {
        this.context = contx;
        setContext(context);
        SQLiteOpenHelper openHelper = new OpenHelper(context);
        setDatabase(openHelper);

        categoriaDAO = new CategoryDAO(getDatabase(), context);
        dadosProductDAO = new ProductDataDAO(getDatabase(), context);
        produtoDAO = new ProductDAO(getDatabase(), context);

    }

    public static DataManager getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public static DataManager getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteOpenHelper database) {
        this.database = database.getWritableDatabase();
    }

    private void closeDb() {
        if (getDatabase().isOpen()) {
            getDatabase().close();
        }
    }

    public CategoryDAO getCategoryDAO() {
        return categoriaDAO;
    }

    public ProductDataDAO getProductDataDAO() {
        return dadosProductDAO;
    }

    public ProductDAO getProductDAO() {
        return produtoDAO;
    }
}
