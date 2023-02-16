import java.io.*;

public class ProductsPreparation implements Runnable {
    private final String products_input;
    private final String order;
    private final FileWriter products_out;
    private final int products_number;
    private final int current_product;
    private final Object time;

    public ProductsPreparation(String order, String products_input, FileWriter products_out,
                               int products_number, int current_product, Object time) {
        this.products_input = products_input;
        this.order = order;
        this.products_out = products_out;
        this.products_number = products_number;
        this.current_product = current_product;
        this.time = time;
    }

    // Metoda in care thread-ul cauta in fisier al n-lea produs al comenzii
    // unde n = current_product
    public String take_product() {
        FileReader file = null;
        try {
            file = new FileReader(products_input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (file != null) {
            BufferedReader products_reader = new BufferedReader(file);
            String line = null;

            // Daca gasesc un produs din comanda, incrementez count.
            // Atunci cand count devine egal cu current_product,
            // thread-ul si-a gasit prosul pe care trebuie sa il prelucreze
            // conform task-ului pe care l-a luat
            int count = 0;
            while (count < current_product) {
                try {
                    line = products_reader.readLine();
                    if (line.contains(order))
                        count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                products_reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return line;
        }

        return  null;
    }

    // Metoda in care thread-ul scrie produsul in fisierul de iesire
    public synchronized void write_products(String line) {
        try {
            products_out.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Thread-ul ia produsul specificat
        String product = take_product();
        if (product != null) {
            String shipped_product = product + ",shipped" + "\n";
            write_products(shipped_product);

            // Daca este ultimul produs al comenzii, notifica thread-ul principal
            // ca poate sa finalizeze comanda
            synchronized (time) {
                if (current_product == products_number) {
                    time.notifyAll();
                }
            }

        }
    }
}
