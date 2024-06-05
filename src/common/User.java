package common;

import java.io.Serializable;

public class User implements Serializable {
	private int idUser;
	private String name;
	private String last_name;
	private String email;
	private String password;
	private String currency;
	private String birthdate;
	private double total_amount;
	
	private static final long serialVersionUID = 1L;
	
	public User(int idUser, String name, String last_name, String email, String password, String birthdate, String currency, double total_amount) {
		this.idUser = idUser;
		this.name = name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.total_amount = total_amount;
		this.birthdate = birthdate;
		this.currency = currency;
	}
	
	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public String getCurrency() {
		return currency;
	}

	public int getIdUser() {
		return idUser;
	}

	public String getName() {
		return name;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getTotal_amount() {
		return total_amount;
	}
}
