package br.com.calculafeira.calculafeira.Activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import br.com.calculafeira.calculafeira.Adapter.AdapterProductData;
import br.com.calculafeira.calculafeira.DialogFragment.DialogFragmentConfig;
import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.Persistence.DataManager;
import br.com.calculafeira.calculafeira.R;
import br.com.calculafeira.calculafeira.Util.Helpers;

public class MainList extends AppCompatActivity implements AbsListView.OnScrollListener {

    private Toolbar mToolbar;
    private View mContainerHeader;
    private FloatingActionButton fab;
    private SharedPreferences mySharedPreferences;
    private ObjectAnimator fade;
    private TextView totalPrice, totalQuantity, porcentAlimento, porcentBebida,
            porcentHigiene, porcentLimpeza, estimate;
    private LinearLayout linearLayoutPorcent;
    private ListView listView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        try {
            DataManager.getInstance(this);
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        setInitialization();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainList.this, ProductCreateEdit.class);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(view, getResources().getString(R.string.info_edit_delete), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
    }

    private void populaAdapter(){
        ArrayAdapter<ProductData> productDataAdapter = new AdapterProductData(
                context,
                R.layout.adapter_product_data,
                DataManager.getInstance().getProductDataDAO().getListProductDatas(),
                totalPrice,
                porcentAlimento,
                porcentBebida,
                porcentHigiene,
                porcentLimpeza,
                totalQuantity,
                estimate,
                mToolbar
        );
        listView.setAdapter(productDataAdapter);
    }

    private void setConfig(){
        mySharedPreferences = context.getSharedPreferences("checkBoxEstimate", Context.MODE_PRIVATE);
        Boolean value01 = mySharedPreferences.getBoolean("checkBoxEstimate", true);
        estimate.setVisibility(View.VISIBLE);
        if(!value01){
            estimate.setVisibility(View.GONE);
        }
        mySharedPreferences = context.getSharedPreferences("checkBoxQuantity", Context.MODE_PRIVATE);
        Boolean value02 = mySharedPreferences.getBoolean("checkBoxQuantity", true);
        totalQuantity.setVisibility(View.VISIBLE);
        if(!value02){
            totalQuantity.setVisibility(View.GONE);
        }
        mySharedPreferences = context.getSharedPreferences("checkBoxPorcent", Context.MODE_PRIVATE);
        Boolean value03 = mySharedPreferences.getBoolean("checkBoxPorcent", true);
        linearLayoutPorcent.setVisibility(View.VISIBLE);
        if(!value03){
            linearLayoutPorcent.setVisibility(View.GONE);
        }
    }

    private void setInitialization(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            totalPrice = mToolbar.findViewById(R.id.textView_total_price);
            totalQuantity = mToolbar.findViewById(R.id.textView_total_quantity);
            porcentAlimento = mToolbar.findViewById(R.id.textView_porcent_alimento);
            porcentBebida = mToolbar.findViewById(R.id.textView_porcent_bebida);
            porcentHigiene = mToolbar.findViewById(R.id.textView_porcent_higiene);
            porcentLimpeza = mToolbar.findViewById(R.id.textView_porcent_limpeza);
            estimate = mToolbar.findViewById(R.id.textView_estimate);
            linearLayoutPorcent = mToolbar.findViewById(R.id.LinearLayoutPorcent);
        }

        // Inflate the header view and attach it to the ListView
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_main_list, listView, false);
        mContainerHeader = headerView.findViewById(R.id.container);
        listView.addHeaderView(headerView);

        totalPrice = headerView.findViewById(R.id.textView_total_price);
        totalQuantity = headerView.findViewById(R.id.textView_total_quantity);
        porcentAlimento = headerView.findViewById(R.id.textView_porcent_alimento);
        porcentBebida = headerView.findViewById(R.id.textView_porcent_bebida);
        porcentHigiene = headerView.findViewById(R.id.textView_porcent_higiene);
        porcentLimpeza = headerView.findViewById(R.id.textView_porcent_limpeza);
        estimate = headerView.findViewById(R.id.textView_estimate);
        linearLayoutPorcent = headerView.findViewById(R.id.LinearLayoutPorcent);

        fab = (FloatingActionButton) findViewById(R.id.add_product);

        fade = ObjectAnimator.ofFloat(mContainerHeader, "alpha", 0f, 1f);
        fade.setInterpolator(new DecelerateInterpolator());
        fade.setDuration(400);

        listView.setOnScrollListener(this);
        populaAdapter();
        setConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_principal, m);
        return super.onCreateOptionsMenu(m);
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
                        Helpers.setAtualizarDadosInicial(context, totalPrice, porcentAlimento, porcentBebida,
                                porcentHigiene, porcentLimpeza, totalQuantity, estimate, mToolbar);
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
                dialogFragmentConfig.show(ftc, getResources().getString(R.string.action_settings));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                mySharedPreferences = context.getSharedPreferences("titleHeader", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean("titleHeader", true);
                editor.apply();
            } else {
                toggleHeader(true, true);
                mySharedPreferences = context.getSharedPreferences("titleHeader", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean("titleHeader", false);
                editor.apply();
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
}
