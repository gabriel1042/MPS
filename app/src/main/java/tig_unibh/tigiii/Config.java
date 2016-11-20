package tig_unibh.tigiii;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class Config extends Activity{
    DBManager db = new DBManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        try {
            TextView txtSegundos = (TextView) findViewById(R.id.txtSegundos);
            ToggleButton btnVibrar = (ToggleButton) findViewById(R.id.btnVibrar);
            RadioButton optIdeal = (RadioButton) findViewById(R.id.optIdeal);
            RadioButton optMediana = (RadioButton) findViewById(R.id.optMediana);
            RadioButton optMinima = (RadioButton) findViewById(R.id.optMinima);
            CheckBox chkGeolocalizacao = (CheckBox) findViewById(R.id.chkGeolocalizacao);

            ArrayList<String> Dados = db.getConfig();
            // 0 - Tempo
            // 1 - Nivel
            // 2 - Geolocalização
            // 3 - Vibrar
            txtSegundos.setText("    " + Dados.get(0));
            if (Integer.parseInt(Dados.get(1)) == 0) {
                optIdeal.setChecked(true);
            } else if (Integer.parseInt(Dados.get(1)) == 1) {
                optMediana.setChecked(true);
            } else {
                optMinima.setChecked(true);
            }
            if(Integer.parseInt(Dados.get(2)) == 0){
                chkGeolocalizacao.setChecked(false);
            }else{
                chkGeolocalizacao.setChecked(true);
            }

            if(Integer.parseInt(Dados.get(3)) == 0){
                btnVibrar.setChecked(false);
            }else{
                btnVibrar.setChecked(true);
            }

        } catch (Exception e){
            Toast.makeText(this,"Erro: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void GravarConfig(View view){
        try{
            TextView txtSegundos = (TextView) findViewById(R.id.txtSegundos);
            ToggleButton btnVibrar = (ToggleButton) findViewById(R.id.btnVibrar);
            RadioButton optIdeal = (RadioButton) findViewById(R.id.optIdeal);
            RadioButton optMediana = (RadioButton) findViewById(R.id.optMediana);
            RadioButton optMinima = (RadioButton) findViewById(R.id.optMinima);
            CheckBox chkGeolocalizacao = (CheckBox) findViewById(R.id.chkGeolocalizacao);
            int postura = 0;
            int vibrar = 1;
            int geolocalizacao = 1;
            if(!chkGeolocalizacao.isChecked()){
                geolocalizacao = 0;
            }
            if(!btnVibrar.isChecked()){
                vibrar = 0;
            }
            if(optIdeal.isChecked()){
                postura = 0;
            }else if (optMediana.isChecked()){
                postura = 1;
            }else{
                postura = 2;
            }
            db.setConfig(postura,Integer.parseInt(txtSegundos.getText().toString().trim()), geolocalizacao, vibrar);
            Toast.makeText(this,"Configuração alterada com sucesso!", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void optIdeal(View view){
        try {
        RadioButton optIdeal = (RadioButton) findViewById(R.id.optIdeal);
        RadioButton optMediana = (RadioButton) findViewById(R.id.optMediana);
        RadioButton optMinima = (RadioButton) findViewById(R.id.optMinima);
        optIdeal.setChecked(true);
        optMediana.setChecked(false);
        optMinima.setChecked(false);
        } catch (Exception e){
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void optMediana(View view){
        try {
            RadioButton optIdeal = (RadioButton) findViewById(R.id.optIdeal);
            RadioButton optMediana = (RadioButton) findViewById(R.id.optMediana);
            RadioButton optMinima = (RadioButton) findViewById(R.id.optMinima);
            optIdeal.setChecked(false);
            optMediana.setChecked(true);
            optMinima.setChecked(false);
        } catch (Exception e){
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void optMinima(View view){
        try {
            RadioButton optIdeal = (RadioButton) findViewById(R.id.optIdeal);
            RadioButton optMediana = (RadioButton) findViewById(R.id.optMediana);
            RadioButton optMinima = (RadioButton) findViewById(R.id.optMinima);
            optIdeal.setChecked(false);
            optMediana.setChecked(false);
            optMinima.setChecked(true);
        } catch (Exception e){
            Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
