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

import common.InterfaceServer;
import common.User;

public class RunServer {
	public static void main(String args[]) throws NotBoundException, NumberFormatException, IOException, SQLException, InterruptedException, ClassNotFoundException {
		InterfaceServer server = new ServerImpl("jdbc:mysql://localhost:3306/networking_proyect", "root", "1234");
		//Lista de objetos que el cliente puede acceder
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("severDePersonas", server);
		System.out.println("Servidor Arriba");
		show_menu(server);
		cerrarServer(server);
	}
	
	public static void show_menu(InterfaceServer server) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int opcion;

        do {
            System.out.println("***********Menú Server: ********");
            System.out.println("[1] Agregar dato");
            System.out.println("[2] Mostrar datos");
            System.out.println("[3] Salir");
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
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 3);

       br.close();
    }
	
	public static void agregarDato(BufferedReader br, InterfaceServer server) throws IOException {
		String rut;
		String name;
		String email;
		String password;

		System.out.println("Ingrese su nombre: ");
		name = br.readLine();
		System.out.println("Ingrese su rut:");
		rut = br.readLine();
		System.out.println("Ingrese su email: ");
		email = br.readLine();
		System.out.println("Ingrese su contraseña: ");
		password = br.readLine();
        
        server.add_user(name, rut, email, password);
	}
	
	public static void mostrarDatos(InterfaceServer server) throws RemoteException {
		List<User> users = server.getUsers();
		System.out.println("*************************************\n");
		for (User p : users) {
			
			System.out.println("Nombre: " + p.getName());
			System.out.println("Rut: " + p.getRut());
			System.out.println("Total: " + p.getTotal_amount());
		}
		System.out.println("*************************************");
	}
	
	private static void cerrarServer(InterfaceServer server) throws RemoteException, NotBoundException, SQLException {
		Registry registry = LocateRegistry.getRegistry(1099);
		registry.unbind("severDePersonas");
		UnicastRemoteObject.unexportObject(server, true);
		System.out.println("Cerrando servidor...");
		server.add_data();
	}
}
