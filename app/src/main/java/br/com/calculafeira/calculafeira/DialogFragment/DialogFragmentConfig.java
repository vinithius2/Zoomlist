package br.com.calculafeira.calculafeira.DialogFragment;

/**
 * Created by Vinithius on 24/01/2018.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.calculafeira.calculafeira.Model.ProductData;
import br.com.calculafeira.calculafeira.Persistence.DataManager;
import br.com.calculafeira.calculafeira.R;

/**
 * Created by Administrador on 25/02/2015.
 */
public class DialogFragmentConfig extends DialogFragment {

    Button fechar;
    TextView developer, versionApp, contact, about, socialNetworksApp, socialNetworksDeveloper;
    PackageInfo packageInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Dialog);

        try {
            packageInfo = getActivity().getApplication().getPackageManager().getPackageInfo(getActivity().getApplication().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_config, null);
        developer = (TextView) view.findViewById(R.id.txt_developer_input);
        versionApp = (TextView) view.findViewById(R.id.txt_version_app_input);
        contact = (TextView) view.findViewById(R.id.txt_contact_input);
        about = (TextView) view.findViewById(R.id.txt_about_input);
        socialNetworksApp = (TextView) view.findViewById(R.id.txt_redes_sociais_app_input);
        socialNetworksDeveloper = (TextView) view.findViewById(R.id.txt_redes_sociais_developer_input);

        developer.setText("Marcos Vinithius Melo Filho");
        versionApp.setText("1.0");
        contact.setText("marcos.vinithius@gmail.com");
        about.setText("Este ap epwofkpwekf pwoef powekf powekfp kwefpo kwepof kwepofk wpeofk wepofkwe pofkwepok");
        socialNetworksApp.setText("Facebook, Instagran");
        socialNetworksDeveloper.setText("Facebook, Instagran");

        fechar = (Button) view.findViewById(R.id.btnFechar);

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);

        return builder.create();
    }

}
