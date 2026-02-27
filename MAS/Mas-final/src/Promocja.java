import util.ObjectPlus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Promocja extends ObjectPlus {
    private String kod;
    private static List<String> kodList = new ArrayList<>();//atrybut klasowy
    private LocalDateTime start;
    private LocalDateTime end;
    private int procentZnizki;
    List<Produkt> produktList = new ArrayList<>();
    public Promocja(String kod, LocalDateTime start, LocalDateTime end, int procentZnizki) {
        setKod(kod);
        setStart(start);
        setEnd(end);
        setProcentZnizki(procentZnizki);
    }

    public void setKod(String kod) {
        if(kodList == null) {
            kodList = new ArrayList<>();
        }
        if(kodList.contains(kod)) {
            throw new IllegalArgumentException("kod musi być unique");
        }
        if(kod.isBlank()) {
            throw new IllegalArgumentException("kod nie może być pusty");
        }
        kodList.add(kod);
    }
    public String getKod() {
        return kod;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    public LocalDateTime getStart() {
        return start;
    }
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public void setProcentZnizki(int procentZnizki) {
        if(procentZnizki < 0 || procentZnizki > 100) {
            throw new IllegalArgumentException("procent zniźki musi być pomiędzy 0 a 100");
        }
        this.procentZnizki = procentZnizki;
    }
    public int getProcentZnizki() {
        return procentZnizki;
    }
    public List<Produkt> getProduktList() {
        return Collections.unmodifiableList(produktList);
    }
    public List<String> getKodList() {
        return Collections.unmodifiableList(kodList);
    }

    public void addProdukt(Produkt produkt) {
        if(produkt != null&& !getProduktList().contains(produkt)) {
            produktList.add(produkt);
            produkt.addPromocja(this);
        }
    }

    public void removeProdukt(Produkt produkt) {
        if(produkt != null&&produktList.contains(produkt)) {
            produktList.remove(produkt);
            produkt.removePromocja(this);
        }
    }
    public void remove(){
        if(!produktList.isEmpty()){
            for(Produkt produkt : new ArrayList<>(produktList)){
                produkt.removePromocja(this);
            }
        }
        if(kodList.contains(getKod())){
            kodList.remove(getKod());
        }
        removeFromExtent();
    }

    @Override
    public String toString() {
        return "Promocja{" +
                "kod='" + kod + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", procentZnizki=" + procentZnizki +
                ", produktList=" + getProduktList().stream().map(p->p.getNazwa()).toList() +
                '}';
    }
}
