import util.ObjectPlus;

import java.io.Serializable;
import java.util.*;

public class Katalog extends ObjectPlus {
    private String nazwa;
    private String typ;
    private Map<Integer, Produkt> katalogMap = new TreeMap<>();
    private List<Klient> klientList = new ArrayList<>();
    private int katalogId;
    private static int id;
    public Katalog(String nazwa, String typ) {
        try{
        setNazwa(nazwa);
        setTyp(typ);
        katalogId = id++;
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    public String getNazwa() {
        return nazwa;
    }
    public void setNazwa(String nazwa) {
        if(nazwa == null || nazwa.isEmpty()){
            throw new NullPointerException("Nazwa nie moze być pusta!");
        }
        this.nazwa = nazwa;
    }
    public String getTyp() {
        return typ;
    }
    public void setTyp(String typ) {
        if(typ == null||typ.isBlank()){
            throw new IllegalArgumentException("Typ nie może być pusty");
        }
        this.typ = typ;
    }
    public static int getId() {
        return id;
    }
    public Map<Integer, Produkt> getKatalogMap() {
        return Collections.unmodifiableMap(katalogMap);
    }

    //asocjacja produkt-katalog
    public void addProdukt(Produkt produkt) {
        int produktID = produkt.getId();
        if (!katalogMap.containsKey(produktID)) {
            katalogMap.put(produktID, produkt);
            produkt.addKatalog(this);
        }
    }
    public void removeProduktFinal(Produkt produkt) {
        if(produkt!=null && katalogMap.containsKey(produkt.getId())) {
            katalogMap.remove(produkt.getId());
//            if(produkt.getKatalogList().contains(this)) {
            produkt.removeKatalogFinal(this);
//            }
        }
    }
    public void removeProdukt(Produkt produkt) {
        if(katalogMap.keySet().size()!=1) {
            if (produkt != null && katalogMap.containsKey(produkt.getId())) {
                katalogMap.remove(produkt.getId());
//            if(produkt.getKatalogList().contains(this)) {
                produkt.removeKatalog(this);
//            }
            }
        }else{
            throw new UnsupportedOperationException("Katalog musi mieć przynajmniej 1 produkt");
        }
    }
    public void addKlient(Klient klient) {
        if(klient !=null && !klientList.contains(klient)) {
            klientList.add(klient);
            klient.addKatalog(this);
        }
    }
    public void removeKlientFinal(Klient klient) {
        if(klientList.contains(klient)){
            klientList.remove(klient);
            klient.removeKatalogFinal(this);
        }
    }
    public void removeKlient(Klient klient) {
        if(klient.getKatalogList().size()!=1) {
            if(klientList.contains(klient)){
                klientList.remove(klient);
                klient.removeKatalog(this);
            }
        }else{
            throw new UnsupportedOperationException("Klient musi posiadać dostęp do chociaż jednego katalogu");
        }
    }

    public void remove(){
        if(!klientList.isEmpty()){
            for(Klient klient: new ArrayList<>(klientList)){
                klient.removeKatalogFinal(this);
            }
        }
        if(!katalogMap.isEmpty()){
            for(Map.Entry<Integer, Produkt> entry: new ArrayList<>(katalogMap.entrySet())){
                entry.getValue().removeKatalogFinal(this);
            }
        }
        removeFromExtent();

    }

    @Override
    public String toString() {
//        return getNazwa()+" "+getTyp()+" "+klientList.stream().map(k->k.getImie()).toList();
        return  getNazwa() + ", " + getTyp();
    }
}
