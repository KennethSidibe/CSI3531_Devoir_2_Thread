public class PrimeNumber {
    
    public static void main(String[] args) {

        listPrimeNumber(Integer.parseInt(args[0]));
    }

    public static void listPrimeNumber(int startNumber) {

        for(int i=startNumber; i >=1; i--){

            if(isPrimeNumber(i)) {
                System.out.println(i);
            }

        }
    }

    public static boolean isPrimeNumber(int numberToVerify) {
        
        for(int i=numberToVerify-1; i>1; i-- ){
            
            if (numberToVerify % i ==0) {
                return false;
            }
        }

        return true;
    }
}