package com.berat.lojistikvekargo;
//Berat Arslanca
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import java.awt.EventQueue;

@SpringBootApplication
public class LojistikVeKargoYonetimSistemiApplication {

    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(LojistikVeKargoYonetimSistemiApplication.class)
                .headless(false)
                .run(args);
        
        EventQueue.invokeLater(() -> {
            KargoEkrani anaEkran = context.getBean(KargoEkrani.class);
            anaEkran.setVisible(true);
        });
        
    }
}