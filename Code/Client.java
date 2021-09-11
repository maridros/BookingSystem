import java.util.HashMap;

//Class for guest (client) 
//which contains their name and their reservations
public class Client {
	private String name;
	private HashMap<String, Integer> reservations;

	public Client(String name) {
		this.name = name;
		reservations = new HashMap<String, Integer>();
		reservations.put("A", 0);
		reservations.put("B", 0);
		reservations.put("C", 0);
		reservations.put("D", 0);
		reservations.put("E", 0);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setReservations(HashMap<String, Integer> reservations) {
		this.reservations = reservations;
	}
			
	public String getName() {
		return name;
	}

	public HashMap<String, Integer> getReservations() {
		return reservations;
	}
	
	public String clientToString() {
		int aNum = reservations.get("A");
		int bNum = reservations.get("B");
		int cNum = reservations.get("C");
		int dNum = reservations.get("D");
		int eNum = reservations.get("E");
		String result = "";
		result += name + ": \n";
		result += "\t- Type A rooms booked: " + aNum + "\n";
		result += "\t- Type B rooms booked: " + bNum + "\n";
		result += "\t- Type C rooms booked: " + cNum + "\n";
		result += "\t- Type D rooms booked: " + dNum + "\n";
		result += "\t- Type E rooms booked: " + eNum + "\n";
		long totalAmount = aNum * 50 + bNum * 70 + cNum * 80 + cNum * 120 + eNum * 150; 
		result += "\tTotal cost: " + totalAmount + " euros";
		return result;
	}

}	
