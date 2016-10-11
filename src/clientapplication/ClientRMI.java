package clientapplication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by o_0 on 2016-10-11.
 */
public interface ClientRMI extends Remote {
    public void sendMsgToclient(String msg) throws RemoteException;
    public String getNickName() throws RemoteException;
    public void setNickName(String nickName) throws RemoteException;
}
