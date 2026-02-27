import util.ObjectPlus;

import java.time.LocalDateTime;

public class OcenaProduktu extends ObjectPlus{
    private Produkt produkt;
    private Klient klient;
    private LocalDateTime data;
    private String komentrz;
    private int ocena;
    private boolean czyZostaloDodane;
    public OcenaProduktu(Produkt produkt, Klient klient, String komentrz, int ocena) {
        try{
        setCzyZostaloDodane(false);
        setProdukt(produkt);
        setKlient(klient);
        setKomentrz(komentrz);
        setOcena(ocena);
        this.data = LocalDateTime.now();
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }
    public OcenaProduktu(Produkt produkt, Klient klient, int ocena) {
        try{
        setCzyZostaloDodane(false);
        setProdukt(produkt);
        setKlient(klient);
        setOcena(ocena);
        this.data = LocalDateTime.now();
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    public void setCzyZostaloDodane(boolean czyZostaloDodane) {
        if(this.klient!=null&this.produkt!=null) {
            this.czyZostaloDodane = czyZostaloDodane;
        }
    }
    public boolean getCzyZostaloDodane() {
        return czyZostaloDodane;
    }

    public void remove(){
        if(this.produkt != null){
            produkt.removeOcenaProduktu(this);
            this.produkt = null;
        }
        if(this.klient != null){
            klient.removeOcenaProduktu(this);
            this.klient = null;
        }
        removeFromExtent();
    }

    public void setKlient(Klient klient) {
        if(klient != null&&this.klient == null) {
            this.klient = klient;
            klient.setOcenaProduktu(this);
            setCzyZostaloDodane(true);
        }
    }

    public Klient getKlient() {
        return klient;
    }

    public void setProdukt(Produkt produkt) {
        if(produkt != null&&this.produkt == null) {
            this.produkt = produkt;
            produkt.setOcenaProduktu(this);
            setCzyZostaloDodane(true);
        }
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setOcena(int ocena) {
        if(ocena <1 || ocena>10){
            throw new IllegalArgumentException("Ocena Produktu musi być pomiędzy 1 a 10");
        }
        this.ocena = ocena;
    }

    public int getOcena() {
        return ocena;
    }

    public void setKomentrz(String komentrz) {
        if(komentrz.isBlank()){
            throw new IllegalArgumentException("Komentarz nie może być pusty");
        }
        this.komentrz = komentrz;
    }

    public String getKomentrz() {
        return komentrz;
    }

    @Override
    public String toString() {
        return "OcenaProduktu{" +
                "produkt=" + produkt.getNazwa() +
                ", klient=" + klient.getImie() +
                ", data=" + data +
                ", komentrz='" + komentrz + '\'' +
                ", ocena=" + ocena +
                '}';
    }
}
