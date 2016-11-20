package tig_unibh.tigiii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Splash extends Activity {

    protected static final int TIMER_RUNTIME = 4000;

    boolean Active;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        try{

            progressBar = (ProgressBar) findViewById(R.id.progressBar6);
            final Thread timerThread = new Thread(){
                @Override
                public void run(){
                    Active = true;
                    try {
                        int nI = 0;
                        while(Active && (nI < TIMER_RUNTIME)){
                            sleep(200);
                            if(Active){
                                nI += 200;
                                updateProgress(nI);
                            }
                        }
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            TelaInfo();
                            finish();
                        }

                    }
                };
                timerThread.start();
                DB db = new DB(this);
            } catch (Exception e){
            Toast.makeText(this,"Mensagem: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    public void updateProgress(final int timePassed){
        if(null != progressBar){
            final int progress = progressBar.getMax() * timePassed /TIMER_RUNTIME;
            progressBar.setProgress(progress);
        }
    }

    public void TelaInfo() {
        Intent secondActivity = new Intent(this, Principal.class);
        startActivity(secondActivity);
    }
}
