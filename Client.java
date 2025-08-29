import java.io.*;
import java.net.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connected to server.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Reading messages (from server)
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                String msg;
                while ((msg = br.readLine()) != null) {
                    if (msg.equals("Terminate")) {
                        System.out.println("Server ended chat.");
                        break;
                    }

                    // ✅ Detect FTP link
                    if (msg.startsWith("File available at: ftp://")) {
                        System.out.println("Server sent a file link: " + msg);
                        String ftpUrl = msg.substring("File available at: ".length());
                        downloadFileFromFTP(ftpUrl, "downloads/");
                    } else {
                        System.out.println("Server: " + msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    // ✅ Writing messages (to server)
    public void startWriting() {
        Runnable r2 = () -> {
            try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    String content = console.readLine();
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    // ✅ FTP Download Helper
    private void downloadFileFromFTP(String ftpUrl, String saveDir) {
        try {
            URL url = new URL(ftpUrl);
            String server = url.getHost();
            int port = (url.getPort() == -1) ? 21 : url.getPort();
            String remoteFile = url.getPath().substring(1);
            String fileName = new File(remoteFile).getName();

            FTPClient ftp = new FTPClient();
            ftp.connect(server, port);
            ftp.login("anonymous", ""); // no credentials
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            File downloadDir = new File(saveDir);
            if (!downloadDir.exists()) downloadDir.mkdirs();

            FileOutputStream fos = new FileOutputStream(saveDir + fileName);
            boolean success = ftp.retrieveFile(remoteFile, fos);
            fos.close();

            ftp.logout();
            ftp.disconnect();

            if (success) {
                System.out.println("✅ File downloaded to: " + saveDir + fileName);
            } else {
                System.out.println(" Failed to download file from FTP.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
