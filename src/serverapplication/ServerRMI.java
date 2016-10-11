package serverapplication;

import clientapplication.ClientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by o_0 on 2016-10-11.
 */
public interface ServerRMI extends Remote {
    public void registrateClient(ClientRMI clientRMI) throws  RemoteException;
    public void postChatMsg(ClientRMI client, String msg) throws RemoteException;
    public void deregistrateClient(ClientRMI clientRMI) throws  RemoteException;
}
