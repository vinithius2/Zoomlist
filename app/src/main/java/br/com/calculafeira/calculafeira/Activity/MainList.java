package br.com.calculafeira.calculafeira.Activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import br.com.calculafeira.calculafeira.Adapter.AdapterProductData;
import br.com.calculafeira.calculafeira.DialogFragment.DialogFragmentConfig;
import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.Persistence.DataManager;
import br.com.calculafeira.calculafeira.R;

public class MainList extends AppCompatActivity implements AbsListView.OnScrollListener {

    Toolbar mToolbar;
    View mContainerHeader;
    FloatingActionButton fab;
    ObjectAnimator fade;
    TextView totalPrice, totalQuantity, porcentAlimento, porcentBebida, porcentHigiene, porcentLimpeza, estimate;
    TableRow tableRowAlimento, tableRowBebida, tableRowHigiene, tableRowLimpeza;
    ListView listView;
    DecimalFormat maskMoney;

    private SharedPreferences mySharedPreferences;
    private Context context = this;
    private ArrayAdapter<ProductData> productDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        try {
            DataManager.getInstance(this);
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview);


        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.value_default));
            setSupportActionBar(mToolbar);
            totalPrice = (TextView) mToolbar.findViewById(R.id.textView_total_price);
            totalQuantity = (TextView) mToolbar.findViewById(R.id.textView_total_quantity);
            porcentAlimento = (TextView) mToolbar.findViewById(R.id.textView_porcent_alimento);
            porcentBebida = (TextView) mToolbar.findViewById(R.id.textView_porcent_bebida);
            porcentHigiene = (TextView) mToolbar.findViewById(R.id.textView_porcent_higiene);
            porcentLimpeza = (TextView) mToolbar.findViewById(R.id.textView_porcent_limpeza);
            estimate = (TextView) mToolbar.findViewById(R.id.textView_estimate);
            tableRowAlimento = (TableRow) mToolbar.findViewById(R.id.tableRow_alimento);
            tableRowBebida = (TableRow) mToolbar.findViewById(R.id.tableRow_bebidas);
            tableRowHigiene = (TableRow) mToolbar.findViewById(R.id.tableRow_higiene);
            tableRowLimpeza = (TableRow) mToolbar.findViewById(R.id.tableRow_limpeza);
        }

        // Inflate the header view and attach it to the ListView
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_main_list, listView, false);
        mContainerHeader = headerView.findViewById(R.id.container);
        listView.addHeaderView(headerView);

        totalPrice = (TextView) headerView.findViewById(R.id.textView_total_price);
        totalQuantity = (TextView) headerView.findViewById(R.id.textView_total_quantity);
        porcentAlimento = (TextView) headerView.findViewById(R.id.textView_porcent_alimento);
        porcentBebida = (TextView) headerView.findViewById(R.id.textView_porcent_bebida);
        porcentHigiene = (TextView) headerView.findViewById(R.id.textView_porcent_higiene);
        porcentLimpeza = (TextView) headerView.findViewById(R.id.textView_porcent_limpeza);
        estimate = (TextView) headerView.findViewById(R.id.textView_estimate);
        tableRowAlimento = (TableRow) headerView.findViewById(R.id.tableRow_alimento);
        tableRowBebida = (TableRow) headerView.findViewById(R.id.tableRow_bebidas);
        tableRowHigiene = (TableRow) headerView.findViewById(R.id.tableRow_higiene);
        tableRowLimpeza = (TableRow) headerView.findViewById(R.id.tableRow_limpeza);
/**
        tableRowAlimento.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, getResources().getString(R.string.os_alimentos_equivalem_a)
                        + " " + porcentAlimento.getText().toString()
                        + " " + getResources().getString(R.string.de_suas_compras), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
        tableRowBebida.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, getResources().getString(R.string.as_bebidas_equivalem_a)
                        + " " + porcentBebida.getText().toString()
                        + " " + getResources().getString(R.string.de_suas_compras), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
        tableRowHigiene.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, getResources().getString(R.string.os_materiais_de_higiene_equivalem_a)
                        + " " + porcentHigiene.getText().toString()
                        + " " + getResources().getString(R.string.de_suas_compras), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
        tableRowLimpeza.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, getResources().getString(R.string.os_materiais_de_limpeza_equivalem_a)
                        + " " + porcentLimpeza.getText().toString()
                        + " " + getResources().getString(R.string.de_suas_compras), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
**/
        // prepare the fade in/out animator
        fade = ObjectAnimator.ofFloat(mContainerHeader, "alpha", 0f, 1f);
        fade.setInterpolator(new DecelerateInterpolator());
        fade.setDuration(400);

        listView.setOnScrollListener(this);
        populaAdapter();


        fab = (FloatingActionButton) findViewById(R.id.add_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainList.this, ProductCreate.class);
                startActivity(intent);
            }
        });
    }

    private void populaAdapter(){

        productDataAdapter = new AdapterProductData(
                this,
                R.layout.adapter_product_data,
                DataManager.getInstance().getProductDataDAO().getListProductDatas(),
                totalPrice,
                totalQuantity,
                porcentAlimento,
                porcentBebida,
                porcentHigiene,
                porcentLimpeza,
                totalQuantity,
                estimate,
                mToolbar
        );

        listView.setAdapter(productDataAdapter);
        productDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * Listen to the scroll events of the listView
     *
     * @param view             the listView
     * @param firstVisibleItem the first visible item
     * @param visibleItemCount the number of visible items
     * @param totalItemCount   the amount of items
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onScroll(AbsListView view,
                         int firstVisibleItem,
                         int visibleItemCount,
                         int totalItemCount) {
        // we make sure the list is not null and empty, and the header is visible
        if (view != null && view.getChildCount() > 0 && firstVisibleItem == 0) {

            // we calculate the FAB's Y position
            int translation = view.getChildAt(0).getHeight() + view.getChildAt(0).getTop();
            fab.setTranslationY(translation > 0 ? translation : 0);

            // if we scrolled more than 16dps, we hide the content and display the title
            if (view.getChildAt(0).getTop() < -dpToPx(16)) {
                toggleHeader(false, false);
            } else {
                toggleHeader(true, true);
            }
        } else {
            toggleHeader(false, false);
        }

        // if the device uses Lollipop or above, we update the ToolBar's elevation
        // according to the scroll position.
        if (isLollipop()) {
            if (firstVisibleItem == 0) {
                mToolbar.setElevation(0);
            } else {
                mToolbar.setElevation(dpToPx(4));
            }
        }
    }

    /**
     * Start the animation to fade in or out the header's content
     *
     * @param visible true if the header's content should appear
     * @param force   true if we don't wait for the animation to be completed
     *                but force the change.
     */
    private void toggleHeader(boolean visible, boolean force) {
        if ((force && visible) || (visible && mContainerHeader.getAlpha() == 0f)) {
            fade.setFloatValues(mContainerHeader.getAlpha(), 1f);
            fade.start();
        } else if (force || (!visible && mContainerHeader.getAlpha() == 1f)) {
            fade.setFloatValues(mContainerHeader.getAlpha(), 0f);
            fade.start();
        }
        // Toggle the visibility of the title.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(!visible);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_principal, m);
        return super.onCreateOptionsMenu(m);
    }

    /**
     * Convert Dps into Pxs
     *
     * @param dp a number of dp to convert
     * @return the value in pixels
     */
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (dp * (displayMetrics.densityDpi / 160f));
    }

    /**
     * Check if the device rocks, and runs Lollipop
     *
     * @return true if Lollipop or above
     */
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getResources().getString(R.string.list_delete));
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (ProductData p : DataManager.getInstance().getProductDataDAO().getListProductDatas()) {
                            DataManager.getInstance().getProductDataDAO().delete(p);
                        }
                        mToolbar.setTitle(getResources().getString(R.string.value_default));
                        totalPrice.setText(getResources().getString(R.string.value_default));
                        porcentAlimento.setText(getResources().getString(R.string.porcent_zero));
                        porcentBebida.setText(getResources().getString(R.string.porcent_zero));
                        porcentHigiene.setText(getResources().getString(R.string.porcent_zero));
                        porcentLimpeza.setText(getResources().getString(R.string.porcent_zero));
                        totalQuantity.setText(getResources().getString(R.string.no_products));
                        estimate.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        estimate.setTypeface(Typeface.DEFAULT);
                        estimate.setTextSize(10);
                        populaAdapter();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.action_estimate:
                Intent intent = new Intent(MainList.this, ConfigActivity.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                FragmentTransaction ftc = getFragmentManager().beginTransaction();
                DialogFragmentConfig dialogFragmentConfig = new DialogFragmentConfig();
                dialogFragmentConfig.show(ftc, "Configuração");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
