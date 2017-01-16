import java.util.*;
import java.util.Random;

public class DiningTable {


    //Number of philosophers at the table + Which
    private ArrayList<String> philosopherNames = new ArrayList<>(Arrays.asList("Aristotle", "Plato", "Socrates",
            "Descartes", "Confucius", "Rawls", "Locke", "Kant", "Nietzsche", "Satre", "Marx"));

    /*
    these two arrays are the arrays that hold the philosopher objects. The
    fork at index 0 is the right fork of the philosopher at index 0, etc.
    */
    private ArrayList <Philosopher> philosophers;
    private ArrayList <Fork> forks;

    public DiningTable() {
        createPhilosophers();
        createForks();
        setUpForks();
        threadGen();
    }

    public void setUpForks() {
        for(Philosopher philosopher : philosophers) {
            philosopher.setLeftRightForks();
        }
    }

    //getter
    public ArrayList<Fork> getForkList() {
        return forks;
    }

    //getter
    public int getIndexOfPhilosopher(Philosopher philosopher) {
        return philosophers.indexOf(philosopher);
    }

    //getter
    public ArrayList<Philosopher> getPhilosopherAL() {
        return philosophers;
    }


    public void createPhilosophers() {

        //generate random number of philosophers each time 6-11
        int randomNumOfPhilo = new Random().nextInt(6) + 5;


        philosophers = new ArrayList<>();

        for (int i = 0; i < randomNumOfPhilo; i++) {
            String name = chooseRandomName();
            //this referencing object - references itself
            Philosopher philosopher = new Philosopher(name, this);
            //add to philosopher array
            philosophers.add(philosopher);
            System.out.println(philosopher.getName() + " is philosopher number " + i + "" +
                    " (fork " + i + " is to his right)");
        }
        System.out.println("--------------------------------------");

    }

    public String chooseRandomName() {

        Random random = new Random();
        String name = philosopherNames.get(random.nextInt(philosopherNames.size()));
        philosopherNames.remove(name);
        return name;

    }
    //creates fork objects
    public void createForks() {
        int numOfForks = philosophers.size();
        forks = new ArrayList<>();

        for (int i = 0; i < numOfForks; i++) {
            Fork fork = new Fork(i);
            forks.add (fork);
        }

    }

    //for each loop generating threads for each philosopher
    public void threadGen() {
        for (Philosopher philosopher : philosophers) {
            Thread threadForPhilosopher = new Thread (philosopher);
            threadForPhilosopher.start();
        }
    }

}