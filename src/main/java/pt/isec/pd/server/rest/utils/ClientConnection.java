package pt.isec.pd.server.rest.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    private Socket socket1;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream1;
    private ObjectInputStream objectInputStream1;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ClientConnection(Socket socket, Socket socket1) throws IOException {
        this.socket = socket;
        this.socket1 = socket1;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.objectOutputStream1 = new ObjectOutputStream(socket1.getOutputStream());
        this.objectInputStream1 = new ObjectInputStream(socket1.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public Socket getSocket1() {
        return socket1;
    }

    public ObjectOutputStream getObjectOutputStream1() {
        return objectOutputStream1;
    }

    public ObjectInputStream getObjectInputStream1() {
        return objectInputStream1;
    }

    public void close() {
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream1 != null) {
                objectOutputStream1.close();
            }
            if (objectInputStream1 != null) {
                objectInputStream1.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (socket1 != null && !socket1.isClosed()) {
                socket1.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
