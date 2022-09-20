import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    JLabel heading= new JLabel("Server Area");
    JTextArea messegeArea=new JTextArea();
    JTextField messegeInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //Constructor
    public Server(){
        try {
            // starts server and waits for a connection

            server=new ServerSocket(7770);  
            System.out.println("Server is ready to accept connection");  
            System.out.println("waiting....");  
            socket=server.accept(); 
            //takes  input from terminal
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            //send output to the socket
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            StartReading();
            // StartWriting();   
        } catch (Exception e) {
            System.out.println("Connection closed");
        }

    }
    public void createGUI(){
        this.setSize(500,600);
        this.setVisible(true);
        this.setTitle("Sever Messeger");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        heading.setFont(font);
        messegeInput.setFont(font);
        messegeArea.setFont(font);
        messegeArea.setEditable(false);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane js=new JScrollPane(messegeArea);
        this.add(js,BorderLayout.CENTER);
        this.add(messegeInput,BorderLayout.SOUTH);
        heading.setIcon(new ImageIcon("l.png"));


    }
    public void handleEvents(){
        messegeInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    // System.out.println("you have presed button ");
                    String contentToSend=messegeInput.getText();
                    if(contentToSend.equals("exit")){
                        messegeInput.setEnabled(false);

                    }
                    else{
                    messegeArea.append("me : "+contentToSend+"\n");
                    }
                    out.println(contentToSend);
                    out.flush();
                    messegeInput.setText("");
                    messegeInput.requestFocus();
                }
                
            }

        });
    }

    public void StartReading(){
        Runnable r1=()->{
            System.out.println("reader Started...");
                try{
                    while(true){

                String str=br.readLine();
                if(str.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this, "Client  terminated the chat");
                    messegeInput.setEnabled(false);
                    socket.close();
                    break;

                }
                // System.out.println("client : " + str);
                messegeArea.append("Client : " + str+ "\n");
            }
             
            }
            catch (Exception e) {
                System.out.println("Connection closed");

            }


        };
        new Thread(r1).start();

    }

    public void StartWriting(){
        Runnable r2=()->{
            System.out.println("Writer Started...");
            try {

            while (!socket.isClosed()) {
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                    
                }
                
            }
            catch (Exception e) {
                System.out.println("Connection closed");;
            }

        };
        new Thread(r2).start();

    }



    public static void main(String[] args) {
        System.out.println("This is server... going to start server");
        new Server();
    }
}