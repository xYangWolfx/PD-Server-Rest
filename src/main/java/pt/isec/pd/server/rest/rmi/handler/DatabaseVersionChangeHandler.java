package pt.isec.pd.server.rest.rmi.handler;

import pt.isec.pd.server.rest.rmi.service.DatabaseVersionChangeListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseVersionChangeHandler {
    private List<DatabaseVersionChangeListener> listeners;

    public DatabaseVersionChangeHandler() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(DatabaseVersionChangeListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (DatabaseVersionChangeListener listener : listeners) {
            listener.onDatabaseVersionChange();
        }
    }
}
