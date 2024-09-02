#include <iostream>
#include <vector>
#include <chrono>
#include <omp.h>

const int VECTOR_SIZE = 100000000;

double suma_secuencial(const std::vector<double>& vec) {
    double suma = 0.0;
    for (const auto& num : vec) {
        suma += num;
    }
    return suma;
}

double suma_paralela(const std::vector<double>& vec) {
    double suma = 0.0;
    #pragma omp parallel for reduction(+:suma)
    for (int i = 0; i < vec.size(); ++i) {
        suma += vec[i];
    }
    return suma;
}

int main() {
    std::vector<double> numeros(VECTOR_SIZE);

    // Inicializar el vector
    for (int i = 0; i < VECTOR_SIZE; ++i) {
        numeros[i] = static_cast<double>(i);
    }

    // Secuencial
    auto inicio = std::chrono::high_resolution_clock::now();
    double resultado_secuencial = suma_secuencial(numeros);
    auto fin = std::chrono::high_resolution_clock::now();
    auto duracion_secuencial = std::chrono::duration_cast<std::chrono::milliseconds>(fin - inicio);

    // Paralela
    inicio = std::chrono::high_resolution_clock::now();
    double resultado_paralelo = suma_paralela(numeros);
    fin = std::chrono::high_resolution_clock::now();
    auto duracion_paralela = std::chrono::duration_cast<std::chrono::milliseconds>(fin - inicio);

    std::cout << "Suma secuencial: " << resultado_secuencial << std::endl;
    std::cout << "Tiempo secuencial: " << duracion_secuencial.count() << " ms" << std::endl;
    std::cout << "Suma paralela: " << resultado_paralelo << std::endl;
    std::cout << "Tiempo paralelo: " << duracion_paralela.count() << " ms" << std::endl;

    return 0;
}
