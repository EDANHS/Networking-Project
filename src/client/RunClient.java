package client;
import java.util.List;
import java.util.Scanner;

import common.User;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {
	public static void main(String args[]) throws RemoteException, NotBoundException {
		Client client = new Client();
		client.startClient();
		menu(client);
	}
	
	public static void menu(Client client) throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n******* Menú Cliente *******");
            System.out.println("1. Buscar persona por nombre");
            System.out.println("2. Agregar persona");
            System.out.println("3. Mostrar todas las personas");
            System.out.println("4. Salir");
            System.out.print("Ingrese su opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea después de nextInt()

            switch (opcion) {
                case 1:
                    
                    break;
                case 2:
                    
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese un número válido.");
            }
        } while (opcion != 4);

        scanner.close();
    }
}