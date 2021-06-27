package com.example.plazmabankam2;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HastaGetirActivity extends AppCompatActivity {
    Gonullu gonullu;
    ArrayList<Hasta> hastalar = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    AdapterHastalar adapterHastalar;
    ListView listViewHastalar;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasta_getir);
        listViewHastalar = findViewById(R.id.listViewHastalar);

        gonullu = (Gonullu)getIntent().getSerializableExtra("gonullu");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("Hasta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Hasta hasta = snapshot.getValue(Hasta.class);

                    if(
                            hasta.getHastaIl().equals(hasta.getHastaIl())
                                    &&
                                    gonullu.getGonullUIlce().equals(hasta.getHastaIlce())
                                    &&
                                    gonullu.getGonulluCinsiyet().equals(hasta.getHastaCinsiyet())
                                    &&
                                    gonullu.getGonulluKanGrubu().equals(hasta.getHastaKan())
                    ){

                        hastalar.add(hasta);

                        /*
                        new Hasta(
                                        hasta.getYakinAdSoyad(),
                                        hasta.getYakinTelefon(),
                                        hasta.getHastaAdSoyad(),
                                        hasta.getHastaYas(),
                                        hasta.getHastaCinsiyet(),
                                        hasta.getHastaKan(),
                                        hasta.getHastaIl(),
                                        hasta.getHastaIlce()
                                )
                         */
                    }
                }
                adapterHastalar = new AdapterHastalar(hastalar,getApplicationContext());
                listViewHastalar.setAdapter(adapterHastalar);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });



    }
}
