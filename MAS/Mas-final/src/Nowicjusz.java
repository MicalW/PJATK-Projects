import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Nowicjusz extends Pracownik{
    private List<Zamowienie> zamowienieList = new ArrayList<>();
    private Starszy starszy;
    private ZamowienieGui zamowienieGui;
    public Nowicjusz(String imie, String nazwisko, String email, String numer_telefon, Adres adres, LocalDateTime dataZatrudnienia, LocalDateTime dataZwolnienia, String numerKonta, int placa) {
        super(imie, nazwisko, email, numer_telefon, adres, dataZatrudnienia, dataZwolnienia, numerKonta, placa);
    }
    public Nowicjusz(String imie, String nazwisko, String email, String numer_telefon, Adres adres, LocalDateTime dataZatrudnienia, String numerKonta, int placa) {
        super(imie, nazwisko, email, numer_telefon, adres, dataZatrudnienia, numerKonta, placa);
    }
    public void setZamowienieGui(ZamowienieGui zamowienieGui) {
        if(zamowienieGui != null) {
            this.zamowienieGui = zamowienieGui;
        }
    }
    public List<Zamowienie> getZamowienieList() {
        return Collections.unmodifiableList(zamowienieList);
    }

    public Starszy awans(){
        Starszy starszy = new Starszy(this);
        this.remove();
        return starszy;
    }
    public void addZamowienie(Zamowienie zamowienie) {
        if(zamowienie!=null&&!zamowienieList.contains(zamowienie)){
            zamowienieList.add(zamowienie);
            zamowienie.setNowicjusz(this);
        }
    }
    public void removeZamowienie(Zamowienie zamowienie) {
        if(zamowienieList.contains(zamowienie)){
            zamowienieList.remove(zamowienie);
            zamowienie.removeNowicjusz(this);
        }
    }
    public void remove(){
        if(!zamowienieList.isEmpty()){
            for(Zamowienie zamowienie: new ArrayList<>(zamowienieList)){
                zamowienie.removeNowicjusz(this);
            }
            removeFromExtent();
        }
    }

}
