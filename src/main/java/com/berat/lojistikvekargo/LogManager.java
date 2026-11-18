package com.berat.lojistikvekargo;
//Berat Arslanca
import org.springframework.stereotype.Component;

@Component
public class LogManager {
    private static LogManager instance;

    private LogManager() {}

    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }
    public void sistemHareketiniKaydet(String islem) {
        String zaman = java.time.LocalTime.now().toString().substring(0, 8);
        System.out.println("[" + zaman + "] SİSTEM LOG: " + islem);
    }
}