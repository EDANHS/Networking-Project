package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public interface InterfaceServer extends Remote {
	public User log_in(String email, String password) throws RemoteException, SQLException;
	public Boolean make_transaction(int origin_id, int dest_id, double total, String currency) throws RemoteException, SQLException;
	public void show_transactions(int id) throws RemoteException, SQLException;
	public ArrayList<User> getUsers() throws RemoteException;
	public Boolean add_user(int idUser, String name, String birthdate, String email, String password) throws RemoteException, SQLException;
	public String getDataFromApi() throws RemoteException;
	public double getValorMonedaEnPeso(String moneda) throws RemoteException;
	public Boolean update_data_base() throws RemoteException, SQLException, ParseException;
}
