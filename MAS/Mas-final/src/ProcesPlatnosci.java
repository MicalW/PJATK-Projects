import util.ObjectPlus;

import java.time.LocalDateTime;

public class ProcesPlatnosci extends ObjectPlus{
    private LocalDateTime dataPlatnosci;
    private Zamowienie zamowienie;
    private MetodaPlatnosci metodaPlatnosci;
    public ProcesPlatnosci(Zamowienie zamowienie){
        try {
        this.dataPlatnosci =  LocalDateTime.now();
        setZamowienie(zamowienie);
    }catch(IllegalArgumentException e){
        removeFromExtent();
        e.printStackTrace();
    }
    }

    public LocalDateTime getDataPlatnosci() {
        return dataPlatnosci;
    }

    public Zamowienie getZamowienie() {
        return zamowienie;
    }

    public void setZamowienie(Zamowienie zamowienie) {
        if(zamowienie != null&&!zamowienie.getProduktMap().isEmpty()&&this.zamowienie == null){
            this.zamowienie = zamowienie;
            zamowienie.setProcesPlatnosci(this);
        }
    }

    public void removeZamowienie(Zamowienie zamowienie) {
        if(zamowienie != null&&this.zamowienie != null){
            this.zamowienie = null;
            zamowienie.removeProcesPlatnosci(this);
        }
    }

    public void addMetodaPlatnosci(MetodaPlatnosci metodaPlatnosci) {
        if(metodaPlatnosci != null&&this.metodaPlatnosci == null){
            this.metodaPlatnosci = metodaPlatnosci;
            metodaPlatnosci.addProcesPlatnosci(this);
        }
    }
    public void removeMetodaPlatnosci(MetodaPlatnosci metodaPlatnosci) {
        if(metodaPlatnosci != null &&this.metodaPlatnosci != null){
            this.metodaPlatnosci = null;
            metodaPlatnosci.removeProcesPlatnosci(this);
        }
    }
    public void remove(){
        if(zamowienie != null){
            zamowienie.removeProcesPlatnosci(this);
        }
        if(metodaPlatnosci != null){
            metodaPlatnosci.removeProcesPlatnosci(this);
        }
        removeFromExtent();
    }

}
