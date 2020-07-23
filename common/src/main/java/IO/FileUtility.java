package IO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {

    public static void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static void createDirectory(String dirName) throws IOException {
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    public static void move(File dir, File file) throws IOException {
        String path = dir.getAbsolutePath() + "/" + file.getName();
        createFile(path);
        InputStream is = new FileInputStream(file);
        try(OutputStream os = new FileOutputStream(new File(path))) {
            byte [] buffer = new byte[8192];
            while (is.available() > 0) {
                int readBytes = is.read(buffer);
                System.out.println(readBytes);
                os.write(buffer, 0, readBytes);
            }
        }
    }

    public static void sendFile(Socket socket, File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long size = file.length();
        int count = (int) (size / 8192) / 10, readBuckets = 0;
        // /==========/
        try(DataOutputStream os = new DataOutputStream(socket.getOutputStream())) {
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

    public static void downloadFile(Socket socket, String fileName) throws IOException{

            DataInputStream is = new DataInputStream(socket.getInputStream());
            System.out.println("fileName: " + fileName);
            File file = new File("./common/client/" + fileName);
            file.createNewFile();
            try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                dos.writeUTF(fileName);
                byte[] buffer = new byte[8192];
                while (true) {
                    int r = is.read(buffer);
                    if (r == -1) break;
                    dos.write(buffer, 0, r);
                }
            }
            System.out.println("File downloaded!");
    }
public void showAllFiles(Socket socket){

    File dir = new File(".common/server/" + serverDir);
    List<File> lst = new ArrayList<>();
    for ( File file : dir.listFiles() ){
        if ( file.isFile() )
            lst.add(file);
    }
    System.out.println(lst);
}

    public static void main(String[] args) throws IOException {
         //createFile("./common/1.txt");
        // createDirectory("./common/dir1");
//        long start = System.currentTimeMillis();
      // move(new File("./common/dir1"), new File("./common/1.txt"));
//        long end = System.currentTimeMillis();
//        System.out.println("time: " + (end - start) + " ms.");
        //sendFile(new Socket("localhost", 8189),
              // new File("./common/gora-22.jpg"));
        downloadFile(new Socket("localhost", 8189),
                "gor.jpg");
    }

}
