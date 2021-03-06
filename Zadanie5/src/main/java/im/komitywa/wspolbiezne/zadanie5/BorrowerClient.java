package im.komitywa.wspolbiezne.zadanie5;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: rafal
 * Date: 27.11.13
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
public class BorrowerClient implements Client {
    private Integer moneyNeeds; // Max
    private Integer borrowedAmount = 0; //Allocation
    private Server server;

    @Override
    public void run() {
        while(getNeed() > 0){
            try {
                ChangeLoanStateTask borrowMoneyTask = new ChangeLoanStateTask();
                    borrowMoneyTask.setServer(server);
                    borrowMoneyTask.setClient(this);
                    borrowMoneyTask.setLoanChange(1);

                Promise promise = new Promise();
                synchronized (promise) {
                    server.executeTask(borrowMoneyTask,promise);
                    promise.wait();
                }

                if(promise.getTaskResult()==null){
                    throw new RuntimeException("Everything is wrong.");
                }

                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //Return all owed money
        ChangeLoanStateTask returnMoneyTask = new ChangeLoanStateTask();
        returnMoneyTask.setServer(server);
        returnMoneyTask.setClient(this);
        returnMoneyTask.setLoanChange(-1*borrowedAmount);

        Promise promise = new Promise();
        synchronized (promise) {
            server.executeTask(returnMoneyTask,promise);
            try {
                promise.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //Die.
        server.setNumberOfRunningClients(server.getNumberOfRunningClients()-1);
    }

    public Integer getMoneyNeeds() {
        return moneyNeeds;
    }

    public void setMoneyNeeds(Integer moneyNeeds) {
        this.moneyNeeds = moneyNeeds;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void addMoney(Integer amount) {
        borrowedAmount += amount;
        System.out.println("pozyczylem " + borrowedAmount + " florenow");
    }

    @Override
    public Integer getMax() {
        return getMoneyNeeds();
    }

    @Override
    public Integer getAllocation() {
        return borrowedAmount;
    }

    @Override
    public Integer getNeed() {
        return getMax() - getAllocation();
    }
}
