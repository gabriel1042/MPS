package tig_unibh.tigiii;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;


public class Relatorio extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatorio);

        Bundle b = getIntent().getExtras();
        GraphViewData[] data = null;
        try {
            if (b.containsKey("DADOS")) {
                ArrayList<String> Dados = b.getStringArrayList("DADOS");
                Double v;
                data = new GraphViewData[Dados.size()];
                for (int i = 0; i < Dados.size(); i++) {
                    v = Double.parseDouble(Dados.get(i));
                    data[i] = new GraphViewData(i, v);
                }
            }

            GraphViewSeries seriesSeno = new GraphViewSeries("Prox. 9 - Ideal",new GraphViewSeries.GraphViewSeriesStyle(Color.BLUE,3),data);

            LineGraphView graph = new LineGraphView(this, "Postura");
            graph.addSeries(seriesSeno);
            graph.setShowLegend(true);
            graph.setLegendAlign(GraphView.LegendAlign.TOP);
            graph.getGraphViewStyle().setGridColor(Color.GRAY);
            graph.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
            graph.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
            graph.getGraphViewStyle().setTextSize(15);
            final ArrayList<String> endereco = b.getStringArrayList("ENDERECO");
            graph.setCustomLabelFormatter(new CustomLabelFormatter() {
                int cont = 0;
                int i = 0;
                @Override
                public String formatLabel(double v, boolean isValueX) {
                    if(!isValueX){
                        return String.valueOf(v);
                    }else{
                        /*
                        if(cont % 2 == 0){
                            cont++;
                            int i2 = i;
                            i++;
                            if(i2 > endereco.size()-1){
                                return String.valueOf(i2);
                            }
                            if(endereco.get(i2) == null){
                                return "S/E";
                            }
                            return endereco.get(i2);
                        }
                        cont++;
                        return "";
                        */
                        return String.valueOf(v);
                    }
                }
            });
            graph.setViewPort(9,40);
            graph.setScrollable(true);
            graph.setScalable(true);

            LinearLayout ll = (LinearLayout) findViewById(R.id.graph);
            ll.addView(graph);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage() + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
