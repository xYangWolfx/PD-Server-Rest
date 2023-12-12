package pt.isec.pd.server.rest.models;

import java.io.Serializable;

public class HeartbeatInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rmiRegistryPort;
    private String rmiServiceName;
    private int databaseVersion;

    public HeartbeatInfo(int rmiRegistryPort, String rmiServiceName, int databaseVersion) {
        this.rmiRegistryPort = rmiRegistryPort;
        this.rmiServiceName = rmiServiceName;
        this.databaseVersion = databaseVersion;
    }

    public int getRmiRegistryPort() {
        return rmiRegistryPort;
    }

    public void setRmiRegistryPort(int rmiRegistryPort) {
        this.rmiRegistryPort = rmiRegistryPort;
    }

    public String getRmiServiceName() {
        return rmiServiceName;
    }

    public void setRmiServiceName(String rmiServiceName) {
        this.rmiServiceName = rmiServiceName;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(int databaseVersion) {
        this.databaseVersion = databaseVersion;
    }
}
