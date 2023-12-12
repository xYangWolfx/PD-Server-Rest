package pt.isec.pd.server.rest.rmi.implementation;



import pt.isec.pd.server.rest.rmi.handler.DatabaseVersionChangeHandler;
import pt.isec.pd.server.rest.rmi.service.DatabaseCallback;
import pt.isec.pd.server.rest.rmi.service.DatabaseService;
import pt.isec.pd.server.rest.rmi.service.DatabaseVersionChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServiceImpl extends UnicastRemoteObject implements DatabaseService, DatabaseVersionChangeListener {
    private List<DatabaseCallback> callbacks;
    private String databasePath;

    public DatabaseServiceImpl(String databasePath, DatabaseVersionChangeHandler changeHandler) throws RemoteException {
        callbacks = new ArrayList<>();
        this.databasePath = databasePath;
        changeHandler.addListener(this);
    }

    public synchronized void registerForCallback(DatabaseCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
            System.out.println("New backup server registered!");
            for (DatabaseCallback databaseCallback : callbacks) {
                System.out.println(databaseCallback);
            }
        }
    }

    public synchronized void unregisterForCallback(DatabaseCallback callback) {
        if (callbacks.remove(callback)) {
            System.out.println("Backup server unregistered!");
        } else {
            System.out.println("Backup server wasn't registered or failed to unregister!");
        }
    }

    @Override
    public void requestUpdatedDatabaseFile() throws RemoteException {
        try {
            // Get the updated database file as a byte array
            byte[] updatedFile = getDatabaseFile();

            // Get all registered backup server callbacks and send them the serialized file
            synchronized (callbacks) {
                for (DatabaseCallback callback : callbacks) {
                    callback.sendDatabaseBackup(updatedFile);
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Error while serializing and sending updated database file", e);
        }
    }

    @Override
    public byte[] getDatabaseFileFirstTime() throws RemoteException {
        byte[] dbFile = getDatabaseFile();
        
        return dbFile;
    }

    private synchronized void notifyCallbacks() {
        for (DatabaseCallback callback : callbacks) {
            try {
                callback.notifyDatabaseChange("New bd update available!");
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDatabaseVersionChange() {
        System.out.println("Database version was updated.");
        notifyCallbacks();
    }

    private byte[] getDatabaseFile() {
        File databaseFile = new File(System.getProperty("user.dir") + "/" + databasePath + "/TP.db");

        try (FileInputStream fileInputStream = new FileInputStream(databaseFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
