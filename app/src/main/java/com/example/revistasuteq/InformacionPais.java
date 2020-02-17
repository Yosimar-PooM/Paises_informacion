package com.example.revistasuteq;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class InformacionPais extends AppCompatActivity implements Asynchtask {

    private ImageView imgbandera;
    private TextView txtpais;
    private TextView txtcapital;


    private double latitud, longitud;
    private String norte;
    private String sur;
    private String oeste;
    private String este;

    private GoogleMap mapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_pais);
        txtcapital = findViewById(R.id.txtcapital);
        imgbandera = findViewById(R.id.imgbandera);
        txtpais = findViewById(R.id.txtpais);
        Bundle bundle = this.getIntent().getExtras();
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws = new WebService("http://www.geognos.com/api/en/countries/info/"+bundle.getString("codISO")+".json", datos, InformacionPais.this, InformacionPais.this);
        ws.execute("");
    }

    @Override
    public void processFinish(String result) throws JSONException {

        JSONObject jsonObject = new JSONObject(result);
        JSONObject jsonResults = jsonObject.getJSONObject("Results");
        txtpais.setText(jsonResults.getString("Name"));
        JSONObject jsonCapital = jsonResults.getJSONObject("Capital");
        txtcapital.setText(jsonCapital.getString("Name"));
        JSONObject jsonRectangulo = jsonResults.getJSONObject("GeoRectangle");
        norte = jsonRectangulo.getString("North");
        sur = jsonRectangulo.getString("South");
        este = jsonRectangulo.getString("East");
        oeste = jsonRectangulo.getString("West");
        JSONArray jsonGeoPt = jsonResults.getJSONArray("GeoPt");
        latitud = jsonGeoPt.getDouble(0);
        longitud = jsonGeoPt.getDouble(1);
        JSONObject jsonCiudades = jsonResults.getJSONObject("CountryCodes");

        Glide.with(this).load("http://www.geognos.com/api/en/countries/flag/"+jsonCiudades.getString("iso2")+".png").into(imgbandera);
        ObtenerMapa();
    }

    public void ObtenerMapa(){
        SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapa);
        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapa = googleMap;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 5);
                mapa.moveCamera(cameraUpdate);
                DiseñoMarco();
            }
        });
    }
    public void DiseñoMarco(){
        LatLng p1= new LatLng(Double.parseDouble(norte),Double.parseDouble(oeste));
        LatLng p2= new LatLng(Double.parseDouble(norte),Double.parseDouble(este));
        LatLng p3= new LatLng(Double.parseDouble(sur),Double.parseDouble(este));
        LatLng p4= new LatLng(Double.parseDouble(sur),Double.parseDouble(oeste));
        LatLng p5= new LatLng(Double.parseDouble(norte),Double.parseDouble(oeste));
        Polygon marco = mapa.addPolygon(new PolygonOptions()
                .add(p1, p2, p3, p4, p5)
                .strokeColor(Color.parseColor("#7B1FA2"))
                .fillColor(Color.argb(32, 156, 39, 176)));

        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitud, longitud), 5));
    }

}
