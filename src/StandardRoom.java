/**
 * Trieda StandardRoom slúži ako potomok pre triedu Room. Celý konštruktor dedí od predka,
 * najviac ako s triedou pracujem je na vypočítanie celkovej hodnoty bookingu a branie ID apartmánu.
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public class StandardRoom extends Room {
    private boolean hasSauna;


    /**
     * Konstruktor ktorý vytvorí objekt typu StandardRoom, nič iné v konštruktore neriešim, všetko dedím od predka.
     * @param idApart plním len RoomID (StandardRoom1) <= Príklad.
     */
    public StandardRoom(String idApart) {
        super(idApart, 2, 100.0);

    }

    /**
     * Getter na atribát saunaService
     * @return vracia true/false hodnotu na základe toho či si človek zakúpil aj saunu alebo nie.
     */
    public boolean hasSaunaService() {
        return this.hasSauna;
    }

    /**
     * Vráti sa mi
     * @param hasSauna plním true / false
     */
    public void setHasSauna(boolean hasSauna) {
        this.hasSauna = hasSauna;
    }

    /**
     * Metóda na zabookovanie apartmánu.
     * @return vráti true / false na základe toho či sa mi podarilo apartmán zabookovať.
     */
    @Override
    public boolean book() {
        if (!isBooked()) {
            this.setBooked(true);
            return true;
        }
        return false;
    }

    /**
     * Metóda calculateTotalCost slúži na
     * @param numOfDays
     * @return vracia finálnu cenu roomky
     */
    public double calculateTotalCost(int numOfDays) {
        double finalnaCena = this.getPrice() * numOfDays;

        if (this.hasSaunaService()) {
            finalnaCena += 50 * numOfDays;
        }

        return finalnaCena;
    }
}