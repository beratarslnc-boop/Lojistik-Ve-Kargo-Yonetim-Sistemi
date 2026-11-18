package com.berat.lojistikvekargo;
//Berat Arslanca
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*; 
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component 
public class KargoEkrani extends JFrame {
    private Map<String, KargoTakipSistemi> aktifKargolar = new HashMap<>();
    private Map<String, String> sonDurumlar = new HashMap<>();
    
    private final String VERITABANI_DOSYASI = "kargo_veritabani.txt";
    private final String FATURA_DOSYASI = "kargo_faturalari.txt";
    
    public KargoEkrani() {
        setTitle("Lojistik ve Kargo Yönetim Sistemi");
        setSize(700, 400); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        
        JPanel anaPanel = new JPanel();
        anaPanel.setLayout(new BorderLayout());
        
        JLabel baslikLabel = new JLabel("Kargo Yönetim Merkezine Hoş Geldiniz", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        baslikLabel.setBorder(BorderFactory.createEmptyBorder(50, 10, 30, 10)); 
        anaPanel.add(baslikLabel, BorderLayout.NORTH);
        
        JPanel ortaPanel = new JPanel(new GridBagLayout()); 
        
        JButton btnKargoEkle = new JButton("Yeni Kargo Ekle");
        btnKargoEkle.setPreferredSize(new Dimension(200, 45));
        btnKargoEkle.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton btnDurumSorgula = new JButton("Kargo Durumunu Güncelle");
        btnDurumSorgula.setPreferredSize(new Dimension(230, 45));
        btnDurumSorgula.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JPanel butonKapsayici = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        butonKapsayici.add(btnKargoEkle);
        butonKapsayici.add(btnDurumSorgula);
        
        ortaPanel.add(butonKapsayici);
        anaPanel.add(ortaPanel, BorderLayout.CENTER);
        
        verileriTxtDosyasindanOku();
        
        btnKargoEkle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogManager.getInstance().sistemHareketiniKaydet("Yeni Kargo Ekleme formu açıldı.");
                
                JTextField txtGonderici = new JTextField();
                JTextField txtAlici = new JTextField();
                JTextField txtAgirlik = new JTextField("0"); 
                
                JComboBox<String> cmbBolge = new JComboBox<>(new String[]{"Yurt İçi Teslimat", "Yurt Dışı Teslimat"});
                JComboBox<String> cmbTip = new JComboBox<>(new String[]{"Standart Kargo", "Hızlı Kargo"});
                JCheckBox chkSigorta = new JCheckBox("Sigortalı Kargo (+50 TL)");
                JCheckBox chkKirilacak = new JCheckBox("Kırılacak Eşya Paketi (+30 TL)");
                
                Object[] formElemanlari = {
                    "Gönderici Adı:", txtGonderici,
                    "Alıcı Adı:", txtAlici,
                    "Gönderim Bölgesi (Abstract Factory):", cmbBolge,
                    "Kargo Tipi (Factory Method):", cmbTip,
                    "Ekstra Özellikler (Decorator):", chkSigorta, chkKirilacak,
                    "Ağırlık (KG):", txtAgirlik
                };
                
                int secim = JOptionPane.showConfirmDialog(null, formElemanlari, "Yeni Kargo Ekle", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (secim == JOptionPane.OK_OPTION) {
                    
                    if (txtGonderici.getText().trim().isEmpty() || txtAlici.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Lütfen Gönderici ve Alıcı adını boş bırakmayınız!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                        return; 
                    }

                    try {
                        double agirlik = Double.parseDouble(txtAgirlik.getText());
                 
                        KargoFactory fabrika = new KargoFactory();
                        Kargo kargo = fabrika.kargoOlustur(cmbTip.getSelectedIndex() == 0 ? "STANDART" : "HIZLI");
                        
                        if (chkSigorta.isSelected()) kargo = new SigortaliKargo(kargo);
                        if (chkKirilacak.isSelected()) kargo = new KirilacakKargo(kargo);
                        
                        FiyatHesaplamaStratejisi strateji = (cmbBolge.getSelectedIndex() == 0) 
                                ? new YurticiFiyatStratejisi() : new YurtdisiFiyatStratejisi();
                        double agirlikMaliyeti = strateji.hesapla(agirlik);
                        double toplamFiyat = kargo.getFiyat() + agirlikMaliyeti;
                        
                        TasimaFabrikasi bolgeFabrikasi = (cmbBolge.getSelectedIndex() == 0) 
                                ? new YurticiFabrikasi() : new YurtdisiFabrikasi();
                        Arac tasimaAraci = bolgeFabrikasi.aracUret();
                        Belge tasimaBelgesi = bolgeFabrikasi.belgeUret();
                        
                        KargoTakipSistemi yeniTakip = new KargoTakipSistemi();
                        Musteri gercekMusteri = new Musteri(txtAlici.getText()); 
                        yeniTakip.aboneEkle(gercekMusteri);
                        
                        String kargoIsmi = txtGonderici.getText() + " -> " + txtAlici.getText();
                        aktifKargolar.put(kargoIsmi, yeniTakip);
                        sonDurumlar.put(kargoIsmi, "Hazırlanıyor");
                        
                        String sonucMesaji = "Kargo Başarıyla Kaydedildi!\n\n"
                                + "Tür: " + kargo.getTeslimatTuru() + "\n"
                                + "Bölge: " + cmbBolge.getSelectedItem() + "\n"
                                + "Taşıma Aracı: " + tasimaAraci.getAracTipi() + "\n"
                                + "İlişik Evrak: " + tasimaBelgesi.getBelgeTipi() + "\n"
                                + "--------------------------\n"
                                + "Toplam Tutar: " + toplamFiyat + " TL";
                        
                        verileriTxtDosyasinaYaz(); 
                        faturayiTxtyeKaydet(kargoIsmi, sonucMesaji);
                        
                        LogManager.getInstance().sistemHareketiniKaydet("Yeni kargo eklendi ve veritabanına kaydedildi: " + kargoIsmi);
                        JOptionPane.showMessageDialog(null, sonucMesaji, "İşlem Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        
                    } catch (NumberFormatException ex) {
                        LogManager.getInstance().sistemHareketiniKaydet("HATA: Kullanıcı ağırlık değerini hatalı girdi!");
                        JOptionPane.showMessageDialog(null, "Lütfen ağırlık için sadece rakam giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    LogManager.getInstance().sistemHareketiniKaydet("Kullanıcı kargo ekleme işlemini iptal etti.");
                }
            }
        });
        
        btnDurumSorgula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (aktifKargolar.isEmpty()) {
                    LogManager.getInstance().sistemHareketiniKaydet("HATA: Sistemde kargo yokken güncelleme yapılmaya çalışıldı.");
                    JOptionPane.showMessageDialog(null, "Sistemde (.txt) kayıtlı kargo bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LogManager.getInstance().sistemHareketiniKaydet("Durum güncelleme formu açıldı.");
                
                String[] kargoListesi = aktifKargolar.keySet().toArray(new String[0]);
                String secilenKargo = (String) JOptionPane.showInputDialog(null,
                        "Hangi kargonun durumunu güncelleyeceksiniz?",
                        "Kargo Seçimi",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        kargoListesi,
                        kargoListesi[0]);

                if (secilenKargo != null) { 
                    String[] durumlar = {"Hazırlanıyor", "Yola Çıktı", "Dağıtıma Çıktı", "Teslim Edildi"};
                    
                    String mevcutDurum = sonDurumlar.getOrDefault(secilenKargo, "Hazırlanıyor");
                    
                    String secilenDurum = (String) JOptionPane.showInputDialog(null,
                            secilenKargo + " için yeni durumu seçin:\n[Şu anki durum: " + mevcutDurum + "]",
                            "Durum Güncelleme",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            durumlar,
                            mevcutDurum); 

                    if (secilenDurum != null) {
                        KargoTakipSistemi ilgiliKargo = aktifKargolar.get(secilenKargo);
                        ilgiliKargo.durumGuncelle(secilenDurum);
                        sonDurumlar.put(secilenKargo, secilenDurum); 
                        
                        LogManager.getInstance().sistemHareketiniKaydet("GÜNCELLEME (Observer): " + secilenKargo + " durumu '" + secilenDurum + "' yapıldı.");
                        
                        if (secilenDurum.equals("Teslim Edildi")) {
                            aktifKargolar.remove(secilenKargo); 
                            sonDurumlar.remove(secilenKargo); 
                            verileriTxtDosyasinaYaz();          
                            faturayiTxtdenSil(secilenKargo);    
                            
                            LogManager.getInstance().sistemHareketiniKaydet("SİLİNDİ: " + secilenKargo + " teslim edildiği için sistemden kaldırıldı.");
                            JOptionPane.showMessageDialog(null, "Kargo başarıyla teslim edildi ve sistem kayıtlarından silindi.", "Teslimat Tamamlandı", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            verileriTxtDosyasinaYaz(); 
                        }
                        
                    } else {
                        LogManager.getInstance().sistemHareketiniKaydet("Kullanıcı durum güncellemeyi iptal etti.");
                    }
                }
            }
        });
        
        add(anaPanel);
    }   
    private void verileriTxtDosyasinaYaz() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(VERITABANI_DOSYASI))) {
            for (String kargoIsmi : aktifKargolar.keySet()) {
                String durum = sonDurumlar.getOrDefault(kargoIsmi, "Hazırlanıyor");
                bw.write(kargoIsmi + "###" + durum);
                bw.newLine();
            }
        } catch (IOException e) {
            LogManager.getInstance().sistemHareketiniKaydet("HATA: Veritabanı (.txt) dosyasına yazılamadı!");
        }
    }
    private void verileriTxtDosyasindanOku() {
        File dosya = new File(VERITABANI_DOSYASI);
        if (!dosya.exists()) return; 

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                String[] anaParcalar = satir.split("###");
                if (anaParcalar.length >= 1) {
                    String kargoIsmi = anaParcalar[0];
                    String durum = (anaParcalar.length == 2) ? anaParcalar[1] : "Hazırlanıyor";
                    
                    String[] isimParcalar = kargoIsmi.split(" -> ");
                    if (isimParcalar.length == 2) {
                        KargoTakipSistemi yuklenenTakip = new KargoTakipSistemi();
                        yuklenenTakip.aboneEkle(new Musteri(isimParcalar[1]));
                        
                        aktifKargolar.put(kargoIsmi, yuklenenTakip);
                        sonDurumlar.put(kargoIsmi, durum);
                    }
                }
            }
            LogManager.getInstance().sistemHareketiniKaydet("Eski veriler ve son durumlar veritabanından başarıyla yüklendi.");
        } catch (IOException e) {
            LogManager.getInstance().sistemHareketiniKaydet("HATA: Veritabanı okuma hatası!");
        }
    }
    
    private void faturayiTxtyeKaydet(String kargoIsmi, String faturaDetayi) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FATURA_DOSYASI, true))) { 
            String zaman = java.time.LocalDateTime.now().toString().substring(0, 19).replace("T", " ");
            bw.write("=== FATURA KAYDI | " + zaman + " ===");
            bw.newLine();
            bw.write("Kargo İşlemi: " + kargoIsmi);
            bw.newLine();
            bw.write(faturaDetayi);
            bw.newLine();
            bw.write("================================================");
            bw.newLine();
            bw.newLine();
        } catch (IOException e) {
            LogManager.getInstance().sistemHareketiniKaydet("HATA: Fatura .txt dosyasına kaydedilemedi.");
        }
    }
    
    private void faturayiTxtdenSil(String silinecekKargo) {
        File dosya = new File(FATURA_DOSYASI);
        if (!dosya.exists()) return;

        StringBuilder yeniIcerik = new StringBuilder();
        StringBuilder gecerliBlok = new StringBuilder();
        boolean bloguSil = false;

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                if (satir.trim().isEmpty() && gecerliBlok.length() == 0) continue; 
                
                gecerliBlok.append(satir).append("\n");
                
                if (satir.startsWith("Kargo İşlemi: " + silinecekKargo)) {
                    bloguSil = true;
                }               
                if (satir.equals("================================================")) {
                    if (!bloguSil) { 
                        yeniIcerik.append(gecerliBlok.toString()).append("\n");
                    }
                    gecerliBlok.setLength(0); 
                    bloguSil = false;
                }
            }
        } catch (IOException e) {
            LogManager.getInstance().sistemHareketiniKaydet("HATA: Fatura okuma hatası.");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FATURA_DOSYASI))) {
            bw.write(yeniIcerik.toString());
        } catch (IOException e) {
            LogManager.getInstance().sistemHareketiniKaydet("HATA: Fatura güncellenirken yazılamadı.");
        }
    }
}