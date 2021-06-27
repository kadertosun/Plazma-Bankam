package com.example.plazmabankam2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterHastalar extends BaseAdapter {

    private ArrayList<Hasta> hastalar = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public AdapterHastalar(){

    }

    public AdapterHastalar(ArrayList<Hasta> hastalar, Context context){
        this.hastalar = hastalar;
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return hastalar.size();
    }

    @Override
    public Hasta getItem(int position) {
        return hastalar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.row_hastalar,null);

        TextView tvHastaAdSoyad = view.findViewById(R.id.txtHastaAdSoyad);
        TextView tvHastaCinsiyet = view.findViewById(R.id.txtHastaCinsiyet);
        TextView tvHastaIl = view.findViewById(R.id.txtHastaIl);
        TextView tvHastaIlce = view.findViewById(R.id.txtHastaIlcesi);
        TextView tvHastaKanGrubu = view.findViewById(R.id.txtHastaKanGrubu);
        TextView tvHastaYas = view.findViewById(R.id.txtHastaYas);
        TextView tvHastaYakinAdSoyad = view.findViewById(R.id.txtHastaYakinAdSoyad);
        TextView tvHastaYakinTelefon = view.findViewById(R.id.txtHastaYakinTelefon);

        tvHastaAdSoyad.setText(hastalar.get(position).getHastaAdSoyad());
        tvHastaCinsiyet.setText(hastalar.get(position).getHastaCinsiyet());
        tvHastaIl.setText(hastalar.get(position).getHastaIl());
        tvHastaIlce.setText(hastalar.get(position).getHastaIlce());
        tvHastaKanGrubu.setText(hastalar.get(position).getHastaKan());
        tvHastaYas.setText(hastalar.get(position).getHastaYas());
        tvHastaYakinAdSoyad.setText(hastalar.get(position).getYakinAdSoyad());
        tvHastaYakinTelefon.setText(hastalar.get(position).getYakinTelefon());

        return view;
    }
}
