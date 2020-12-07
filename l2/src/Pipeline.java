import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pipeline {
    double product;
    Lock mutex = new ReentrantLock();
    Condition condition = mutex.newCondition();
    boolean variable = false;
    public Pipeline() {}


    public void putInPipe(double value){
        mutex.lock();
        try {
            while (variable){
                condition.await();
            }
            product = value;
            System.out.println("produced " + product);
            variable = true;
            condition.signal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            mutex.unlock();
        }
    }
    public double outFromPipe(){
        mutex.lock();
        try {
            while(!variable){
                condition.await();
            }
            double result = this.product;
            System.out.println("consumed " + result);
            variable = false;
            condition.signal();
            return result;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            mutex.unlock();
        }
        return 1;
    }
}
