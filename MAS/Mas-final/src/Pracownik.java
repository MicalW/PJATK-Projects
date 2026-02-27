import java.time.LocalDateTime;

public abstract class Pracownik extends Osoba{
    private LocalDateTime dataZatrudnienia;
    private LocalDateTime dataZwolnienia;//(opcjonalne)
    private String numerKonta;
    private int placa;
    private static int premia = 0;// atrybut klasowy

    /**
     * Konstruktor dla pracownika z datą zwolnienia
     */
    public Pracownik(String imie, String nazwisko, String email, String numerTelefonu, Adres adres,
                     LocalDateTime dataZatrudnienia, LocalDateTime dataZwolnienia, String numerKonta, int placa) {
        super(imie, nazwisko, email, numerTelefonu, adres);
        try{
            setDataZatrudnienia(dataZatrudnienia);
            setDataZwolnienia(dataZwolnienia);
            setNumerKonta(numerKonta);
            setPlaca(placa);
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    /**
     * Konstruktor dla aktywnego pracownika (bez daty zwolnienia)
     */
    public Pracownik(String imie, String nazwisko, String email, String numerTelefonu, Adres adres,
                     LocalDateTime dataZatrudnienia, String numerKonta, int placa) {
        super(imie, nazwisko, email, numerTelefonu, adres);
        try{
            setDataZatrudnienia(dataZatrudnienia);
            this.dataZwolnienia = null;
            setNumerKonta(numerKonta);
            setPlaca(placa);
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    /**
     * Konstruktor kopiujący - używany przy awansach
     */
    public Pracownik(Pracownik pracownik) {
        super(pracownik.getImie(), pracownik.getNazwisko(), pracownik.getEmail(),
                pracownik.getNumerTelefon(), pracownik.getAdres());
        try{
            setDataZatrudnienia(pracownik.getDataZatrudnienia());
            setNumerKonta(pracownik.getNumerKonta());
            setPlaca(pracownik.getPlaca());
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    //get i set

    public LocalDateTime getDataZatrudnienia() {
        return dataZatrudnienia;
    }

    public void setDataZatrudnienia(LocalDateTime dataZatrudnienia) {
        if(dataZatrudnienia == null){
            throw new IllegalArgumentException("Data zatrudnienia nie może być null");
        }
        this.dataZatrudnienia = dataZatrudnienia;
    }

    public LocalDateTime getDataZwolnienia() {
        return dataZwolnienia;
    }

    public void setDataZwolnienia(LocalDateTime dataZwolnienia) {
        if(dataZwolnienia == null){
            throw new IllegalArgumentException("Data zwolnienia nie może być null");
        }
        this.dataZwolnienia = dataZwolnienia;
    }

    public String getNumerKonta() {
        return numerKonta;
    }

    /**
     * Walidacja numeru konta - musi mieć 16 cyfr
     */
    public void setNumerKonta(String numerKonta) {
        if(numerKonta == null || numerKonta.isBlank()){
            throw new IllegalArgumentException("Numer konta nie może być pusty");
        }
        if(numerKonta.length() != 16){
            throw new IllegalArgumentException("Numer konta musi mieć 16 cyfr");
        }
        if (numerKonta.matches(".*\\p{L}.*")) {
            throw new IllegalArgumentException("Numer konta nie może mieć liter");
        }
        this.numerKonta = numerKonta;
    }

    public int getPlaca() {
        return placa;
    }

    public void setPlaca(int placa) {
        if(placa <= 0){
            throw new IllegalArgumentException("Płaca nie może być mniejsza lub równa 0");
        }
        this.placa = placa;
    }

    public static int getPremia() {
        return premia;
    }

    public static void setPremia(int premiaNew) {
        if(premiaNew <= 0){
            throw new IllegalArgumentException("Premia nie może być mniejsza lub równa 0");
        }
        premia = premiaNew;
    }

    /**
     * Metoda obliczająca indywidualną płacę (płaca + premia)
     */
    public int obliczIndywidualnaPlace(){
        return placa + premia;
    }
}