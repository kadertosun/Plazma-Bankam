package com.example.plazmabankam2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    Button btn1,btn2 ,btn3;
    Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        btn1 = findViewById ( R.id.buttonGonullu );
        btn2 = findViewById ( R.id.buttonHasta );



        //Butonumuza tıklama özelliği kazandırıyoruz.
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GonulluKayitActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, HastaKayitActivity.class);
                startActivity(intent);
            }
        });


    }


}