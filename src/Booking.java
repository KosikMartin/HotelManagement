/**
 * Trieda booking sa stará o ukladanie informácií o rezervácií ako napr.
 * meno hosťa, číslo apartmánu, počet dní a celkovú čiastku pobytu.
 *
 * @author Martin Košík
 * @date 21/05/2023
 */

public class Booking {
    private final User person;
    private final String apartmanID;
    private final int numOfDays;
    private double totalCost;

    /**
     * Vytvára objekt typu Booking s daným používateľom, ID apartmánu, počtom dní a celkovou cenou za apartmán na 1 deň.
     *
     * @param person         používateľ, ktorý robí rezerváciu.
     * @param apartmanID    ID apartmánu, ktorý je rezervovaný.
     * @param numOfDays     počet dní, na ktoré je rezervácia spravená.
     * @param totalCost      celková cena rezervácie.
     */
    public Booking(User person, String apartmanID, int numOfDays, double totalCost) {
        this.person = person;
        this.apartmanID = apartmanID;
        this.numOfDays = numOfDays;
        this.totalCost = totalCost;
    }

    /**
     * Metóda ktorá mi vráti používateľa ktorý robil / robí rezerváciu.
     *
     * @return vracia používateľa.
     */
    public User getPerson() {
        return this.person;
    }

    /**
     * Metóda získa IDApartmánu.
     * @return vracia idApartmánu (e.g StandardRoom1)
     */
    public String getApartmanID() {
        return this.apartmanID;
    }

    /**
     * Metóda mi záska počet dní na koľko si užívateľ spravil rezerváciu.
     * @return počet dní.
     */
    public int getNumOfDays() {
        return this.numOfDays;
    }
    /**
     * Metóda na získanie celkovej ceny rezervácie.
     *
     * @return celková cena rezervácie
     */
    public double getTotalCost() {
        return this.totalCost;
    }

}
