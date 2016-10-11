import java.io.IOException;

/**
 * Created by o_0 on 2016-09-20.
 */
public interface ChatServerModel {
    /**
     * Post a message to the server
     * @param msg the message
     * @throws IOException
     */
    public void postMessage(String msg) throws IOException;
}
