# Hotel Management System (SK)
The Hotel Management System is a Java application that allows users to book hotel rooms. It provides a simple command-line interface for users to select available rooms, make bookings, and save the booking details.
## Funkcie
* Zobrazenie zoznamu dostupných izieb a ich podrobností.
* Umožnenie používateľom rezervovať izbu pomocou identifikátora izby.
* Zabránenie rezervácie, ak je izba už obsadená.
* Uloženie údajov o rezervácii do súboru CSV.
* Načítanie a uloženie údajov používateľa do súboru CSV.


## Usage (in Slovak)
1. Stiahnite projekt a spustite HotelManagement.java
2.   Zadaj svoje meno a priezvisko do promptu.
3.   Zadaj typ miestnosti (e.g. StandardRoom5) ak si prajete namiesto toho vstúpiť do privilegovaného režimu, zadajte frázu "Enter Privileged Mode"
4.   Riaď sa výpismi na terminále.
5.   Ak si prajete ukončiť booking, napíšte do terminálu "n"
6.   Následne sa bookingy uložia do bookings.csv súboru.
7.   Každú metódu zakomentoval nech je možné si to zobraziť aj v javadocs a porozumieť kódu.


## Privilegovaný mód

Možnosť správy bookingov pomocou textového rozhrania alebo GUI.

<>Základné príkazy<>

- delete – zmazanie políčka riadok, stĺpec  (e.g. ak mám na 2,2 Priezvisko Hraško tak ho tým vymažem.)
- showData – vypíše textovú reprezentáciu súboru bookings.csv
- exit – opustenie privilegovaného režimu a vrátenie sa späť ako bežný užívateľ.
- adminPanel – po zadaní príkazu sa zobrazí GUI loginPanel ktorý obsahuje 2 vstupné 
polia pre meno a heslo. V tomto prípade pre test to Sú: admin a password
 následne sa spustí admin panel ako GUI ktorý obsahuje informácie o všetkých 
rezerváciách a užívateľoch spolu s tromi tlačidlami slúžiacimi na pridanie, odstránenie 
a aktualizovanie dát. Po zavretí okna sa administrátor vracia späť do privilegovaného 
režimu v textovom rozhraní, z ktorého môže nasledovne odísť príkazom exit.

## File Structure
The project consists of the following files:
* **HotelManagement.java**: The main class that runs the Hotel Management System.
* **Room.java**: An abstract class representing a room with common attributes and methods.
*  **StandardRoom.java and DeluxeRoom.java**: Concrete classes representing specific types of rooms.
*  **RoomDB.java**: A database class for managing the list of available rooms.
*  **Bookable.java and Cancellable.java**: Interfaces for bookable and cancellable items.
*  **User.java**: A class representing a user with first name, last name, and ID.
*  **UserManager.java**: A class for managing user data and saving it to a file.
*  **Booking.java**: A class representing a booking with user and room details.
