/**
 * Táto abstraktná trieda mi slúži ako šablóna a predok pre triedy StandardRoom a DeluxeRoom.
 *Implementuje Interface Bookable a Cancellable.
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public abstract class Room implements Bookable, Cancellable {
    private final String roomID;
    private boolean booked;
    private final int beds;
    private double price;

    /**
     * Vytvorí objekt Room s danou kapacitou, ID, a cenou.
     *
     * @param roomID   the unique identifier of the room
     * @param beds the maximum number of occupants the room can accommodate
     * @param price     the cost per booking of the room
     */
    public Room(String roomID, int beds, double price) {
        this.roomID = roomID;
        this.beds = beds;
        this.price = price;
    }


    /**
     * Getter pre ID apartmánu.
     *
     * @return vracia ID apartmánu v String podobe.
     */
    public String getRoomID() {
        return this.roomID;
    }


    /**
     * Metóda slúži na zrušenie rezervacie.
     *
     * @return vracia true hodnotu.
     */
    @Override
    public boolean cancel() {
        this.setBooked(false);
        return true;
    }
    /**
     * Getter pre informáciu o bookingu.
     *
     * @return vracia boolean hodnotu.
     */
    public boolean isBooked() {
        return this.booked;
    }

    /**
     * Setter pre bookovanie.
     *
     * @param booked zadávam true/false hodnotu podla poziadavky.
     */
    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    /**
     * Getter pre kapacitu apartmánu.
     *
     * @return vracia velkosť / kapacitu / počet postelí apaartmánu.
     */
    public int getBeds() {
        return this.beds;
    }


    /**
     * Getter pre cenu apartmánu.
     *
     * @return vracia cenu apaartmánu.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Abstraktná metóda ktorú implementujem v potomkoch.
     *
     * @param numOfDays počet dní - používam pri počítaní.
     * @return vracia celkovú sumu.
     */
    public abstract double calculateTotalCost(int numOfDays);
}