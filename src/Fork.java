import java.util.concurrent.Semaphore;

public class Fork {

    //Gives one permit to the semaphore
    private Semaphore semaphore = new Semaphore(1);
    private int index;

    public Fork(int index) {
        //this refers to a particular object / makes reference to instance variable
        this.index = index;
    }

    //getter
    public int getIndex() {
        return index;
    }

    //getter
    public Semaphore getSemaphore() {
        return semaphore;
    }

}
