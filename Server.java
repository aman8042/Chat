import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
class Server
{
    ServerSocket server;
    Socket socket;
    BufferedReader br; // data reading:
    PrintWriter out; // data writing:
    //constructor bnana hai:
public Server(){
    try{

        server = new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting......");
        socket = server.accept();

        br = new BufferedReader( new InputStreamReader( socket.getInputStream()));  // data andr lene ka kaam ;
        out = new PrintWriter(socket.getOutputStream()); // data baahr fekne ka kaam ;
         
        startReading();
        startWriting();

    } catch(Exception e){
        e.printStackTrace();
    }
}

   public void startReading(){
    // thread to read the data

    Runnable r1=()->{

        System.out.println("reader started......");
        
        while(true){
        try {
            
            
                String msg = br.readLine();
                if(msg.equals("Terminate")){
                System.out.println("Client terminated the chat ");
                break;
            } 
            System.out.println("Client :" + msg );
        }catch (Exception e) {
                // TODO Auto-generated catch block
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
        try {
            
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                out.println(content);
                out.flush();
        }    

            
            catch(Exception e) 
            {
               e.printStackTrace();
            }
        }
     };

     

     new Thread(r2).start();  // this could be done in two ways. firstly ya to thread extend krke ya  fir thread  runnable constructor ko call krke.
   }
    
    
    public static void main(String[] args) {
        System.out.println("mike check: ");
        new Server();
    }
}