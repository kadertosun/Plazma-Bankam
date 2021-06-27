package com.example.plazmabankam2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class BilgilerActivity extends Activity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    Button btndegisken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_bilgiler );

        tv1 = findViewById ( R.id.tv1 );
        tv2 = findViewById ( R.id.tv2 );
        tv3 = findViewById ( R.id.tv3 );
        tv4 = findViewById ( R.id.tv4 );
        tv5 = findViewById ( R.id.tv5 );
        tv6 = findViewById ( R.id.tv6 );
        tv7 = findViewById ( R.id.tv7 );
        tv8 = findViewById ( R.id.tv8 );
        btndegisken=findViewById ( R.id.BtnTamam );


        tv1.setText ( "COVİD-19 NEDİR?" );
        tv2.setText ( "COVID-19 hastalığı, solunum yolu belirtileri; ateş, öksürük, nefes darlığı gibi belirti gösteren  bir virüstür." );
        tv3.setText ( "COVİD-19 PLAZMA TEDAVİSİ NEDİR?" );
        tv4.setText ( "Hastalık geçiren ve sağlığına kavuşan hastaların kanlarına halen virüse karşı oluşan antikor ve proteinler bulunmaktadır. Bu protein ve antikorlar kan plazmasında serum adı verilen sarı sıvı alanda yer alır. İmmün plazma tedavisinde ise hastalığı geçiren ve sağlığına kavuşan kişilerden alınan kan plazmaları, durumu ağrı olan hastalara enjekte edilmektedir." );
        tv5.setText ( "KİMLER KAN VEREBİLİR?" );
        tv6.setText ("  1) 18-60 yaş arası bağışçılar.\n" +
                "  2) Tüm bağışçı adaylarının mutlaka COVID-19 hastalığı tanısı aldığına dair laboratuvar test sonucu olmalıdır.\n" +
                "  3) COVID-19 tanısı almış ancak tedavi ve/veya karantina sürecini evde tamamlayan bağışçı adaylarının klinik olarak tam iyileşmesinin üzerinden en az 28 gün geçmiş ve bağış öncesinde nazofarenks sürüntü örneğinden çalışılmış SARS-CoV-2 moleküler test sonucu “NEGATİF” olmalıdır."

        );
        tv7.setText ( "KİMLER KAN ALABİLİR?" );
        tv8.setText ( "  1) BT’de COVID-19 ile uyumlu görüntü olması ve bilateral yaygın tutulum varlığı,\n" +
                "  2) Solunum sayısı > 30/dakika,\n" +
                "  3) PaO2 / FiO2 <300 mm Hg> 5litre/dakika nazal oksijen desteğine rağmen oksijen saturasyonu <%90>5litre/dakika nazal oksijen desteğine rağmen parsiyel oksijen basıncı < 70 mmHg,\n" +
                "  4) Mekanik ventilasyon ihtiyacı,\n" +
                "  5) SOFA skorunda artış,\n" +
                "  6) Vazopressör ihtiyacı,\n" +
                "  7) Hızlı klinik progresyon beklenen ve kötü prognostik parametreleri olan hastalar (lenfopeni; CRP, ESH, ferritin, LDH ve D-dimer yüksekliği). "
        );
        btndegisken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Butona tıklandığında ne yapmasını gerektiğini belirttik
                Intent gecisYap = new Intent (BilgilerActivity.this, MapsActivity.class);
                startActivity(gecisYap);
            }

        });
    }
}

