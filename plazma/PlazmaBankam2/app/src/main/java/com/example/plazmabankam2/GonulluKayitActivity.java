package com.example.plazmabankam2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GonulluKayitActivity extends AppCompatActivity {
    EditText editTextil;
    EditText editTextilce;
    EditText editCinsiyet;
    EditText editTextKanGrubu;
    Button kytbtn;
    Gonullu gonullu;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_gonullu_kayit );
        editTextil=(EditText)findViewById(R.id.Gil);
        editTextilce=(EditText)findViewById(R.id.Gilce);
        editCinsiyet=(EditText)findViewById(R.id.Cinsiyett);
        editTextKanGrubu=(EditText)findViewById(R.id.Kangrubu);



        kytbtn=(Button)findViewById(R.id.kayıtbtnn);
        kytbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef= FirebaseDatabase.getInstance().getReference().child("Gönüllüler");

                gonullu = new Gonullu(
                        editTextil.getText().toString(),
                        editTextilce.getText().toString(),
                        editTextKanGrubu.getText().toString(),
                        editCinsiyet.getText().toString()

                );

                dbRef.push().setValue(
                        gonullu
                );
                Intent intent = new Intent(GonulluKayitActivity.this, HastaGetirActivity.class);
                intent.putExtra("gonullu",gonullu);
                startActivity(intent);
            }
        });

    }

}
