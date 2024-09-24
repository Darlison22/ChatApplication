package group.chatting.client.server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable {
    
    Socket socket;
    
    //atributo estatico, ou seja, é compartilhado por todas as instancias de Server
    public static Vector client = new Vector(); // vetor de envio de dados aos clientes
    
    //O Server recebe o objeto socket que representa a conexão com um cliente e armazena na variavel socket da classe
    public Server (Socket socket) {
        try {
            this.socket = socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //O metodo run será executado sempre que o Servidor aceitar uma nova conexão
    public void run() {
        try {
        	
        	//lê as mensagens dos clientes
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            
          //envia mensagem a todos os clientes conectados
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
            
            //Adiciona o meio de comunicação com um novo cliente ao array de clientes
            Server.client.add(writer);
            
            while(true) {
                String data = reader.readLine().trim();
                System.out.println("Received " + data);
                
                for (int i = 0; i < client.size(); i++) {
                    try {
                        BufferedWriter bw = (BufferedWriter) client.get(i);
                        bw.write(data);
                        bw.write("\r\n");
                        bw.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * As threads são essencias para que haja comunicação simultanea entre os clientes
     * pois, cada conexão com um cliente gera uma thread que roda o metodo run() da interface runnable
     * Esse metodo, por sua vez é reponsavel por escutar um cliente e enviar mensagens a todos os outros
     * de forma paralela, havendo assim, multiplas conexões funcionando de forma fluida e continua.
     * */


    public static void main(String[] args) throws Exception {
        ServerSocket s = new ServerSocket(12000);
        while(true) {
            Socket socket = s.accept(); // aceita a conexão de um cliente
            Server server = new Server(socket); // cria um objeto Server para o cliente conectado
            Thread thread = new Thread(server); // cria uma nova thread para o cliente
            thread.start(); // inicia a thread
        }
    }

}
