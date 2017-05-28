package util;

/**
 * Interface implemented by main controllers of the server and the client. Allows to pass port number to any of them
 */
public interface Controller {
    public void setPort(int portNumber);
}
