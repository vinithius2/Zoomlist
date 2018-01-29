package br.com.calculafeira.calculafeira.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import br.com.calculafeira.calculafeira.Util.Helpers;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Objects;

import br.com.calculafeira.calculafeira.R;


public class ConfigActivity extends AppCompatActivity {

    private static final String CHECK_BOX_ESTIMATE = "checkBoxEstimate";
    private static final String CHECK_BOX_QUANTITY = "checkBoxQuantity";
    private static final String CHECK_BOX_PORCENT = "checkBoxPorcent";
    private CheckBox checkBoxEstimate, checkBoxQuantity, checkBoxPorcent;
    private SharedPreferences mySharedPreferences;
    private EditText editTextEstimate;
    private Context context = this;
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editTextEstimate = (EditText)findViewById(R.id.edit_text_estimate);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mySharedPreferences = context.getSharedPreferences("estimate", Context.MODE_PRIVATE);
        editTextEstimate.setText(Helpers.getMonetary("000"));
        if (!mySharedPreferences.getString("estimate", "").isEmpty()){
            editTextEstimate.setText(Helpers.getMonetary(String.valueOf(
                    Double.parseDouble(mySharedPreferences.getString("estimate", ""))
            )));
        }

        checkBoxEstimate = (CheckBox)findViewById(R.id.checkBoxEstimate);
        checkBoxPorcent = (CheckBox)findViewById(R.id.checkBoxPorcent);
        checkBoxQuantity = (CheckBox)findViewById(R.id.checkBoxQuantity);
        atualizarCheckBoxs(checkBoxEstimate, checkBoxQuantity, checkBoxPorcent);

        editTextEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    editTextEstimate.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[R$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    editTextEstimate.setText(formatted);
                    editTextEstimate.setSelection(formatted.length());

                    editTextEstimate.addTextChangedListener(this);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editTextEstimate.getText().toString();
                if (!Objects.equals(value, "")){
                    mySharedPreferences = context.getSharedPreferences("estimate", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("estimate", String.valueOf(value).replaceAll("[R$,.]", ""));
                    editor.apply();

                    addValueCheckBox(CHECK_BOX_ESTIMATE, checkBoxEstimate);
                    addValueCheckBox(CHECK_BOX_QUANTITY, checkBoxQuantity);
                    addValueCheckBox(CHECK_BOX_PORCENT, checkBoxPorcent);

                    Intent intent = new Intent(ConfigActivity.this, MainList.class);
                    startActivity(intent);
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void atualizarCheckBoxs(CheckBox checkBoxEstimate, CheckBox checkBoxQuantity, CheckBox checkBoxPorcent){
        mySharedPreferences = context.getSharedPreferences("checkBoxEstimate", Context.MODE_PRIVATE);
        Boolean value01 = mySharedPreferences.getBoolean("checkBoxEstimate", true);
        checkBoxEstimate.setChecked(value01);

        mySharedPreferences = context.getSharedPreferences("checkBoxQuantity", Context.MODE_PRIVATE);
        Boolean value02 = mySharedPreferences.getBoolean("checkBoxQuantity", true);
        checkBoxQuantity.setChecked(value02);

        mySharedPreferences = context.getSharedPreferences("checkBoxPorcent", Context.MODE_PRIVATE);
        Boolean value03 = mySharedPreferences.getBoolean("checkBoxPorcent", true);
        checkBoxPorcent.setChecked(value03);
    }

    private void addValueCheckBox(String nameCheckBox, View v){
        mySharedPreferences = context.getSharedPreferences(nameCheckBox, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if (((CheckBox) v).isChecked()) {
            editor.putBoolean(nameCheckBox, true);
            editor.apply();
        } else {
            editor.putBoolean(nameCheckBox, false);
            editor.apply();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(ConfigActivity.this, MainList.class);
        startActivity(intent);
    }
}
