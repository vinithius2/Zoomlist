package br.com.calculafeira.calculafeira.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import br.com.calculafeira.calculafeira.R;

/**
 * Created by Vinithius on 24/01/2018.
 */

public class Helpers {

    private static SharedPreferences mySharedPreferences;
    public static SortedMap<Currency, Locale> currencyLocaleMap;

    public static String getMonetary(String money){
        double parsed = Double.parseDouble(money);
        return NumberFormat.getCurrencyInstance().format((parsed/100));
    }

    public static void setAtualizarDadosInicial(Context context,
                                      TextView totalPrice,
                                      TextView porcentAlimento,
                                      TextView porcentBebida,
                                      TextView porcentHigiene,
                                      TextView porcentLimpeza,
                                      TextView totalQuantity,
                                      TextView estimate,
                                      Toolbar mToolbar) {

        String defaultMoney = context.getResources().getString(R.string.value_default);
        String defaultPorcent = context.getResources().getString(R.string.porcent_zero);
        mToolbar.setTitle(defaultMoney);
        totalPrice.setText(defaultMoney);
        porcentAlimento.setText(context.getResources().getString(R.string.porcent_zero));
        porcentBebida.setText(defaultPorcent);
        porcentHigiene.setText(defaultPorcent);
        porcentLimpeza.setText(defaultPorcent);
        totalQuantity.setText(context.getResources().getString(R.string.no_products));
        estimate.setText(defaultMoney);
        estimate.setBackground(null);
        estimate.setTextColor(context.getResources().getColor(R.color.colorWhite));
        estimate.setTypeface(Typeface.DEFAULT);
        estimate.setTextSize(12);
    }

    public static String getEstimate(Context context){
        mySharedPreferences = context.getSharedPreferences("estimate", Context.MODE_PRIVATE);
        String value = mySharedPreferences.getString("estimate", "");
        return !value.isEmpty() && !value.equals("000") ? value : null;
    }


    public static String getCurrencySymbol(String currencyCode) {
        currencyLocaleMap = new TreeMap<>(new Comparator<Currency>() {
            public int compare(Currency c1, Currency c2) {
                return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
            }
        });
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencyLocaleMap.put(currency, locale);
            } catch (Exception e) {
            }
        }
        Currency currency = Currency.getInstance(currencyCode);
        System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    public static String getClearBlank(String cleanString) {
        char[] aux = cleanString.toCharArray();
        String value = "";
        for (int i = 0; i < aux.length; i++) {
            if (Character.isDigit(aux[i])) {
                value += aux[i];
            }
        }
        return value;
    }
}
