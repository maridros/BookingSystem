import java.io.*; 
import java.rmi.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.rmi.server.UnicastRemoteObject;

// The client class, implements the client interface
public class HRClient extends UnicastRemoteObject implements CancelEventListener {

	// Constructor method, does nothing
	public HRClient() throws RemoteException{
    	super();
    }

	//function for printing usage depending on the client's call mistake
	public static void printUsage(int flag) {
		if (flag == 0) {
			System.out.println("Usage: java HRClient parameters");
			System.out.println("Possible parameters:");
			System.out.println("\tlist <hostname>");
			System.out.println("\tbook <hostname> <type> <number> <name>");
			System.out.println("\tguests <hostname>");
			System.out.println("\tcancel <hostname> <type> <number> <name>");
		}
		else if (flag == 1) {
			System.out.println("Usage: java HRClient list <hostname>");
		}
		else if (flag == 2) {
			System.out.println("Usage: java HRClient book <hostname> <type> <number> <name>");
		}
		else if (flag == 3) {
			System.out.println("Usage: java HRClient guests <hostname>");
		}
		else if (flag == 4) {
			System.out.println("Usage: java HRClient cancel <hostname> <type> <number> <name>");
		}
	}

	
	//process called for option book:
	//it calls the corresponding server function, 
	//informs the client for the result and
	//interracts with the client if something goes wrong
	private static boolean bookProcess(HRInterface hrI, String type, int number, String name) throws RemoteException {
		Scanner input = new Scanner(System.in);
		long result = number;
		boolean exitFlag = false;
		boolean successFlag = false;
		while (!exitFlag) {
			//call server function for booking
			result = hrI.bookFunction(type, number, name);
			if(result >= number) {
				//if enough rooms were available the booking succeeded 
				//and the result is the total cost
				System.out.println("Reservation succeeded");
				System.out.println("Total cost: " + result + " euros");
				exitFlag = true;
				successFlag = true;
			}
			else if(result == 0) {
				//if there are no rooms,
				//there is no point to ask guest if they want to book only them.
				//instead a message of failure is displayed:
				System.out.println("Reservation failed, because there are no type " + type + " rooms available.");
				exitFlag = true;
			}
			else {
				//if there are rooms but not enough
				//guest is asked if they want to book only them
				//if yes, then the booking process (while loop) will be 
				//reapeated for the new number of rooms
				System.out.println("There are only " + result + " type " + type + " rooms.");
				System.out.println("Do you want to book only them? (y/n)");
				String answer = input.nextLine();
				if(answer.equals("n"))
					exitFlag = true;
				else
					number = (int) result;
					
			}
		}
		return successFlag;
	}

	//function called from server to inform interested clients for cancellations
	public void roomsCancelled(String type, int availability)  throws RemoteException {
		System.out.println("Due to a cancellation there are " + availability + " " + type +" type rooms available now.");
	}


	public static void main(String argv[]) {
		if(argv.length < 1) {
			printUsage(0);
			System.exit(0);
		}

		String lookUpName = "";
		HRInterface hrI;
		Scanner input = new Scanner(System.in);

		if(argv.length > 1) {

			lookUpName = "rmi://" + argv[1] + "/HRServer";
		}

		if(argv[0].equals("list")) {
			if(argv.length < 2) {
				printUsage(1);
				System.exit(0);
			}
			try {
				//call method for option list
				hrI = (HRInterface) Naming.lookup(lookUpName);
				String rooms = hrI.listFunction();
				System.out.println(rooms);
				
			} catch(Exception e) {
				System.err.println("HRServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
		}
		else if(argv[0].equals("book")) {
			if(argv.length < 5) {
				printUsage(2);
				System.exit(0);
			}
			try {
				//call method for option book
				hrI = (HRInterface) Naming.lookup(lookUpName);
				String type = argv[2];
				int number = Integer.parseInt(argv[3]);
				String name = argv[4];
				boolean success = bookProcess(hrI, type, number, name);
				if (!success) {
					System.out.println("Would you like to be informed if there is a cancel for type " + type + " rooms? (y/n)");
					String answer = input.nextLine();
					if(answer.equals("y")) {
						HRClient hrC = new HRClient();
						hrI.addCancelEventListener(hrC, type);
						System.out.println("Listener registered");
					}
				}
				
			} catch(Exception e) {
				System.err.println("HRServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
			
		}
		else if(argv[0].equals("guests")) {
			if(argv.length < 2) {
				printUsage(3);
				System.exit(0);
			}
			try {
				//call method for option guests
				hrI = (HRInterface) Naming.lookup(lookUpName);
				String clients = hrI.guestsFunction();
				System.out.println(clients);
				
			} catch(Exception e) {
				System.err.println("HRServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
		}
		else if(argv[0].equals("cancel")) {
			if(argv.length < 5) {
				printUsage(4);
				System.exit(0);
			}
			try {
				//call method for option cancel
				hrI = (HRInterface) Naming.lookup(lookUpName);
				String result = hrI.cancelFunction(argv[2], Integer.parseInt(argv[3]), argv[4]);
				System.out.println(result);
				
				
			} catch(Exception e) {
				System.err.println("HRServer exception: "+ e.getMessage());
				e.printStackTrace();
			}
		}
		else {
			printUsage(0);
			System.exit(0);
		}

	}

}

