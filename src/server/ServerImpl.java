package server;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
	
	
	public ServerImpl() throws RemoteException, FileNotFoundException {
		UnicastRemoteObject.exportObject(this, 0);
		this.database = new ArrayList<User>();
		dataInicial();
	}
	
	public void dataInicial() {
		this.database.add(new User(null, null, null, null, 0));
		this.database.add(new User(null, null, null, null, 0));
		this.database.add(new User(null, null, null, null, 0));
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
	public Boolean add_user(String name, String rut, String email, String password) {
		// TODO Auto-generated method stub
		return null;
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
	public double getPrecioDolar() {
		String output = getDataFromApi();
		ObjectMapper objectMapper = new ObjectMapper();
		double valor =0.0;
		
		try {
			JsonNode jsonNode = objectMapper.readTree(output);

			valor = jsonNode.get("dolar").get("valor").asDouble();
			
			
			
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return valor;
	}
	
}