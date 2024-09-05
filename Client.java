import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {
    Socket socket;
    BufferedReader br; // data reading:
    PrintWriter out; // data writing:

    // Declaring components for GUI using swing:

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput = new JTextField();  
    private Font font = new Font("Roboto", Font.PLAIN,16); 

    public Client(){
        try {
            // System.out.println("Request sent to server::");
            // socket = new Socket("192.168.32.115",7777);
            // System.out.println("Established connection");
            // br = new BufferedReader( new InputStreamReader( socket.getInputStream()));  // data andr lene ka kaam ;
            // out = new PrintWriter(socket.getOutputStream()); // data baahr fekne ka kaam ;

        createGUI();
        handleEvents();
        startReading();
        startWriting();

        } catch (Exception e) {
            
            e.printStackTrace();
        }


    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
                // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
                System.out.println(e.getKeyCode());
                // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            } 

        });    
        // System.out.println(messageInput);     

        }
private void createGUI(){
    this.setTitle("Client Messager[END]");
    this.setSize(300,300);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //coding for components
    heading.setFont(font);
    messageArea.setFont(font);
    messageInput.setFont(font);
    heading.setIcon(new ImageIcon("java waala project")); // ye nhi aa rha
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    // setting the frame layout:
    this.setLayout(new BorderLayout());
    //adding the componets to frame:
    this.add(heading,BorderLayout.NORTH); 
    this.add(messageArea,BorderLayout.SOUTH);
    this.setVisible(true);
}

    public void startReading(){
        // thread to read the data
    
        Runnable r1=()->{
    
            System.out.println("reader started......");

            while(true){
             try{
                    String msg = br.readLine();
                    if(msg.equals("Terminate")){
                    System.out.println("Server terminated the chat ");
                    JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                    messageInput.setEnabled(false);
                    break;
                } 
               // System.out.println("Server :" + msg );
               messageArea.append("Server : "+msg+"\n");
            }
        
                catch (Exception e) {
                    
                    e.printStackTrace();
                }
            }  
                
        };
    
        new Thread(r1).start();  // this could be done in two ways. firstly ya to thread extend krke ya  fir thread  runnable constructor ko call krke
        
    }


    
       public void startWriting(){
        // this  thread will take data from the user and send it to the client:
         Runnable r2 = ()-> {
            
            while (true) {
            try{
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
    
                } catch(Exception e) {
                   e.printStackTrace();
                }
            }    
        
         };
    
         new Thread(r2).start();  // this could be done in two ways. firstly ya to thread extend krke ya  fir thread  runnable constructor ko call krke.
       }
           
    public static void main(String[] args) {
        System.out.println("Mike mike check: ");
        
        Client obj =  new Client();
    }
}

 