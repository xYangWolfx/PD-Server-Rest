package pt.isec.pd.server.rest.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatabaseCallback extends Remote {
    String notifyDatabaseChange(String message) throws RemoteException;

    void sendDatabaseBackup(byte[] databaseBackup) throws RemoteException;

    void requestDatabaseFile() throws RemoteException;
}
