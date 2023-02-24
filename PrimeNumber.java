import java.util.Arrays;

public class PrimeNumber implements Runnable {

    private int maxNumberThread = 3;
    private int arrayIndex = 0;
    private int[][] workRange;
    private volatile boolean threadStop = false;
    private int numberToVerify;

    public static void main(String[] args) {

        PrimeNumber testPrime = new PrimeNumber();

        testPrime.isPrimeNumber(7);

        System.out.println(testPrime.threadStop);

    }

    public void start() {
        Thread[] threadsToRun = new Thread[workRange.length];

        for (int i = 0; i < threadsToRun.length; i++) {
            Thread thread = new Thread(this);
            thread.setName("thread-range-" + i);
            threadsToRun[i] = thread;
            threadsToRun[i].start();
        }
    }

    private void increaseArrayIndex() {
        if(arrayIndex >= workRange.length) {
            arrayIndex =0;
        } else {
            arrayIndex++;
        }
    }

    public void listPrimeNumber(int startNumber) {

        for(int i=startNumber; i >=1; i--){
            
            if(isPrimeNumber(i)) {
                System.out.println(i);
            }

        }
    }

    public boolean doesNumberNegatesNumberFromPrime(int numberToVerify, int numberToDivide) {
        if(numberToDivide == 0) {
            return false;
        }
        return numberToVerify % numberToDivide == 0;
    }

    public boolean isPrimeNumber(int numberToVerifyIsPrime) {

        this.numberToVerify = numberToVerifyIsPrime;
        this.workRange = divideWork(this.numberToVerify);
        this.threadStop = false;
        this.arrayIndex = 0;
        
        boolean isPrimeNumberChecker = false;
       
        this.start();

        while(Thread.interrupted()) {
            // thread is running
        }

        return isPrimeNumberChecker;
       
    }

    // Calculate number of arrays needed to divide work
    public int calculateSizeRangeArray(int numberToVerify) {
        if((numberToVerify - 1) % maxNumberThread == 0) {
            return (numberToVerify - 1) / maxNumberThread;
        }

        return ( (numberToVerify -1) / maxNumberThread) +1;
    }

    // splits the range of number to verify into n arrays
    public int[][] divideWork(int numberToVerify) {

        // Keep track of placement of number to verify
        int j = 0;
        // Keep track of placement of array 
        int x = 0;

        int rangeArraySize = calculateSizeRangeArray(numberToVerify);

        int[] numberRange = new int[maxNumberThread];
        int[][] localWorkRange = new int[rangeArraySize][maxNumberThread];

        for(int i = 2; i < numberToVerify;i++) {

            numberRange[j] = i; 
            j++;

            if(j >= maxNumberThread || i == numberToVerify - 1) {
                localWorkRange[x] = numberRange;
                numberRange = new int[maxNumberThread];
                j = 0;
                x++;
            }

            
        }

        return localWorkRange;

    }

    public void run() {
        
        try {

            int[] valueRange = this.workRange[this.arrayIndex];
            increaseArrayIndex();
            int x = 0;

            while(!threadStop && x < valueRange.length) {

                // System.out.println(Thread.currentThread().getName() + " is checking : " + numberToVerify + " and " + valueRange[x]);
                threadStop = doesNumberNegatesNumberFromPrime(this.numberToVerify, valueRange[x]);
                // System.out.println(Thread.currentThread().getName() + " found that  : " + numberToVerify + " and " + valueRange[x] + " gives => " + threadStop + "\n");
                x++;

            }

            return;

        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Thread tried accessing array index which does not exist");
        }        
        
    }
}