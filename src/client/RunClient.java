package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class RunClient {
	public static void main(String args[]) throws NotBoundException, NumberFormatException, SQLException, IOException {
		Client client = new Client();
		client.startClient();
		if (client.serverActivo()){
			menu(client);
		}
		else {
			System.out.println("No hay conexión con servidor; Apagando...");
		}
		
	}
	
	public static void menu(Client client) throws NumberFormatException, SQLException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int opcion;
        	
        if(client.log_in(br) == null) {
        	System.out.println("El usuario no existe");
        	System.out.println("Desea crear una nueva cuenta?");
        	System.out.println("[1] Si [2] No");
        	opcion = Integer.parseInt(br.readLine());
        	
        	if(opcion == 1) client.register(br);
        	else return;
        }
        
        do {
            System.out.println("*********** Menú Cliente: ********");
            System.out.println("[1] Mostrar Transacciones");
            System.out.println("[2] Realizar Transacción");
            System.out.println("[3] Mostrar Perfilr");
            System.out.println("[4] Mostrar valor del Euro");
            System.out.println("[5] Salir");
            System.out.print("Ingrese su opción: ");
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
                	client.show_transaction();
                    break;
                case 2:
                	client.performBankTransaction(br);
                    break;

                case 3:
                	client.checkAccountStatus();
                    break;
                case 4:
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
}