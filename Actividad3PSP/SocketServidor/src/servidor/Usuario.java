package servidor;

import java.util.Objects;

public class Usuario {
	
	private String nombre, password;
	
	public Usuario(String nombre, String password) {
		super();
		this.nombre = nombre;
		this.password = password;
	}
	
	public Usuario() {
		super();
	}

	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	@Override
	public int hashCode() {
		return Objects.hash(nombre, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(nombre, other.nombre) && Objects.equals(password, other.password);
	}
	
	
}
