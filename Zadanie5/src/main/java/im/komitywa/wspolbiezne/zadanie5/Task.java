package im.komitywa.wspolbiezne.zadanie5;

/**
 * Created with IntelliJ IDEA.
 * User: rafal
 * Date: 27.11.13
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public interface Task {
    public Server getServer();
    public void setServer(Server server);
    public Client getClient();
    public void setClient(Client client);
    public BooleanTaskResult execute();
}
