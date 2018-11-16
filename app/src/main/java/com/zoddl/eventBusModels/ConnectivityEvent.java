package com.zoddl.eventBusModels;

public class ConnectivityEvent{
    private boolean status;

    public ConnectivityEvent(boolean status) {
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return Gets the value of status and returns status
     */
    public boolean isStatus() {
        return status;
    }
}
