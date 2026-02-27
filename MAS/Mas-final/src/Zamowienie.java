import util.ObjectPlus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Zamowienie extends ObjectPlus{
    private Klient klient;
    private Nowicjusz nowicjusz;
    private Map<Produkt,Integer> produktMap = new HashMap<>();
    private LocalDateTime localDateTime;
    private StatusZamowienie status;
    private ProcesPlatnosci procesPlatnosci;
    private static int id = 0;
    private int idZamowienie;

    public Zamowienie(Klient klient, Nowicjusz nowicjusz) {
        setKlient(klient);
        setNowicjusz(nowicjusz);
        this.idZamowienie = id++;
        localDateTime = LocalDateTime.now();
        this.status = StatusZamowienie.PENDING;
    }

    public int getIdZamowienie() {
        return idZamowienie;
    }
    public void setKlient(Klient klient) {
        if(klient!=null){
            this.klient = klient;
            klient.zlozZamowienie(this);
        }
    }
    public Klient getKlient() {
        return klient;
    }
    public void removeKlient(Klient klient) {
        if(klient!=null&&klient.equals(getKlient())){
            this.klient = null;
            klient.removeZamowienie(this);
        }
    }
    public void setNowicjusz(Nowicjusz nowicjusz) {
        if(nowicjusz!=null){
            this.nowicjusz = nowicjusz;
            nowicjusz.addZamowienie(this);
        }
    }
    public Nowicjusz getNowicjusz() {
        return nowicjusz;
    }
    public void removeNowicjusz(Nowicjusz nowicjusz) {
        if(this.nowicjusz != null && nowicjusz==getNowicjusz()){
            this.nowicjusz = null;
            nowicjusz.removeZamowienie(this);
        }
    }

    public void setProduktList(Produkt produkt,int amount) {
        if(produkt!=null){
            if(!produktMap.containsKey(produkt)){
                if(amount<=produkt.getIlosc() && amount>0){
                    this.produktMap.put(produkt, amount);
                    produkt.addZamowienie(this,amount);
                    produkt.setIlosc(produkt.getIlosc()-amount);
                }else{
                    throw new ArithmeticException("Ilość produktu jest nie poprawna");
                }
            }
        }
    }

    public Map<Produkt, Integer> getProduktMap() {
        return Collections.unmodifiableMap(produktMap);
    }

    public void removeProdukt(Produkt produkt) {
        if(produkt!=null&&produktMap.containsKey(produkt)){
            int x = produktMap.get(produkt);
            this.produktMap.remove(produkt);
            produkt.setIlosc(produkt.getIlosc()+x);
            produkt.removeZamowienie(this);
        }
    }

    public void setProcesPlatnosci(ProcesPlatnosci procesPlatnosci) {
        if(procesPlatnosci!=null&&this.procesPlatnosci==null){
            this.procesPlatnosci = procesPlatnosci;
            procesPlatnosci.setZamowienie(this);
        }
    }

    public void removeProcesPlatnosci(ProcesPlatnosci procesPlatnosci) {
        if(procesPlatnosci!=null&&this.procesPlatnosci!=null){
            this.procesPlatnosci = null;
            procesPlatnosci.removeZamowienie(this);
        }
    }

    public int cenaZamówienia(){
        int suma = this.produktMap.entrySet().stream().mapToInt(x -> x.getValue()*x.getKey().getCena()).sum();
        return suma;
    }

    public void start(){
        this.status =  StatusZamowienie.IN_PROGRESS;
    }
    public void completed(){
        this.status =  StatusZamowienie.COMPLETED;
    }
    public void failed(){
        this.status =  StatusZamowienie.FAILED;
    }
    public int getKoszt(){//atrybut wyliczalny
        int suma = 0;
        if(this.produktMap!=null){
            throw new UnsupportedOperationException("Nie ma produktów w koszyku");
        }
        for(Map.Entry<Produkt, Integer> entry : produktMap.entrySet()){
            suma += entry.getKey().getCena()*entry.getValue();
        }
        return suma;
    }

    public void remove(){
        if(klient!=null){
            klient.removeZamowienie(this);
        }
        if(nowicjusz!=null){
            nowicjusz.removeZamowienie(this);
        }
        if(!produktMap.isEmpty()){
            for(Produkt produkt : new ArrayList<>(produktMap.keySet())){
                produkt.removeZamowienie(this);
            }
        }
        if(procesPlatnosci!=null){
            procesPlatnosci.removeZamowienie(this);
        }
        removeFromExtent();
    }
    @Override
    public String toString() {
        return "Zamowienie{" +
                "klient=" + klient +
                ", nowicjusz=" + nowicjusz +
                ", produktMap=" + produktMap +
                '}';
    }
}
