package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		this.server = (InterfaceServer) registry.lookup("severDePersonas");
		System.out.println("Cliente conectando");
	}
	
	public User log_in(BufferedReader br) throws SQLException, IOException {
		System.out.println("Indique su email: ");
		String email = br.readLine();
		System.out.println("Indique su contraseña: ");
		String password = br.readLine();
		
		this.user = server.log_in(email, password);
		return this.user;
	}
	
	public void show_transaction() throws RemoteException, SQLException {
		server.show_transactions(user.getIdUser());
	}
	
	public void checkAccountStatus() {
		String[] user_name = user.getName().split(" ");
		
		System.out.println("Account Status:");
        System.out.println("RUT: " + user.getIdUser());
        System.out.println("Name: " + user_name[0]);
        System.out.println("Last Name: " + user_name[1]);
        System.out.println("Email: " + user.getEmail());
        System.out.println("Birthdate: " + user.getBirthdate());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Currency: " + user.getCurrency());
        System.out.println("Total Amount: " + user.getTotal_amount());
	}
	
	public boolean performBankTransaction(BufferedReader br) throws SQLException, NumberFormatException, IOException {
		
		System.out.println("Make a transaction");

        System.out.print("Enter origin ID: ");
        int originId = Integer.parseInt(br.readLine());

        System.out.print("Enter destination ID: ");
        int destId = Integer.parseInt(br.readLine());

        System.out.print("Enter total amount: ");
        double total = Double.parseDouble(br.readLine());

        System.out.print("Enter currency type: ");
        String currencyType = br.readLine();
        
        return server.make_transaction(originId, destId, total, currencyType);
	}
	
	public boolean register(BufferedReader br) throws SQLException, IOException {
		System.out.println("indique su rut: ");
		int idUser = Integer.parseInt(br.readLine());
		System.out.println("indique su nombre (primer_nombre primer_apellido): ");
		String name = br.readLine();
		System.out.println("indique su fecha de nacimiento (aaaa-mm-dd): ");
		String birthdate = br.readLine();
		System.out.println("indique su email: ");
		String email = br.readLine();
		System.out.println("indique su contraseña: ");
		String password = br.readLine();
		
		return server.add_user(idUser, name, birthdate, email, password);
	}
	
}