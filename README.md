# Hotel Management System
This is a simple Hotel Management System implemented in Java. It allows users to book rooms, calculate booking costs, and save bookings to a CSV file.
## Features
* Display a list of available rooms and their details.
* Allow users to book a room by providing the room ID.
* Booking of Standard and Deluxe rooms
* Additional services available for Standard and Deluxe rooms (sauna, massage, hot tub)
* Prevent booking if the room is already booked.
* Calculation of total booking cost based on the number of days and selected services.
* Error handling for incorrect room IDs and invalid number of days.
* Save booking details to a CSV file.
* Load and save user data to a CSV file.
## Usage
1. Run the HotelManagement class to start the application.
2.   Enter the user's first name and last name.
3.   Choose a room to book by entering the room ID.
4.   Repeat step 3 to book additional rooms.
5.   Enter "n" when prompted to book another room to complete the booking process.
6.   The bookings will be saved to a file named bookings.csv.
## File Structure
The project consists of the following files:
* **HotelManagementApp.java**: The main class responsible for running the Hotel Management System.
*  **HotelManagement.java**: The class responsible for managing the hotel, handling user interactions, and saving bookings.
*  **Room.java**:An abstract class representing a room with common properties and behaviors.
*  **StandardRoom.java and DeluxeRoom.java**: Subclasses of Room representing specific types of rooms with additional features.
*  **RoomDB.java**: A database class for managing the list of available rooms.
*  **Bookable.java and Cancellable.java**: Interfaces for bookable and cancellable items.
*  **User.java**: A class representing a user with first and last names.
*  **Booking.java**: A class representing a booking with details such as user, room ID, number of days, and total cost.