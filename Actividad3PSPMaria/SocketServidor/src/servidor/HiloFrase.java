package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import dao.Usuarios;

public class HiloFrase implements Runnable{
	
	private Thread hilo;
	private Socket socketAlCliente;
	private static int numCliente = 0;
	
	public HiloFrase(Socket socketAlCliente){
		numCliente++;
		
		hilo = new Thread(this, "Cliente -> " + numCliente);
		this.socketAlCliente = socketAlCliente;
		hilo.start();
	}
	
	@Override
	public void run() {
		System.out.println("Estableciendo comunicación con hilo " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBf = null;
		String fraseCifrada = "";
		
		try {
			
			salida = new PrintStream(socketAlCliente.getOutputStream());
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			entradaBf = new BufferedReader(entrada);
			Usuarios u = new Usuarios();

			String info = "";
			
			boolean continuar = true;
			
			while(continuar) {
				
				info = entradaBf.readLine();
				System.out.println("Recibida información de login");
				
				String[] infoPartes = info.split("-");
				
				String nombreUsuario = infoPartes[0];
				String pwdUsuario = infoPartes[1];
				
//				if(!u.comprobarNombreUsuario(nombreUsuario) || !u.comprobarPassword(pwdUsuario) ) {
					
					if(!u.comprobarNombreUsuario(nombreUsuario)) {
						System.out.println("Nombre incorrecto");
						salida.println("Nombre incorrecto");
						
					} else if (!u.comprobarPassword(pwdUsuario)){
						System.out.println("Contraseña incorrecta");
						salida.println("Contraseña incorrecta");
						
//					} 
					
					
				} else {
					
					salida.println("Usuario loggeado");
					System.out.println("Usuario Loggeado");
					
					
					KeyGenerator generador = KeyGenerator.getInstance("AES");
					System.out.println("Servidor -> Obtenido generador de clave");
					SecretKey claveSimetrica = generador.generateKey();
					System.out.println("Servidor -> Obtenida la clave");
					Cipher cifrador;
					cifrador = Cipher.getInstance("AES");
					System.out.println("Servidor -> Obtenido cifrador/descifrador");
					
					boolean cifrar = true;
					
					while(cifrar) {
						
						info = entradaBf.readLine();
						infoPartes = info.split("---");
						
						String opcion = infoPartes[0];
						int opcionNum = Integer.valueOf(opcion);
						String frase = infoPartes[1];
						
						
						
						if (opcionNum == 3){
							System.out.println("Servidor -> el " + hilo.getName() + " ha elegido la opción " + opcionNum);
							System.out.println("Servidor -> el " + hilo.getName() + " dice " + frase);
							salida.println("FIN");
							System.out.println(hilo.getName() + " ha cerrado la comunicación");
							cifrar = false;
							continuar = false;
						} else {
							System.out.println("Servidor -> Frase a cifrar/descifrar: " + frase + " del " + hilo.getName());
							
						fraseCifrada = cifrarFrase(frase, opcionNum, cifrador, claveSimetrica);
						salida.println(fraseCifrada);
						}
 
						
					}
					
				
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public synchronized String cifrarFrase(String frase, int opcion, Cipher cifrador, SecretKey claveSimetrica) {
		
		try {
			
			if(opcion == 1) {
				System.out.println("Servidor -> Configurado el cifrador");
				cifrador.init(Cipher.ENCRYPT_MODE, claveSimetrica);
				byte[] bytesFraseOriginal = frase.getBytes();
				System.out.println("Servidor -> Cifrando la frase original");
//				byte[] bytesFraseCifrada = cifrador.doFinal(bytesFraseOriginal);
//				String fraseCifrada = new String(bytesFraseCifrada);
//				System.out.println("Cliente -> Frase cifrada: " + fraseCifrada);
				byte[] cipherText = cifrador.doFinal(bytesFraseOriginal);
				String fraseCifrada = new String(Base64.getEncoder().encodeToString(cipherText));
				System.out.println("Servidor -> Frase cifrada: " + fraseCifrada);
	            return fraseCifrada;
				
				
			} else  {
				System.out.println("Servidor -> Configurado el descifrador");
				cifrador.init(Cipher.DECRYPT_MODE, claveSimetrica);
				System.out.println("Servidor -> descifrando...");
				byte[] cipherText = cifrador.doFinal(Base64.getDecoder().decode(frase));
				String fraseDescifrada = new String(cipherText);
				System.out.println("Servidor -> frase descifrada: " + fraseDescifrada);
				return fraseDescifrada;
			}
			
		
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		
	}

}
