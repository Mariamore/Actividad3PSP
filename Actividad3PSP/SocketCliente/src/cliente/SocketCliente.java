package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SocketCliente {
	
	public static final int PUERTO = 2018;
	public static final String IP_SERVER = "localhost";
	
	public static void main (String []args) {
		
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
		
		try(Scanner sc = new Scanner(System.in);){
			
			System.out.println("Cliente -> Esperando que el servidor acepte la conexión");
			Socket socketAlServidor = new Socket();
			
			socketAlServidor.connect(direccionServidor);
			System.out.println("Cliente -> Conexión establecida por el puerto " + PUERTO + " a " + IP_SERVER);
			
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader entradaBf = new BufferedReader(entrada);
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			
			boolean continuar = true;
			
			do {
				
				String respuesta = "";
				
				for (int i = 1; i <= 3; i ++) {
					
				String infoLogin = login();
				salida.println(infoLogin);
				System.out.println("Cliente -> Esperando validación de usuario y contraseña");
				respuesta = entradaBf.readLine();
				
				if(respuesta.equals("Usuario loggeado")) {
					
					System.out.println("Cliente -> El servidor dice " + respuesta);
					
					int opcion = menu();
					
					while(opcion !=3) {
						
						if (opcion == 1){
					
							System.out.println("Escribe la frase a encriptar:");
							String frase = sc.nextLine();
							String info = String.valueOf(opcion) + "---" + frase;
							System.out.println(info);
							salida.println(info);
							System.out.println("Cliente esperando respuesta...");
							String respuesta1 = entradaBf.readLine();
							System.out.println("Cliente -> El servidor responde: " + respuesta1);
							
							int opcion1 = menu();
							
							if(opcion1 == 2) {
								info = String.valueOf(opcion1) + "---" + respuesta1;
								System.out.println(info);
								salida.println(info);
								System.out.println("Cliente esperando respuesta");
								String respuesta2 = entradaBf.readLine();
								System.out.println("Cliente -> El servidor responde: " + respuesta2);
						
							}else {
								continue;
							}
						
						} if (opcion == 2) {
							 System.out.println("No hay frase para encriptar");
						} 
						opcion = menu();
						
					}
					String info = String.valueOf(opcion) + "---" + "fin";
					salida.println(info);
					System.out.println("Cliente -> Opcion elegida " + opcion);
					System.out.println("Cliente -> Saliendo de la aplicación,");
					break;
				}else {
					
					System.out.println("Cliente -> El servidor dice " + respuesta + " intentos restantes: " +(3-i));
					if (i == 3) {
						System.out.println("Intentos agotados");
						break;
					}
				}
				
				
				}
				System.out.println("FIN DE LA APLICACIÓN");
				continuar = false;
				
			} while(continuar);
			
			socketAlServidor.close();
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.out.println("Error -> Tipo de dato introducido incorrecto");
			e.printStackTrace();
		}
	}
	
	public static String login() {
		
		Scanner sc = new Scanner(System.in);
			System.out.println(" ***** LOGIN DE USUARIO ***** ");
			System.out.println(" **************************** ");
			System.out.println("Introduce tu nombre de usuario:");
			String nombreUsuario = sc.nextLine();
			System.out.println("Introduce tu contraseña:");
			String password = sc.nextLine();
			String infoLogin = nombreUsuario + "-" + password;
			return infoLogin;
		
	}
	
	public static int menu() {
		Scanner sc = new Scanner(System.in);
		int opcion = 0;
		
		 System.out.println(" ------ MENÚ ------- ");
		 System.out.println("1. Encriptar frase");
		 System.out.println("2. Desencriptar frase");
		 System.out.println("3. Salir");
		 
		 while(opcion > 3 || opcion < 1) {
			 System.out.println("Introduce el número de la opción elegida:");
			 opcion = sc.nextInt();
			 
		 }
		 return opcion;
	}
}