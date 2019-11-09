package com.example.revistasuteq;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
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
        //registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
            pais.setTitulo(paise.getString("Name"));
            JSONObject codigopais = paise.getJSONObject("CountryCodes");
            pais.setUrlPdf("http://www.geognos.com/api/en/countries/flag/"+codigopais.getString("iso2")+".png");
            paises.add(pais);
        }
        AdaptadorPaises adaptadorPaises = new AdaptadorPaises(this, paises);
        lstOpciones.setAdapter(adaptadorPaises);

    }

    private DownloadManager.Request request;
    //long downloadID ;

   /* private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };*/
    /*@Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this.getApplicationContext(),((Paises)parent.getItemAtPosition(position)).getUrlPdf(),Toast.LENGTH_LONG).show();
        request = new DownloadManager.Request(Uri.parse(((Paises)parent.getItemAtPosition(position)).getUrlPdf()));
        request.setDescription("PDF	Paper");
        request.setTitle("Pdf Artcilee");
        if (Build.VERSION.SDK_INT >=	Build.VERSION_CODES.HONEYCOMB)	{
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,	"filedownload.pdf");
        DownloadManager manager	=	(DownloadManager)this.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        try	{
            manager.enqueue(request);
        }	catch	(Exception e)	{
            Toast.makeText(this.getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        //DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //downloadID= downloadManager.enqueue(request);// enqueue puts the download request in the queue.

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
