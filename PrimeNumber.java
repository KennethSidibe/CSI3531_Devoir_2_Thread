import java.util.Arrays;

public class PrimeNumber implements Runnable {

    private int maxNumberThread = 3;
    private int arrayIndex = 0;
    private int[][] workRange;
    private volatile boolean threadStop = false;
    private int numberToVerify = 0;
    private int xVolatileIndex = 0;
    private int[] xVolatile = new int[5];

    public static void main(String[] args) {

        PrimeNumber testPrime = new PrimeNumber();
        testPrime.numberToVerify = 20;
        testPrime.workRange = testPrime.divideWork(testPrime.numberToVerify);

        testPrime.start();

        System.out.println(Arrays.toString(testPrime.xVolatile));
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

    public boolean doesListNegatesNumber(int numberToVerify, int[] listToVerify) {
        
        for(int i = 0; i < listToVerify.length;i++) {

            if(doesNumberNegatesNumberFromPrime(numberToVerify, listToVerify[i])){
                threadStop = true;
                return true;
            }
        }

        return false;
    }

    public boolean isPrimeNumber(int numberToVerify) {
        
        for(int i=numberToVerify-1; i>1; i-- ){
            
            if (numberToVerify % i ==0) {
                return false;
            }
        }

        return true;
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
        int[][] workRange = new int[rangeArraySize][maxNumberThread];

        for(int i = 2; i < numberToVerify;i++) {

            numberRange[j] = i; 
            j++;

            if(j >= maxNumberThread || i == numberToVerify - 1) {
                workRange[x] = numberRange;
                numberRange = new int[maxNumberThread];
                j = 0;
                x++;
            }

            
        }

        return workRange;

    }

    public void run() {
        
        try {

            int[] valueRange = workRange[arrayIndex];
            arrayIndex++;
            int x = 0;

            while(!threadStop && x < valueRange.length) {

                // System.out.println(Thread.currentThread().getName() + " is checking : " + numberToVerify + " and " + valueRange[x]);
                threadStop = doesNumberNegatesNumberFromPrime(numberToVerify, valueRange[x]);
                // System.out.println(Thread.currentThread().getName() + " found that  : " + numberToVerify + " and " + valueRange[x] + " gives => " + threadStop + "\n");
                if(threadStop) {
                    xVolatile[xVolatileIndex] = valueRange[x];
                    xVolatileIndex++;
                }
                x++;

            }

            return;

        }
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Thread tried accessing array index which does not exist");
        }        
        
    }
}