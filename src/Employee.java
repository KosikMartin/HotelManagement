/**
 * Trieda ktorá dedí od triedy "User".
 *
 * Niekde používam anglické a niekde slovenské názvy, záleží kde sa to lepšie hodí na prehľadnosť kódu.
 * @author Martin Košík
 * @date 21/05/2023
 */
public class Employee extends User {
    private final String position;

    /**
     *  Vytvorí novú inštanciu  Guest zo špecifikovaným menom a priezviskom.
     * @param firstName používateľa.
     * @param lastName používateľa.
     * @param position pozícia používateľa.
     */
    public Employee(String firstName, String lastName, String position) {
        super(firstName, lastName);
        this.position = position;
    }

    /**
     * Vráti mi textovú reprezentáciu pozície.
     * @return Pozícia zamestnanca ako String.
     */
    @Override
    public String getPosition() {
        return this.position;
    }
}
