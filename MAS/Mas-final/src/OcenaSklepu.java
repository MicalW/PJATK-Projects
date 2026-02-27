import util.ObjectPlus;

import java.time.LocalDateTime;

public class OcenaSklepu extends ObjectPlus{
    private LocalDateTime dateTime;
    private String note;
    private int grade;
    private Klient klient;
    public OcenaSklepu(String note, int grade, Klient klient) {
        try{
        this.dateTime = LocalDateTime.now();
        setGrade(grade);
        setNote(note);
        setKlient(klient);
    }catch(IllegalArgumentException e){
        removeFromExtent();
        e.printStackTrace();
    }
    }
    public OcenaSklepu(int grade, Klient klient) {
        try{
        this.dateTime = LocalDateTime.now();
        setGrade(grade);
        setKlient(klient);
    }catch(IllegalArgumentException e){
        removeFromExtent();
        e.printStackTrace();
    }
    }

    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        if(grade < 1 || grade > 10){
            throw new IllegalArgumentException("Ocena musi być pomiędzy 1 a 10");
        }
        this.grade = grade;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        if(note == null || note.isEmpty()){
            throw new IllegalArgumentException("Komentarz nie może być pusty");
        }
        this.note = note;
    }
    public void setKlient(Klient klient) {
        if(klient!=null&&this.klient==null){
            this.klient = klient;
            klient.addOcena(this);
        }
    }
    public void removeKlient(Klient klient) {
        if(klient!=null&&this.klient==klient){
            this.klient = null;
            klient.removeOcena(this);
        }
    }
    public void remove() {
        if(this.klient!=null){
            klient.removeOcena(this);
        }
        removeFromExtent();
    }
    @Override
    public String toString() {
        return "OcenaSklepu{" +
                "dateTime=" + dateTime +
                ", note='" + note + '\'' +
                ", grade=" + grade +
                ", klient=" + (klient != null ? klient.getKlientId() : null) +
                '}';
    }

}
