package br.com.calculafeira.calculafeira.Persistence;

import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NotificationCompat;

import br.com.calculafeira.calculafeira.DAO.CategoriaDAO;
import br.com.calculafeira.calculafeira.DAO.DadosProdutoDAO;
import br.com.calculafeira.calculafeira.DAO.ProdutoDAO;

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
    private CategoriaDAO categoriaDAO;
    private DadosProdutoDAO dadosProdutoDAO;
    private ProdutoDAO produtoDAO;


    public DataManager(Context contx) {
        this.context = contx;
        setContext(context);
        SQLiteOpenHelper openHelper = new OpenHelper(context);
        setDatabase(openHelper);

        categoriaDAO = new CategoriaDAO(getDatabase(), context);
        dadosProdutoDAO = new DadosProdutoDAO(getDatabase(), context);
        produtoDAO = new ProdutoDAO(getDatabase(), context);

    }

    // Singleton
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

    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public DadosProdutoDAO getDadosProdutoDAO() {
        return dadosProdutoDAO;
    }

    public ProdutoDAO getProdutoDAO() {
        return produtoDAO;
    }
}
