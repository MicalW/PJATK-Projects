import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Klient extends Osoba{
    private String login;
    private String haslo;

    private List<Katalog> katalogList = new ArrayList<>();        // relacja z Katalog
    private List<Zamowienie> zamowienieList = new ArrayList<>();  // relacja z Zamowienie
    private List<OcenaProduktu> ocenaProduktuList = new ArrayList<>(); // relacja z ocenaProduktu
    private List<OcenaProduktu> ocenaProduktuListSub3 = new ArrayList<>(); // oceny poniżej 3
    private ZamowienieGui zamowienieGui;

    // Relacje jeden-do-jeden
    private OcenaSklepu ocenaSklepu;
    private KartaStalegoKlienta kartaStalegoKlienta;

    private int klientId;
    private static int id;

    /**
     * Konstruktor podstawowy dla nowego klienta
     */
    public Klient(String imie, String nazwisko, String email, String numerTelefonu, Adres adres,
                  String haslo, String login, Katalog katalog) {
        super(imie, nazwisko, email, numerTelefonu, adres);
        try {
            setLogin(login);
            setHaslo(haslo);
            klientId = id++;
            addKatalog(katalog); // Klient musi mieć dostęp do co najmniej jednego katalogu
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    public void setZamowienieGui(ZamowienieGui zamowienieGui) {
        if(zamowienieGui != null) {
            this.zamowienieGui = zamowienieGui;
        }
    }

    public ZamowienieGui getZamowienieGui() {
        return zamowienieGui;
    }

    /**
     * Konstruktor dla klienta z kartą stałego klienta
     */
    public Klient(String imie, String nazwisko, String email, String numerTelefonu, Adres adres,
                  String haslo, String login, Katalog katalog, KartaStalegoKlienta karta){
        super(imie, nazwisko, email, numerTelefonu, adres);
        try {
            setLogin(login);
            setHaslo(haslo);
            klientId = id++;
            setKartaStalegoKlienta(karta);
            addKatalog(katalog);
        } catch (Exception e) {
            e.printStackTrace();
            removeFromExtent();
        }
    }

    // GETTERY I SETTERY
    public void setLogin(String login) {
        if(login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login nie może być pusty");
        }
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setHaslo(String haslo) {
        if(haslo == null || haslo.isEmpty() || haslo.length() < 5) {
            throw new IllegalArgumentException("Hasło nie posiada przynajmniej 5 znaków");
        }
        this.haslo = haslo;
    }

    public String getHaslo() {
        return haslo;
    }

    public List<Katalog> getKatalogList() {
        return Collections.unmodifiableList(katalogList);
    }

    public int getKlientId() {
        return klientId;
    }

    public OcenaSklepu getOcenaSklepu() {
        return ocenaSklepu;
    }

    public List<OcenaProduktu> getOcenaProduktuList() {
        return Collections.unmodifiableList(ocenaProduktuList);
    }

    public List<OcenaProduktu> getOcenaProduktuListSub3() {
        return Collections.unmodifiableList(ocenaProduktuListSub3);
    }

    public List<Zamowienie> getZamowienieList() {
        return Collections.unmodifiableList(zamowienieList);
    }

    public void setKartaStalegoKlienta(KartaStalegoKlienta kartaStalegoKlienta) {
        this.kartaStalegoKlienta = kartaStalegoKlienta;
    }

    public KartaStalegoKlienta getKartaStalegoKlienta() {
        return kartaStalegoKlienta;
    }

    /**
     * Dodaje dostęp klienta do katalogu - dwukierunkowa relacja
     */
    public void addKatalog(Katalog katalog){
        if(katalog != null && !katalogList.contains(katalog)) {
            katalogList.add(katalog);
            katalog.addKlient(this);
        }
    }

    /**
     * Usuwa dostęp do katalogu - klient musi mieć dostęp do co najmniej jednego katalogu
     */
    public void removeKatalog(Katalog katalog){
        if(katalogList.size() != 1) {
            if (katalogList.contains(katalog)) {
                katalogList.remove(katalog);
                katalog.removeKlient(this);
            }
        } else {
            throw new UnsupportedOperationException("Klient musi posiadać dostęp do chociaż jednego katalogu");
        }
    }

    /**
     * Usuwanie końcowe (przy usuwaniu całego obiektu)
     */
    public void removeKatalogFinal(Katalog katalog){
        if (katalogList.contains(katalog)) {
            katalogList.remove(katalog);
            katalog.removeKlientFinal(this);
        }
    }


    /**
     * Tworzy powiązanie między zamówieniem a klientem
     */
    public void zlozZamowienie(Zamowienie zamowienie){
        if(zamowienie != null && !zamowienieList.contains(zamowienie)){
            zamowienieList.add(zamowienie);
            zamowienie.setKlient(this);
        }
    }

    public void removeZamowienie(Zamowienie zamowienie){
        if(zamowienieList.contains(zamowienie)){
            zamowienieList.remove(zamowienie);
            zamowienie.removeKlient(this);
        }
    }

    /**
     * Dodaje ocenę sklepu - klient może mieć tylko jedną ocenę sklepu
     */
    public void addOcena(OcenaSklepu ocenaSklepu) {
        if(ocenaSklepu != null && this.ocenaSklepu == null){
            this.ocenaSklepu = ocenaSklepu;
            ocenaSklepu.setKlient(this);
        }
    }

    public void removeOcena(OcenaSklepu ocenaSklepu) {
        if(ocenaSklepu.equals(this.ocenaSklepu)){
            this.ocenaSklepu = null;
            ocenaSklepu.removeKlient(this);
        }
    }

    /**
     * Ustawia ocenę produktu - używana przez klasę OcenaProduktu
     */
    public void setOcenaProduktu(OcenaProduktu ocenaProduktu) {
        if(ocenaProduktu != null && !ocenaProduktu.getCzyZostaloDodane()){
            ocenaProduktuList.add(ocenaProduktu);
            // Oceny poniżej 3 dodawane są do osobnej listy dla łatwego dostępu
            if(ocenaProduktu.getOcena() < 3){
                ocenaProduktuListSub3.add(ocenaProduktu);
            }
        }
    }

    /**
     * Dodaje nową ocenę produktu z komentarzem
     */
    public void addProdukt(Produkt produkt, String komentarz, int ocena){
        new OcenaProduktu(produkt, this, komentarz, ocena);
    }

    /**
     * Dodaje nową ocenę produktu bez komentarza
     */
    public void addProdukt(Produkt produkt, int ocena){
        new OcenaProduktu(produkt, this, ocena);
    }

    /**
     * Usuwa ocenę produktu z obu list
     */
    public void removeOcenaProduktu(OcenaProduktu ocenaProduktu){
        List<OcenaProduktu> toRemove = new ArrayList<>();
        List<OcenaProduktu> toRemoveSub3 = new ArrayList<>();

        for(OcenaProduktu o : ocenaProduktuList){
            if((o.getProdukt() == null || o.getProdukt().equals(ocenaProduktu.getProdukt()))
                    && o.getData().equals(ocenaProduktu.getData())){
                toRemove.add(o);
                if(ocenaProduktu.getOcena() < 3){
                    toRemoveSub3.add(o);
                }
            }
        }

        ocenaProduktuList.removeAll(toRemove);
        ocenaProduktuListSub3.removeAll(toRemoveSub3);
    }

    /**
     * Usuwa wszystkie oceny danego produktu
     */
    public void removeProdukt(Produkt produkt){
        List<OcenaProduktu> toRemove = new ArrayList<>();
        List<OcenaProduktu> toRemoveSub3 = new ArrayList<>();

        for(OcenaProduktu o : ocenaProduktuList){
            if(o.getProdukt().equals(produkt)){
                toRemove.add(o);
            }
        }
        for(OcenaProduktu o : ocenaProduktuListSub3){
            if(o.getProdukt().equals(produkt)){
                toRemoveSub3.add(o);
            }
        }

        if(!toRemove.isEmpty()){
            for(OcenaProduktu o : toRemove){
                o.remove();
            }
        }
        if(!toRemoveSub3.isEmpty()){
            for(OcenaProduktu o : toRemoveSub3){
                o.remove();
            }
        }
    }


    /**
     * Usuwa klienta i wszystkie jego powiązania
     */
    public void remove(){
        if(!this.katalogList.isEmpty()){
            for(Katalog katalog : new ArrayList<>(this.katalogList)){
                katalog.removeKlientFinal(this);
            }
        }

        if(!zamowienieList.isEmpty()){
            for(Zamowienie zamowienie : new ArrayList<>(this.zamowienieList)){
                zamowienie.removeKlient(this);
            }
            zamowienieList.clear();
        }

        if(ocenaSklepu != null){
            ocenaSklepu.removeKlient(this);
        }

        if(!ocenaProduktuList.isEmpty()){
            for(OcenaProduktu o : new ArrayList<>(this.ocenaProduktuList)){
                o.remove();
            }
        }
        if(!ocenaProduktuListSub3.isEmpty()){
            for(OcenaProduktu o : new ArrayList<>(this.ocenaProduktuListSub3)){
                o.remove();
            }
        }

        removeFromExtent();
    }

    @Override
    public String toString() {
        return "Klient{" +
                "login='" + login + '\'' +
                ", haslo='" + haslo + '\'' +
                ", katalogList=" + katalogList.stream().map(k->k.getNazwa()).toList() +
                ", klientId=" + klientId +
                ", ocenaSklepu=" + ocenaSklepu +
                ", ocenaProduktuList=" + ocenaProduktuList +
                ", karta " + kartaStalegoKlienta + (kartaStalegoKlienta != null ? kartaStalegoKlienta : null) +
                '}';
    }
}