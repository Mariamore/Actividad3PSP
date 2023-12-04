package menu;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Menu {
	
	private static SecretKey claveSimetrica;
	private static byte[] bytesFraseUsuarioCifrado;
	//Datos de los usuarios registrados
    private static Persona p1 = new Persona("Ana Medina", "1234");
    private static Persona p2 = new Persona("Álvaro López", "Lewan");
    private static Persona p3 = new Persona("Elvira Velilla", "5678");
	
	public static void main(String[] args) {
		
		//Metodo 
		seguridad();
	}
 	
	public static int pintarMenu() {
		Scanner leer= new Scanner (System.in);
		int opcion;
		
		
		System.out.println("1.-ENCRIPTAR LA FRASE");
		System.out.println("2.-DESENCRIPTAR LA FRASE");
		System.out.println("3.-SALIR");
		System.out.println("***********************************");
		opcion=leer.nextInt();
		while(opcion<1 || opcion>3) {
			System.out.println("ERROR: Del 1 al 3");
			opcion=leer.nextInt();
		}
		return opcion;
		
		
	}

		public static void encriptarFrase() {
			
			try {

				System.out.println("Bienvenido al encriptador de frases");
				System.out.println("----------------------------------");
				System.out.println("Introduzca la frase:");
				
				Scanner sc = new Scanner(System.in);
				
				String fraseUsuario = sc.nextLine();
				
				//Obtenemos el generador de claves (KeyGenerator)
				KeyGenerator generador = KeyGenerator.getInstance("AES");//Algoritmo que utilizamos
				
				//Clave simetrica, la generamos con el generador
				claveSimetrica = generador.generateKey();
		
				//Objeto para cifrar
				
				Cipher cifrador = Cipher.getInstance("AES");
				//Configuramos el cifrador para que use la clave simetrica generada anterirmente para encriptar
				
				cifrador.init(Cipher.ENCRYPT_MODE, claveSimetrica);
				
				//Convertimos a bytes el String introducido por el usuario
				byte[] bytesFraseUsuario = fraseUsuario.getBytes();
				
				//Una vez convertido a bytes el mensaje original, pasamos a cifrarlo
				bytesFraseUsuarioCifrado = cifrador.doFinal(bytesFraseUsuario);
				
				//Comprobacion de la encriptacion 

				String mensajeCifrado = new String(bytesFraseUsuarioCifrado);
				//System.out.println("Mensaje Usuario: " + fraseUsuario);
				System.out.println("Mensaje Cifrado: " + mensajeCifrado);
				
				//Capturamos excepciones
			} catch (GeneralSecurityException  gse) {
				System.out.println("Error:.... "+ gse.getMessage());
				System.out.println("Error: No se puede desencriptar una frase sin haber encriptado otra");
			}	
		}
		
		

		private static void desencriptarFrase() {
			
			try {
				System.out.println("Bienvenido al desencriptador de frases");
				System.out.println("----------------------------------");
				Cipher descifrador = Cipher.getInstance("AES");
				descifrador.init(Cipher.DECRYPT_MODE, claveSimetrica);
				byte[] bytesFraseUsuarioDescifrar = descifrador.doFinal(bytesFraseUsuarioCifrado);
				
				System.out.println("Su frase descifrada es: " + new String (bytesFraseUsuarioDescifrar));
				
			} catch (GeneralSecurityException  gse) {
				System.out.println("Error:.... "+ gse.getMessage());
			}	
			
		}
		
		public static  void seguridad() {
		//Introducimos los datos por la consola dentro de un bucle, para contabilizar el número de intentos.
			
			int intentosFallidos=0;
			
			while (intentosFallidos < 3) {
				
			Persona user1;
			
			Scanner sc = new Scanner(System.in);
			System.out.println("USUARIO: ");
			String nomUser = sc.nextLine();
			
			System.out.println("CONTRASEÑA: ");
			String passUser = sc.nextLine();
			
		//hasheamos lo que nos han intrducido por consola y pasamos a compararlo
			
			try {	
			
				MessageDigest md = MessageDigest.getInstance("SHA-512");
		        md.update(passUser.getBytes());
		        byte[] bytesHashPassIntroducido = md.digest();
		        String hashPassIntroducido = Base64.getEncoder().encodeToString(bytesHashPassIntroducido);
		        
		     //compararamos los hashes llamando al metodo, si es igual, desplieqa el menu, si no puedes volver a intentarlo hasta 3 veces
		        boolean validar = compararHashes(hashPassIntroducido);
		        
		        if (validar) {
			            System.out.println("Validación correcta para " + nomUser);
			            System.out.println("Bienvenido..." + nomUser);
			        	System.out.println("************* MENU ****************");
			    		
			    		int opcion=0;
			    		do {
			    			
			    		//pintamos menu
			    		opcion=pintarMenu();
			    		
			    		switch (opcion) {
			    		case 1:
			    			//método para encriptar la frase
			    			encriptarFrase();
			    			break;
			    		case 2:
			    			//método para desencriptar la frase 
			    			if(claveSimetrica==null) {
			    				System.out.println("Antes debe encriptar alguna frase");
			    			}else {	
			    				desencriptarFrase();
			    			}
			    			break;	
			    		case 3:
			    			System.out.println("Fin del programa");
			    			 System.exit(0); //Finaliza el programa
			    			break;
			    		default:
			    			System.out.println("opcion erronea");
			    			}	
			    		}while(opcion !=3);
		            
		        } else {
                    System.out.println("Credenciales incorrectas");
                    intentosFallidos++;
                    System.out.println("Intentos fallidos: " + intentosFallidos);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        //Si superamos los 3 intentos fallidos, se cierra el programa
        System.out.println("Demasiados intentos fallidos. Saliendo del programa....");
        //terminamos con la ejecucion del programa
        System.exit(0);
    }
				
		
		
		
		private static String hashearContraseña(String contraseña) {
		
			 //Hasheamos las contraseñas de los objetos creados
			 try {
		            MessageDigest md = MessageDigest.getInstance("SHA-512");
		            byte[] bytesHash = md.digest(contraseña.getBytes());
		            return Base64.getEncoder().encodeToString(bytesHash);//convertir el array de bytes en texto y poder compararlos
		        } catch (NoSuchAlgorithmException e) {
		            e.printStackTrace();
		        }
		        return "";
	}
		
	//Validar contraseñas 
		
		 private static boolean compararHashes(String hashPassIntroducido) {
		        //hashPass1, hashPass2 y hashPass3 son los hashes generados previamente
		        //Con el método hashearContraseña para las contraseñas p1, p2 y p3.

			 	String hashPass1 = hashearContraseña(p1.getContraseña());
		        String hashPass2 = hashearContraseña(p2.getContraseña());
		        String hashPass3 = hashearContraseña(p3.getContraseña());
		        
		        //Comprobacion de hash introducido con el previamente generado para ver que son iguales
		        System.out.println("Hash de la contraseña introducida: " + hashPassIntroducido);
		        System.out.println("Hash de la contraseña p1: " + hashPass1);
		        System.out.println("Hash de la contraseña p2: " + hashPass2);
		        System.out.println("Hash de la contraseña p3: " + hashPass3);

		        return hashPassIntroducido.equals(hashPass1) || hashPassIntroducido.equals(hashPass2) || hashPassIntroducido.equals(hashPass3);
		    }
		    
	 
}
