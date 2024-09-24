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
	
	 public Client () {
		 try {
	            Socket socket = new Socket("localhost", 12000);
	            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public void enviar(String msg) throws IOException {
		 writer.write(msg);
         writer.write("\r\n");
         writer.flush();
	 }
	 
	 public String receber () throws IOException {
		 return reader.readLine();
	 }

}
