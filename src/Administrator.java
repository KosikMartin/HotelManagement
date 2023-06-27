import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

/**
 *Trieda Administrator sa stará o textového manažéra dát ktorý vie spravovať jednotlivé políčka v zadaných riadkoch.
 *V tejto triede sa nachádzajú private metódy ktoré bude volať len sám administrátor, a nieje treba aby ich volal niekto iný.
 * Trieda obsahuje zložitejšiu logiku a prácu so stringami, spájanie a rozdeľovanie stringov a polí stringov.
 *
 * @author Martin Košík
 * @date 21.05.2023
 */
public class Administrator {

    private final String token = "Enter Privileged Mode";
    private boolean privilegedMode = false;
    private static final String FILE_PATH = "bookings.csv";

    /**
     * Vytvorí objekt typu Administrator.
     */

    public Administrator() {

    }

    /**
     * Číta vstup od používateľa a vykoná operáciu súvisiacu so správou súboru.
     *
     * @param scanner preberá sa v hlavnej triede ktorá pracuje s logikou HotelManagementu.
     * @throws java.io.IOException vyhodí výnimku ak nastane neaký error počas managovania bookingu.
     */

    public void manageBookings(Scanner scanner) throws IOException {
        this.privilegedMode = true;
        if (this.privilegedMode) {
            System.out.println("Zadaj jeden z príkazov (update/delete/exit/showData)");
            System.out.println("Ak chcete zapnut admin panel, zadajte prikaz adminPanel.");
            System.out.println();
            String command = scanner.nextLine();

            while (!command.equalsIgnoreCase("exit")) {
                switch (command) {
                    case "update" -> this.modifyField();
                    case "showData" -> this.getBookedPeople();
                    case "delete" -> this.deleteField();
                    case "exit" -> this.exitPrivilegedMode();
                    case "adminPanel" -> this.other();
                    default -> System.out.println("Neplatny command!");
                }
                System.out.println("Enter another command or exit. (update/delete/exit/showData/adminPanel)");
                command = scanner.nextLine();
            }
        }
    }

    /**
     * Metóda na ukončenie privilegovaného módu
     */
    private void exitPrivilegedMode() {
        this.privilegedMode = false;
        System.out.println("Opúšťaš privilegovaný mód");
    }

    /**
     * Metóda slúži na spustenie admin panelu,ktorý vieme použiť na rýchlu modifikáciu dát.
     * Používa sa na modifikáciu v prípadoch preklepu viacerých užívateľov, po prípade náhlych rozhodnutí že človek booking už nechce.
     */
    private void other() {
        LoginPanel loginPanel = new LoginPanel();
        loginPanel.executeLogin();
    }

    /**
     * Spravuje konkrétne pole v súbore a aktualizuje ho na novú hodnotu.
     * Pracujem ako v 2D poli.
     *
     * @throws IOException ak sa vyskytne chyba pri čítaní alebo zápise do súboru
     */

    private void modifyField() throws IOException {
        if (this.privilegedMode) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Zadaj číslo riadku");
                int cisloRiadku = Integer.parseInt(scanner.nextLine());

                System.out.println("Zadaj stĺpec");
                int cisloStlpcu = Integer.parseInt(scanner.nextLine()) - 1; //-1 lebo zacinam od nuly a uzivatel toto nevie

                System.out.println("Zadaj novú hodnotu");
                String newValue = scanner.nextLine();

                List<String> riadky = this.readBookingFile();
                if (cisloRiadku >= 0 && cisloRiadku < riadky.size()) {

                    String[] policka = riadky.get(cisloRiadku).split(","); //rozdelim na policka

                    if (cisloStlpcu >= 0 && cisloStlpcu < policka.length) {
                        policka[cisloStlpcu] = newValue;
                        riadky.set(cisloRiadku, String.join(",", policka));
                        this.writeToBookingFile(riadky);
                        System.out.println("Pole bolo aktualizované.");
                    } else {
                        System.out.println("Index políčka je neplatný.");
                    }
                } else {
                    System.out.println("Neplatný index riadku.");
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else {
            System.out.println("Not in privileged mode.");
        }
    }

    /**
     * Vymaže konkrétne pole v súbore. Metóda pracuje v textovom podaní, naopak,
     * admin panel je orientovaný na prácu s tlačidlami.
     *
     * @throws IOException ak sa vyskytne chyba pri čítaní alebo zápise do súboru
     */

    private void deleteField() throws IOException {
        if (this.privilegedMode) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Zadaj riadok");
                int cisloRiadku = Integer.parseInt(scanner.nextLine());

                System.out.println("Zadaj stĺpec");
                int cisloStlpca = Integer.parseInt(scanner.nextLine()) - 1;
                List<String> riadky = this.readBookingFile();
                if (cisloRiadku >= 0 && cisloRiadku < riadky.size()) {
                    String[] policka = riadky.get(cisloRiadku).split(",");
                    if (cisloStlpca >= 0 && cisloStlpca < policka.length) {
                        List<String> noveRiadky = new ArrayList<>();
                        for (int i = 0; i < policka.length; i++) {
                            if (i != cisloStlpca) {
                                noveRiadky.add(policka[i]);
                            }
                        }

                        riadky.set(cisloRiadku, String.join(",", noveRiadky));
                        this.writeToBookingFile(riadky);
                        System.out.println("Policko uspesne vymazane.");

                    } else {
                        System.out.println("Neplatny index policka");
                    }
                } else {
                    System.out.println("Neplatny index riadku");
                }

            } catch (IOException e) {
                System.out.println("Chyba: " + e.getMessage());
            }

        } else {
            System.out.println("Not in privileged mode");
        }

    }

    /**
     * Zapisuje riadky do súboru
     *
     * @param linesToWrite riadky, ktoré sa majú zapísať do súboru.
     * @throws IOException vyhodí sa, ak pri zápise do súboru nastane chyba.
     */
    private void writeToBookingFile(List<String> linesToWrite) throws IOException {
        BufferedWriter zapisovac = new BufferedWriter(new FileWriter(this.FILE_PATH));
        for (String riadok : linesToWrite) {
            zapisovac.write(riadok);
            zapisovac.newLine();
        }
        zapisovac.close();
    }

    /**
     * Číta obsah súboru a vráti ho ako zoznam riadkov.
     *
     * @return zoznam riadkov v subore bookings.csv
     * @throws IOException ak sa vyskytne chyba pri čítaní / načítaní súboru.
     */

    private List<String> readBookingFile() throws IOException {
        List<String> riadky = new ArrayList<>();
        BufferedReader citac = new BufferedReader(new FileReader(this.FILE_PATH));
        String riadok;
        while ((riadok = citac.readLine()) != null) {
            riadky.add(riadok);
        }
        citac.close();
        return riadky;
    }

    /**
     * Číta obsah súboru a na terminál mi vypíše jeho obsah.
     *
     * @return zoznam riadkov v súbore vo formáte CSV
     * @throws IOException ak sa vyskytne chyba pri čítaní súboru
     */
    private void getBookedPeople() {
        String f = "bookings.csv";
        String singleRiadok = "";
        BufferedReader bfrdr = null;

        try {
            bfrdr = new BufferedReader(new FileReader(f));
            while ((singleRiadok = bfrdr.readLine()) != null) {
                String[] riadokSplit = singleRiadok.split(",");
                for (String i : riadokSplit) {
                    System.out.printf("%-12s", i);
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace(); //pouzivam na vypis aby som videl co presne sa udeje ak to padne.
        } finally {
            try {
                bfrdr.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Setter na privilegovaný mód.
     *
     * @param privilegedMode nadstavím hodnotu privilegovaného módu na hodnotu prebranú s parametra.
     */
    public void setPrivilegedMode(boolean privilegedMode) {
        this.privilegedMode = privilegedMode;
    }

    /**
     * Getter na token
     *
     * @return vráti admin token.
     */
    public String getToken() {
        return this.token;
    }

}

