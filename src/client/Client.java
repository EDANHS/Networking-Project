package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import common.InterfaceServer;
import common.User;

public class Client {
	private InterfaceServer server;
	private User user;
	
	public void startClient() throws NotBoundException, NumberFormatException, SQLException, IOException{
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);
			this.server = (InterfaceServer) registry.lookup("severDePersonas");
			System.out.println("Cliente conectando con server principal...");
		}catch(ConnectException e){
			System.out.println("Error de conexion, conectando con servidor de Respaldo...");
			cambiarAServerRespaldo();
		}
		
	}
	
	public User log_in(String email, String password) throws SQLException, RemoteException {
		this.user = server.log_in(email, password);
		return this.user;
	}
	
	public void show_transaction() throws RemoteException, SQLException {
		server.show_transactions(user.getIdUser());
	}
	
	public void checkAccountStatus() {
		
		System.out.println("Account Status:");
        System.out.println("RUT: " + user.getIdUser());
        System.out.println("Name: " + user.getName());
        System.out.println("Last Name: " + user.getLast_name());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Birthdate: " + user.getBirthdate());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Currency: " + user.getCurrency());
        System.out.println("Total Amount: " + user.getTotal_amount());
	}
	
	public boolean performBankTransaction(int ID, String email, double total) throws SQLException, NumberFormatException, IOException {
        return server.make_transaction(user.getIdUser(), ID, email, total);
	}
	
	public boolean register(int idUser, String name, String last_name, String email, String password, String birthdate, String currency, double total_amount) throws SQLException, RemoteException {
		return server.add_user(idUser, name, last_name, email, password, birthdate, currency, total_amount);
	}
	
	public boolean delete_account(int id, String email, String password) throws SQLException, RemoteException {
		return server.delete_user(id, email, password);
	}
	
	public void cambiarAServerRespaldo() throws NotBoundException, NumberFormatException, SQLException, IOException{
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1199);
			this.server = (InterfaceServer) registry.lookup("severDePersonas");
			System.out.println("Cliente conectando con server de respaldo...");
		}catch(ConnectException e) {
			System.out.println("ERROR: Cliente de respaldo no disponible");
		}
	}
	
	public boolean serverActivo() {
		return(server!=null);
	}
}