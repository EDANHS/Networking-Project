package server;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
	
}