package com.berat.lojistikvekargo;
//Berat Arslanca
public class KirilacakKargo extends KargoEkstra {
    public KirilacakKargo(Kargo temelKargo) {
        super(temelKargo);
    }
    @Override
    public String getTeslimatTuru() {
        return super.getTeslimatTuru() + " + Kırılacak Eşya Paketi";
    }
    @Override
    public double getFiyat() {
        return super.getFiyat() + 30.0; 
    }
}