import java.util.Arrays;

public class FibonnaciSolver implements Runnable {

    private int fiboNumber;
    private int[] fibSeries;

    FibonnaciSolver(int fiboNumber) {
        this.fiboNumber = fiboNumber;
    }

    public void start() {
        Thread threadFib = new Thread(this);
        threadFib.start();
    }
    
    public static void main(String[] args) {

        int inputFiboNumber = Integer.parseInt(args[0]);

        FibonnaciSolver testFib = new FibonnaciSolver(inputFiboNumber);

        testFib.start();

        try {
            Thread threadFib = new Thread(testFib);
            threadFib.start();
            threadFib.join(); // wait for the thread to finish running
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println(Arrays.toString(testFib.fibSeries));

    }

    public int[] generateFibSeriesForN() {
        
        int[] fibSeries = new int[this.fiboNumber+1];
        fibSeries[0] = 0;
        fibSeries[1] = 1;
        int i = 2;

        while(i <= this.fiboNumber) {
            fibSeries[i] = fibSeries[i-2] + fibSeries[i-1];
            i++;
        }

        this.fibSeries = fibSeries;
        return fibSeries;
    }

    public void run() {
        this.generateFibSeriesForN();
    }

}
