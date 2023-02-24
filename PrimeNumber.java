// Kenneth Sidibe
// 300099184


public class PrimeNumber implements Runnable {

    private int maxNumberThread = 3;
    private int arrayIndex = 0;
    private int[][] workRange;
    private volatile boolean threadStop = false;
    private int numberToVerify;
    private int[] primeNumber;

    public static void main(String[] args) {

        PrimeNumber testPrime = new PrimeNumber();

        // System.out.println(testPrime.isPrimeNumber(25));
        testPrime.listPrimeNumber(Integer.parseInt(args[0]));

    }

    public void start() {
        Thread[] threadsToRun = new Thread[workRange.length];

        for (int i = 0; i < threadsToRun.length; i++) {
            Thread thread = new Thread(this);
            thread.setName("thread-range-" + i);
            threadsToRun[i] = thread;
            threadsToRun[i].start();
        }

        for (Thread thread : threadsToRun) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        
        boolean isThisPrime = false;
        primeNumber = new int[startNumber];

        for(int i=startNumber; i >= 2; i--){
            isThisPrime = this.isPrimeNumber(i);

            if(isThisPrime) {
                System.out.println(i);
                isThisPrime = false;
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

        this.start();

        while(Thread.interrupted()) {

        }
    
        return !threadStop;
    
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

        int[] numberRange = new int[rangeArraySize];
        int[][] localWorkRange = new int[maxNumberThread][rangeArraySize];

        for(int i = 2; i < numberToVerify;i++) {

            numberRange[j] = i; 
            j++;

            if(j >= rangeArraySize || i == numberToVerify - 1) {
                localWorkRange[x] = numberRange;
                numberRange = new int[rangeArraySize];
                j = 0;
                x++;
            }

            
        }

        return localWorkRange;

    }

    public void run() {
        
        try {

            int[] valueRange = this.workRange[this.arrayIndex];
            if(arrayIndex >= workRange.length) {
                arrayIndex = 0;
            } else {
                arrayIndex++;
            }
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