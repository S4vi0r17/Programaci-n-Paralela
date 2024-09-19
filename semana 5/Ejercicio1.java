import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final int N = 10;

    public static void init(double V[]) {
        Random r = new Random();
        for (int i = 0; i < N; i++) {
            V[i] = r.nextDouble();
        }
    }

    public static void print(double V[]) {
        for (int i = 0; i < N; i++) {
            System.out.print(V[i] + " ");
        }
    }

    public static void main(String[] args) {
        double V[] = new double[N];
        init(V);
        print(V);

        Suma s = new Suma(V, 0, N - 1);
        s.run();
        System.out.println("\nSequential sum: " + s.getResult());

        ForkJoinPool pool = new ForkJoinPool();
        SumaParalela sp = new SumaParalela(V, 0, N - 1);
        pool.invoke(sp);
        System.out.println("Parallel sum: " + sp.getResult());

        SumaParalelaUmbral spu = new SumaParalelaUmbral(V, 0, N - 1);
        pool.invoke(spu);
        System.out.println("Parallel sum with threshold: " + spu.getResult());
    }
}

class Suma {
    private double A[];
    private int lo, hi;
    private double result;

    public Suma(double A[], int lo, int hi) {
        this.A = A;
        this.lo = lo;
        this.hi = hi;
        this.result = 0;
    }

    public void run() {
        for (int i = lo; i <= hi; i++) {
            result += A[i];
        }
    }

    public double getResult() {
        return result;
    }
}

class SumaParalela extends RecursiveAction {
    private static final int THRESHOLD = 1000;
    private double A[];
    private int lo, hi;
    private double result;

    public SumaParalela(double A[], int lo, int hi) {
        this.A = A;
        this.lo = lo;
        this.hi = hi;
        this.result = 0;
    }

    @Override
    protected void compute() { // Método que se ejecuta en paralelo, similar al async
        if (hi < lo) {result = 0; return;}
        if (hi == lo) {result = A[lo]; return;}
        int mid = (lo + hi) / 2;
        SumaParalela left = new SumaParalela(A, lo, mid);
        SumaParalela right = new SumaParalela(A, mid + 1, hi);
        // left.fork();
        // right.compute();
        // left.join();
        // result = left.result + right.result;
        invokeAll(left, right);
        result = left.result + right.result;
    }

    public double getResult() {
        return result;
    }
}

class SumaParalelaUmbral extends RecursiveAction {
    private static final int THRESHOLD = 1000;
    private double A[];
    private int lo, hi;
    private double result;

    public SumaParalelaUmbral(double A[], int lo, int hi) {
        this.A = A;
        this.lo = lo;
        this.hi = hi;
        this.result = 0;
    }

    @Override
    protected void compute() {
        if (hi - lo <= THRESHOLD) {
            for (int i = lo; i <= hi; i++) {
                result += A[i];
            }
        } else {
            int mid = (lo + hi) / 2;
            SumaParalelaUmbral left = new SumaParalelaUmbral(A, lo, mid);
            SumaParalelaUmbral right = new SumaParalelaUmbral(A, mid + 1, hi);
            invokeAll(left, right);
            result = left.result + right.result;
        }
    }

    public double getResult() {
        return result;
    }
}
