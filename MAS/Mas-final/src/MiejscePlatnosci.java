import java.io.Serializable;

public abstract class MiejscePlatnosci implements Serializable{
    private MetodaPlatnosci metodaPlatnosci;
    public void setMetodaPlatnosci(MetodaPlatnosci metodaPlatnosci) {
        if(metodaPlatnosci!=null&&this.metodaPlatnosci==null){
            this.metodaPlatnosci = metodaPlatnosci;
            metodaPlatnosci.setMiejscePlatnosci(this);
        }
    }

    public MetodaPlatnosci getMetodaPlatnosci() {
        return metodaPlatnosci;
    }
}

class Online extends MiejscePlatnosci{
    private String adresIP;
    public Online(String adresIP) {
        setAdresIP(adresIP);
    }

    public void setAdresIP(String adresIP) {//ograniczenia
        if(adresIP==null||adresIP.isEmpty()){
            throw new IllegalArgumentException("Adres Ip nie może być pusty");
        }
        String regex = "^([0-9]+\\.){3}[0-9]+$";
        if(!adresIP.matches(regex)){
            throw new IllegalArgumentException("Nie poprawny adres ip");
        }
        this.adresIP = adresIP;
    }
    public String getAdresIP() {
        return adresIP;
    }
    public void wyslijPotwierdzenieMail(){
        if(getMetodaPlatnosci().getProcesPlatnosci().getZamowienie().getKlient()==null){
            throw new IllegalArgumentException("Nie można znaleźć klienta");
        }
        String email = getMetodaPlatnosci().getProcesPlatnosci().getZamowienie().getKlient().getEmail();
        System.out.println("Email wysłany na mail "+email);
    }
}
class NaMiejscu extends MiejscePlatnosci{
    private boolean czyZrealizowane;
    public NaMiejscu(boolean czyZrealizowane) {
        setCzyZrealizowane(czyZrealizowane);
    }

    public void setCzyZrealizowane(boolean czyZrealizowane) {
        this.czyZrealizowane = czyZrealizowane;
    }
    public boolean getCzyZrealizowane() {
        return czyZrealizowane;
    }
    public boolean potwierdzOdbiór(){
        if(czyZrealizowane!=false){
            return true;
        }
        return false;
    }
}
