package server;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.ParseException;

import common.InterfaceServer;
import common.User;

public class RunServer {
	private static String name_server = "North Corea";
	private static int port = 1099;
	private static String url = "jdbc:mysql://localhost:3306/money transaction system";
	private static String username = "root";
	private static String password = "";
	
	public static void main(String args[]) throws NotBoundException, NumberFormatException, IOException, SQLException, InterruptedException, ClassNotFoundException, ParseException {
		

		InterfaceServer server = new ServerImpl(name_server,url, username, password);
		//Lista de objetos que el cliente puede acceder
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind("severDePersonas", server);
		System.out.println(name_server + " server up...");
		show_menu(server);
		cerrarServer(server);
	}
	
	public static void show_menu(InterfaceServer server) throws NumberFormatException, IOException, SQLException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int opcion;

        do {
            System.out.println("***********Menú Server: ********");
            System.out.println("[1] Agregar dato");
            System.out.println("[2] Mostrar datos");
            System.out.println("[3] Mostrar valor del Dolar");
            System.out.println("[4] Mostrar valor del Euro");
            System.out.println("[5] Salir");
            System.out.print("Ingrese su opción: ");
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
                    agregarDato(br, server);
                    break;
                case 2:
                    mostrarDatos(server);
                    break;

                case 3:
                    System.out.println("Valor del Dólar: "+ server.getValorMonedaEnPeso("dolar"));
                    break;
                case 4:
                    System.out.println("Valor del Euro: " + server.getValorMonedaEnPeso("euro"));
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 5);

       br.close();
    }
	
	public static void agregarDato(BufferedReader br, InterfaceServer server) throws IOException, SQLException {
		String name, last_name, birthdate, email, password, currency;
		double total_amount;
		int idUser;
		
		System.out.println("Ingrese su rut:");
		idUser = Integer.parseInt(br.readLine());
		
		System.out.println("Ingrese su nombre: ");
		name = br.readLine();
		
		System.out.println("Ingrese su apellido: ");
		last_name = br.readLine();
		
		System.out.println("Ingrese su email: ");
		email = br.readLine();
		
		System.out.println("Ingrese su contraseña: ");
		password = br.readLine();
		
		System.out.println("Ingrese su fecha de cumpleaños (aaaa-mm-dd)");
		birthdate = br.readLine();
		
		System.out.println("Ingrese su tipo de moneda: ");
		currency = br.readLine();
		
		System.out.println("Ingrese su dinero total: ");
		total_amount = Double.parseDouble(br.readLine());
        
		server.add_user(idUser, name, last_name, email, password, birthdate, currency, total_amount);
	}
	
	public static void mostrarDatos(InterfaceServer server) throws RemoteException {
		List<User> users = server.getUsers();
		System.out.println("*************************************\n");
		for (User p : users) {
			
			System.out.println("Nombre: " + p.getName());
			System.out.println("Rut: " + p.getIdUser());
			System.out.println("Total: " + p.getTotal_amount());
		}
		System.out.println("*************************************");
	}
	
	private static void cerrarServer(InterfaceServer server) throws RemoteException, NotBoundException, SQLException, ParseException {
		Registry registry = LocateRegistry.getRegistry(port);
		registry.unbind("severDePersonas");
		UnicastRemoteObject.unexportObject(server, true);
		System.out.println("Cerrando servidor...");
		server.update_data_base();
	}
}
