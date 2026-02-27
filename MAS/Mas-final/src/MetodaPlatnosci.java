import util.ObjectPlus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class MetodaPlatnosci extends ObjectPlus {
    private MiejscePlatnosci miejscePlatnosci;
    private ProcesPlatnosci procesPlatnosci;
    public MetodaPlatnosci(MiejscePlatnosci miejscePlatnosci) {
        try{
        setMiejscePlatnosci(miejscePlatnosci);
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    public void setMiejscePlatnosci(MiejscePlatnosci miejscePlatnosci) {
        if(miejscePlatnosci != null&&this.miejscePlatnosci!=null){
            this.miejscePlatnosci = miejscePlatnosci;
            miejscePlatnosci.setMetodaPlatnosci(this);
        }
    }
    public ProcesPlatnosci getProcesPlatnosci() {
        return procesPlatnosci;
    }
    public MiejscePlatnosci getMiejscePlatnosci() {
        return miejscePlatnosci;
    }
    public void addProcesPlatnosci(ProcesPlatnosci procesPlatnosci) {
        if(procesPlatnosci != null&&this.procesPlatnosci==null){
            this.procesPlatnosci = procesPlatnosci;
            procesPlatnosci.addMetodaPlatnosci(this);
        }
    }
    public void removeProcesPlatnosci(ProcesPlatnosci procesPlatnosci) {
        if(procesPlatnosci != null&&this.procesPlatnosci!=null){
            this.procesPlatnosci = null;
            procesPlatnosci.removeMetodaPlatnosci(this);
            removeFromExtent();
        }
    }
    public abstract double obliczProwizje(double kwota);
    public abstract boolean wykonajPlatnosc();
}
class Blik extends MetodaPlatnosci{
    private String kodBlik;
    public Blik(MiejscePlatnosci miejscePlatnosci,String kodBlik) {
        super(miejscePlatnosci);
    }

    public String getKodBlik() {
        return kodBlik;
    }

    public void setKodBlik(String kodBlik) {
        if((kodBlik.matches(".*\\p{L}.*"))){
            throw new IllegalArgumentException("Kod Blik musi mieć tylko cyfry");
        }
        if(kodBlik.length()!=6){
            throw new IllegalArgumentException("kod blik musi mieć 6 cyfr");
        }
        this.kodBlik = kodBlik;
    }

    @Override
    public double obliczProwizje(double kwota) {
        return kwota * 0.015;
    }

    @Override
    public boolean wykonajPlatnosc() {
        return true;
    }

}
class Karta extends MetodaPlatnosci{
    private String numerKarty;
    private String dataWaznosci;
    private String cvv;
    public Karta(MiejscePlatnosci miejscePlatnosci,String numerKarty,String dataWaznosci,String cvv) {
        super(miejscePlatnosci);
        setNumerKarty(numerKarty);
        setDataWaznosci(dataWaznosci);
        setCvv(cvv);
    }

    public void setNumerKarty(String numerKarty) {
        if(numerKarty==null || numerKarty.isBlank()){
            throw new IllegalArgumentException("Numer konta nie może być pusty");
        }
        if(numerKarty.length()!=16){
            throw new IllegalArgumentException("Numer konta musi mieć 16 cyfr");
        }
        if (numerKarty.matches(".*\\p{L}.*")) {
            throw new IllegalArgumentException("Numer konta nie może mieć liter");
        }
        this.numerKarty = numerKarty;
    }

    public String getNumerKarty() {
        return numerKarty;
    }

    public void setDataWaznosci(String dataWaznosci) {
        if (dataWaznosci == null || dataWaznosci.isBlank()) {
            throw new IllegalArgumentException("Data ważności nie może być pusta");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(dataWaznosci, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Niepoprawny format daty, użyj dd/MM/yyyy");
        }
        this.dataWaznosci = dataWaznosci;
    }

    public String getDataWaznosci() {
        return dataWaznosci;
    }

    public void setCvv(String cvv) {
        if(cvv==null || cvv.isBlank()){
            throw new IllegalArgumentException("cvv nie może byc puste");
        }
        if(cvv.matches(".*\\p{L}.*")){
            throw new IllegalArgumentException("cvv może zawierać tylko cyfry");
        }
        if(cvv.length()!=3){
            throw new IllegalArgumentException("cvv musi mieć 3 cyfr");
        }
        this.cvv = cvv;
    }

    public String getCvv() {
        return cvv;
    }

    @Override
    public double obliczProwizje(double kwota) {
        return kwota * 0.02;
    }

    @Override
    public boolean wykonajPlatnosc() {
        return true;
    }
}
