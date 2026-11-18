package com.berat.lojistikvekargo;
//Berat Arslanca
public class SigortaliKargo extends KargoEkstra {

    public SigortaliKargo(Kargo temelKargo) {
        super(temelKargo);
    }
    @Override
    public String getTeslimatTuru() {
        return super.getTeslimatTuru() + " + Sigortalı Teslimat";
    }
    @Override
    public double getFiyat() {
        return super.getFiyat() + 50.0; 
    }
}