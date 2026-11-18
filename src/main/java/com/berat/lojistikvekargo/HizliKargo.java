package com.berat.lojistikvekargo;
//Berat Arslanca
public class HizliKargo implements Kargo {
    @Override
    public String getTeslimatTuru() { 
        return "Hızlı Teslimat (1 İş Günü)"; 
    }
    @Override
    public double getFiyat() { 
        return 100.0; 
    }
}
