package com.example.plazmabankam2;

public class Hasta {
    private String YakinAdSoyad;
    private  String YakinTelefon;
    private String HastaAdSoyad;
    private  String HastaYas;
    private String HastaCinsiyet;
    private String HastaKan;
    private String HastaIl;
    private  String HastaIlce;
public Hasta(){}

    public Hasta(String yakinAdSoyad, String yakinTelefon, String hastaAdSoyad, String hastaYas, String hastaCinsiyet, String hastaKan, String hastaIl, String hastaIlce) {

        YakinAdSoyad = yakinAdSoyad;
        YakinTelefon = yakinTelefon;
        HastaAdSoyad = hastaAdSoyad;
        HastaYas = hastaYas;
        HastaCinsiyet = hastaCinsiyet;
        HastaKan = hastaKan;
        HastaIl = hastaIl;
        HastaIlce = hastaIlce;
    }

    public String getYakinAdSoyad() {
        return YakinAdSoyad;
    }

    public void setYakinAdSoyad(String yakinAdSoyad) {
        YakinAdSoyad = yakinAdSoyad;
    }

    public String getYakinTelefon() {
        return YakinTelefon;
    }

    public void setYakinTelefon(String yakinTelefon) {
        YakinTelefon = yakinTelefon;
    }

    public String getHastaAdSoyad() {
        return HastaAdSoyad;
    }

    public void setHastaAdSoyad(String hastaAdSoyad) {
        HastaAdSoyad = hastaAdSoyad;
    }

    public String getHastaYas() {
        return HastaYas;
    }

    public void setHastaYas(String hastaYas) {
        HastaYas = hastaYas;
    }

    public String getHastaCinsiyet() {
        return HastaCinsiyet;
    }

    public void setHastaCinsiyet(String hastaCinsiyet) {
        HastaCinsiyet = hastaCinsiyet;
    }

    public String getHastaKan() {
        return HastaKan;
    }

    public void setHastaKan(String hastaKan) {
        HastaKan = hastaKan;
    }

    public String getHastaIl() {
        return HastaIl;
    }

    public void setHastaIl(String hastaIl) {
        HastaIl = hastaIl;
    }

    public String getHastaIlce() {
        return HastaIlce;
    }

    public void setHastaIlce(String hastaIlce) {
        HastaIlce = hastaIlce;
    }
}
