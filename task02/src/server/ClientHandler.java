package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket client;
    private String docRoot;

    public ClientHandler(Socket client, String docRoot){
        this.client = client;
        this.docRoot = docRoot;
    }

    @Override
    public void run(){
        try{
            System.out.println("Starting thread ...");
            start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        InputStream is = client.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String request = br.readLine();
        String resource = request.split(" ")[1].trim();
        if (resource.equals("/")){
            resource = "/index.html";
        }

        String filePath;
        if (resource.split("/").length > 1){
            filePath = "%s%s".formatted(docRoot, resource);
        }
        else {
            filePath = "%s%s".formatted(docRoot, resource);
        }
        OutputStream os = client.getOutputStream();
        HttpWriter hw = new HttpWriter(os);
        
        if (fileExists(filePath)){
            String fileContent = getFileContent(filePath);
            writeValidResponse(hw, fileContent);
            hw.flush();
        }
        else {
            writeErrorResponse(hw, resource);
            hw.flush();
        }

        hw.close();
    }

    private boolean fileExists(String file){
        File f = new File(file);
        return f.exists();
    }

    private String getFileContent(String file) throws Exception{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        
        br.lines().forEach(line -> {
            sb.append(line);
            sb.append("\n");
        });

        br.close();
        fr.close();
        return sb.toString();
    }

    private void writeErrorResponse(HttpWriter writer, String resource) throws Exception{
        writer.writeString("HTTP/1.1 404 Not Found\nContent-Type:text/html");
        writer.writeString();
        resource = resource.replaceFirst("/", "");
        writer.writeStringNewLine("Resource %s not found".formatted(resource));
    }

    private void writeValidResponse(HttpWriter writer, String fileContent) throws Exception {
        writer.writeString("HTTP/1.1 200 OK");
        writer.writeString("Content-Type: text/html");
        writer.writeStringOnly(fileContent);
    }
}
