package com.example.plazmabankam2;

import java.io.Serializable;

public class Gonullu implements Serializable {
    private String GonulluIl;
    private String GonullUIlce;
    private  String GonulluKanGrubu;
    private  String GonulluCinsiyet;

    public Gonullu(){}

    public Gonullu(String gonulluIl, String gonullUIlce, String gonulluKanGrubu, String gonulluCinsiyet) {
        GonulluIl = gonulluIl;
        GonullUIlce = gonullUIlce;
        GonulluKanGrubu = gonulluKanGrubu;
        GonulluCinsiyet = gonulluCinsiyet;
    }

    public String getGonulluIl() {
        return GonulluIl;
    }

    public void setGonulluIl(String gonulluIl) {
        GonulluIl = gonulluIl;
    }

    public String getGonullUIlce() {
        return GonullUIlce;
    }

    public void setGonullUIlce(String gonullUIlce) {
        GonullUIlce = gonullUIlce;
    }

    public String getGonulluKanGrubu() {
        return GonulluKanGrubu;
    }

    public void setGonulluKanGrubu(String gonulluKanGrubu) {
        GonulluKanGrubu = gonulluKanGrubu;
    }

    public String getGonulluCinsiyet() {
        return GonulluCinsiyet;
    }

    public void setGonulluCinsiyet(String gonulluCinsiyet) {
        GonulluCinsiyet = gonulluCinsiyet;
    }
}
