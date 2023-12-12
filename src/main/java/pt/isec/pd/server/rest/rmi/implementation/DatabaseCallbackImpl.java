package pt.isec.pd.server.rest.rmi.implementation;

import pt.isec.pd.server.rest.rmi.service.DatabaseCallback;
import pt.isec.pd.server.rest.rmi.service.DatabaseService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DatabaseCallbackImpl extends UnicastRemoteObject implements DatabaseCallback {
    private DatabaseService databaseService;
    private String backupDirectory;
    private String backupName;

    public DatabaseCallbackImpl(DatabaseService databaseService, String backupDirectory, String backupName) throws RemoteException {
        this.databaseService = databaseService;
        this.backupDirectory = backupDirectory;
        this.backupName = backupName;
    }

    @Override
    public String notifyDatabaseChange(String message) throws RemoteException {
        String returnMessage = "Call back received: " + message;
        System.out.println(returnMessage);
        requestDatabaseFile();
        return returnMessage;
    }

    @Override
    public void sendDatabaseBackup(byte[] databaseBackup) throws RemoteException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir")+"/" + backupDirectory + "/" + backupName)) {
            fileOutputStream.write(databaseBackup);
            System.out.println("Backup file received and saved successfully.");
        } catch (IOException e) {
            throw new RemoteException("Error saving backup file on the backup server.", e);
        }
    }

    @Override
    public void requestDatabaseFile() throws RemoteException {
        databaseService.requestUpdatedDatabaseFile();
    }
}
