
/**
 * Alumno: Eder Gustavo Benites Pardave
 * Codigo: 22200007
 * Sección: G1
*/

import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main
{
    public static final int N = 50000000;
    public static final int U = 12000000;
    
    public static void inicializa(double X[]) {
        Random ran = new Random();
        for(int i=0; i<X.length; i++) {
            X[i] = ran.nextDouble();
        }
    }

    public static void imprime(double X[], int n) {
        for(int i=0; i<n; i++)
            System.out.print(X[i] + " | ");
        System.out.println();
    }
    
	public static void main(String[] args) {
        double V[] = new double[N];
        double inicio;
        double duracion;
	    inicializa(V);

	    Suma s1 = new Suma(V,0,N-1);
        inicio = System.currentTimeMillis();
	    s1.compute();
        duracion = System.currentTimeMillis() - inicio;
	    System.out.println( "Suma secuencial " + s1.getSuma());
        System.out.println( "Duracion: " + duracion + " ms");
	    
	    ForkJoinPool pool = new ForkJoinPool();

	    SumaParalelaFuturo s3 = new SumaParalelaFuturo(V,0,N-1,U);
        inicio = System.currentTimeMillis();
	    double suma = pool.invoke(s3);
        duracion = System.currentTimeMillis() - inicio;
	    System.out.println( "Suma paralela UMBRAL " + suma);
        System.out.println( "Duracion: " + duracion + " ms");
	}
}

class SumaParalelaFuturo extends RecursiveTask<Double> {
    private double A[];
    private int lo, hi;
    private int UMBRAL;
    
    public SumaParalelaFuturo(double A[], int lo, int hi, int UMBRAL) {
        this.A = A;
        this.lo = lo; this.hi = hi;
        this.UMBRAL = UMBRAL;
    }
    
    @Override
    public Double compute() {
        if ( hi-lo <= UMBRAL ) {
            double suma = 0.0;
            for(int i=lo; i<=hi; i++) {
                suma = suma + A[i];
            }
            return suma;
        }
        int mitad = (lo + hi) / 2;
        SumaParalelaFuturo s2 = new SumaParalelaFuturo(A,lo,mitad,UMBRAL);
        SumaParalelaFuturo s3 = new SumaParalelaFuturo(A,mitad+1,hi,UMBRAL);
        // invokeAll(s2,s3);
        s2.fork();
        s3.fork();
        // s3.compute(); // Se aharra un hilo
        return s2.join() + s3.join();
    }
    
}

class Suma {
    private double A[];
    private int lo, hi;
    private double suma;
    
    public Suma(double A[], int lo, int hi) {
        this.A = A;
        this.lo = lo; this.hi = hi;
        this.suma = 0.0;
    }
    
    public void compute() {
        for(int i=lo; i<=hi; i++)
            suma += A[i];
    }
    
    public double getSuma() {
        return suma;
    }
}

// source : https://onlinegdb.com/afcUUsyOv
