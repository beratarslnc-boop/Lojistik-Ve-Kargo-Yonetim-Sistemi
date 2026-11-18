package com.berat.lojistikvekargo;
//Berat Arslanca
import javax.swing.JOptionPane;

public class Musteri implements Gozlemci {
    private String ad;

    public Musteri(String ad) {
        this.ad = ad;
    }

    @Override
    public void guncelle(String kargoDurumu) {
        JOptionPane.showMessageDialog(null, 
            ad + " adlı müşteriye sistem bildirimi:\nKargonuzun Yeni Durumu: " + kargoDurumu, 
            "Müşteri SMS Bildirimi", 
            JOptionPane.WARNING_MESSAGE);
    }
}