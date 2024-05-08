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

import common.InterfaceServer;
import common.User;

public class RunServer {
	public static void main(String args[]) throws NotBoundException, NumberFormatException, IOException {
		InterfaceServer server = new ServerImpl();
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
	
	public static void agregarDato(BufferedReader br, InterfaceServer server) throws IOException {
		System.out.print("Ingrese el nombre de la persona: ");
        String nombre = br.readLine();
        System.out.print("Ingrese la edad de la persona: ");
        int edad = Integer.parseInt(br.readLine());
        
        server.add_user(nombre, nombre, nombre, nombre);
	}
	
	public static void mostrarDatos(InterfaceServer server) throws RemoteException {
		List<User> personas = server.getUsers();
		System.out.println("*************************************\n");
		for (User p : personas) {
			
			System.out.println("Nombre: " + p.getName());
			System.out.println();
		}
		System.out.println("*************************************");
	}
	
	@SuppressWarnings("unused")
	private static void cerrarServer(InterfaceServer server) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(1099);
		registry.unbind("severDePersonas");
		UnicastRemoteObject.unexportObject(server, true);
		System.out.println("Cerrando servidor...");
	}
}
