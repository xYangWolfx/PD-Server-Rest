package pt.isec.pd.server.rest.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DatabaseService extends Remote {
    /**
     * This remote method allows an object client to register for callback
     * @param callbackClientObject is a reference to the
     * object of the client; to be used by the server
     * // to make its callbacks
     * @throws RemoteException
     */
    void registerForCallback(DatabaseCallback callbackClientObject) throws RemoteException;

    /**
     * This remote method allows an object client to cancel its registration for callback
     * @param callbackClientObject
     * @throws RemoteException
     */
    void unregisterForCallback(DatabaseCallback callbackClientObject) throws RemoteException;

    /**
     * This remote method allows an object client to request an update of the database file
     * Only clients registered for backup will receive the file
     * @throws RemoteException
     */
    void requestUpdatedDatabaseFile() throws RemoteException;

    /**
     * This remote method allows an object client to request a database
     * file when it connects for the first time to the rmi service
     *
     * @return
     * @throws RemoteException
     */
    byte[] getDatabaseFileFirstTime() throws RemoteException;
}
