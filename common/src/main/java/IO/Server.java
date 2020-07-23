package IO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    DataInputStream is;
    DataOutputStream os;
    ServerSocket server = new ServerSocket(8189);
    Socket socket = server.accept();

    public Server() throws IOException {
        sendFile();
    }

    public void uploadFile() throws IOException {

        System.out.println("Client accepted!");
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        String fileName = is.readUTF();
        System.out.println("fileName: " + fileName);
        File file = new File("./common/server/" + fileName);
        file.createNewFile();
        try (FileOutputStream os = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            while (true) {
                int r = is.read(buffer);
                if (r == -1) break;
                os.write(buffer, 0, r);
            }
        }
        System.out.println("File uploaded!");
    }

    public void sendFile() throws IOException {


        // /==========/
        try(DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
            String filename = "";
            os.writeUTF(filename);
            File file = new File(".common/server/files/" + filename);
            InputStream is = new FileInputStream(file);
            long size = file.length();
            int count = (int) (size / 8192) / 10, readBuckets = 0;

            byte [] buffer = new byte[8192];
            os.writeUTF(file.getName());
            System.out.print("/");
            while (is.available() > 0) {
                int readBytes = is.read(buffer);
                readBuckets++;
                if (readBuckets % count == 0) {
                    System.out.print("=");
                }
                os.write(buffer, 0, readBytes);
            }
            System.out.println("/");
        }
    }
    public static void main(String[] args) throws IOException {
        new Server();
    }
}
