#include <stdio.h>
#include <math.h>
#include <omp.h>

double f(double x) {
    return exp(x) + 3 * pow(x, 3) + 2 * pow(x, 2) + x / 0.3;
}

double suma_riemann_secuencial(double a, double b, int n) {
    double delta_x = (b - a) / n;
    double suma_total = 0.0;

    for (int i = 0; i < n; i++) {
        double x_i = a + i * delta_x;
        suma_total += f(x_i) * delta_x;
    }
    
    return suma_total;
}

// double suma_riemann_paralelo(double a, double b, int n) {
//     double delta_x = (b - a) / n;
//     double suma_total = 0.0;
//     double s1 = 0, s2 = 0, s3 = 0, s4 = 0;

//     #pragma omp parallel sections
//     {
//         #pragma omp section
//         {
//             s1 = suma_riemann_secuencial(a, a + n / 4 * delta_x, n / 4);
//         }
//         #pragma omp section
//         {
//             s2 = suma_riemann_secuencial(a + n / 4 * delta_x, a + n / 2 * delta_x, n / 4);
//         }
//         #pragma omp section
//         {
//             s3 = suma_riemann_secuencial(a + n / 2 * delta_x, a + 3 * n / 4 * delta_x, n / 4);
//         }
//         #pragma omp section
//         {
//             s4 = suma_riemann_secuencial(a + 3 * n / 4 * delta_x, b, n / 4);
//         }
//     }

//     suma_total = s1 + s2 + s3 + s4;
    
//     return suma_total;
// }

/* Otra forma de hacerlo */

double suma_riemann_paralelo(double a, double b, int n) {
    double delta_x = (b - a) / n;
    double suma_total = 0.0;

    omp_set_num_threads(4); // 8

    #pragma omp parallel
    {
        double suma_local = 0.0;
        #pragma omp for
        for (int i = 0; i < n; i++) {
            double x_i = a + i * delta_x;
            suma_local += f(x_i) * delta_x;
        }
        #pragma omp critical
        suma_total += suma_local;
    }
    
    return suma_total;
}

int main() {
    double a = 1.0, b = 10.0;
    int n = 10000;
    
    // secuencial
    double inicio_secuencial = omp_get_wtime();
    double resultado_secuencial = suma_riemann_secuencial(a, b, n);
    double fin_secuencial = omp_get_wtime();
    
    // paralelo
    double inicio_paralelo = omp_get_wtime();
    double resultado_paralelo = suma_riemann_paralelo(a, b, n);
    double fin_paralelo = omp_get_wtime();
    

    printf("Resultado de la integral (secuencial): %f\n", resultado_secuencial);
    printf("Tiempo de ejecución (secuencial): %f segundos\n", fin_secuencial - inicio_secuencial);
    
    printf("Resultado de la integral (paralelo): %f\n", resultado_paralelo);
    printf("Tiempo de ejecución (paralelo): %f segundos\n", fin_paralelo - inicio_paralelo);
    
    // Aceleración
    double aceleracion = (fin_secuencial - inicio_secuencial) / (fin_paralelo - inicio_paralelo);
    printf("Aceleración obtenida: %f\n", aceleracion);
    
    return 0;
}
