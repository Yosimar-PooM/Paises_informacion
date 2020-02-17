package com.example.revistasuteq;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener {

    private ListView lstOpciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws = new WebService("http://www.geognos.com/api/en/countries/info/all.json", datos, MainActivity.this, MainActivity.this);
        ws.execute("");
        lstOpciones = (ListView)findViewById(R.id.lstopciones);
        lstOpciones.setOnItemClickListener(this);
        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    //public ArrayList<HashMap<String, String>> articuloslist = new ArrayList<>();
    @Override
    public void processFinish(String result) throws JSONException {
        ArrayList<Paises> paises = new ArrayList<>();
        JSONObject Objectjson = new JSONObject(result);
        JSONObject resultado = Objectjson.getJSONObject("Results");
        Iterator<?> iteracion = resultado.keys();
        while (iteracion.hasNext()){
            String clave =(String)iteracion.next();
            JSONObject paise = resultado.getJSONObject(clave);
            Paises pais= new Paises();
            pais.setNombre(paise.getString("Name"));
            JSONObject codigopais = paise.getJSONObject("CountryCodes");
            pais.setCodigo(codigopais.getString("iso2"));
            pais.setUrlPdf("http://www.geognos.com/api/en/countries/flag/"+codigopais.getString("iso2")+".png");
            paises.add(pais);
        }
        AdaptadorPaises adaptadorPaises = new AdaptadorPaises(this, paises);
        lstOpciones.setAdapter(adaptadorPaises);
        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InformacionPais.class);
                Bundle bundle = new Bundle();
                bundle.putString("codISO", ((Paises)parent.getItemAtPosition(position)).getCodigo());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private DownloadManager.Request request;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this.getApplicationContext(),((Paises)parent.getItemAtPosition(position)).getUrlPdf(),Toast.LENGTH_LONG).show();
        request = new DownloadManager.Request(Uri.parse(((Paises)parent.getItemAtPosition(position)).getUrlPdf()));
        request.setDescription("Imagen Paises");
        request.setTitle("Imagenes de paises");
        if (Build.VERSION.SDK_INT >=	Build.VERSION_CODES.HONEYCOMB)	{
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,	"filedownload.png");
        DownloadManager manager	=	(DownloadManager)this.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        try	{
            manager.enqueue(request);
        }	catch	(Exception e)	{
            Toast.makeText(this.getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    public void getPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1) {
            Toast.makeText(this.getApplicationContext(),"OK", Toast.LENGTH_LONG).show();
        }
    }


}
