package tig_unibh.tigiii;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gabriel on 08/11/2016.
 */

public class DB extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DB_NAME = "mps";
    private static String TABLE_INI =
            "CREATE TABLE IF NOT EXISTS ini(primeiro_acesso INTEGER);";
    private static String TABLE_POSTURA =
            "CREATE TABLE IF NOT EXISTS postura(id INTEGER PRIMARY KEY AUTOINCREMENT, data DATETIME DEFAULT CURRENT_TIMESTAMP, endereco TEXT, postura REAL);";
    private static String TABLE_CONFIG =
            "CREATE TABLE IF NOT EXISTS config(id INTEGER PRIMARY KEY AUTOINCREMENT, tempo INTEGER, nivel INTEGER, geolocalizacao INTEGER, vibrar INTEGER);";
    private static String INSERT_CONFIG =
            "INSERT INTO config (tempo, nivel, geolocalizacao, vibrar) VALUES (20,1,1,1);";

    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public void CriarDB(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(TABLE_INI);
            db.execSQL(TABLE_POSTURA);
            db.execSQL(TABLE_CONFIG);

            SQLiteDatabase dbConfig = getReadableDatabase();
            String sql = "SELECT * FROM config";
            Cursor cursor = dbConfig.rawQuery(sql, null);

            if(cursor.getCount() == 0){
                db.execSQL(INSERT_CONFIG);
            }




        } catch (Exception e){
            e.getMessage();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
