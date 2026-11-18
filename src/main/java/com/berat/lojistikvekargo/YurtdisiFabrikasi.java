package com.berat.lojistikvekargo;
//Berat Arslanca
public class YurtdisiFabrikasi implements TasimaFabrikasi {
    public Arac aracUret() {
        return new Arac() { public String getAracTipi() { return "Kargo Uçağı (Hava Yolu)"; } };
    }
    public Belge belgeUret() {
        return new Belge() { public String getBelgeTipi() { return "Gümrük Beyannamesi"; } };
    }
}