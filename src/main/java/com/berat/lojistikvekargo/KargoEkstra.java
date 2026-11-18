package com.berat.lojistikvekargo;
//Berat Arslanca
public abstract class KargoEkstra implements Kargo {
    protected Kargo temelKargo;

    public KargoEkstra(Kargo temelKargo) {
        this.temelKargo = temelKargo;
    }
    @Override
    public String getTeslimatTuru() {
        return temelKargo.getTeslimatTuru();
    }
    @Override
    public double getFiyat() {
        return temelKargo.getFiyat();
    }
}