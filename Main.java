import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tema2 {
    public static void main(String[] args) throws IOException {
        String orders_file_path = args[0] + "/orders.txt";
        String products_file_path = args[0] + "/order_products.txt";
        FileWriter orders_out =  new FileWriter("orders_out.txt");
        FileWriter products_out = new FileWriter("order_products_out.txt");
        int threads_number = Integer.parseInt(args[1]);

        // BuufferReader folosit de thread-urile care citesc comenzile
        BufferedReader orders_reader = new BufferedReader(new FileReader(orders_file_path));

        // Pool-ul de taskuri pentru thread urile care se ocupa de produsele unei comenzi
        ExecutorService tasks_pool = Executors.newFixedThreadPool(threads_number);

        // Creez maxim threads_number thread-uri de nivel 1
        Thread[] threads = new Thread[threads_number];
        for (int i = 0; i < threads_number; i++) {
            threads[i] = new Thread(new OrdersPreparation(i, tasks_pool,
                                    orders_out, products_out, orders_reader, products_file_path));
            threads[i].start();
        }

        for (int i = 0; i < threads_number; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Toate thread-urile si-au terminat treaba, deci pot inchide pool-ul
        tasks_pool.shutdown();

        orders_out.close();
        products_out.close();
    }
}
