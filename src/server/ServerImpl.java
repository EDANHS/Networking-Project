package server;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import common.InterfaceServer;
import common.User;

public class ServerImpl implements InterfaceServer{
	
	private ArrayList<User> database;
	private String url;
	private String username;
	private String password;
	
	public ServerImpl(String url, String username, String password) throws RemoteException, FileNotFoundException, SQLException, InterruptedException {		
		UnicastRemoteObject.exportObject(this, 0);
		this.database = new ArrayList<User>();
		this.url = url;
		this.username = username;
		this.password = password;

		init_data();
	}

	private void init_data() throws SQLException, InterruptedException {
		Connection connection = null;
		Statement query = null;
		ResultSet results = null;
		String name, email, password, currency, birthdate;
		double total_amount;
		int idUser;

		while(true) {
			connection = connect_data_base(this.url, this.username, this.password);
			if (connection != null) break;

			TimeUnit.SECONDS.sleep(2);
		}

		query = connection.createStatement();
		results = query.executeQuery("SELECT * FROM user");

		while (results.next()) {
			idUser = results.getInt("IDUser");
			name = results.getString("Name") + " " + results.getString("Last Name");
			birthdate = results.getString("Birthdate");
			total_amount = results.getInt("Total Money");
			email = results.getString("Email");
			password = results.getString("Password");
			currency = results.getString("Currency");

			database.add(new User(idUser, name, email, password, birthdate, currency, total_amount));
		}

		connection.close(); 
	}
	
	private Connection connect_data_base(String url, String username, String password) {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la base de datos");
			return null;
		}

		return connection;
	}

	@Override
	public ArrayList<User> getUsers() throws RemoteException {
		return database;
	}

	@Override
	public User log_in(String email, String password) throws RemoteException, SQLException {
		Connection connection = connect_data_base(this.url, this.username, this.password);
		PreparedStatement query;
		ResultSet result;
		
		String sql = "SELECT * FROM user";
		
		query = connection.prepareStatement(sql);
		
		result = query.executeQuery();
		
		while(result.next()) {
			String aux_email = result.getString("Email");
			String aux_password = result.getString("Password");
			
			if (email.equals(aux_email)) {
				if (password.equals(aux_password)) {
					
					User new_user = new User(result.getInt("IDUser"),
											result.getString("Name") + " " + result.getString("Last Name"),
											result.getString("Email"),
											result.getString("Password"),
											result.getString("Birthdate"),
											result.getString("Currency"),
											result.getDouble("Total Money"));
					connection.close();
					return new_user;
				}
			}
		}
		connection.close();
		return null;
	}

	@Override
	public Boolean make_transaction(int origin_id, int dest_id, double total, String currency_type) throws RemoteException, SQLException {
				boolean flag = false;
		
				Connection connection = connect_data_base(this.url, this.username, this.password);
				PreparedStatement query;
				ResultSet result;
				
				String sql = "SELECT * FROM user WHERE IDUser = ?";
				
				query = connection.prepareStatement(sql);
				query.setInt(1, origin_id);
				result = query.executeQuery();
				
				// Guardar el usuario auxiliar
				User aux_user = new User(result.getInt("IDUser"),
										result.getString("Name"),
										result.getString("Birthdate"),
										result.getString("Email"),
										result.getString("Password"),
										result.getString("Currency"),
										result.getDouble("Total Money"));
				
				// Obtengo el usuario de origin para ver si existe
				if (origin_id == result.getInt("IDUser")) {
					sql = "SELECT * FROM user WHERE IDUser = ?";
					query = connection.prepareStatement(sql);
					query.setInt(1, dest_id);
					result = query.executeQuery();
					
					// Obtengo el usuario destino para ver si existe
					if (dest_id == result.getInt("IDUser")) {
						if (aux_user.getTotal_amount() >= result.getDouble("Total Money")) {
							
							// Hacer la transacción hacia el destinatario
							double transaction = this.convertirPesoAMoneda(aux_user.getTotal_amount(), result.getString("Currency"));
							sql = "UPDATE user SET Total Money = ? WHERE IDUser = ?";
							query = connection.prepareStatement(sql);
							query.setDouble(1, result.getDouble("Total Money") + transaction);
							query.setInt(2, result.getInt("IDUser"));
							query.executeUpdate();
							
							// Hacer la transacción hacia el origen
							sql = "UPDATE user SET Total Money = ? WHERE IDUser = ?";
							query = connection.prepareStatement(sql);
							query.setDouble(1, result.getDouble("Total Money") - total);
							query.setInt(2, aux_user.getIdUser());
							query.executeUpdate();
							flag = true;
						}
					}
				}
				
				connection.close();
				
				if(flag) return true;
				return false;
	}

	@Override
	public void show_transactions(int id) throws RemoteException, SQLException {
		Connection connection = connect_data_base(this.url, this.username, this.password);
		PreparedStatement query;
		ResultSet results;
		
		String sql = "SELECT * FROM transaction WHERE IDSourceUser = ?";
		
		query = connection.prepareStatement(sql);
		
		results = query.executeQuery();
		results.first();		
		while(true) {
			
			int idTrans = results.getInt("IDTransaction");
			int idSource = results.getInt("IDSourceUser");
			int idDestination = results.getInt("IDDestinationUser");
			double total = results.getDouble("TotalAmount");
			String currency = results.getString("Currency");
			
			System.out.println("*****************************");
			System.out.println("Transaction ID: " + idTrans);
			System.out.println("Source ID: " + idSource);
			System.out.println("Destination ID: " + idDestination);
			System.out.println("Amount: " + total);
			System.out.println("Currency: " + currency);
			System.out.println("*****************************\n");
			
			if(results.next() == false) break;
		}
		
		connection.close();
	}

	@Override
	public Boolean add_user(int idUser, String name, String birthdate, String email, String password) throws RemoteException, SQLException {
		User new_user = new User(idUser, name,  email, password, birthdate, "USD", 0);
		
		Connection connection = connect_data_base(this.url, this.username, this.password);
		PreparedStatement query;
		
		String sql = "INSERT INTO user (`IDUser`, `Name`, `Last Name`, `Birthdate`, `Total Money`, `Email`, `Password`, `Currency`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		String[] user_name = new_user.getName().split(" ");
		query = connection.prepareStatement(sql);
		query.setInt(1, new_user.getIdUser());
		query.setString(2, user_name[0]);
		query.setString(3, user_name[1]);
		query.setString(4, new_user.getBirthdate());
		query.setDouble(5, new_user.getTotal_amount());
		query.setString(6, new_user.getEmail());
		query.setString(7, new_user.getPassword());
		query.setString(8, new_user.getCurrency());
		
		query.executeUpdate();
		
		connection.close();
		
		return database.add(new_user);
	}

	@Override
	public Boolean update_data_base() throws RemoteException, SQLException, ParseException {
		Connection connection = connect_data_base(this.url, this.username, this.password);
		PreparedStatement query;
		String sql;
		String[] user_name;
		User temp;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		sql = "INSERT INTO user (`IDUser`, `Name`, `Last Name`, `Birthdate`, `Total Money`, `Email`, `Password`, `Currency`) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
				"ON DUPLICATE KEY UPDATE " +
				"`Name` = VALUES(`Name`), `Last Name` = VALUES(`Last Name`), `Total Money` = VALUES(`Total Money`), " + 
				"`Email` = VALUES(`Email`), `Password` = VALUES(`Password`), `Currency` = VALUES(`Currency`);";

		for(int i = 0; i < database.size(); i++) {
			temp = database.get(i);
			user_name = temp.getName().split(" ");
			query = connection.prepareStatement(sql);
			java.util.Date parsedDate = dateFormat.parse(temp.getBirthdate());

			query.setInt(1, temp.getIdUser());
			query.setString(2, user_name[0]);
			query.setString(3, user_name[1]);
			query.setDate(4, new Date(parsedDate.getTime()));
			query.setDouble(5, temp.getTotal_amount());
			query.setString(6, temp.getEmail());
			query.setString(7, temp.getPassword());
			query.setString(8, temp.getCurrency());
			
			query.executeUpdate();
		}

		connection.close();
		return true;
	}
	
	@Override
	public String getDataFromApi() {
		String output = null;
		 
		try {
            // URL de la API REST
            URL apiUrl = new URL("https://mindicador.cl/api");

            // Abre la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Configura la solicitud (método GET en este ejemplo)
            connection.setRequestMethod("GET");

            // Obtiene el código de respuesta
            int responseCode = connection.getResponseCode();

            // Procesa la respuesta si es exitosa
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta del servidor
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Cierra la conexión y muestra la respuesta
                in.close();
                output = response.toString();
            } else {
                System.out.println("Error al conectar a la API. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		//Como resultado tenemos un String output que contiene el JSON de la respuesta de la API
		return output;
	}
	
	@Override
	public double getValorMonedaEnPeso(String moneda) {
		String output = getDataFromApi();
		ObjectMapper objectMapper = new ObjectMapper();
		double valor =0.0;
		
		try {
			JsonNode jsonNode = objectMapper.readTree(output);

			valor = jsonNode.get(moneda).get("valor").asDouble();
			
			
			
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	
		return valor;
	}
	
	public double convertirPesoAMoneda(double peso, String moneda) {
		double valorMoneda= getValorMonedaEnPeso(moneda);
		double result = peso/valorMoneda;
		return result;
		
	}
	
}