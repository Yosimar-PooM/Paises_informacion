package com.example.revistasuteq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdaptadorPaises extends ArrayAdapter<Paises> {

    public AdaptadorPaises(Context context, ArrayList<Paises> datos) {
        super(context, R.layout.ly_items, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.ly_items, null);
        TextView lblTitulo = (TextView)item.findViewById(R.id.txt_titulo);
        lblTitulo.setText(getItem(position).getTitulo());
        ImageView imageView = (ImageView)item.findViewById(R.id.imageView);
        imageView.setTag(getItem(position).getUrlPdf());
        Glide.with(this.getContext()).load(getItem(position).getUrlPdf())
                //error(R.drawable.imgnotfound)
                .into(imageView);

        return(item);
    }
}
