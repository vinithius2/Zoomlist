package br.com.calculafeira.calculafeira.Util;

import java.text.NumberFormat;

/**
 * Created by Vinithius on 24/01/2018.
 */

public class Helpers {

    public static String getMonetary(String money){
        double parsed = Double.parseDouble(money);
        return NumberFormat.getCurrencyInstance().format((parsed/100));
    }
}
