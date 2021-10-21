import java.util.LinkedList;
import java.util.Scanner;

public class FactorialThread extends Thread {
    private static LinkedList<Integer> numeros = new LinkedList<>();
    private static LinkedList<Integer> factoriales = new LinkedList<>();

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
        Scanner sc = new Scanner(System.in);

        System.out.println("¿Cuantos numeros va a ingresar?");
        int cant = sc.nextInt();
        System.out.println("Ingrese los numeros (sin decimal) de los que quiera obtener su factorial: ");

        for (int i = 0; i < cant; i++) {
            numeros.add(sc.nextInt());
        }

        thread.start();

        System.out.println("Lista de numeros: "+numeros.toString());
        System.out.println("Lista de factorial: "+factoriales.toString());


    }
}
