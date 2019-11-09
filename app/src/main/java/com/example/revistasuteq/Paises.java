package com.example.revistasuteq;

import org.json.JSONException;

public class Paises {
    private String nombre;
    private String urlPdf;

    public String getTitulo() {
        return nombre;
    }

    public void setTitulo(String titulo) {
        this.nombre = titulo;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }
    public Paises() throws JSONException {

        }

    public Paises(String title, String url){
        nombre = title;
        urlPdf=url;
    }

}
