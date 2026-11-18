package com.berat.lojistikvekargo;
//Berat Arslanca
import org.springframework.stereotype.Component;
@Component
public class KargoFactory {
    public Kargo kargoOlustur(String tip) {
        if (tip.equalsIgnoreCase("STANDART")) {
            return new StandartKargo();
        } else if (tip.equalsIgnoreCase("HIZLI")) {
            return new HizliKargo();
        }
        return null;
    }
}
