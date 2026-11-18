package com.berat.lojistikvekargo;
//Berat Arslanca
public class StandartKargo implements Kargo {
    @Override
    public String getTeslimatTuru() { 
        return "Standart Teslimat (3-5 İş Günü)"; 
    }
    @Override
    public double getFiyat() { 
        return 50.0; 
    }
}
