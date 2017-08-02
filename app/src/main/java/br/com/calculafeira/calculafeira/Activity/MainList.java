package br.com.calculafeira.calculafeira.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Adapter.AdapterProductData;
import br.com.calculafeira.calculafeira.Model.Product;
import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.Persistence.DataManager;
import br.com.calculafeira.calculafeira.R;
import br.com.calculafeira.calculafeira.Util.Mask;

public class MainList extends AppCompatActivity implements AbsListView.OnScrollListener {


    Toolbar mToolbar;
    View mContainerHeader;
    FloatingActionButton fab;
    ObjectAnimator fade;
    TextView totalPrice, totalQuantity;
    ListView listView;
    DecimalFormat maskMoney;
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
        }

        // Inflate the header view and attach it to the ListView
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_main_list, listView, false);
        mContainerHeader = headerView.findViewById(R.id.container);
        listView.addHeaderView(headerView);

        totalPrice = (TextView) headerView.findViewById(R.id.textView_total_price);
        totalQuantity = (TextView) headerView.findViewById(R.id.textView_total_quantity);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final ProductData productData = (ProductData) listView.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Você deseja realmente deletar o item: " + productData.getProduct().getNameProduct());
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DataManager.getInstance().getProductDataDAO().delete(productData);
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
                return false;
            }
        });
    }

    private void populaAdapter(){
        ArrayList<Product> products = DataManager.getInstance().getProductDAO().getListProducts();
        if (products != null) {
            productDataAdapter = new AdapterProductData(
                    this,
                    R.layout.adapter_product_data,
                    DataManager.getInstance().getProductDataDAO().getListProductDatas(),
                    totalPrice,
                    totalQuantity,
                    mToolbar
            );
            listView.setAdapter(productDataAdapter);
            productDataAdapter.notifyDataSetChanged();
        }
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
        if (id == R.id.action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Você deseja realmente deletar todos os ítens?");
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    for (ProductData p : DataManager.getInstance().getProductDataDAO().getListProductDatas()) {
                        DataManager.getInstance().getProductDataDAO().delete(p);
                    }
                    mToolbar.setTitle("R$0,00");
                    totalPrice.setText("R$0,00");
                    totalQuantity.setText("Produtos: 0");
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

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
