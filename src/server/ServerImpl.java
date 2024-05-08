package server;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

	public void init_data() throws SQLException, InterruptedException {
		Connection connection = null;
		Statement query = null;
		ResultSet results = null;
		String rut, name, email, password;
		int total_amount;

		while(true) {
			connection = connect_data_base(this.url, this.username, this.password);
			if (connection != null) break;

			TimeUnit.SECONDS.sleep(2);
		}

		query = connection.createStatement();
		results = query.executeQuery("SELECT * FROM user");

		while (results.next()) {
			rut = results.getString("rut");
			name = results.getString("name");
			email = results.getString("email");
			password = results.getString("password");
			total_amount = results.getInt("total_amount");

			database.add(new User(rut, name, email, password, total_amount));
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
	public Boolean log_in(String email, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean make_transaction(String origin_id, String dest_id, int total, String currency_type)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void show_transactions(String id) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean add_user(String name, String rut, String email, String password) throws RemoteException {
		User new_user = new User(rut, name, email, password, 0);

		return database.add(new_user);
	}

	@Override
	public Boolean add_data() throws RemoteException, SQLException {
		Connection connection = connect_data_base(this.url, this.username, this.password);
		PreparedStatement query;
		ResultSet result;
		String sql;
		
		for(int i = 0; i < database.size(); i++) {
			User temp = database.get(i);
			
			sql = "SELECT * FROM user WHERE rut = ?";
			query = connection.prepareStatement(sql);
			query.setString(1, temp.getRut());
			result = query.executeQuery();

			if(result.next()) {
				String updateSql = "UPDATE user SET name = ?, email = ?, password = ?, total_amount = ? WHERE rut = ?";

				query = connection.prepareStatement(updateSql);
				query.setString(1, temp.getName());
				query.setString(2, temp.getEmail());
				query.setString(3, temp.getPassword());
				query.setDouble(4, temp.getTotal_amount());
				query.setString(5, temp.getRut());
			} 
			else {
				String insertSQL = "INSERT INTO user (rut, name, email, password, total_amount) VALUES (?, ?, ?, ?, ?)";

				query = connection.prepareStatement(insertSQL);
				query.setString(1, temp.getRut());
				query.setString(2, temp.getName());
				query.setString(3, temp.getEmail());
				query.setString(4, temp.getPassword());
				query.setDouble(5, temp.getTotal_amount());
			}

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