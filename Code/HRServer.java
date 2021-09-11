import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class HRServer {
	public static void main(String argv[]) {
		try { //special exception handler for registry creation
			LocateRegistry.createRegistry(1099); 
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			//do nothing, error means registry already exists
			System.out.println("java RMI registry already exists.");
		}

		try {
			HRImpl hrI = new HRImpl();
			Naming.rebind("rmi://localhost/HRServer", hrI);
			Thread lThread = new Thread(hrI);
			lThread.start();
		} catch(Exception e) {
			System.out.println("HRServer: "+e.getMessage());
			e.printStackTrace();
		}
	}
}
