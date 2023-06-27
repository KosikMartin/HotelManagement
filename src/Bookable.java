/**
 * Interface trieda ktorá slúži na určenie apartmánov či je možne ich zabookovať alebo nie.
 *
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public interface Bookable {
    /**
     * Metóda v interface ktorá sa používa na bookovanie apartmánov.
     * @return vráti true / false na základe či bol daný apartmán rezervovaný.
     */
    boolean book();
}
