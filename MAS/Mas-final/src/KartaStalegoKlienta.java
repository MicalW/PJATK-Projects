import util.ObjectPlus;

import java.io.Serializable;

public class KartaStalegoKlienta implements Serializable{
    private int punkty;
    public KartaStalegoKlienta() {
        setPunkty(0);
    }
    public void setPunkty(int punkty) {
        if(punkty<0){
            throw new IllegalArgumentException("Punkty nie mogą być na minusie");
        }
        this.punkty = punkty;
    }
    public int getPunkty() {
        return punkty;
    }

    @Override
    public String toString() {
        return "KartaStalegoKlienta{" +
                "punkty=" + punkty +
                '}';
    }
}
