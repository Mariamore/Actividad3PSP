package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import servidor.Usuario;

public class Usuarios implements IntGestionUsuarios {
	
	private ArrayList<Usuario> usuarios;
	
	public Usuarios() {
		usuarios = new ArrayList<>();
			try {
				cargarDatos();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	//Creamos el método cargarDatos() para poder tener cargados 3 usuarios en el servidor
	private void cargarDatos() throws NoSuchAlgorithmException {
		
		Usuario u1 = new Usuario("Ironman", "Iron1234.");
		Usuario u2 =  new Usuario("Spiderman", "Spider5678.");
		Usuario u3 =  new Usuario("Thor", "Th91011.");
		usuarios.add(u1);
		usuarios.add(u2);
		usuarios.add(u3);
		
		//Como el servidor ha de guardar la contraseña hasheada, procedemos a hashearlas
		
		//Creamos un objeto MessageDigest a través del método estático 
		//getInstance() al que se le pasa el tipo de algoritmo que vamos a 
		//utilizar, en este caso, SHA-512 
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		
		for (Usuario u: usuarios) {
			byte[] pwd = u.getPassword().getBytes();
			md.update(pwd);
			byte[] resumenHash = md.digest();
			
		//Lo pasamos a Base64 para así poder enviarlo y guardarlo mejor
			
			String pwdHashBase64 = Base64.getEncoder().encodeToString(resumenHash);
			u.setPassword(pwdHashBase64);
		}
		
	}
	
	/**
	 * Método que compueba si el nombre del usuario existe en la lista
	 * @param nombreUsuario el nombre de usuario a comprobar
	 * @return devuelve true si existe un usuario con ese nombre, devuelve 
	 * false en caso contrario 
	 */
	@Override
	public boolean comprobarNombreUsuario(String nombreUsuario) {
		Usuario u2 = new Usuario();
		u2.setNombre(nombreUsuario);
		ArrayList<Usuario> aux = new ArrayList<>();
		for (Usuario u1 : usuarios) {
			if(u1.getNombre().equals(u2.getNombre())) {
				aux.add(u1);
			} 
		}
		
		if (aux.isEmpty()) {
			return false;
		} else {
			return true;
		}
		
	}
	
	
	/**
	 * Método que compueba si la contraseña existe en la lista
	 * @param password la contraseña a comprobar
	 * @return devuelve true si existe un usuario con esa contraseña, devuelve 
	 * false en caso contrario 
	 */
	@Override
	public boolean comprobarPassword(String password){
		Usuario uComprobar = new Usuario();
		
		uComprobar.setPassword(password);
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] pwd = password.getBytes();
			md.update(pwd);
			byte[] resumenHash = md.digest();
			String pwdHashBase64 = Base64.getEncoder().encodeToString(resumenHash);
			uComprobar.setPassword(pwdHashBase64);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayList<Usuario> aux = new ArrayList<>();
		for (Usuario u1 : usuarios) {
			if(u1.getPassword().equals(uComprobar.getPassword())) {
				aux.add(u1);
			} 
		}
		
		if (aux.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
