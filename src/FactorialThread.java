
/*
*   Andrea Duarte Hernández
*   Materia: Desarrollo de Sistemas III
*   Fecha: 21/oct/2021
 */

public class FactorialThread extends Thread {
    private int numero;

    public FactorialThread(String n){
        this.numero = Integer.parseInt(n);
    }

    @Override
    public void run() {
        int factorial = 1;
        int temp = numero;
        int n = numero;

        while(n>0){
            n = n-1;
            factorial *= (temp-n);
        }

        System.out.println("El factorial de "+numero+" es: "+factorial);
    }
}

class Main {
    public static void main(String[] args) {
        FactorialThread[] threads = new FactorialThread[args.length];

        for (int i = 0; i < args.length; i++) {
            threads[i] = new FactorialThread(args[i]);
            threads[i].start();
        }
    }
}
