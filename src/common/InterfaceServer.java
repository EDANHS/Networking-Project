package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceServer extends Remote {
	public Boolean log_in(String email, String password) throws RemoteException;
	public Boolean make_transaction(String origin_id, String dest_id, int total, String currency_type) throws RemoteException;
	public void show_transactions(String id) throws RemoteException;
	public ArrayList<User> getUsers() throws RemoteException;
	public Boolean add_user(String name, String rut, String email, String password);
}
