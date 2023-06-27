import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *Trieda slúži ako databáza pre objekty typu Room.
 * Je to generická trieda, <T extends room> mi ohraničí, aké objekty tam môžem vkladať.
 *
 * @param <T> Objekt typu Room.
 * @author Martin Košík
 * @date 21/05/2023
 */
public class RoomDB<T extends Room> {
    private final List<T> rooms;

    /**
     * Inicializuje Objekt RoomDB (Databáza apartmánov) a naplní ich hodnotami s poľa RoomID
     */
    public RoomDB() {
        this.rooms = new ArrayList<>();
        String[] apartmany = {"StandardRoom1", "StandardRoom2", "StandardRoom3", "StandardRoom4", "StandardRoom5", "StandardRoom6", "StandardRoom7", "StandardRoom8", "StandardRoom9", "StandardRoom10", "DeluxeRoom1", "DeluxeRoom2", "DeluxeRoom3", "DeluxeRoom4", "DeluxeRoom5", "DeluxeRoom6", "DeluxeRoom7", "DeluxeRoom8", "DeluxeRoom9", "DeluxeRoom10"};

        //plním apartmány
        for (int i = 0; i < apartmany.length; i++) {
            if (i < 10) {
                this.rooms.add((T)new StandardRoom(apartmany[i]));
            } else {
                this.rooms.add((T)new DeluxeRoom(apartmany[i]));
            }
        }
    }

    /**
     *Getter pre nemodifikovateľný list rooms. Týmto spôsobom sa neporušuje pravidlo zapúzdrenia.
     * @return Vráti nemodifikovateľný list rooms.
     */
    public List<T> getRooms() {
        return Collections.unmodifiableList(this.rooms);
    }
}

