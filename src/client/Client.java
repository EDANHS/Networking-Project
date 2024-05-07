package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import common.InterfaceServer;
import common.User;

public class Client {
	private InterfaceServer server;
	
	public void startClient() throws RemoteException, NotBoundException{
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		this.server = (InterfaceServer) registry.lookup("severDePersonas");
		System.out.println("Cliente conectando");
	}
	
	
}