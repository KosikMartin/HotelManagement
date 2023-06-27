import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Trieda AdminPanel slúži na grafické spracovanie dát v Booking.csv súbore, dáta v csv súbore sú oddelené čiarkou.
 * DefaultTableModel je používaná na správu dát.
 * data v tabulke je používaný na zobrazenie dát v tabulke. To že sú oddelené čiarkou mi umožní ľahšie spracovanie s dátami.
 * Táto trieda je k dispozícií len Administrátorovy po zadaní administrátor tokenu po prihlásení do Systému na bookovanie.
 * Po zadaní tokenu je Administrátor vyzvaný aby zadal príkazy a jedným z nich je aj príkaz na zapnutie admin panela.
 * AdminPanel CloseOperation som nadstavil na dispose_on_close ktorá mi zaručí že program sa nestopne ale bude pokračovať naďalej aj keď okno zavriem.
 *
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public class AdminPanel extends JFrame {
    private JTable dataInTable;
    private DefaultTableModel tableModel;
    private final String filePath = "bookings.csv";

    /**
     * Vytvorí objekt typu AdminPanel, kde inicializuje potrebné atribúty. Dedí sa od triedy JFrame.
     * Nastaví sa titul okna, velkosť, akcia pri zatvorení okna.
     *
     *Pridá sa ku každému tlačidlu ActionListener ktorý bude vykonávať metódy pre dané tlačidlo.
     *Nastavím formátovanie (layout) okna a zalomenie a načítam dáta.
     */
    public AdminPanel() {
        this.setTitle("Admin Panel");
        this.setSize(750, 420);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.tableModel = new DefaultTableModel();
        this.dataInTable = new JTable(this.tableModel);
        JScrollPane scrollPane = new JScrollPane(this.dataInTable);
        JButton tlacidloPridaj = new JButton("Pridať");
        JButton tlacidloUpdate = new JButton("Aktualizuj");
        JButton tlacidloVymaz = new JButton("Vymaž");

        tlacidloPridaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPanel.this.addBooking();
                AdminPanel.this.reloadFile();
            }
        });

        tlacidloVymaz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AdminPanel.this.delReservation();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                AdminPanel.this.reloadFile();
            }
        });

        tlacidloUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPanel.this.updateBooking();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(tlacidloPridaj);
        buttonPanel.add(tlacidloUpdate);
        buttonPanel.add(tlacidloVymaz);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        try {
            this.loadCSVData(this.filePath);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }

    /**
     * Metóda loadCSVData zobrazí dáta do GUI s CSV súboru.
     * hlavickaVSubore sa berie pre 0 riadok kde je napísané základné formátovanie dát.
     * S 0tým riadkom používateľ nepracuje.
     *
     * @param filePath cesta k súboru.
     * @throws IOException výnimka v prípade chyby počas práce so súborom.
     */
    private void loadCSVData(String filePath) throws IOException {
        BufferedReader citac = new BufferedReader(new FileReader(filePath));
        String riadok;
        String[] hlavickaVSubore = citac.readLine().split(","); //toto beriem ako 0ty riadok ktory mi urcuje hlavicky a podla nich usporiadavam

        this.tableModel.setRowCount(0);
        this.tableModel.setColumnCount(0);

        for (String stlpec : hlavickaVSubore) {
            this.tableModel.addColumn(stlpec);
        }

        while ((riadok = citac.readLine()) != null) {
            String[] data = riadok.split(",");
            this.tableModel.addRow(data);
        }

        citac.close();
    }

    /**
     *Metóda reloadFile slúži na zmazanie a znova načítanie dát zo súboru CSV do GUI.
     *To mi zaručí aktualitu informácií a zamedzí duplikovaniu dát na obrazovke.
     */
    private void reloadFile() {
        this.tableModel.setRowCount(0);

        try {
            this.loadCSVData(this.filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metóda addbooking sa stará o manuálne pridanie informácií o používateľovi.
     * Nejedná sa o reprezentáciu v textovom formáte ale v GUI.
     * Vyhodí mi 3 okná zasebou. Meno Priezvisko a typ apartmánu.
     */
    private void addBooking() {
        String meno = JOptionPane.showInputDialog("Zadaj Meno: ");
        String priezvisko = JOptionPane.showInputDialog("Zadaj Priezvisko: ");
        String typApartmanu = JOptionPane.showInputDialog("Zadaj Typ Apartmánu(Standard / Deluxe): ");



        //TODO
        //Tu treba pridat aby sa tam pridavala cena automaticky ked budem pridavat veci ako sauna atd.


        String[] rowData = {meno, priezvisko, typApartmanu};
        this.tableModel.addRow(rowData);

        try {
            this.updateCSVFile();
        } catch (IOException e) {   //odchytavam lebo updateCSV pracuje so súborom.
            e.printStackTrace();
        }

    }

    /**
     * Aktualizuje záznam o rezervácií v tabuľke, získava výbraný riadok v tabuľke a získava hodnoty buniek pre daný riadok.
     * Zobrazuje dialógové okno pre aktualizáciu týchto hodnôt.
     * Ak preruším aktualizáciu metóda sa skončí.
     * Ak sú dostupné nové dáta, aktualizuje tabuľku, a potom aktualizuje aj CSV súbor.
     * @throws IOException inak vyhodí
     */
    private void updateBooking() {
        int selectedRows = this.dataInTable.getSelectedRow();

        if (selectedRows == -1) {
            JOptionPane.showMessageDialog(this, "Prosím označ správny riadok");
            return;
        }

        String[] existingData = new String[this.tableModel.getColumnCount()];
        for (int i = 0; i < existingData.length; i++) {
            Object val = this.tableModel.getValueAt(selectedRows, i);
            existingData[i] = (val != null) ? val.toString() : "";

        }

        String[] updatedData = this.updateDialog(existingData);
        if (updatedData == null) {
            //update prerušený.
            return;
        }

        for (int i = 0; i < updatedData.length; i++) {
            this.tableModel.setValueAt(updatedData[i], selectedRows, i);
        }

        try {
            this.updateCSVFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metóda slúži na odstránenie označených riadkov, tu už nepracujem zo stĺpcami.
     * Ide mi o rýchle vymazávanie z databázy bookingov.
     * Následne si vytváram arraylist s označenými riadkami a potom ich mažem s tabuľky.
     * Musím ich mazať a tak isto aj sortovať, aby som predišiel masívnej duplikácií riadkov.
     * @throws IOException sa vyhodí ak sa vyskytne chyba pri práci zo súborom.
     */
    private void delReservation() throws IOException {
        int[] oznaceneRiadky = this.dataInTable.getSelectedRows();
        if (oznaceneRiadky.length == 0) {
            JOptionPane.showMessageDialog(this, "Prosím označ riadky.");
            return;
        }
        Arrays.sort(oznaceneRiadky); //usporiadam riadky, ak by som ich neusporiadaval a potom nevymazaval, riadky by sa mi pridavali.
        for (int i = 0; i < oznaceneRiadky.length; i++) {
            this.tableModel.removeRow(oznaceneRiadky[i] - i);
        }

        this.updateCSVFile();
    }

    /**
     *  Táto metóda slúži na aktualizáciu obsahu CSV súboru na základe údajov z tabuľky (JTable) v programe.
     *  Cyklus ktorí iteruje cez stĺpce a zapisuje názvy stĺpcov do súboru pomocou metódy zapisovac.append();
     *  Ak stĺpec nieje posledným, oddelím ho čiarkou.
     *  Inak pridám nový riadok n
     *  Vnorený cyklus prechádza riadky, .getRowCount, a stĺpce tabulky získavajú hodnoty
     *  z jednotlivých buniek pomocou modelTabulky.getValueAt(i,j)
     *  znova podmienka o novom stĺpci.
     *  zapisovac.flush vyprázdni vyrovnávaciu pamäť a uistí sa že sa všetky dáta zapísali do súboru.
     *  zapisovac.close() uzavrie súbor, a uvolní zdroje.
     *
     * @throws IOException vracia mi
     */
    private void updateCSVFile() throws IOException {
        FileWriter zapisovac = new FileWriter(this.filePath);

        for (int i = 0; i < this.tableModel.getColumnCount(); i++) {
            zapisovac.append(this.tableModel.getColumnName(i));
            if (i < this.tableModel.getColumnCount() - 1) {
                zapisovac.append(",");
            } else {
                zapisovac.append("\n");
            }
        }
        for (int i = 0; i < this.tableModel.getRowCount(); i++) {
            for (int j = 0; j < this.tableModel.getColumnCount(); j++) {
                Object value = this.tableModel.getValueAt(i, j);
                if (value != null) {
                    zapisovac.append(value.toString());
                }
                if (j < this.tableModel.getColumnCount() - 1) {
                    zapisovac.append(",");
                } else {
                    zapisovac.append("\n");
                }
            }
        }
        zapisovac.flush();
        zapisovac.close();
    }


    /**
     * Metóda prijíma pole data, ktoré obsahuje existujúce údaje pre aktualizáciu.
     * showConfirmDialog vráti výsledok, ktorý sa ukladá do premennej result.
     * Ak používateľ stlačí tlačidlo OK, získa sa hodnota z každého textového poľa (JTextField.getText())
     * @param data pole dát, obsahuje už existujúce údaje pre aktualizáciu.
     * @return vráti pole typu String[]. Toto pole sa následne vráti ako výstup z metódy.
     */
    private String[] updateDialog(String[] data) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField menoF = new JTextField(data[0]);
        JTextField priezviskoF = new JTextField(data[1]);
        JTextField apartmanF = new JTextField(data[2]);

        panel.add(new JLabel("Meno"));
        panel.add(menoF);
        panel.add(new JLabel("Priezvisko"));
        panel.add(priezviskoF);
        panel.add(new JLabel("Typ Apartmánu"));
        panel.add(apartmanF);

        int result = JOptionPane.showConfirmDialog(this, panel, "Aktualizuj Booking", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String meno = menoF.getText();
            String priezvisko = priezviskoF.getText();
            String typApartmanu = apartmanF.getText();

            return new String[]{meno, priezvisko, typApartmanu};

        } else {
            return null; //uzivatel zrusil prompt
        }

    }

    /**
     * Metóda startAdminPanel mi slúži na spustenie, incializovanie a zobrazenie AdminPanelu
     */
    public void startAdminPanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminPanel adminpanel = new AdminPanel();
                adminpanel.setVisible(true);
            }
        });
    }
}
