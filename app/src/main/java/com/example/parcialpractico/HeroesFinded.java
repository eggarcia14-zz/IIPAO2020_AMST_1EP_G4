package com.example.parcialpractico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HeroesFinded extends AppCompatActivity{
    private String filtro;
    private RequestQueue mQueue;
    private JSONArray heroes;
    private TextView txt_resultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_finded);
        mQueue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        txt_resultados = findViewById(R.id.txt_resultados);
        if (extras != null) {
            String filtro = extras.getString("filtro");
            buscarHeroe(filtro);
        }
    }

    private void buscarHeroe(String filtro) {
        String url_temp = "https://www.superheroapi.com/api.php/1956154021217901/search/" + filtro;
        System.out.println(url_temp);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url_temp, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            heroes = response.getJSONArray("results");
                            System.out.println(heroes.toString());
                            txt_resultados.setText("Resultados: "+heroes.length());
                            anadirScrollView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + "1956154021217901");
                System.out.println("1956154021217901");
                return params;
            }
        };
        ;
        mQueue.add(request);
    }

    private void anadirScrollView() throws JSONException {
        LinearLayout container = (LinearLayout) findViewById(R.id.linearItems);
        final TextView[] myTextViews = new TextView[heroes.length()]; // create an empty array;//this will be your container layout
        for (int i = 0; i < heroes.length(); i++) {
            final TextView rowTextView = new TextView(this);
            // set some properties of rowTextView or something
            JSONObject elemen = heroes.getJSONObject(i);
            Intent intent = new Intent(this, PerfilUsuario.class);
            rowTextView.setText(elemen.getString("name"));
            rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            rowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        intent.putExtra("id", elemen.getString("id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            // add the textview to the linearlayout
            container.addView(rowTextView);

            // save a reference to the textview for later
            myTextViews[i] = rowTextView;
        }
    }
    public void onClick(View v, JSONObject data) {

    }
}