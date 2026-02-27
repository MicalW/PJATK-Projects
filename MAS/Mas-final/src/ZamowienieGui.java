import util.ObjectPlus;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ZamowienieGui extends JFrame{
    private JList<Katalog> katalogList;
    private JList<Produkt> produktList;
    private JTextField quantityField;
    private JButton selectButton;
    private JButton cartButton;
    private Klient klient;
    private Nowicjusz nowicjusz;
    private Zamowienie zamowienie;

    public ZamowienieGui (Klient klient, Nowicjusz nowicjusz) {
        this.klient = klient;
        this.nowicjusz = nowicjusz;
        this.klient.setZamowienieGui(this);
        incjalizacja();
    }

    private void incjalizacja() {
        setTitle("Sklep Rosswoman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new BorderLayout());
        add(stworzPanelKatalog(), BorderLayout.WEST);
        add(stworzPanelProdukt(), BorderLayout.CENTER);
        add(stworzPanelPrzyciskow(), BorderLayout.SOUTH);
        add(stworzPanelKoszyka(), BorderLayout.EAST);
    }

    private JPanel stworzPanelKatalog() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Katalogi"));

        katalogList = new JList<>(new DefaultListModel<>());
        stworzKatalogi();

        katalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        katalogList.addListSelectionListener(e -> katalogWybrany());

        panel.add(new JScrollPane(katalogList), BorderLayout.CENTER);
        return panel;
    }

    private void stworzKatalogi() {
        DefaultListModel<Katalog> model = (DefaultListModel<Katalog>) katalogList.getModel();
        if (klient != null && klient.getKatalogList() != null) {
            for (Katalog katalog : klient.getKatalogList()) {
                if (katalog != null) {
                    model.addElement(katalog);
                }
            }
        }
    }

    private JPanel stworzPanelProdukt() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Produkty"));

        produktList = new JList<>(new DefaultListModel<>());
        produktList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(produktList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel stworzPanelPrzyciskow() {
        JPanel panel = new JPanel(new FlowLayout());

        quantityField = new JTextField(10);
        selectButton = new JButton("Wybierz");
        selectButton.addActionListener(e -> produktWybrany());

        panel.add(selectButton);
        panel.add(new JLabel("Ilość:"));
        panel.add(quantityField);

        return panel;
    }

    private JPanel stworzPanelKoszyka() {
        JPanel panel = new JPanel();

        cartButton = new JButton("Koszyk");
        cartButton.addActionListener(e -> onKoszyk());

        panel.add(cartButton);
        return panel;
    }

    private void katalogWybrany() {
        Katalog selectedKatalog = katalogList.getSelectedValue();
        if (selectedKatalog == null) return;

        DefaultListModel<Produkt> model = new DefaultListModel<>();
        if (selectedKatalog.getKatalogMap() != null) {
            for (Produkt produkt : selectedKatalog.getKatalogMap().values()) {
                if (produkt != null) {
                    model.addElement(produkt);
                }
            }
        }
        produktList.setModel(model);
    }

    private void produktWybrany() {
        try {
            Produkt selectedProdukt = produktList.getSelectedValue();
            if (selectedProdukt == null) {
                showError("Wybierz produkt z listy.");
                return;
            }

            String text = quantityField.getText().trim();
            if (!text.matches("\\d+")) {
                showError("Ilość może zawierać tylko cyfry.");
                quantityField.setText("");
                return;
            }
            int quantity = Integer.parseInt(text);

            if (zamowienie == null) {
                zamowienie = new Zamowienie(klient, nowicjusz);
            }

            if (!zamowienie.getProduktMap().containsKey(selectedProdukt)) {
                zamowienie.setProduktList(selectedProdukt, quantity);
                showInfo("Dodano " + quantity + " sztuk produktu: " + selectedProdukt.getNazwa());
                quantityField.setText("");
            } else {
                showError("Produkt " + selectedProdukt.getNazwa() + " został już dodany");
            }

        } catch (ArithmeticException ex) {
            showError(ex.getMessage());
        }
    }
    private void onKoszyk() {
        if (zamowienie == null || zamowienie.getProduktMap().isEmpty()) {
            showInfo("Koszyk jest pusty.");
            return;
        }
        otworzKoszyk();
    }

    private void otworzKoszyk() {
        JFrame cartFrame = new JFrame("Zawartość koszyka");
        cartFrame.setSize(400, 300);
        cartFrame.setLocationRelativeTo(this);
        cartFrame.setLayout(new BorderLayout());

        DefaultListModel<Produkt> listModel = new DefaultListModel<>();
        zamowienie.getProduktMap().keySet().forEach(listModel::addElement);

        JList<Produkt> cartList = new JList<>(listModel);
        cartList.setCellRenderer(new CartItemRenderer());
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cartFrame.add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Usuń produkt");
        JButton payButton = new JButton("Płatność");

        removeButton.addActionListener(e -> removeProduct(cartFrame, cartList, listModel));
        payButton.addActionListener(e -> przetworzPlatnosc(cartFrame));

        buttonPanel.add(removeButton);
        buttonPanel.add(payButton);
        cartFrame.add(buttonPanel, BorderLayout.SOUTH);

        cartFrame.setVisible(true);
    }

    private void removeProduct(JFrame frame, JList<Produkt> list, DefaultListModel<Produkt> model) {
        Produkt selected = list.getSelectedValue();
        if (selected == null) {
            showError("Wybierz produkt do usunięcia.", frame);
            return;
        }

        zamowienie.removeProdukt(selected);
        model.removeElement(selected);
        showInfo("Produkt został usunięty z koszyka.", frame);
    }

    private void przetworzPlatnosc(JFrame frame) {
        if (zamowienie == null || zamowienie.getProduktMap().isEmpty()) {
            showInfo("Koszyk jest pusty.", frame);
            return;
        }

        int totalAmount = zamowienie.cenaZamówienia();

        int confirm = JOptionPane.showConfirmDialog(frame,
                String.format("Całkowita cena: %.2f PLN\nCzy chcesz kontynuować płatność?", (double) totalAmount),
                "Płatność", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        MiejscePlatnosci place = wybierzMiejscePlatnosci(frame);
        if (place == null) return;

        MetodaPlatnosci method = wyborMetodyPlatnosci(frame, place);
        if (method == null) return;



        if (method.wykonajPlatnosc()) {
            zamowienie.completed();
            try {
                    ObjectPlus.saveExtent();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            finalizacjaPlatnosci(frame, method, place, totalAmount);
            frame.dispose();
        } else {
            zamowienie.failed();
            showError("Płatność nie powiodła się. Spróbuj ponownie.", frame);
        }
    }

    private MiejscePlatnosci wybierzMiejscePlatnosci(JFrame frame) {
        String[] options = {"Na miejscu", "Online"};
        String selected = (String) JOptionPane.showInputDialog(frame,
                "Wybierz miejsce płatności:", "Miejsce płatności",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if ("Na miejscu".equals(selected)) {
            return new NaMiejscu(false);
        } else if ("Online".equals(selected)) {
            return new Online("127.0.0.1");
        }
        return null;
    }

    private MetodaPlatnosci wyborMetodyPlatnosci(JFrame frame, MiejscePlatnosci place) {
        String[] options = {"Karta", "Blik"};
        String selected = (String) JOptionPane.showInputDialog(frame,
                "Wybierz metodę płatności:", "Metoda płatności",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if ("Blik".equals(selected)) {
            return platnoscBlikiem(frame, place);
        } else if ("Karta".equals(selected)) {
            return platnoscKarta(frame, place);
        }
        return null;
    }

    private MetodaPlatnosci platnoscBlikiem(JFrame frame, MiejscePlatnosci place) {
        String code = JOptionPane.showInputDialog(frame, "Wprowadź kod Blik (6 cyfr):");
        if (code == null || code.trim().isEmpty()) {
            showError("Kod Blik jest wymagany.", frame);
            return null;
        }

        try {
            return new Blik(place, code.trim());
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), frame);
            return null;
        }
    }

    private MetodaPlatnosci platnoscKarta(JFrame frame, MiejscePlatnosci place) {
        JTextField cardField = new JTextField();
        JTextField expiryField = new JTextField();
        JTextField cvvField = new JTextField();

        Object[] inputs = {
                "Numer karty (16 cyfr):", cardField,
                "Data ważności (DD/MM/YYYY):", expiryField,
                "CVV (3 cyfry):", cvvField
        };

        int result = JOptionPane.showConfirmDialog(frame, inputs, "Dane karty", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return null;

        String cardNumber = cardField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            showError("Wszystkie pola karty są wymagane.", frame);
            return null;
        }

        try {
            return new Karta(place, cardNumber, expiry, cvv);
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage(), frame);
            return null;
        }
    }

    private void finalizacjaPlatnosci(JFrame frame, MetodaPlatnosci method, MiejscePlatnosci place, int amount) {
        String methodName = (method instanceof Blik) ? "Blik" : "Karta";
        String placeName = (place instanceof NaMiejscu) ? "Na miejscu" : "Online";
        if(method.wykonajPlatnosc()) {
            JOptionPane.showMessageDialog(frame,
                    String.format("Płatność została pomyślnie zrealizowana!\nMetoda: %s\nMiejsce: %s\nKwota: %.2f PLN",
                            methodName, placeName, (double) amount),
                    "Potwierdzenie płatności", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showError(String message) {
        showError(message, this);
    }
    private void showError(String message, Component parent) {
        JOptionPane.showMessageDialog(parent, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }
    private void showInfo(String message) {
        showInfo(message, this);
    }
    private void showInfo(String message, Component parent) {
        JOptionPane.showMessageDialog(parent, message, "Informacja", JOptionPane.INFORMATION_MESSAGE);
    }

    private class CartItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Produkt && zamowienie != null) {
                Produkt produkt = (Produkt) value;
                Integer quantity = produkt.getZamowienieMap().get(zamowienie);
                setText(produkt.getNazwa() + " - " + (quantity != null ? quantity : 0) + " szt.");
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
//                Katalog katalog1 = new Katalog("zimowy", "promocyjny");
//                Katalog katalog2 = new Katalog("letni", "VIP");
//
//                Klient klient = new Klient("Jan", "Kowalski", "jan@gmail.com", "123456789",new Adres("Testowa", "00-000", "Warszawa", 1, 1),"haslo123", "janko", katalog1);
//
//                Nowicjusz nowicjusz = new Nowicjusz("Anna", "Nowak", "anna@gmail.com", "987654321",new Adres("Nowa", "00-000", "Warszawa", 2, 3),LocalDateTime.now(), "1234567890123456", 123);
//
//                Starszy starszy = new Starszy("Piotr", "Senior", "piotr@gmail.com", "555666777",new Adres("Stara", "00-000", "Warszawa", 5, 10),LocalDateTime.now(), LocalDateTime.now(), "9876543210987654", 456);
//
//                Produkt produkt1 = starszy.stworzProdukt(40, 3, "Krem do twarzy", "Pielęgnacja");
//                Produkt produkt2 = starszy.stworzProdukt(30, 5, "Balsam do ciała", "Pielęgnacja");
//                Produkt produkt3 = starszy.stworzProdukt(40, 10, "Krem do rąk", "Pielęgnacja");
//
//
//                katalog1.addProdukt(produkt1);
//                katalog1.addProdukt(produkt2);
//                katalog2.addProdukt(produkt1);
//                klient.addKatalog(katalog2);
//                katalog1.addProdukt(produkt3);
//                produkt3.addKatalog(katalog1);
//
//                ZamowienieGui gui = new ZamowienieGui(klient, nowicjusz);
//                gui.setVisible(true);

                try {
                    ObjectPlus.loadExtent();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ZamowienieGui gui = new ZamowienieGui(ObjectPlus.getExtentFromClass(Klient.class).getFirst(), ObjectPlus.getExtentFromClass(Nowicjusz.class).getFirst());
//                ZamowienieGui gui = ObjectPlus.getExtentFromClass(Klient.class).getFirst().getZamowienieGui();
                gui.setVisible(true);


            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nie udało się uruchomić aplikacji: " + e.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}