package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.sql.SQLException;

public class RunClient {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static void main(String args[]) throws NotBoundException, NumberFormatException, SQLException, IOException {
		Client client = new Client();
		client.startClient();
		if(client.serverActivo()) {
			menu(client);
		}
		br.close();
		
	}
	
	public static void menu(Client client) throws NumberFormatException, SQLException, IOException, NotBoundException {
        int opcion;

        do {
	        System.out.println("******Bienvenido Invocador********");
	        System.out.println("[1] Iniciar sesión");
	        System.out.println("[2] Registrase");
	        System.out.println("[3] Borrar cuenta");
	        System.out.println("[4] Salir");
	        opcion = Integer.parseInt(br.readLine());
	        switch(opcion) {
		        case 1:
		        	if(log_in(client)) {
		        		System.out.println("Inicio de sesión exitoso...\n\n");
		        		menu_inicio_sesion(client);
		        		
		        	}else {
		        		System.out.println("Error en la email o contraseña");
		        	}
		        case 2:
		        	if(register(client)) {
		        		System.out.println("Usuario creado exitosamente");
		        	}else {
		        		System.out.println("Hubo un problema al crear su cuenta ;c ...");
		        	}
		        case 3:
		        	if(delete_account(client)) {
		        		System.out.println("Cuenta borrada con éxito...");
		        	}else {
		        		System.out.println("Hubo un problema al eliminar su cuenta");
		        	}
		        case 4:
		        	System.out.println("Saliendo del programa...");
		        default:
		        	System.out.println("Opción inválida. Intente de nuevo.");
	        }
	        
        }while(opcion!=3);
        
    }	
	
	public static boolean log_in(Client client) throws IOException, SQLException {
		System.out.println("Indique su email: ");
		String email = br.readLine();
		System.out.println("Indique su contraseña: ");
		String password = br.readLine();
		
		
		if(client.log_in(email, password) != null) {
			return true;
		}
		return false;
	}
	
	public static boolean delete_account(Client client) throws IOException, SQLException {
		
		System.out.println("********** Delete account ***********");
		
		System.out.println("Indique su ID: ");
		int id = Integer.parseInt(br.readLine());
		System.out.println("Indique su email: ");
		String email = br.readLine();
		System.out.println("Indique su contraseña: ");
		String password = br.readLine();
		
		System.out.println("*************************************");
		
		System.out.print("Esta seguro de borrar su cuenta [Y/N]: ");
		String letter = br.readLine();
		if(letter.equals("Y")) {
			if(client.delete_account(id, email, password)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean register(Client client) throws NumberFormatException, IOException, SQLException {
		
		System.out.println("indique su ID: ");
		int idUser = Integer.parseInt(br.readLine());
		
		System.out.println("Indique su nombre: ");
		String name = br.readLine();
		
		System.out.println("Indique su apellido: ");
		String last_name = br.readLine();
		
		System.out.println("Indique su email: ");
		String email = br.readLine();
		
		System.out.println("Indique su contraseña: ");
		String password = br.readLine();
		
		System.out.println("Indique su fecha de nacimiento (aaaa-mm-dd): ");
		String birthdate = br.readLine();
		
		System.out.println("Indique su tipo de moneda: ");
		String currency = br.readLine();
		
		System.out.println("Indique su dinero: ");
		double total_amount = Double.parseDouble(br.readLine());
		
		if(client.register(idUser, name, last_name, email, password, birthdate, currency, total_amount)) {
			return true;
		}
		return false;
	}
	
	public static void menu_inicio_sesion(Client client) throws NumberFormatException, IOException, SQLException, NotBoundException {
        int opcion;
		do {
            System.out.println("*********** Menú Cliente: ********");
            System.out.println("[1] Mostrar Transacciones");
            System.out.println("[2] Realizar Transacción");
            System.out.println("[3] Mostrar Perfil");
            System.out.println("[4] Salir");
            System.out.println("Ingrese su opción: ");
            opcion = Integer.parseInt(br.readLine());

            switch (opcion) {
                case 1:
					try {
                		client.show_transaction();
					} 
					catch(Exception e) {
						System.out.println("Servidor caido, cambiando al más cercano");
						client.cambiarAServerRespaldo();
					}
					
					break;
                case 2:
					try {
                		realizar_transaccion(client);
					}
					catch(Exception e) {
						System.out.println("Servidor caido, cambiando al más cercano");
						client.cambiarAServerRespaldo();
					}

                    break;
                case 3:
					try {
                		client.checkAccountStatus();
					}
					catch(Exception e) {
						System.out.println("Servidor caido, cambiando al más cercano");
						client.cambiarAServerRespaldo();
					}
					
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 5);
	}
	
	public static boolean realizar_transaccion(Client client) throws NumberFormatException, IOException, SQLException {
		System.out.println("******* Haciendo una transacción ********");
		System.out.println("Ingrese los datos de la persona que desea");
		System.out.println("transferir....");


        System.out.print("Ingrese ID destino: ");
        int id = Integer.parseInt(br.readLine());

        System.out.print("Email destino: ");
        String email = br.readLine();

        System.out.print("Monto a transferir: ");
        double total = Double.parseDouble(br.readLine());
        
        System.out.println("****************************************");
        
        System.out.println("Realizando transacción...");
        if(client.performBankTransaction(id, email, total)) {
        	System.out.println("Transacción realizada con éxito...");
        	return true;
        }
        return false;
	}
}