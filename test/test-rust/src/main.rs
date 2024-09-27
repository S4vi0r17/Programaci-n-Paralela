use rayon::prelude::*;
use std::time::Instant;

fn main() {
    let data: Vec<i64> = (0..10_000_000).collect();

    // ---- Suma Secuencial ----
    let start = Instant::now();
    let sum_sequential: i64 = data.iter().sum();
    let duration_sequential = start.elapsed();
    println!("Suma secuencial: {}", sum_sequential);
    println!("Tiempo secuencial: {:?}\n", duration_sequential);

    // ---- Suma Paralela usando Rayon ----
    let start = Instant::now();
    let sum_parallel: i64 = data.par_iter().sum();
    let duration_parallel = start.elapsed();
    println!("Suma paralela con Rayon: {}", sum_parallel);
    println!("Tiempo paralelo con Rayon: {:?}\n", duration_parallel);

    // ---- Suma Paralela usando Threads ----
    let start = Instant::now();
    let num_threads = 12;
    let chunk_size = data.len() / num_threads;
    let mut handles = vec![];

    // Crear los threads
    for i in 0..num_threads {
        let chunk = data[i * chunk_size..(i + 1) * chunk_size].to_vec();
        let handle = std::thread::spawn(move || chunk.iter().sum::<i64>());
        handles.push(handle);
    }

    let mut sum_parallel = 0;
    for handle in handles {
        sum_parallel += handle.join().unwrap();
    }

    let duration_threads = start.elapsed();
    println!("Suma paralela con threads: {}", sum_parallel);
    println!("Tiempo paralelo con threads: {:?}\n", duration_threads);
}
