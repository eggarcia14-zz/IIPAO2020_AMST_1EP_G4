package com.example.parcialpractico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PerfilUsuario extends AppCompatActivity {
    String v_idHero = "idHero";
    TextView txt_name_hero,txt_name_comp, text_id;
    int h_id;
    BarChart barchart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Intent intent = getIntent();
        HashMap<String, String> info_user = (HashMap<String, String>) intent.getSerializableExtra("info_user");

        txt_name_hero = findViewById(R.id.txt_nombre);
        txt_name_comp = findViewById(R.id.txt_nombre_comp);
        text_id = findViewById(R.id.txt_userId);
        barchart = findViewById(R.id.graficoBarras);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            h_id = Integer.parseInt(extras.getString("id"));
            getHero();
        }
    }


    private void getHero(){
        String url= String.format("%s/%d", "https://www.superheroapi.com/api.php/1956154021217901",h_id);
        JsonObjectRequest request = new JsonObjectRequest(
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            text_id.setText(response.getString("id"));
                            txt_name_hero.setText(response.getString("name"));
                            txt_name_comp.setText(response.getJSONObject("biography").getString("full-name"));

                            JSONObject powerStats= response.getJSONObject("powerstats");

                            ArrayList<IBarDataSet> datos = new ArrayList<>();
                            ArrayList<BarEntry> lista = new ArrayList<>();
                            Iterator<String> clave = powerStats.keys();
                            int valor=0;
                            while (clave.hasNext() ){

                                String clave_actual= clave.next();
                                try {
                                    BarEntry bar=new BarEntry(0+valor, powerStats.getInt(clave_actual));
                                    lista.add(bar);
                                    valor+=1;

                                }catch (Exception e){
                                    //nada
                                }
                            }
                            BarDataSet conjunto = new BarDataSet(lista, "Power");
                            conjunto.setColors(ColorTemplate.LIBERTY_COLORS);
                            conjunto.setDrawValues(true);
                            datos.add(conjunto);

                            BarData valores = new BarData(datos);
                            barchart.setData(valores);
                            barchart.setFitBars(true);
                            barchart.invalidate();
                            barchart.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);



    }


}