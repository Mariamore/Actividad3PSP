package entidad;

public class Usuario {
	
	//Creamos la clase Usuario para poder crear usuarios predeterminados.
	
	private String nombre;
	private String password;
	
	//Generamos constructores
	
	public Usuario() {
		super();
	}

	public Usuario(String nombre, String password) {
		super();
		this.nombre = nombre;
		this.password = password;
	}
	
	//Generamos getters y setters

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
	
	//Sobreescribimos toString para poder ver la información del usuario.

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", password=" + password + "]";
	}
	
}
