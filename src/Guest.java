/**
 * Trieda ktorá dedí od triedy "User".
 *
 * Niekde používam anglické a niekde slovenské názvy, záleží kde sa to lepšie hodí na prehľadnosť kódu.
 * @author Martin Košík
 * @date 21/05/2023
 */
public class Guest extends User {

    /**
     *  Vytvorí novú inštanciu  Guest zo špecifikovaným menom a priezviskom.
     * @param firstName používateľa.
     * @param lastName používateľa.
     */

    public Guest(String firstName, String lastName) {
        super(firstName, lastName);
    }

    /**
     * Môže sa použiť na bližšiu identifikáciu osoby.
     * @return vracia pozíciu, či je hosť, alebo zamestnanec.
     */
    @Override
    public String getPosition() {
        String host = "Hosť";
        return host;
    }
}
