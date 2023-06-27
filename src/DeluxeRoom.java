/**
 * Trieda je používaná ako typ apartmánu , sú 2
 *
 * @author Martin Košík
 * @date 21.05
 */
public class DeluxeRoom extends Room {
    private boolean massageServ; //boolean hodnota pre evidovanie doplnkovej služby.
    private boolean hotTubServ; //boolean hodnota pre evidovanie doplnkovej služby


    /**
     * Konštruktor, vytváram inštanciu a dedím kapacitu a cenu apartmánu.
     *
     * @param roomID mi slúži na priradenie roomID napr DeluxeRoom10 k inštancií ktorú následne vytvorím.
     */
    public DeluxeRoom(String roomID) {
        super(roomID, 4, 200.0);
    }

    /**
     * Getter na vratenie hodnoty boolean, ci mam doplnkovú službu alebo nie.
     *
     * @return vracia boolean hodnotu masazDoplnok.
     */
    public boolean hasMassageService() {
        return this.massageServ;
    }

    /**
     * Setter na príslušnú službu masáže.
     */
    public void setMassageServ(boolean massageServ) {
        this.massageServ = massageServ;

    }

    /**
     * Getter na vratenie hodnoty boolean, ci mam doplnkovú službu alebo nie.
     *
     * @return vracia boolean hodnotu virivka.
     */
    public boolean hasHotTubService() {
        return this.hotTubServ;
    }

    /**
     * Setter na príslušnú službu "Vírivka"
     */
    public void setHotTubServ(boolean hotTubServ) {
        this.hotTubServ = hotTubServ;
    }


    /**
     * Implementovaná metóda s interface Bookable, nadstaví mi apartmán ako zabookovaný, ďalej sa už nemôže bookovať.
     *
     * @return vráti mi true / false, na základe toho, či je apartmán už zabookovaný alebo nie.
     */
    @Override
    public boolean book() {
        if (!this.isBooked()) {
            this.setBooked(true);
            return true;
        }
        return false;
    }

    /**
     * Metóda slúži na vypočítanie finálnej ceny apartmánu.
     *
     * @param numOfDays príjmam parameter počet dní podľa ktorého vypočítam finálnu cenu za Apartmán.
     * @return Vracia mi cenu apartmánu typu double.
     */
    @Override
    public double calculateTotalCost(int numOfDays) {
        double celkovaCena = this.getPrice() * numOfDays;

        if (this.hasHotTubService()) {
            celkovaCena += 100 * numOfDays;
        }
        if (this.hasMassageService()) {
            celkovaCena += 75 * numOfDays;
        }
        return celkovaCena;
    }
}
