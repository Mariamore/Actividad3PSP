package aplicacion;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import entidad.Usuario;

public class Application {
	
	//Creamos los atributos necesarios para codificar y descodificar sin cambiar de clave.
	
	private static KeyGenerator generador;
	private static SecretKey clave;
	private static Cipher cifrador;
	private static Cipher descifrador;

	public static void main(String[] args) {
		
		try (Scanner sc = new Scanner(System.in)) {
			//Creamos listaAuxiliar para descubrir si existe el usuario
			List<Usuario> listaAuxiliar = new ArrayList<>();
			System.out.println("Bienvenido a Encriptador de Frases.");
			System.out.println("Por favor, introduzca su nombre de usuario:");
			String usuario = sc.nextLine();
			System.out.println("Introduzca su contraseña:");
			String password = sc.nextLine();
			//Usamos el método usuarioPassword por primera vez
			listaAuxiliar = usuarioPassword(usuario, password);
			//Si no coinciden los datos, listaAuxiliar estará vacío
			if (listaAuxiliar.isEmpty()) {
				System.out.println("Datos de acceso incorrectos.");
				System.out.println("Por favor, introduzca su nombre de usuario:");
				usuario = sc.nextLine();
				System.out.println("Introduzca su contraseña:");
				password = sc.nextLine();
				//Usamos el método usuarioPassword por segunda vez
				listaAuxiliar = usuarioPassword(usuario, password);
				//Si no coinciden los datos, listaAuxiliar estará vacío
				if(listaAuxiliar.isEmpty()) {
					System.out.println("Datos de acceso incorrectos.");
					System.out.println("Por favor, introduzca su nombre de usuario:");
					usuario = sc.nextLine();
					System.out.println("Introduzca su contraseña:");
					password = sc.nextLine();
					//Usamos el método usuarioPassword por tercera y última vez
					listaAuxiliar = usuarioPassword(usuario, password);
					//Si no coinciden los datos se cierra la aplicación.
					if(listaAuxiliar.isEmpty()) {
						System.out.println("Datos de acceso incorrectos.");
						System.out.println("Cerrando aplicación...");
					}
				}
			}
		}
	}
	
	public static List<Usuario> cargarUsuarios() {
		//Guardaremos los usuarios en una lista
		List<Usuario> listaUsuarios = new ArrayList<>();
		//Para esta prueba obtenemos manualmente los hash de los password en Base 64 a través de una página web externa.
		//Así garantizamos que nuestra aplicación esté obteniendo los hash correctamente.
		
		// Creamos tres usuarios. En comentario escribimos sus contraseñas originales
		// user1 - Camisa
		Usuario usuario1 = new Usuario("user1","8S/MK65gyS1ysI6o+GPNLliHcVN7xzEahBjWPXBQIG/6xpp/9QZZXIoBLU4/iEmaS1iJHE85i3vBN9gKEJFKeQ==");
		listaUsuarios.add(usuario1);
		// user2 - Zapato
		Usuario usuario2 = new Usuario("user2","5XHgoLMX0DIOtRz77Aev7QYXpNUhyIIAjatHnN5jbiKavdMHZIIl86kiNHarhF3xXYruKPeAvGDX5DVQBn1iUQ==");
		listaUsuarios.add(usuario2);
		// user3 - Pantalon
		Usuario usuario3 = new Usuario("user3","iYRVMIhXRcD3+IE37bN2uzbI5icE1f+OjCVNuSYQZeZgIWJjJqHssheZbokYnronShmSuT68NFMOOjCqAhG7Yg==");
		listaUsuarios.add(usuario3);
		return listaUsuarios;
	}
	
	public static String codificarHash(String sinEncriptar) {
		//Recibimos el password y obtenemos su código hash
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] passwordBytes = sinEncriptar.getBytes();
			md.update(passwordBytes);
			byte[] hashBytes = md.digest();
			String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);
			return hashBase64;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Usuario> usuarioPassword(String usuario, String password) {
		//Usaremos una lista auxiliar para añadir o quitar el contacto a devolver
		List<Usuario> listaAuxiliar = new ArrayList<>();
		//Cargamos la lista de usuarios predefinidos
		List<Usuario> listaUsuarios = cargarUsuarios();
		//Recorremos la lista buscando el nombre y viendo si el hash de la contraseña
		//coincide con el hash que tenemos guardado
		for(int i=0; i < listaUsuarios.size(); i++) {
			String usuarioLista = listaUsuarios.get(i).getNombre();
			if(usuarioLista.equalsIgnoreCase(usuario)) {
				listaAuxiliar.add(listaUsuarios.get(i));
				String passwordLista = listaUsuarios.get(i).getPassword();
				//Obtenemos el hash de la contraseña recogida
				String passwordCodificado = codificarHash(password);
				//Si coinciden abriremos menu()
				if(passwordLista.equalsIgnoreCase(passwordCodificado)) {
					System.out.println("Bienvenido, " + usuarioLista);
					menu();
				//Si no coinciden, borraremos el usuario de la lista, quedando vacía
				}else {
					listaAuxiliar.remove(listaUsuarios.get(i));
				}
			}
		}
		return listaAuxiliar;
	}
	
	public static void menu() {
		
		try (Scanner sc = new Scanner(System.in)) {
			String opcion, frase, fraseEncriptada = null, fraseDesencriptada;
			//El menú se imprimirá una vez y se repetirá hasta que salgamos con la opción 3
			do {
				System.out.println("********** MENÚ **********");
				System.out.println("1 - Encriptar frase");
				System.out.println("2 - Desencriptar frase");
				System.out.println("3 - Salir");
				System.out.println("Por favor, escriba la opción deseada:");
				opcion = sc.nextLine();
				switch(opcion) {
					case "1":
						System.out.println("Escriba la frase a encriptar:");
						frase = sc.nextLine();
						try {
							//Usaremos método encriptar y guardamos el mensaje en fraseEncriptada
							fraseEncriptada = encriptar(frase);
							System.out.println("La frase se ha encriptado y guardado en memoria.");
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						break;
					case "2":
						if(clave==null) {
							//Si no tenemos clave es porque todavía no hemos encriptado nada previamente.
							System.out.println("Antes de desencriptar debe encriptar una frase con la opción 1.");
						}else {
							System.out.println("Desencriptando la frase guardada en memoria...");
							try {
								//Usamos el método desencriptar y guardamos el mensaje en fraseDesencriptada
								fraseDesencriptada = desencriptar(fraseEncriptada);
								System.out.println("La frase desencriptada es: " + fraseDesencriptada);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						break;
					case "3":
						//Evitamos que se imprima "Opción no válida"
						break;
					default:
						System.out.println("Opción no válida");
				}
			//Saldremos del bucle con la opción 3
			} while (!opcion.equals("3"));
			System.out.println("Aplicación cerrada. Gracias por utilizar nuestro servicio de encriptación.");
		}
		
	}
	
	public static String encriptar(String mensaje) throws Exception {
		//Preparamos el KeyGenerator con el algoritmo "AES"
		generador = KeyGenerator.getInstance("AES");
		//Generamos una clave, que será la misma para todo el proceso
		clave = generador.generateKey();
		//Preparamos el Cipher con el algoritmo "AES"
		cifrador = Cipher.getInstance("AES");
		//Lo ponemos en modo encriptación y le pasamos la clave
		cifrador.init(Cipher.ENCRYPT_MODE, clave);
		//Pasamos el mensaje recibido a Bytes
		byte[] fraseBytes = mensaje.getBytes();
		//Codificamos el mensaje en Bytes
		byte[] fraseEncriptadaBytes = cifrador.doFinal(fraseBytes);
		//Pasamos el mensaje codificado a Base 64 y lo guardamos en el String fraseEncriptadaBase64 
		String fraseEncriptadaBase64 = Base64.getEncoder().encodeToString(fraseEncriptadaBytes);
		//Devolvemos la frase encriptada en Base 64
		return fraseEncriptadaBase64;
	}
	
	public static String desencriptar(String mensajeBase64) throws Exception{
		//Preparamos el Cipher de descifrar con el algoritmo AES
		descifrador = Cipher.getInstance("AES");
		//Ponemos el Cipher en modo desencriptación y le pasamos la clave
		descifrador.init(Cipher.DECRYPT_MODE, clave);
		//Pasamos el mensaje recibido en Base 64 a Bytes
		byte[] mensajeBase64Bytes = mensajeBase64.getBytes();
		//Decodificamos desde Base 64 en Bytes
		byte[] mensajeCodificado = Base64.getDecoder().decode(mensajeBase64Bytes);
		//Decodificamos la frase en Bytes
		byte[] fraseDesencriptadaBytes = descifrador.doFinal(mensajeCodificado);
		//Pasamos la frase de Bytes a String
		String fraseDesencriptada = new String(fraseDesencriptadaBytes);
		//Devolvemos la frase en String
		return fraseDesencriptada;	
	}

}
