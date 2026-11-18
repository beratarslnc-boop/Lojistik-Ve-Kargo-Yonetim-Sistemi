package com.berat.lojistikvekargo;
//Berat Arslanca
public class YurticiFabrikasi implements TasimaFabrikasi {
    public Arac aracUret() {
        return new Arac() { public String getAracTipi() { return "Kamyon (Kara Yolu)"; } };
    }
    public Belge belgeUret() {
        return new Belge() { public String getBelgeTipi() { return "Standart Yurt İçi Faturası"; } };
    }
}