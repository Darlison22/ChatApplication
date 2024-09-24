package group.chatting.application;

import javax.swing.*;
import javax.swing.border.*;

import group.chatting.client.server.Client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class UserOne implements ActionListener, Runnable { //runnable: receber e exibir mensagens no chat
    
    JTextField text;
    JPanel a1; //mensagens do chat são exibidas
    static Box vertical = Box.createVerticalBox(); //usado para organizar componentes verticalmente
    static JFrame f = new JFrame(); //janela principal
    static DataOutputStream dout;
    static Client client;
    
    String name = "Hadassa";
    
    UserOne() {
    	
    	client = new Client();
    
        f.setLayout(null); //deixa aberto para definição de posição e tamanho
        
        JPanel p1 = new JPanel(); //barra do título
        p1.setBackground(new Color(200, 162, 200)); 
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        //imagem seta
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        
        //metodo chamado para quando o usuário clica na JLabel (seta)
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        //imagem do grupo
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/mirzapur.png"));
        Image i5 = i4.getImage().getScaledInstance(60,60, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40, 5, 60, 60);
        p1.add(profile);
        
        //imagem dos três pontos
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel morevert = new JLabel(i15);
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);

        JLabel name = new JLabel("Grupo POO");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);
        
        JLabel status = new JLabel("Hadassa, Dassu, Bonetti");
        status.setBounds(110, 35, 160, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        //onde as mensagens serão exibidas
        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setBackground(Color.WHITE);
        f.add(a1);

        //criação do campo que permite digitar mensagens 
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        //botão de enviar
        JButton send = new JButton("Enviar");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(200, 162, 200)); 
        send.setForeground(Color.WHITE);
        //chamando o método actionPerformed
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        //definições da janela 
        f.setSize(450, 700);
        f.setLocation(20, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
        
    }
    //quando o botão enviar é clicado
    public void actionPerformed(ActionEvent ae) {
        try {
            //mensagem formatada em html
            String out = "<html><p>" + name + "</p><p>" + text.getText() + "</p></html>";
            //formatação com mensagem e horário
            JPanel p2 = formatLabel(out);

            //definindo a1 como borderlayout para poder organizar como preferir
            a1.setLayout(new BorderLayout());
            //criação do painel da mensagem
            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(Color.WHITE);
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            a1.add(vertical, BorderLayout.PAGE_START); //adiciona ao topo

            try {
            	client.enviar(out); //chamar metodo enviar do cliente e passar a mensagem formatada
            } catch (Exception e) {
                e.printStackTrace(); 
            }

            text.setText(""); //limpar o campo do texto
            //atualização da janela
            f.repaint();
            f.invalidate();
            f.validate();   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //formatação da mensagem e hrário
    public static JPanel formatLabel(String out) {
        //panel criado para conter a mensagem e o horário
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //formatação da mensagem
        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 15, 0, 50));
        
        panel.add(output);
        //obtenção da hora
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //formatar modelo 24:00
        //exibição da hora formatada
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        panel.add(time);
        
        return panel;
    }
    
    public void run() {
        try {
            String msg = "";
            while(true) {
                msg = client.receber(); // aguardando receber mensagem do servidor
                if (msg.contains(name)) {
                    continue;
                }
                //formatar a mensagem recebida
                JPanel panel = formatLabel(msg);
                //criação do painel da mensagem
                JPanel left = new JPanel(new BorderLayout());
                left.setBackground(Color.WHITE);
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                a1.add(vertical, BorderLayout.PAGE_START);
                
                f.repaint();
                f.invalidate();
                f.validate();   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        UserOne one = new UserOne();
        Thread t1 = new Thread(one);
        t1.start();
    }
}
