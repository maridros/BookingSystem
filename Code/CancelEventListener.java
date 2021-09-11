import java.rmi.Remote;
import java.rmi.RemoteException;

// This interface's method is implemented by the client application used by the server 
// application to notify the client for cancellations.
public interface CancelEventListener extends Remote
{
    void roomsCancelled(String type, int availability) throws RemoteException;
}
