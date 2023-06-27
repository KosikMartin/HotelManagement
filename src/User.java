/**
 * Abstraktná trieda User, ktorá reprezentuje šablónu používateľa / človeka.
 * S triedou user pracujem keď vytváram potomkov pre triedu, napr employee, alebo guest.
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public abstract class User {
    private final String firstName;
    private final String lastName;

    /**
     * Konštruktor classy user
     * @param firstName krstné meno používateľa.
     * @param lastName priezvisko používateľa.
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Vráti meno používateľa
     * @return krstné meno.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Vráti priezvisko používateľa.
     * @return priezvisko.
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Abstraktná metóda na získavanie role užívateľa
     *
     * @return rola používateľa
     */

    public abstract String getPosition();
}

