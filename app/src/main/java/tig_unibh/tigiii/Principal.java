package tig_unibh.tigiii;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Principal extends AppCompatActivity implements SensorEventListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private GoogleApiClient mGoogleApiClient;
    Sensor accelerometer;
    Sensor proximidade;
    SensorManager sensorManager;
    float sensorZ;
    float nPostura;
    long nCont;
    boolean bParar;
    ArrayList<Float> media_postura;
    float nMedia = 0;
    int i = 0;
    private Timer timerAtual = new Timer();
    private TimerTask task;
    private final Handler handler = new Handler();
    private GoogleApiClient client;
    final Dados info = new Dados();
    DBManager db = new DBManager(this);
    private String endereco;

    public void TelaConfig(View view) {
        Intent secondActivity = new Intent(this, Config.class);
        startActivity(secondActivity);
    }
    public void TelaRelatorio(View view) {
        Intent secondActivity = new Intent(this, Relatorio.class);
        secondActivity.putStringArrayListExtra("DADOS",db.getMediaPostura());
        secondActivity.putStringArrayListExtra("ENDERECO",db.getEnderecos());
        startActivity(secondActivity);
    }

    public void PararTimer(final View view)
    {
        bParar = true;
        Toast.makeText(getApplicationContext(), "Processamento Finalizado!", Toast.LENGTH_SHORT).show();
        view.setEnabled(false);
        final Button bt = (Button) findViewById(R.id.btnIniciar);
        bt.setEnabled(true);
    }


    public void IniciarTimer(final View view) {
        view.setEnabled(false);
        Toast.makeText(this,"Iniciando Monitoramento...",Toast.LENGTH_SHORT).show();
        final Button btnIniciar = (Button) findViewById(R.id.btnIniciar);
        //bt.setEnabled(true);
        final Button btnParar = (Button) findViewById(R.id.btnParar);
        btnParar.setEnabled(true);
        bParar = false;
        nPostura = 0;
        nCont = 0;
        new CountDownTimer(600 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                nCont++;
                if (nCont == 3) {
                    nMedia = nPostura / nCont;

                    if(db.getGeolocalizacao()){
                        callConnection();
                    }else{
                        endereco = "S/P";
                    }

                    db.GravarPostura(nMedia, endereco);
                    info.setMedia_postura(nMedia);
                    if (nPostura / nCont > 8) {

                        gerarNotificacao(view, "Você ficou muito tempo nessa posição.");
                    }
                    nPostura = 0;
                    nCont = 0;
                }
                nPostura = nPostura + sensorZ;
                if (bParar == true) {
                    bParar = false;
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                IniciarTimer(view);
            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void gerarNotificacao(View view, String msg) {

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // expandir uma nova atividade // chamar a atividade
        // estudar intent pendentenai
        PendingIntent p = PendingIntent.getActivity(this,0,new Intent(this, Principal.class),0);
        // builder - construir titulo
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this);
        // mensagem na tela do usuario
        builder.setTicker("Ajuste sua Postura!");
        builder.setContentTitle("Postura Incorreta!");
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(p);
        // trabalhando a notificação
        Notification n = builder.build();
        // 150 ms de espera - 300 ms de espera
        // notificacao vibração
        if(db.getVibrar()){
            n.vibrate = new long[] {150, 300, 150, 600};
        }

        nm.notify(R.mipmap.ic_launcher, n);
        // toque da notificacao
        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch (Exception e) {}
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private synchronized void callConnection() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

    }

    //LISTENER
    public void onConnected(@Nullable Bundle bundle) {
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        String localizacao = "";
        if (l != null) {
            Address local = null;
            try {
                local = buscarEndereco(l.getLatitude(), l.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            endereco = local.getPostalCode();
            //Toast.makeText(getApplicationContext(),"Rua: " + local.getThoroughfare()  + "\nCEPº:" + local.getPostalCode(), Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), localizacao, Toast.LENGTH_LONG).show();
            //gerarNotificacao(view, localizacao);
        } else if (l == null) {
            Toast.makeText(getApplicationContext(), "L é NULL", Toast.LENGTH_LONG).show();
            //gerarNotificacao(view, "L é NULL");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorZ = event.values[2];
    }
    public Address buscarEndereco(double latitude, double longitude) throws IOException {
        Address address = null;
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null){
            if(addresses.size()> 0){
                address = addresses.get(0);
            }
        }
        return address;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

