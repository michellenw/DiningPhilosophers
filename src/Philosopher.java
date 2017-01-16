import java.util.*;
import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
    private String name;
    private Fork leftFork;
    private Fork rightFork;
    private static int timerVariable = 1;
    //one table
    private DiningTable diningTable;
    private String state = "Normal";
    private ArrayList <Fork> forksInHand = new ArrayList<>();


    public Philosopher(String name, DiningTable table) {
        this.name = name;
        this.diningTable = table;

    }

    /*if state does not equal dead is asked at many points throughout this section of code, this is due to the inability
    to effectively stop a thread from running mid-loop*/
    //_____________________________________________________________________________________
    public void run() {

            while (!state.equals("Dead")) {
                think();
                boolean leftFirst = new Random().nextBoolean();
                if (leftFirst) {
                    if (pickUpLeftFork()) {
                        //wait random amount of time
                        think();
                        //now try to pic up right fork in order to eat
                        if (pickUpRightFork()) {
                            if (!state.equals("Dead")) {

                                System.out.println(name + " is eating.");
                            }
                            eat();
                            dropFork();
                            dropFork();
                        } else {
                            increaseHunger();
                            dropFork();
                        }
                    } else {
                        increaseHunger();
                    }
                } else {
                    if (pickUpRightFork()) {
                        //wait random amount of time
                        think();
                        //now try to pic up right fork in order to eat
                        if (pickUpLeftFork()) {
                            if (!state.equals("Dead")) {
                                System.out.println(name + " is eating.");
                            }
                            eat();
                            dropFork();
                            dropFork();
                        } else {
                            increaseHunger();
                            dropFork();
                        }
                    } else {
                        increaseHunger();
                    }
                }
            }



    }
    //_____________________________________________________________________________________

    public void setLeftRightForks() {
        int numOfPhilos = diningTable.getForkList().size();
        int index = diningTable.getIndexOfPhilosopher(this);
        leftFork = diningTable.getForkList().get(index);
        rightFork = diningTable.getForkList().get((index + 1) % numOfPhilos);
    }

    public void think() {

        try {
            //test think timer change
            Thread.sleep((new Random().nextInt(1000)+500));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void increaseHunger() {

        if (state.equals("Normal")) {
            state = "Hungry";
        } else if (state.equals("Hungry")) {
            state = "Very Hungry";
        } else if (state.equals("Very Hungry")) {
            state = "Starving";
        } else if (state.equals("Starving")) {
            state = "Dead";
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tRIP " + name);
            die();
        }
        if (!state.equals("Dead")) {
            System.out.println("\t\t\t" + name + " is now: " + state);
        }
    }

    public void increaseTimer() {
        timerVariable *= (new Random().nextInt(40) + 110) / 100;
    }

    ///boolean to keep track if philosopher was successful in picking up fork
    public boolean pickUpLeftFork() {
        Semaphore forkCheck = leftFork.getSemaphore();

        if (forkCheck.tryAcquire()) {
            //permits go to 0 - mutually exclusive - no one else can pick up the same fork
            if (!state.equals("Dead")) {
                forksInHand.add (leftFork);
                System.out.println(name + " picked up fork " + leftFork.getIndex() + ".");
            }
            return true;
        }

        else {
            if (!state.equals("Dead")) {
                System.out.println(name + " was unable to pick up fork " + leftFork.getIndex() + ". ");
            }
        }
        return false;
    }

    public boolean pickUpRightFork() {
        Semaphore forkCheck = rightFork.getSemaphore();

        if (forkCheck.tryAcquire()) {
            //permits go to 0 - mutually exclusive - no one else can pick up the same fork
            if (!state.equals("Dead")) {
                forksInHand.add (rightFork);
                System.out.println(name + " picked up fork " + rightFork.getIndex() + ".");
            }

            return true;
        }
        else {
            if (!state.equals("Dead")) {
                System.out.println(name + " was unable to pick up fork " + rightFork.getIndex() + ". ");

            }
        }
        return false;
    }

    public void dropFork() {
        if (!state.equals("Dead")) {
            for (Fork fork : forksInHand) {
                fork.getSemaphore().release();
                System.out.println(name + " dropped fork " + fork.getIndex());
            }
        }
        forksInHand.clear();
    }



    public void eat() {
        try {
            //Test think timer change
                Thread.sleep((new Random().nextInt(1000) + 500) * timerVariable);
            if (!state.equals("Dead")) {
                state = "Normal";
                System.out.println(name + " finished eating and his state is back to normal.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void die() {
        int index = diningTable.getIndexOfPhilosopher(this);
        diningTable.getPhilosopherAL().remove(index);
        diningTable.getForkList().remove(index);
        diningTable.setUpForks();
        increaseTimer();
        if (diningTable.getPhilosopherAL().size() == 1) {
            Philosopher winningPhilo = diningTable.getPhilosopherAL().get(0);
            winningPhilo.state = "Dead";
            System.out.println("\t\t\t\t" + winningPhilo.getName() + " has won!");
            System.out.println("GAME OVER!");
        }
    }

    public String getName() {
        return name; }

}