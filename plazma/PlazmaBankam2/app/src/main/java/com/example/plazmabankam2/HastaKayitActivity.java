package com.example.plazmabankam2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HastaKayitActivity extends AppCompatActivity {

    EditText YadSoyad,YTel,HadSoyad,Hyas,Hcinsiyet,Hkan,Hil,Hilce;
    Button btnkayit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.hasta_iletisim );
        YadSoyad=(EditText)findViewById(R.id.Yakinadsoyad);
        YTel=(EditText)findViewById(R.id.Yakiniletisim);
        HadSoyad=(EditText)findViewById(R.id.hastaAd);
        Hyas=(EditText)findViewById(R.id.hastaYas);
        Hcinsiyet=(EditText)findViewById(R.id.cinsiyettxt);
        Hkan=(EditText)findViewById(R.id.kan);
        Hil=(EditText)findViewById(R.id.il);
        Hilce=(EditText)findViewById(R.id.ilce);
        btnkayit=(Button)findViewById(R.id.Kayitbtn);


        btnkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Hasta");
               dbRef.push().setValue(
                       new Hasta(
                               YadSoyad.getText().toString(),
                               YTel.getText().toString(),
                               HadSoyad.getText().toString(),
                               Hyas.getText().toString(),
                               Hcinsiyet.getText().toString(),
                               Hkan.getText().toString(),
                               Hil.getText().toString(),
                               Hilce.getText().toString()

                       )
               );
               startActivity(new Intent(HastaKayitActivity.this, MainActivity.class));
            }
        });




    }



}