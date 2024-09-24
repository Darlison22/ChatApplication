package group.chatting.client.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
	
    BufferedReader reader;
    BufferedWriter writer;

	// Quando um client é instanciado, então uma conexão com o servidor é aberta para escuta e escrita de dados.
	 public Client () {
		 try {
	            Socket socket = new Socket("localhost", 12000);
	            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Converte um fluxo de bytes do socket em fluxo de caracteres e armazena em writer (escrita)
	            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Converte um fluxo de byter do socker em fluxo de caracteres e armazena em reader (leitura)
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public void enviar(String msg) throws IOException {
		 writer.write(msg); // o método writer.write() armazena os dados em buffer, antes de enviar
        	 writer.write("\r\n"); 
         	 writer.flush(); // este método é reponsavel por enviar a mensagem armazena ao seu destino, no caso, o servidor.
	 }
	 
	 public String receber () throws IOException {
		 return reader.readLine(); // recebe texto vindo do servidor (linha \n\r)
	 }

}
