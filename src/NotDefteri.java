import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Singleton Tasarım Deseni: NotDefteri
class NotDefteri {

    private static NotDefteri instance;
    private JFrame frame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private DosyaIslemleri dosyaIslemleri;
    private List<Observer> observers;
    private State state;

    private NotDefteri() {
        // Observer listesi oluştur
        observers = new ArrayList<>();

        // Ana pencereyi oluştur
        frame = new JFrame("Not Defteri");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Metin alanı oluştur
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Menü çubuğu oluştur
        JMenuBar menuBar = new JMenuBar();
        JMenu menuDosya = new JMenu("Dosya");

        JMenuItem yeni = new JMenuItem("Yeni");
        JMenuItem ac = new JMenuItem("Aç");
        JMenuItem kaydet = new JMenuItem("Kaydet");
        JMenuItem cikis = new JMenuItem("Çıkış");

        menuDosya.add(yeni);
        menuDosya.add(ac);
        menuDosya.add(kaydet);
        menuDosya.addSeparator();
        menuDosya.add(cikis);
        menuBar.add(menuDosya);

        frame.setJMenuBar(menuBar);

        // Dosya seçici oluştur
        fileChooser = new JFileChooser();

        // Dosya işlemleri atanıyor
        dosyaIslemleri = new GercekDosyaIslemleri();

        // Olay dinleyicileri ekle
        yeni.addActionListener(e -> yeniDosya());
        ac.addActionListener(e -> dosyaIslemleri.dosyaAc(textArea, frame, fileChooser));
        kaydet.addActionListener(e -> {
            dosyaIslemleri.dosyaKaydet(textArea, frame, fileChooser);
            setState(new KaydedildiState());
        });
        cikis.addActionListener(e -> frame.dispose());

        // Pencereyi görünür yap
        frame.setVisible(true);
    }

    public static NotDefteri getInstance() {
        if (instance == null) {
            instance = new NotDefteri();
        }
        return instance;
    }

    private void yeniDosya() {
        textArea.setText("");
        setState(new YeniState());
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(String state) {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }

    public void setState(State state) {
        this.state = state;
        System.out.println("Geçerli durum: " + state);
    }

}
