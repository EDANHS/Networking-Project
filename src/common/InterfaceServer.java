package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public interface InterfaceServer extends Remote {
	public Boolean log_in(String email, String password) throws RemoteException;
	public Boolean make_transaction(String origin_id, String dest_id, int total, String currency) throws RemoteException;
	public void show_transactions(String id) throws RemoteException;
	public ArrayList<User> getUsers() throws RemoteException;
	public Boolean add_user(int idUser, String name, String birthdate, String email, String password) throws RemoteException;
	public String getDataFromApi() throws RemoteException;
	public double getValorMonedaEnPeso(String moneda) throws RemoteException;
	public Boolean update_data_base() throws RemoteException, SQLException, ParseException;
}
