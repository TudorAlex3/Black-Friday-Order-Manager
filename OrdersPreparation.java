import java.io.*;
import java.util.concurrent.ExecutorService;

public class OrdersPreparation implements Runnable {
    public int id;
    private final BufferedReader line_order;
    private final String file_products;
    private final FileWriter orders_out;
    private final FileWriter products_out;
    private final ExecutorService tasks_pool;
    private final Object time = new Object();

    public OrdersPreparation(int id, ExecutorService tasks_pool,
                             FileWriter orders_out, FileWriter products_out,
                             BufferedReader line_order, String file_products) {
        this.id = id;
        this.orders_out = orders_out;
        this.products_out = products_out;
        this.tasks_pool = tasks_pool;
        this.line_order = line_order;
        this.file_products = file_products;
    }

    // Metoda prin care thread ul ia comanda de la linia curenta
    public synchronized String take_order() {
        try {
            return line_order.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    // Metoda in care thread-ul scrie comanda in fisierul de iesire
    public synchronized void write_orders(String line) {
        try {
            orders_out.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Cat timp mai sunt linii in fisier, citesc o linie
        boolean finish = false;
        while (!finish) {
            String order_and_products = take_order();
            String[] order_number = null;

            // Daca exista linia si este diferita de "\n"
            if (order_and_products != null && order_and_products.contains(",")) {
                order_number = order_and_products.split(",");
                // ID-ul comenzii si numarul de produse
                String order_name = order_number[0];
                int products_number = Integer.parseInt(order_number[1]);

                // Daca in comanda sunt mai mult de o produse
                if (products_number != 0) {

                    // Folosesc synchronized pentru a lasa thread-ul in asteptare cat timp
                    // produsele comenzii sale sunt executate in pool
                    synchronized (time) {
                        // Adaug un task pentru fiecare produs
                        for (int i = 1; i <= products_number; i++)
                            tasks_pool.submit(new ProductsPreparation(order_name, file_products,
                                                    products_out, products_number, i, time));

                        try {
                            time.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    String shipped_order = order_name + "," + products_number + ",shipped" + "\n";
                    write_orders(shipped_order);
                }
            }

            // Daca nu mai sunt comenzi in fisier, thread-ul a terminat treaba
            if (order_number == null) {
                finish = true;
            }
        }
    }
}
