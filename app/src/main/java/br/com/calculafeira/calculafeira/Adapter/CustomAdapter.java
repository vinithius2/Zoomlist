package br.com.calculafeira.calculafeira.Adapter;

/**
 * Created by Vinithius on 27/01/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.calculafeira.calculafeira.R;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private Integer[] categoryImages;
    private String[] categoryNames;
    private LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, Integer[] categoryImages, String[] categoryNames) {
        this.context = applicationContext;
        this.categoryImages = categoryImages;
        this.categoryNames = categoryNames;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categoryImages.length;
    }

    @Override
    public Object getItem(int i) {
        return categoryNames[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_spinner_category, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView_spinner_category);
        TextView names = (TextView) view.findViewById(R.id.textView_spinner_category);
        icon.setImageResource(categoryImages[i]);
        names.setText(categoryNames[i]);
        return view;
    }
}