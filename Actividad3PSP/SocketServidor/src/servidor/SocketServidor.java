package servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServidor {

	public static final int PUERTO = 2018;
	
	public static void main(String []args) {
		System.out.println(" ******** APLICACIÓN DEL SERVIDOR ******** ");
		System.out.println(" ***************************************** ");
		
		int peticion = 0;
		
		try (ServerSocket servidor = new ServerSocket();){
			
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			
			servidor.bind(direccion);
			System.out.println("Servidor -> esperando petición por el puerto " + PUERTO);
			while(true) {
				
				Socket socketAlCliente = servidor.accept();
				System.out.println("Servidor -> petición número " + ++peticion + " recibida.");
				new HiloFrase(socketAlCliente);
			}
			
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada o salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		} 
	}
}
