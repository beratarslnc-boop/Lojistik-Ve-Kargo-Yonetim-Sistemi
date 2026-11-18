package com.berat.lojistikvekargo;
//Berat Arslanca
import java.util.ArrayList;
import java.util.List;

public class KargoTakipSistemi {
    private List<Gozlemci> gozlemciler = new ArrayList<>();
    private String durum;

    public void aboneEkle(Gozlemci g) {
        gozlemciler.add(g);
    }

    public void durumGuncelle(String yeniDurum) {
        this.durum = yeniDurum;
        
        for (Gozlemci g : gozlemciler) {
            g.guncelle(durum);
        }
    }
}