# Black-Friday-Order-Manager


###### ➢ Task

It is desired to implement a Black Friday order processor in the Java programming language that uses parallel mechanisms. The idea is to process orders in parallel, and to process each product separately (even within the same order) in parallel.

The program should read two input files (containing orders and the products they contain) and create two output files in which to write the shipped orders and the shipped products.


###### ➢ Runnable

One way to use threads is to create a class that implements the Runnable interface. This class is responsible for resolving the orders. In the run() method, each thread extracts an order from the order file, then adds to a pool a number of tasks equal to the number of products in that order. After doing this, the thread waits, using the wait() method, to receive notification from one of the threads that handles the products, in order to complete the order.


###### ➢ ExecutorService

The other method is ExecutorService, which I used to process the products of an order. This pool will be occupied by a maximum number of threads equal to the parameter offered at runtime. Each task in the pool contains details such as the name of the order to which the product belongs, the position of the product in the file, the total number of products in the order, and an Object used to notify the order thread.

To complete a task, a thread must perform the following steps: <br>
-> extract the line containing the product from the file <br>
&nbsp;&nbsp;&nbsp;* parse the file line by line <br>
&nbsp;&nbsp;&nbsp;* if the line contains the order name from the task, check what number the product on that line has <br>
&nbsp;&nbsp;&nbsp;* if the product number corresponds to the current position specified in the task, return the line on which that product is located <br>
-> add the product to the output file, using the specified format <br>
-> check if the product specified in the task is the last product found, and notify the order thread that all products have been processed. <br>
