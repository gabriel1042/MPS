package tig_unibh.tigiii;
import java.util.ArrayList;

/**
 * Created by Gabriel on 31/10/2016.
 */

public class Dados {
    private ArrayList<String> media_postura = new ArrayList<String>();;
    private ArrayList<String> endereco = new ArrayList<String>();;

    public float getMedia_postura(){
        float mediafinal = 0;
        for (int ni = 0;ni<this.media_postura.size();ni++){
            mediafinal = mediafinal + Float.parseFloat(media_postura.get(ni));
        }
        mediafinal = mediafinal /this.media_postura.size();
        return mediafinal;
    }
    public ArrayList getArrayList_Media_postura(){
        return this.media_postura;
    }
    public void setMedia_postura(Float media_postura){
        this.media_postura.add(media_postura.toString());
    }

}

