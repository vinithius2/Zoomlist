package br.com.calculafeira.calculafeira.Service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by DPGE on 23/06/2017.
 */

public class AsyncTaskCategoria extends AsyncTask<SQLiteDatabase, Integer, String> {
    private Context context;
    private ArrayList<String> script;
    private final int id = 1;

    public AsyncTaskCategoria(Context context) {
        this.context = context;
        Log.i("AsyncTaskCategoria", "AsyncTaskCategoria");
    }

    @Override
    protected String doInBackground(SQLiteDatabase... params) {
        Log.i("doInBackground", "doInBackground");
        //script = SqlCategoria.getCodigoSql();
        int TAM = script.size();
        int i = 0;
        try {
            for (i = 0; i < TAM; i++) {
                String scriptSql = script.get(i);
                params[0].execSQL(scriptSql);
                publishProgress((int) ((i / (float) TAM) * 100));
                Log.i("doInBackground", i + " de " + TAM + " - " + scriptSql);
            }
        } catch (Exception e) {
            Log.i("doInBackground", "" + e);
        }
        return i + "%";
    }

    @Override
    protected void onPreExecute() {
        Log.i("onPreExecute", "onPreExecute");
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.i("onProgressUpdate", "onProgressUpdate");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("onPostExecute", "onPostExecute");
    }
}
