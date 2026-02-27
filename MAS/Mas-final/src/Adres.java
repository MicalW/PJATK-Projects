import java.io.Serializable;

public class Adres implements Serializable {
    private String ulica;
    private String kodPocztowy;
    private String miasto;
    private int numerMieszkania;
    private int numerDomu;

    /**
     * Konstruktor tworzący nowy adres z walidacją wszystkich pól
     */
    public Adres(String ulica, String kodPocztowy, String miasto, int numerMieszkania, int numerDomu) {
        setUlica(ulica);
        setKodPocztowy(kodPocztowy);
        setMiasto(miasto);
        setNumerMieszkania(numerMieszkania);
        setNumerDomu(numerDomu);
    }

    // Gettery i settery z walidacją
    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) throws IllegalArgumentException{
        if(ulica == null || ulica.isBlank()){
            throw new IllegalArgumentException("Ulica nie może być pusta");
        }
        this.ulica = ulica;
    }

    public String getKodPocztowy() {
        return kodPocztowy;
    }

    /**
     * Setter dla kodu pocztowego z walidacją formatu XX-XXX
     */
    public void setKodPocztowy(String kodPocztowy) throws IllegalArgumentException{
        if(kodPocztowy == null || kodPocztowy.isBlank()){
            throw new IllegalArgumentException("Kod pocztowy nie może być pusty");
        }
        if(kodPocztowy.length() != 6){
            throw new IllegalArgumentException("Kod pocztowy jest niepełny");
        }
        if(kodPocztowy.charAt(2) != '-'){
            throw new IllegalArgumentException("Kod pocztowy ma zły format");
        }
        this.kodPocztowy = kodPocztowy;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) throws IllegalArgumentException{
        if(miasto == null || miasto.isBlank()){
            throw new IllegalArgumentException("Miasto nie może być puste");
        }
        this.miasto = miasto;
    }

    public int getNumerMieszkania(){
        return numerMieszkania;
    }

    public void setNumerMieszkania(int numerMieszkania) throws IllegalArgumentException{
        if(numerMieszkania < 0){
            throw new IllegalArgumentException("Numer mieszkania nie może być mniejszy od zera");
        }
        this.numerMieszkania = numerMieszkania;
    }

    public int getNumerDomu() {
        return numerDomu;
    }

    public void setNumerDomu(int numerDomu) throws IllegalArgumentException{
        if(numerDomu < 0){
            throw new IllegalArgumentException("Numer domu nie może być mniejszy od zera");
        }
        this.numerDomu = numerDomu;
    }
}