import util.ObjectPlus;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
//        try {
//            ObjectPlus.loadExtent();
//        } catch (Exception e) {
//            System.out.println("Rozpoczynanie z pustą bazą danych");
//        }

        Katalog katalogKosmetyki = new Katalog("Kosmetyki", "Premium");
        Katalog katalogZdrowie = new Katalog("Zdrowie", "Standard");
        Katalog katalogDzieci = new Katalog("Mama i Dziecko", "Specjalistyczny");

        Starszy kierownik = new Starszy("Anna", "Kowalska", "anna@rossmann.pl", "123456789",
                new Adres("Handlowa", "00-001", "Warszawa", 1, 10),
                LocalDateTime.now().minusYears(2), "1234567890123456", 5000);

        Nowicjusz sprzedawca1 = new Nowicjusz("Piotr", "Nowak", "piotr@rossmann.pl", "987654321",
                new Adres("Sklepowa", "00-002", "Kraków", 2, 20),
                LocalDateTime.now().minusMonths(6), "9876543210987654", 3500);

        Nowicjusz sprzedawca2 = new Nowicjusz("Katarzyna", "Zielińska", "kasia@rossmann.pl", "555666777",
                new Adres("Handlowa", "00-003", "Gdańsk", 3, 30),
                LocalDateTime.now().minusMonths(3), "5556667770123456", 3200);

        Produkt kremDoTwarzy = kierownik.stworzProdukt(45, 30, "Krem do twarzy Nivea", "Pielęgnacja");
        Produkt szampon = kierownik.stworzProdukt(18, 50, "Szampon Pantene", "Włosy");
        Produkt witaminaC = kierownik.stworzProdukt(35, 25, "Witamina C 1000mg", "Suplementy");
        Produkt pastaDozEbow = kierownik.stworzProdukt(12, 60, "Pasta Colgate", "Higiena");
        Produkt szamponDzieciecy = kierownik.stworzProdukt(24, 20, "Szampon Johnson's Baby", "Dzieci");

        katalogKosmetyki.addProdukt(kremDoTwarzy);
        szampon.addKatalog(katalogKosmetyki);
        katalogZdrowie.addProdukt(witaminaC);
        pastaDozEbow.addKatalog(katalogZdrowie);
        katalogDzieci.addProdukt(szamponDzieciecy);

        Klient klient1 = new Klient("Maria", "Wójcik", "maria@gmail.com", "111222333",
                new Adres("Piękna", "00-003", "Warszawa", 3, 30), "haslo123", "maria_w", katalogKosmetyki);

        KartaStalegoKlienta karta = new KartaStalegoKlienta();
        karta.setPunkty(150);
        Klient klient2 = new Klient("Jan", "Kowalski", "jan@gmail.com", "444555666",
                new Adres("Długa", "00-004", "Poznań", 4, 40), "tajne456", "jan_k", katalogZdrowie, karta);

        klient1.addKatalog(katalogZdrowie);
        katalogDzieci.addKlient(klient2);

        System.out.println("Katalogi: " + ObjectPlus.getExtentFromClass(Katalog.class).size());
        System.out.println("Produkty: " + ObjectPlus.getExtentFromClass(Produkt.class).size());
        System.out.println("Klienci: " + ObjectPlus.getExtentFromClass(Klient.class).size());

        System.out.println("===========================================");

        Zamowienie zamowienie = new Zamowienie(klient1, sprzedawca1);
        System.out.println("Zamówienia: " + ObjectPlus.getExtentFromClass(Zamowienie.class).size());

        zamowienie.setProduktList(kremDoTwarzy, 3);
        zamowienie.setProduktList(szampon, 2);
        System.out.println("Produktów w zamówieniu: " + zamowienie.getProduktMap().size());

        klient1.removeZamowienie(zamowienie);
        klient2.zlozZamowienie(zamowienie);

        zamowienie.start();
        zamowienie.completed();

        System.out.println("===========================================");

        OcenaSklepu ocenaSklepu = new OcenaSklepu("Świetny sklep", 9, klient2);
        System.out.println("Oceny sklepu: " + ObjectPlus.getExtentFromClass(OcenaSklepu.class).size());

        klient2.removeOcena(ocenaSklepu);
        klient1.addOcena(ocenaSklepu);

        klient1.addProdukt(kremDoTwarzy, "Dobry krem", 8);
        klient2.addProdukt(kremDoTwarzy, "Słaby", 2);
        System.out.println("Oceny produktów: " + ObjectPlus.getExtentFromClass(OcenaProduktu.class).size());

        System.out.println("===========================================");

        Promocja promocjaWiosenna = new Promocja("WIOSNA2024",
                LocalDateTime.of(2024, 3, 1, 0, 0),
                LocalDateTime.of(2024, 4, 30, 23, 59), 25);

        kremDoTwarzy.addPromocja(promocjaWiosenna);
        promocjaWiosenna.addProdukt(szampon);
        System.out.println("Promocje: " + ObjectPlus.getExtentFromClass(Promocja.class).size());

        promocjaWiosenna.removeProdukt(kremDoTwarzy);
        szampon.removePromocja(promocjaWiosenna);
        System.out.println("Produkty w promocji: " + promocjaWiosenna.getProduktList().size());

        System.out.println("===========================================");

        ProcesPlatnosci platnosc = new ProcesPlatnosci(zamowienie);
        Online online = new Online("192.168.1.100");
        NaMiejscu wSklepie = new NaMiejscu(false);

        Karta karta_platnicza = new Karta(online, "1234567890123456", "01/01/2026", "123");
        Blik blik = new Blik(wSklepie, "123456");

        platnosc.addMetodaPlatnosci(karta_platnicza);
        System.out.println("Prowizja karta: " + karta_platnicza.obliczProwizje(100.0));
        System.out.println("Prowizja BLIK: " + blik.obliczProwizje(100.0));

        System.out.println("===========================================");

        System.out.println("Nowicjuszy: " + ObjectPlus.getExtentFromClass(Nowicjusz.class).size());
        System.out.println("Starszych: " + ObjectPlus.getExtentFromClass(Starszy.class).size());

        Starszy awansowany = sprzedawca1.awans();
        System.out.println("Po awansie - Nowicjuszy: " + ObjectPlus.getExtentFromClass(Nowicjusz.class).size());
        System.out.println("Po awansie - Starszych: " + ObjectPlus.getExtentFromClass(Starszy.class).size());

        Produkt balsam = awansowany.stworzProdukt(28, 15, "Balsam do ciała", "Pielęgnacja");
        katalogKosmetyki.addProdukt(balsam);

        System.out.println("===========================================");

        klient2.getKartaStalegoKlienta().setPunkty(300);
        System.out.println("Punkty VIP: " + klient2.getKartaStalegoKlienta().getPunkty());

        klient2.setKartaStalegoKlienta(null);
        klient1.setKartaStalegoKlienta(karta);
        System.out.println("Karta przeniesiona do klient1");

        System.out.println("===========================================");

        Zamowienie zamowienie2 = new Zamowienie(klient2, sprzedawca2);
        zamowienie2.setProduktList(witaminaC, 5);
        zamowienie2.setProduktList(pastaDozEbow, 3);
        zamowienie2.setProduktList(szamponDzieciecy, 2);

        zamowienie2.removeProdukt(pastaDozEbow);
        System.out.println("Wartość zamówienia2: " + zamowienie2.cenaZamówienia());

        System.out.println("===========================================");

        System.out.println("Przed usunięciem - Produkty: " + ObjectPlus.getExtentFromClass(Produkt.class).size());
        balsam.remove();
        System.out.println("Po usunięciu balsamu - Produkty: " + ObjectPlus.getExtentFromClass(Produkt.class).size());

        System.out.println("===========================================");

        System.out.println("Test: próba usunięcia ostatniego katalogu klienta");
        try {
            if (klient1.getKatalogList().size() == 1) {
                klient1.removeKatalog(klient1.getKatalogList().get(0));
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Zabroniono: " + e.getMessage());
        }

        System.out.println("Test: próba duplikatu kodu promocji");
        try {
            new Promocja("WIOSNA2024", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Zabroniono: " + e.getMessage());
        }

        System.out.println("===========================================");

        System.out.println("STAN KOŃCOWY:");
        System.out.println("Katalogi: " + ObjectPlus.getExtentFromClass(Katalog.class).size());
        System.out.println("Produkty: " + ObjectPlus.getExtentFromClass(Produkt.class).size());
        System.out.println("Klienci: " + ObjectPlus.getExtentFromClass(Klient.class).size());
        System.out.println("Zamówienia: " + ObjectPlus.getExtentFromClass(Zamowienie.class).size());
        System.out.println("Pracownicy: " + ObjectPlus.getExtentFromClass(Pracownik.class).size());
        System.out.println("Promocje: " + ObjectPlus.getExtentFromClass(Promocja.class).size());

        klient1.addKatalog(katalogDzieci);
        katalogKosmetyki.removeKlient(klient1);


        Zamowienie zamowienie1 = new Zamowienie(klient2, sprzedawca1);
        zamowienie.removeKlient(klient1);
        klient2.removeZamowienie(zamowienie1);

//        try {
//            ObjectPlus.saveExtent();
//            System.out.println("Dane zapisane");
//        } catch (Exception e) {
//            System.out.println("Błąd zapisu: " + e.getMessage());
//        }
    }
}