import java.util.Random;
import java.util.concurrent.*;

public class Main
{
    public static final int N = 1000;
    
    public static void inicializa(double arr[]){
        Random ran = new Random();
        for(int i = 0; i<arr.length; i++){
            arr[i]=ran.nextDouble();
        }
    }
    
    public static void print(double arr[]){
        for(int i=0;i<10;i++){
            System.out.print(arr[i] + " ");
        }
    }
	public static void main(String[] args) {
	    double V[] = new double [N];
	    inicializa(V);
	    //print(V);
	    
	    Suma s1 = new Suma(V,0,N-1);
	    s1.compute();
	    System.out.print("Suma secuencial: " + s1.getSuma());
	    
	    ForkJoinPool pool = new ForkJoinPool();
	    
	    SumaParalela s2 = new SumaParalela(V,0,N-1);
	    pool.invoke(s2);
	    System.out.print("\nSuma paralela: " + s2.getSumaParalela());
	    
	    SumaParalelaUmbral s3 = new SumaParalelaUmbral(V,0,N-1,250);
	    pool.invoke(s3);
	    System.out.print("\nSuma paralela umbral: " + s3.getSumaParalelaUmbral());
	    
	}
}


class Suma{
    private double A[];
    private int lo, hi;
    private double sumaT;
    
    public Suma(double A[], int lo, int hi){
        this.A = A;
        this.lo = lo;
        this.hi = hi;
        this.sumaT = 0.00;
    }
    
    public void compute(){
        for(int i = lo; i<=hi;i++){
            sumaT += A[i];
        }
    }
    public double getSuma(){
        return sumaT;
    }
}


class SumaParalela extends RecursiveAction{
    private double A[];
    private int lo, hi;
    private double sumaT;
    
    public SumaParalela(double A[], int lo, int hi){
        this.A = A;
        this.lo = lo;
        this.hi = hi;
        this.sumaT = 0.00;
    }
    
    @Override
    public void compute(){
        if(lo>hi){ sumaT = 0.0; return;}
        if(lo == hi) { sumaT = A[lo]; return;}
        int mitad = (lo+hi)/2;
        SumaParalela s2= new SumaParalela(A,lo,mitad);
        SumaParalela s3= new SumaParalela(A,mitad+1,hi);
        invokeAll(s2,s3);
        sumaT = sumaT + s2.getSumaParalela() + s3.getSumaParalela();
        return;
    }
    
    public double getSumaParalela(){
        return sumaT;
    }
}

class SumaParalelaUmbral extends RecursiveAction{
    private double A[];
    private int lo, hi;
    private double sumaT;
    private double umbral;
    
    public SumaParalelaUmbral(double A[], int lo, int hi, int umbral){
        this.A = A;
        this.lo = lo;
        this.hi = hi;
        this.umbral=umbral;
        this.sumaT = 0.00;
    }
    
    @Override
    public void compute(){
        if(hi-lo <= umbral){
            for(int i=lo;i<=hi;i++){
                sumaT = sumaT + A[i];
            }
            return;
        }
        int mitad = (lo+hi)/2;
        SumaParalelaUmbral s1= new SumaParalelaUmbral(A,lo,hi/4, 250);
        SumaParalelaUmbral s2= new SumaParalelaUmbral(A,hi/4,mitad, 250);
        SumaParalelaUmbral s3= new SumaParalelaUmbral(A,mitad,(3*hi)/4, 250);
        SumaParalelaUmbral s4= new SumaParalelaUmbral(A,(3*hi)/4,hi, 250);
        invokeAll(s1,s2,s3,s4);
        sumaT = sumaT + s1.getSumaParalelaUmbral() + s2.getSumaParalelaUmbral() + s3.getSumaParalelaUmbral() + s4.getSumaParalelaUmbral();
        return;
    }
    
    public double getSumaParalelaUmbral(){
        return sumaT;
    }
}