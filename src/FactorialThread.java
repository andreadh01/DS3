import java.util.ArrayList;

/*
*   Andrea Duarte Hernández
*   Materia: Desarrollo de Sistemas III
*   Fecha: 21/oct/2021
 */

public class FactorialThread extends Thread {
    private static ArrayList<Integer> numeros = new ArrayList<>();
    private static ArrayList<Integer> factoriales = new ArrayList<>();

    @Override
    public void run() {
        int factorial;
        int temp;
        for (int num: numeros) {
            factorial = 1;
            temp = num;
            while(num>0){
                num = num-1;
                factorial = factorial*(temp-num);
            }
            factoriales.add(factorial);
        }
    }

    public static void main(String[] args) {
        FactorialThread thread = new FactorialThread();

        for (int i = 0; i < args.length; i++) {
            numeros.add(Integer.valueOf(args[i]));
        }

        thread.start();

        System.out.println("Lista de numeros: "+ numeros);
        System.out.println("Lista de factorial: "+factoriales);


    }
}
