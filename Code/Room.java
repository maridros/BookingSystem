//class for room-types 
//which contains type name, availability and price
public class Room {
	private String type;
	private int availableRooms;
	private long price;
	
	public Room(String type, int availableRooms, long price) {
		this.type = type;
		this.availableRooms = availableRooms;
		this.price = price;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAvailableRooms(int availableRooms) {
		this.availableRooms = availableRooms;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public int getAvailableRooms() {
		return availableRooms;
	}

	public long getPrice() {
		return price;
	}

	public String roomToString() {
		String result = availableRooms + " " + type + " type rooms - " + price + " euros per night";
		return result;
	}
}
