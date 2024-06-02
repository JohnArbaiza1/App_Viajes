package com.example.destinos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.destinos.Destinos;
import com.example.destinos.R;

import java.util.ArrayList;

public class destinoAdapater extends BaseAdapter {

    public Context context;

    public ArrayList<Destinos> dataDestinos;


    public destinoAdapater(Context context, ArrayList<Destinos> dataDestinos) {
        this.context = context;
        this.dataDestinos = dataDestinos;
    }

    @Override
    public int getCount() {
        return this.dataDestinos.size();
    }

    @Override
    public Object getItem(int position) {
        return dataDestinos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = LayoutInflater.from(context).inflate(R.layout.item_list,null);

        Destinos ds = dataDestinos.get(position);


        TextView txtnombre= view.findViewById(R.id.lblnameD);
        TextView txtdes= view.findViewById(R.id.lbldesD);
        TextView txtdire= view.findViewById(R.id.lbldireD);
        ImageView img= view.findViewById(R.id.imageView3);



        return view;
    }
}
