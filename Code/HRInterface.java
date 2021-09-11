import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface HRInterface extends Remote {
	public String listFunction() throws RemoteException;
	public long bookFunction(String type, int number, String name) throws RemoteException;
	public String guestsFunction() throws RemoteException;
	public String cancelFunction(String type, int number, String name) throws RemoteException;
	void addCancelEventListener(CancelEventListener ceListener, String type) throws RemoteException;
	void removeCancelEventListener(CancelEventListener ceListener, String type) throws RemoteException;
}
