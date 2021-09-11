import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class HRImpl extends UnicastRemoteObject
implements HRInterface, Runnable {

	private ArrayList<Client> clients; //list of guests
	private ArrayList<Room> rooms; //list of rooms
	//list of listeners for each room type:
	private ArrayList<CancelEventListener> aCancelEventListerners;
	private ArrayList<CancelEventListener> bCancelEventListerners;
	private ArrayList<CancelEventListener> cCancelEventListerners;
	private ArrayList<CancelEventListener> dCancelEventListerners;
	private ArrayList<CancelEventListener> eCancelEventListerners;
	//flags which become true after a cancel in order to inform run()
	//that it has to notify the corresponding listeners for the cancellation
	volatile boolean aCancelEventFlag;
	volatile boolean bCancelEventFlag;
	volatile boolean cCancelEventFlag;
	volatile boolean dCancelEventFlag;
	volatile boolean eCancelEventFlag;

		
	//constructor 
	public HRImpl() throws RemoteException{
		super();
		//initialise all lists
		rooms = new ArrayList<Room>();
		clients = new ArrayList<Client>();
		aCancelEventListerners = new ArrayList<CancelEventListener>();
		bCancelEventListerners = new ArrayList<CancelEventListener>();
		cCancelEventListerners = new ArrayList<CancelEventListener>();
		dCancelEventListerners = new ArrayList<CancelEventListener>();
		eCancelEventListerners = new ArrayList<CancelEventListener>();
		
		//initialise flags to false which means there is 
		//no need for notifying CancelEventListeners for now
		aCancelEventFlag = false;
		bCancelEventFlag = false;
		cCancelEventFlag = false;
		dCancelEventFlag = false;
		eCancelEventFlag = false;

		//add existing rooms
		rooms.add(new Room("A", 30, 50));
		rooms.add(new Room("B", 45, 70));
		rooms.add(new Room("C", 25, 80));
		rooms.add(new Room("D", 10, 120));
		rooms.add(new Room("E", 5, 150));
	}

	public void run() {
		System.out.println("Notifying thread started");
		for(;;) {
			if (aCancelEventFlag) {
				System.out.println("Notifying listerners for A type rooms cancellation");
				aCancelEventFlag = false;
				notifyCancelEventListeners("A", aCancelEventListerners);
			}
			if (bCancelEventFlag) {
				System.out.println("Notifying listerners for B type rooms cancellation");
				bCancelEventFlag = false;
				notifyCancelEventListeners("B", bCancelEventListerners);
			}
			if (cCancelEventFlag) {
				System.out.println("Notifying listerners for C type rooms cancellation");
				cCancelEventFlag = false;
				notifyCancelEventListeners("C", cCancelEventListerners);
			}
			if (dCancelEventFlag) {
				System.out.println("Notifying listerners for D type rooms cancellation");
				dCancelEventFlag = false;
				notifyCancelEventListeners("D", dCancelEventListerners);
			}
			if (eCancelEventFlag) {
				System.out.println("Notifying listerners for E type rooms cancellation");
				eCancelEventFlag = false;
				notifyCancelEventListeners("E", eCancelEventListerners);
			}
		}
	}
	
	//function called for list option (returns list of rooms)
	public String listFunction() throws RemoteException {
		System.out.println("listFunction called.");
		String result = "";
		if(rooms.isEmpty())
			result = "There are no rooms";
		else
			for (Room r : rooms) {
				result += (r.roomToString() + "\n");
			}
		return result;
	}
	
	//function called for book option
	//if it succeed it returns the total price
	//else it returns the number of not booked rooms (= number of rooms asked to be booked)
	public long bookFunction(String type, int number, String name) throws RemoteException {
		long result = number;
		boolean clientFlag = false;
		System.out.println("bookFunction called.");

		for (Room r : rooms) {
			if(r.getType().equals(type))
				if(r.getAvailableRooms() >= number) {
					for (Client c : clients) {
						if(c.getName().equals(name)) {
							clientFlag = true;
							HashMap<String, Integer> reservations = new HashMap<String, Integer>();
							reservations = c.getReservations();
							reservations.put(type, reservations.get(type) + number);
							c.setReservations(reservations);
							result = number * r.getPrice();
						}
					}
					if(!clientFlag) {
						Client c = new Client(name);
						HashMap<String, Integer> reservations = new HashMap<String, Integer>();
						reservations = c.getReservations();
						reservations.put(type, reservations.get(type) + number);
						c.setReservations(reservations);
						clients.add(c);
						result = number * r.getPrice();
					}
					r.setAvailableRooms(r.getAvailableRooms() - number);
				}
				else {
					result = r.getAvailableRooms();
				}
		}
		return result;
	}	
	
	//function called for guests option (returns guests - clients list)
	public String guestsFunction() throws RemoteException {
		System.out.println("guestsFunction called.");
		String result = "";
		if(clients.isEmpty())
			result = "There are no guests";
		else
			for (Client c : clients) {
				result += (c.clientToString() + "\n");
			}
		return result;
	}
	
	//function called for cancel option
	//returns a String:
	//if it succeeded it is the rooms that the corresponding client has booked
	//else it is a message saying that cancel failed for a reason
	public String cancelFunction(String type, int number, String name) throws RemoteException {
		String answer = "Cancel failed - Rooms not found";
		boolean clientFlag = false;
		System.out.println("cancelFunction called.");
		
		
		for (Room r : rooms) 
			if(r.getType().equals(type)) {
				for (Client c : clients) 
					if(c.getName().equals(name)) {
						clientFlag = true;
						HashMap<String, Integer> reservations = new HashMap<String, Integer>();
						reservations = c.getReservations();
						if(reservations.get(type) >= number) {
							reservations.put(type, reservations.get(type) - number);
							c.setReservations(reservations);
							r.setAvailableRooms(r.getAvailableRooms() + number);
							answer = c.clientToString();
							//inform other clients for cancellation
							if (type.equals("A"))
								aCancelEventFlag = true;
							else if (type.equals("B"))
								bCancelEventFlag = true;
							else if (type.equals("C"))
								cCancelEventFlag = true;
							else if (type.equals("D"))
								dCancelEventFlag = true;
							else if (type.equals("E"))
								eCancelEventFlag = true;							
						}
					}
				if (!clientFlag)
					answer = "Cancel failed - Guest not found";
				}
		return answer;
	}


	// Notify Listeners for cancellations. For all listeners in the list, 
    // call roomsCanceled() method which is declared in the client interface
	private synchronized void notifyCancelEventListeners(String type, ArrayList<CancelEventListener> cancelEventListerners) {
		ArrayList<CancelEventListener> toRemove = new ArrayList<CancelEventListener>();
		int availability = 0;
		//get updated availability of rooms of this type 
		//so that it will be send to clients via method roomsCancelled
		for (Room r : rooms) 
			if(r.getType().equals(type))
				availability = r.getAvailableRooms();
		for (CancelEventListener ceListener : cancelEventListerners) {
			try {
				ceListener.roomsCancelled(type, availability);
			}
			catch (RemoteException aInE) {
				// If the listener is not responding and the call to the listener's method fails, 
               	// remove listener from the list
           		System.out.println("Removing listener");
           		toRemove.add(ceListener);
			}
		}
		for (CancelEventListener toRemListener : toRemove) {
			if (cancelEventListerners.contains(toRemListener)) {
				cancelEventListerners.remove(toRemListener);
				System.out.println("Removed listener for " + type + " type rooms");
			}
		}
	}


	//function for adding listener to the corresponding list, according to the room type
	public synchronized void addCancelEventListener(CancelEventListener ceListener, String type) throws RemoteException {
		if (type.equals("A")) 
			aCancelEventListerners.add(ceListener);
		else if (type.equals("B")) 
			bCancelEventListerners.add(ceListener);
		else if (type.equals("C")) 
			cCancelEventListerners.add(ceListener);
		else if (type.equals("D")) 
			dCancelEventListerners.add(ceListener);
		else if (type.equals("E")) 
			eCancelEventListerners.add(ceListener);
		System.out.println("Added new listener for " + type + " type rooms");
	}

	//function for removing listener from the corresponding list
	public synchronized void removeCancelEventListener(CancelEventListener ceListener, String type) throws RemoteException {
		boolean successFlag = false;
		if (type.equals("A"))
			if (aCancelEventListerners.remove(ceListener))
				successFlag = true;
		else if (type.equals("B"))
			if (bCancelEventListerners.remove(ceListener))
				successFlag = true;
		else if (type.equals("C"))
			if (cCancelEventListerners.remove(ceListener))
				successFlag = true;
		else if (type.equals("D"))
			if (dCancelEventListerners.remove(ceListener))
				successFlag = true;
		else if (type.equals("E"))
			if (eCancelEventListerners.remove(ceListener))
				successFlag = true;
		if(successFlag)
			System.out.println("Removed listener for " + type + " type rooms");
		else
			System.out.println("Listener is already removed");
	}
	
	
}

