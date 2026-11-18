package com.berat.lojistikvekargo;
//Berat Arslanca
public class YurticiFiyatStratejisi implements FiyatHesaplamaStratejisi {
    @Override
    public double hesapla(double agirlik) {
        return agirlik * 10.0; 
    }
}