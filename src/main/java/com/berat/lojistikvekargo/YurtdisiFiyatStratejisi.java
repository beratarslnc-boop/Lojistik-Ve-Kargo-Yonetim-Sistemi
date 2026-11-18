package com.berat.lojistikvekargo;
//Berat Arslanca
public class YurtdisiFiyatStratejisi implements FiyatHesaplamaStratejisi {
    @Override
    public double hesapla(double agirlik) {
        return agirlik * 50.0; 
    }
}