package tig_unibh.tigiii;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class Informacoes extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacoes);
    }
    public void TelaPrincipal(View view) {
        Intent secondActivity = new Intent(this, Principal.class);
        startActivity(secondActivity);
    }
}
