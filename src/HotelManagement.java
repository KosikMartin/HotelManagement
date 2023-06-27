import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Trieda s hlavnou logikou aplikácie kde je implementovaný hlavný algoritmus pre chod programu v textovej podobe.
 * Tak isto slúži na formátovanie vstupu od používateľa a následne formátovanie do súboru.
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public class HotelManagement {
    private final RoomDB<Room> roomDB;
    private final String filepath = "bookings.csv";
    private final Administrator admin;
    private final ArrayList<Booking> bookings;
    private double totalBookingCost = 0.0;

    /**
     * Konštruktor vytvori Objekt typu HotelManagement, v konštruktore inicializujem atribúty.
     */
    public HotelManagement() {
        this.roomDB = new RoomDB<>();
        this.bookings = new ArrayList<>();
        this.admin = new Administrator();
    }

    /**
     * Metóda pre hlavný chod aplikácie, obsahuje 2 cykly ktoré čakajú na userInput. (Vstup od používateľa)
     * Na základe logických testov a podľa vstupu sa vykoná séria metód ktoré zodpovedajú tomu čo používateľ chce.
     */
    public void run() {
        String roomID;
        Room apartman;
        Scanner inputID = new Scanner(System.in);

        try {
            this.checkForBookings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Vitajte v Hotel Continental Management Systéme!\n");

        // Get user's first name
        System.out.println("Zadajte vaše krstné meno: ");
        String krstneMeno = inputID.nextLine();
        // Get user's last name
        System.out.println("Zadajte vaše priezvisko: ");
        String priezvisko = inputID.nextLine();

        User user = new Guest(krstneMeno, priezvisko);

        do {
            // Display available rooms and ask the user to choose a room
            System.out.println("Vyberte si z nasledovných možností rezervácie:\n" + this.showAllApartments());
            do {
                System.out.println("Zadaj ID Apartmánu (napr. StandardRoom1) (Napíšte X ak si želáte zrušiť booking): ");
                roomID = inputID.nextLine();

                if (roomID.equalsIgnoreCase(this.admin.getToken())) {
                    System.out.println("Entering privileged mode");
                    try {
                        this.admin.manageBookings(inputID);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    this.admin.setPrivilegedMode(false);
                    continue;
                }
                if (roomID.equalsIgnoreCase("x")) {
                    System.out.println("Booking process sa ukončí");
                    return;
                }
                apartman = this.lookForApartment(roomID);
                if (apartman == null) {
                    System.out.println("ID Apartmánu nesprávne, prosím zadaj ID Znovu alebo stlač X pre ukončenie.");
                } else {
                    if (!apartman.isBooked()) {
                        if (apartman.book()) {
                            System.out.println("Zabookovali ste si apartmán úspešne.");

                            int pocetDni;
                            boolean povoleneDni;

                            do {

                                System.out.println("Zadaj počet dní na koľko si apartmán chceš rezervovať (maximum 7 dní): ");
                                pocetDni = Integer.parseInt(inputID.nextLine());
                                povoleneDni = pocetDni > 0 && pocetDni <= 7;

                                if (!povoleneDni) {
                                    System.out.println("Nesprávny počet dní zadaný, prosím zadaj číslo medzi 1 až 7.");
                                }
                            } while (!povoleneDni);


                            // Check if additional services are available for the room type
                            if (apartman instanceof StandardRoom) {
                                System.out.println("Prajete si prístup k saune? (Poplatok!) (y/n)");
                                String saunaVyber = inputID.nextLine();
                                if (saunaVyber.equalsIgnoreCase("y")) {
                                    ((StandardRoom)apartman).setHasSauna(true);
                                }
                            } else if (apartman instanceof DeluxeRoom) {
                                System.out.println("Prajete si objednať prístup na masáže? (Poplatok!) (y/n)");
                                String massageChoice = inputID.nextLine();
                                if (massageChoice.equalsIgnoreCase("y")) {
                                    ((DeluxeRoom)apartman).setMassageServ(true);
                                }

                                System.out.println("Môžem k tomu ponúknuť Vírivku? (Poplatok!) (y/n)");
                                String hotTubChoice = inputID.nextLine();
                                if (hotTubChoice.equalsIgnoreCase("y")) {
                                    ((DeluxeRoom)apartman).setHotTubServ(true);
                                }
                            }
                            double celkovaCena = apartman.calculateTotalCost(pocetDni);
                            Booking booking = new Booking(user, roomID, pocetDni, celkovaCena);
                            //checkpoint pre array
                            this.bookings.add(booking);

                            this.totalBookingCost += celkovaCena;
                            break;
                        } else {
                            System.out.println("Nepodarilo sa zabookovať apartmán. Prosím zadaj Id znova alebo skonči pomocou X.");
                        }
                    } else {
                        System.out.println("Apartmán už je zabookovaný. Prosím zadaj iné ID, alebo skonči pomocou X.");
                    }
                }
            } while (true);


            System.out.println("Máte v pláne si zabookovať ďalšie apartmány? (y/n): ");
            String dalsiBooking = inputID.nextLine();
            if (!dalsiBooking.equalsIgnoreCase("y")) {
                System.out.println("Booking process úspešný. Celková cena apartmánov: €" + this.totalBookingCost);

                try {
                    this.saveBooking();
                    System.out.println("Bookovanie úspešné.");
                } catch (IOException e) {
                    System.out.println("Nepodarilo sa uložiť booking.");
                    e.printStackTrace();
                }

                break;
            }

        } while (true);
    }

    /**
     *Metóda vytvára reťazec obsahujúci informácie o izbách. Prechádza zoznam izieb v objekte roomDB a pre každú izbu vykoná nasledujúce:
     * Ak je izba voľná (tj. nie je zabookovaná), pridá informácie o izbe do reťazca roomList.
     * Tieto informácie zahŕňajú idApartmánu (getRoomID()), počet postelí / kapacitu (getBeds()), cenu izby (getPrice()) a označenie, že je voľná.
     * Ak je izba zabookovaná, pridá informáciu o bookingu do reťazca roomList spolu s IdApartmánu.
     *
     * @return Metóda vráti reťazec roomList v podobe Stringu.
     */
    public String showAllApartments() {
        StringBuilder roomList = new StringBuilder();
        for (Room r : this.roomDB.getRooms()) {
            if (!r.isBooked()) {
                roomList.append(r.getRoomID()).append(" je Voľná (Postele: ").append(r.getBeds()).append(", Cena: €").append(r.getPrice()).append(")").append(" \n  ");
            } else {
                roomList.append(r.getRoomID()).append(" je už zabookovaná!").append("\n");
            }
        }
        return roomList.toString();
    }

    /**
     * Metóda slúži na vyhľadávanie apartmánu pomocou Apartman ID.
     * Následne prejde databázu, skontroluje či je to ten apartmán s danou ID.
     * Ak áno, vráti mi objekt s príslušnou ID.
     * @param roomID plním Stringovou hodnotou.
     * @return vracia mi objekt typu room, s ktorým viem ďalej pracovať.
     */
    public Room lookForApartment(String roomID) {
        for (Room r : this.roomDB.getRooms()) {
            if (r.getRoomID().equals(roomID)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Táto metóda číta zo súboru 2 políčko. (začínam od 0 takže je to 3tie na pohľad)
     *
     *
     *                              field0      field1     field2
     * Dáta sú uložené nasledovne [firstname],[lastname],[roomtype]
     *
     * plní mi roomID hodnotou v druhom políčku (field2)
     * Vytvorím si lokálnu premennú typu room, a naplním ju 2hím políčkom (hodnota na index 0)
     * Ak moja premenná nenadobúda hodnotu null, a teda je to valid apartmán, následne ho zabookujem.
     *
     * Túto metódu volám pred každým začiatkom HotelManagementu, nech mám istotu, že si nezabookujem obsadený apartmán.
     * (Bez tejto metódy by mi súbor bol viac menej nanič)
     * @throws IOException Vyhadzuje výnimku v prípade chyby pri práci s súborom (write, read)
     */

    public void checkForBookings() throws IOException {
        File file = new File(this.filepath);
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] oddeleneCastiCiarka = line.split(",");
                if (oddeleneCastiCiarka.length >= 3) {
                    String roomID = oddeleneCastiCiarka[2];
                    Room room = this.lookForApartment(roomID);
                    if (room != null) {
                        room.setBooked(true);
                    }
                }
            }
        }
    }

    /**
     * Metóda saveBooking mi zapisuje a informácie z logiky hry do súboru bookings.csv.
     * V metóde som to naformátoval kúsok krajšie, nech to je prehladnejšie.
     * Prechádzam tu ArrayList bookings a následne uložím hodnoty do premennej bookingInfo a následne ju na konci zapíšem.
     * Potom Zapisujem Nový riadok.
     *
     * ()()Ako som spomínal, tu vo výpise má deluxeRoom Includnutú saunu, je to zadarmo ako perk Deluxe room.()()
     * @throws IOException metóda hádže IOException ak sa vyskytne chyba pri práci so súborom.(Write, Read)
     */
    public void saveBooking() throws IOException {
        File file = new File(this.filepath);
        FileWriter writer = new FileWriter(file, true); // Append mode
        // Write field labels
        for (Booking booking : this.bookings) {
            if (booking.getApartmanID().startsWith("D")) {
                String bookingInfo = booking.getPerson().getFirstName() + "," +
                        booking.getPerson().getLastName() + "," +
                        this.getApartmentType(booking.getApartmanID()) + "," +
                        "Included" + "," +
                        this.hasMassageServ(booking.getApartmanID()) + "," +
                        this.hasHotTubServ(booking.getApartmanID()) + "," +
                        booking.getNumOfDays() + "," +
                        this.calculateDailyCost(booking.getApartmanID()) + "," +
                        booking.getTotalCost();
                writer.write(bookingInfo + "\n");
            } else {
                String standardNoValue = "No";
                String bookingInfo = booking.getPerson().getFirstName() + "," +
                        booking.getPerson().getLastName() + "," +
                        this.getApartmentType(booking.getApartmanID()) + "," +
                        this.hasSauna(booking.getApartmanID()) + "," +
                        standardNoValue + "," +
                        standardNoValue + "," +
                        booking.getNumOfDays() + "," +
                        this.calculateDailyCost(booking.getApartmanID()) + "," +
                        booking.getTotalCost();
                writer.write(bookingInfo + "\n");
            }
        }
        writer.close();
    }


    /**
     * Metóda mi vráti typ Apartmánu, na základe ID apartmánu. Vytvorím si lokálnu premennú typu room, tú naplním Metódou lookForApartment
     * ktorej passnem parameter s metódy (roomID) (lookForApartment vracia Objekt typu Room)
     * A potom následne dostanem stringovú hodnotu
     * typu apartmánu ktorý viem pekne zapísať do súboru.
     * @param roomID String roomID (napríklad StandardRoom1)
     * @return vráti Typ apartmánu v podobe Stringu ktorý potom zapisujem do súboru.
     */
    private String getApartmentType(String roomID) {
        Room room = this.lookForApartment(roomID);
        if (room instanceof StandardRoom) {
            return room.getRoomID();
        } else if (room instanceof DeluxeRoom) {
            return room.getRoomID();
        }
        return "";
    }
    /**
     *Metóda slúži na zistovanie boolean hodnoty doplnkových služieb apartmánu typu StandardRoom.
     * @param roomID Nakŕmim metódu ID apartmánu (StandardRoom1) v logike Správy dát, a následne mi vráti boolean hodnotu na základe
     *               ktorá mi hovorí či má inštancia typu Standard room prístup k saune..
     *               Ostatné prípady = Nič. >> to mi tak isto aj naplní do súboru. Vo výpise ale už mám spravené to, že každá deluxe room už
     *               prístup k saune má.
     * @return       vracia string hodnotu na základe logických testov if.
     */
    private String hasSauna(String roomID) {
        Room room = this.lookForApartment(roomID);
        if (room instanceof StandardRoom) {
            if (((StandardRoom)room).hasSaunaService()) {
                return "Yes";
            } else {
                return "No";
            }
        }
        return "";
    }

    /**
     *Metóda slúži na zistovanie boolean hodnoty doplnkových služieb apartmánu typu DeluxeRoom.
     * @param roomID Nakŕmim metódu ID apartmánu (DeluxeRoom1) v logike Správy dát, a následne mi vráti boolean hodnotu na základe
     *               ktorá mi hovorí či má inštancia typu Deluxe room prístup k vírivke.
     *               Ostatné prípady = nie. >> to mi tak isto aj naplní do súboru.
     * @return       vracia string hodnotu na základe logických testov if.
     */
    private String hasHotTubServ(String roomID) {
        Room room = this.lookForApartment(roomID);
        if (room instanceof DeluxeRoom) {
            if (((DeluxeRoom)room).hasHotTubService()) {
                return "Yes";
            } else {
                return "No";
            }
        }
        return "No";
    }

    /**
     * Metóda slúži na zistovanie boolean hodnoty doplnkových služieb apartmánu typu DeluxeRoom.
     * @param roomID Nakŕmim metódu ID apartmánu (DeluxeRoom1) v logike Správy dát, a následne mi vráti boolean hodnotu na základe
     *               ktorá mi hovorí či má inštancia typu Deluxe room prístup k masážam.
     *               Ak áno hodnota v tabulke bude yes, inak Nie.
     *               Následne mi to naplní do súboru v logike hry.
     * @return       vracia string hodnotu na základe logických testov if.
     */
    private String hasMassageServ(String roomID) {
        Room room = this.lookForApartment((roomID));
        if (room instanceof DeluxeRoom) {
            if (((DeluxeRoom)room).hasMassageService()) {
                return "Yes";
            } else {
                return "No";
            }
        }
        return "No";
    }

    /**
     *
     * @param roomID Nakŕmim metódu ID apartmánu (StandardRoom1) v logike Správy dát, a následne mi vráti cenu.
     *               používam na výpočet celkovej ceny a dennej ceny.
     * @return vráti dennú cenu pre nájdený apartmán.
     */
    private double calculateDailyCost(String roomID) {
        Room room = this.lookForApartment(roomID);
        return room.getPrice();
    }
}