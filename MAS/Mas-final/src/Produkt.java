import util.ObjectPlus;

import java.io.Serializable;
import java.util.*;

public class Produkt extends ObjectPlus {
    private int cena;
    private int ilosc;
    private String nazwa;
    private List<String> tagi;
    private static int IDcounter = 0;
    private int id;
    private List<Katalog> katalogList = new ArrayList<>();
    Map<Zamowienie, Integer> zamowienieMap = new HashMap<>();
    Promocja promocja;
    Starszy starszy;
    List<OcenaProduktu> ocenaProduktuList = new ArrayList<>();
    List<OcenaProduktu> ocenaProduktuListSub3 = new ArrayList<>();


    private Produkt(int cena, int ilosc, String nazwa, String tag) {
        try{
        setCena(cena);
        setIlosc(ilosc);
        setNazwa(nazwa);
        id = IDcounter++;
        setTagi(tag);
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    public static Produkt createProdukt(int cena, int ilosc, String nazwa,String tag, Starszy starszy){//metoda klasowa
        if (starszy == null) {
            throw new IllegalArgumentException("Tylko Starszy może tworzyć produkt!");
        }
        return new Produkt(cena, ilosc, nazwa,tag);
    }


    public int getCena() {
        return cena;
    }
    public void setCena(int cena) {
        if(cena <= 0){
            throw new IllegalArgumentException("Cena musi być większa od 0");
        }
        this.cena = cena;
    }
    public int getIlosc() {
        return ilosc;
    }
    public void setIlosc(int ilosc) {
        if(ilosc >= 0){
            this.ilosc = ilosc;
        }else{
            throw new IllegalArgumentException("Produkt nie może być mniejszy od 0");
        }
    }
    public String getNazwa() {
        if(nazwa.isBlank()){
            throw new IllegalArgumentException("Nazwa produktu nie moze być pusta");
        }
        return nazwa;
    }
    public void setNazwa(String nazwa) {
        if(nazwa == null || nazwa.isBlank()){
            throw new IllegalArgumentException("Nazwa nie może być pusta");
        }
        this.nazwa = nazwa;
    }
    public List<String> getTagi() {
        return tagi;
    }
    public void setTagi(String tag) {
        if(tag == null || tag.isBlank()){
            throw new IllegalArgumentException("Tag nie może być pusty");
        }
        if(tagi==null){
            this.tagi = new ArrayList<>();
        }
        tagi.add(tag);
    }
    public int getId() {
        return id;
    }
    public List<Katalog> getKatalogList() {
        return Collections.unmodifiableList(katalogList);
    }
    public Map<Zamowienie, Integer> getZamowienieMap() {
        return Collections.unmodifiableMap(zamowienieMap);
    }
    public void setPromocja(Promocja promocja) {
        if(promocja == null){
            throw new IllegalArgumentException("Promocja nie może być pusta");
        }
        this.promocja = promocja;
    }
    public Promocja getPromocja() {
        return promocja;
    }
    public Starszy getStarszy() {
        return starszy;
    }
    public List<OcenaProduktu> getOcenaProduktuList() {
        return Collections.unmodifiableList(ocenaProduktuList);
    }
    public List<OcenaProduktu> getOcenaProduktuListSub3() {
        return Collections.unmodifiableList(ocenaProduktuListSub3);
    }

    public void addKatalog(Katalog katalog) {
        if(katalog != null || !katalogList.contains(katalog)) {
            katalogList.add(katalog);
            katalog.addProdukt(this);
        }
    }
    public void removeKatalogFinal(Katalog katalog) {
        if(katalogList.contains(katalog)) {
            katalogList.remove(katalog);
            katalog.removeProduktFinal(this);
        }
    }

    public void removeKatalog(Katalog katalog) {
        if(katalogList.size() != 1) {
            if (katalogList.contains(katalog)) {
                katalogList.remove(katalog);
                katalog.removeProdukt(this);
            }
        }else{
            throw new IllegalArgumentException("Produkt musi być w chociaż 1 katalogu");
        }
    }

    public void addZamowienie(Zamowienie zamowienie, int amount) {
        if(!zamowienieMap.containsKey(zamowienie)){
            zamowienieMap.put(zamowienie,amount);
            zamowienie.setProduktList(this,amount);
        }
    }
    public void removeZamowienie(Zamowienie zamowienie) {
        if(zamowienieMap.containsKey(zamowienie)){
            zamowienieMap.remove(zamowienie);
            zamowienie.removeProdukt(this);
        }
    }
    public void addPromocja(Promocja promocja) {
        if(promocja != null && getPromocja()==null){
            this.promocja = promocja;
            promocja.addProdukt(this);
        }
    }
    public void removePromocja(Promocja promocja) {
        if(promocja!=null&&getPromocja()!=null){
            this.promocja=null;
            promocja.removeProdukt(this);
        }
    }
    public void addStarszy(Starszy starszy) {
        if(starszy!=null&&getStarszy()==null){
            this.starszy = starszy;
            starszy.addProdukt(this);
        }
    }
    public void removeStarszy(Starszy starszy) {
        if(starszy!=null&&getStarszy()!=null){
            this.starszy=null;
            starszy.removeProdukt(this);
        }
    }
    public void setOcenaProduktu(OcenaProduktu ocenaProduktu) {
        if(ocenaProduktu != null && !ocenaProduktu.getCzyZostaloDodane()){
            ocenaProduktuList.add(ocenaProduktu);
            if(ocenaProduktu.getOcena()<3){
                ocenaProduktuListSub3.add(ocenaProduktu);
            }
        }
    }
    public void addKlient(Klient klient, String komentarz, int ocena) {
//        if(ocenaProduktuList.stream().noneMatch(o -> o.getKlient().equals(klient))){
            new OcenaProduktu(this, klient, komentarz, ocena );
//        }
    }
    public void addKlient(Klient klient, int ocena) {
//        if(ocenaProduktuList.stream().noneMatch(o -> o.getKlient().equals(klient))){
            new OcenaProduktu(this, klient, ocena );
//        }
    }
    public void removeOcenaProduktu(OcenaProduktu ocenaProduktu) {
//        if(ocenaProduktuList.contains(ocenaProduktu)){
//            ocenaProduktuList.remove(ocenaProduktu);
//            if(ocenaProduktu.getOcena()<3){
//                ocenaProduktuListSub3.remove(ocenaProduktu);
//            }
//        }
        List<OcenaProduktu> tmp = new ArrayList<>();
        List<OcenaProduktu> tmpSub3 = new ArrayList<>();
        for(OcenaProduktu o : ocenaProduktuList){
            if((o.getProdukt() == null || o.getKlient().equals(ocenaProduktu.getKlient())) && o.getData().equals(ocenaProduktu.getData())){
                tmp.add(o);
                if(ocenaProduktu.getOcena()<3){
                    tmpSub3.add(o);
                }
            }
        }
        ocenaProduktuList.removeAll(tmp);
        ocenaProduktuList.removeAll(tmpSub3);
    }
    public void removeKlient(Klient klient) {
        List<OcenaProduktu> temp1 = new ArrayList<>();
        List<OcenaProduktu> temp2 = new ArrayList<>();
//        OcenaProduktu temp = null;
//        OcenaProduktu temp2 = null;
        for(OcenaProduktu o : ocenaProduktuList){
            if(o.getKlient().equals(klient)){
                temp1.add(o);
            }
        }
//        ocenaProduktuList.removeAll(temp1);
        for(OcenaProduktu o : ocenaProduktuListSub3){
            if(o.getKlient().equals(klient)){
                temp2.add(o);
            }
        }
//        ocenaProduktuListSub3.removeAll(temp2);
        if(!temp1.isEmpty()){
            for(OcenaProduktu o : temp1){
                o.remove();
            }
        }
        if(!temp2.isEmpty()){
            for(OcenaProduktu o : temp2){
                o.remove();
            }
        }
    }
    public void remove(){
        if(!katalogList.isEmpty()){
            for(Katalog k : new ArrayList<>(katalogList)){
                k.removeProduktFinal(this);
            }
        }
        if(!zamowienieMap.isEmpty()){
            for(Map.Entry<Zamowienie,Integer> e : new ArrayList<>(zamowienieMap.entrySet())){
                e.getKey().removeProdukt(this);
            }
        }
        if(this.promocja!=null){
            this.promocja.removeProdukt(this);
        }
        if(this.starszy!=null){
            this.starszy.removeProdukt(this);
        }
        if(!ocenaProduktuList.isEmpty()){
            for(OcenaProduktu o : new ArrayList<>(ocenaProduktuList)){
                o.remove();
            }
        }
        if(!ocenaProduktuListSub3.isEmpty()){
            for(OcenaProduktu o : ocenaProduktuListSub3){
                o.remove();
            }
        }
        removeFromExtent();
    }


    @Override
    public String toString() {
        return "Produkt - " + nazwa+
                ", kosztuje - " + cena +
                ", na stanie - " + ilosc +
                '}';
    }
}
