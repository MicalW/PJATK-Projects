import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Starszy extends Pracownik{
    List<Produkt> produktList = new ArrayList<>();
    public Starszy(String imie, String nazwisko, String email, String numer_telefon, Adres adres, LocalDateTime dataZatrudnienia, LocalDateTime dataZwolnienia, String numerKonta, int placa) {
        super(imie, nazwisko, email, numer_telefon, adres, dataZatrudnienia, dataZwolnienia, numerKonta, placa);
    }
    public Starszy(String imie, String nazwisko, String email, String numer_telefon, Adres adres, LocalDateTime dataZatrudnienia, String numerKonta, int placa) {
        super(imie, nazwisko, email, numer_telefon, adres, dataZatrudnienia, numerKonta, placa);
    }
    public Starszy(Nowicjusz nowicjusz){
        super(nowicjusz);
    }
    public Produkt stworzProdukt(int cena, int ilosc, String nazwa, String tag) {
        return Produkt.createProdukt(cena, ilosc, nazwa,tag, this);
    }
    public List<Produkt> getProduktList() {
        return Collections.unmodifiableList(produktList);
    }

    public void addProdukt(Produkt produkt) {
        if(produktList!=null&&!getProduktList().contains(produkt)){
            produktList.add(produkt);
            produkt.addStarszy(this);
        }
    }
    public void removeProdukt(Produkt produkt) {
        if(produktList!=null&&getProduktList().contains(produkt)){
            produktList.remove(produkt);
            produkt.removeStarszy(this);
        }
    }
    public void remove(){
        if(!produktList.isEmpty()){
            for(Produkt produkt : new ArrayList<>(produktList)){
                produkt.removeStarszy(this);
            }
        }
        removeFromExtent();
    }
}
