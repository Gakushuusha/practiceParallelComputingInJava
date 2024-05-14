package task21;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putPtr, takePtr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                System.out.println("Buffer is full. Producer is waiting...");
                notFull.await();
            }
            items[putPtr] = x;
            if (++putPtr == items.length) putPtr = 0;
            ++count;
            System.out.println("Produced: " + x);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                System.out.println("Buffer is empty. Consumer is waiting...");
                notEmpty.await();
            }
            Object x = items[takePtr];
            if (++takePtr == items.length) takePtr = 0;
            --count;
            System.out.println("Consumed: " + x);
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
