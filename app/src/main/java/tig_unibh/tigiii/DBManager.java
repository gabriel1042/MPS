
package tig_unibh.tigiii;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Camera;

import java.util.ArrayList;

/**
 * Created by Gabriel on 08/11/2016.
 */

public class DBManager {
    private static DB db = null;
    public DBManager(Context context){
        if(db == null){
            db = new DB(context);
            db.CriarDB();
        }
    }

    public void GravarPostura(float eixo_Z, String endereco){
        String sql = "INSERT INTO postura (postura, endereco, data) VALUES (" + eixo_Z + ", '" + endereco + "', datetime());";
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL(sql);
    }
    public ArrayList<String> getConfig(){
        try {
            SQLiteDatabase db = this.db.getReadableDatabase();
            String sql = "SELECT tempo, nivel, geolocalizacao, vibrar FROM config";
            Cursor cursor = db.rawQuery(sql, null);
            ArrayList<String> config = new ArrayList<String>();

            if (cursor != null && cursor.moveToFirst()) {
                config.add(cursor.getString(0));
                config.add(cursor.getString(1));
                config.add(cursor.getString(2));
                config.add(cursor.getString(3));
            }
            return config;
        } catch(Exception e){
            return null;
            //Toast.makeText(this, "Erro: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public ArrayList<String> getMediaPostura(){
        try {
            SQLiteDatabase db = this.db.getReadableDatabase();
            String sql = "SELECT postura FROM postura";
            Cursor cursor = db.rawQuery(sql, null);
            ArrayList<String> mediaPostura = new ArrayList<String>();

            while(cursor.moveToNext()) {
                mediaPostura.add(cursor.getString(0));
            }
            return mediaPostura;
        } catch(Exception e){
            return null;
            //Toast.makeText(this, "Erro: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public ArrayList<String> getEnderecos(){
        try {
            Camera c = null;

            SQLiteDatabase db = this.db.getReadableDatabase();
            String sql = "SELECT endereco FROM postura";
            Cursor cursor = db.rawQuery(sql, null);
            ArrayList<String> mediaEndereco = new ArrayList<String>();

            while(cursor.moveToNext()) {
                mediaEndereco.add(cursor.getString(0));
            }
            return mediaEndereco;
        } catch(Exception e){
            return null;
            //Toast.makeText(this, "Erro: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public boolean getVibrar(){
        try {
            SQLiteDatabase db = this.db.getReadableDatabase();
            String sql = "SELECT vibrar FROM config";
            Cursor cursor = db.rawQuery(sql, null);

            if(cursor.moveToNext()) {
                if(Integer.parseInt(cursor.getString(0)) == 1){
                    return true;
                }else{
                    return false;
                }
            }
            return true; //default
        } catch(Exception e){
            return true; //default
            //Toast.makeText(this, "Erro: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean getGeolocalizacao(){
        try {
            SQLiteDatabase db = this.db.getReadableDatabase();
            String sql = "SELECT geolocalizacao FROM config";
            Cursor cursor = db.rawQuery(sql, null);

            if(cursor.moveToNext()) {
                if(Integer.parseInt(cursor.getString(0)) == 1){
                    return true;
                }else{
                    return false;
                }
            }
            return true; //default
        } catch(Exception e){
            return true; //default
            //Toast.makeText(this, "Erro: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void setConfig(int nivelPostura, int segundos, int geolocalizacao, int vibrar){
        String sql = "UPDATE config SET nivel = " + nivelPostura + ", tempo = " + segundos + ", geolocalizacao = "+ geolocalizacao + ", vibrar = " + vibrar + ";";
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL(sql);
    }
}
