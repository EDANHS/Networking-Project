package common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public interface InterfaceServer extends Remote {
	public User log_in(String email, String password) throws RemoteException, SQLException;
	public void show_transactions(int id) throws RemoteException, SQLException;
	public ArrayList<User> getUsers() throws RemoteException;
	public boolean add_user(int idUser, String name, String last_name, String email, String password, String birthdate, String currency, double total_amount) throws RemoteException, SQLException;
	public String getDataFromApi() throws RemoteException;
	public double getValorMonedaEnPeso(String moneda) throws RemoteException;
	public Boolean update_data_base() throws RemoteException, SQLException, ParseException;
	public boolean delete_user(int id, String email, String password) throws RemoteException, SQLException;
	public boolean requestMutex() throws RemoteException;
	public void releaseMutex() throws RemoteException;
	public Boolean make_transaction(int id_source, int id_destination, String email, double total) throws RemoteException, SQLException, IOException;
}
