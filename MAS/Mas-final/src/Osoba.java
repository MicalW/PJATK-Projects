import util.ObjectPlus;

public abstract class Osoba extends ObjectPlus {
    private static int id;
    private String imie;
    private String nazwisko;
    private String email;
    private String numerTelefon;
    private Adres adres;
    private int osobaId;
    public Osoba(String imie, String nazwisko, String email, String numerTelefon, Adres adres) throws IllegalArgumentException{
        try{
            id++;
            setImie(imie);
            setNazwisko(nazwisko);
            setEmail(email);
            setNumerTelefon(numerTelefon);
            setAdres(adres);
            this.osobaId = id++;
        }catch(IllegalArgumentException e){
            removeFromExtent();
            e.printStackTrace();
        }
    }

    public String getImie(){
        return imie;
    }

    public void setImie(String imie) throws IllegalArgumentException {
        if(imie == null || imie.isEmpty()) {
            throw new IllegalArgumentException("Imie nie może być puste");
        }
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) throws IllegalArgumentException {
        if(nazwisko == null || nazwisko.isEmpty()) {
            throw new IllegalArgumentException("Nazwisko nie może być puste");
        }
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IllegalArgumentException {
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email nie może być pusty");
        }
        if(!email.contains("@")) {
            throw new IllegalArgumentException("email musi posiadać @");
        }
        this.email = email;
    }

    public String getNumerTelefon() {
        return numerTelefon;
    }

    public void setNumerTelefon(String numerTelefon) throws IllegalArgumentException {
        if(numerTelefon == null || numerTelefon.isEmpty()) {
            throw new IllegalArgumentException("numer telefonu nie może być pusty");
        }
        if(numerTelefon.length()!=9 && numerTelefon.length()!=12) {
            throw new IllegalArgumentException("numer musi być wpisany tak 123456789 lub +48123456789");
        }
        this.numerTelefon = numerTelefon;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }
    public Adres getAdres() {
        return adres;
    }

    public int getOsobaId() {
        return osobaId;
    }
}
