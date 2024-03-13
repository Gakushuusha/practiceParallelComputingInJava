package task1;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Bank {
    public static final int N_TEST = 10000;
    private final int[] accounts;
    private long n_transacts;

    private final ReentrantLock lock = new ReentrantLock();

    public Bank(int n, int initialBalance) {
        this.accounts = new int[n];
        Arrays.fill(accounts, initialBalance);
        n_transacts = 0;
    }

    // the first way to manage - lock
    public void lockTransfer(int from, int to, int amount) {
        lock.lock();
        try {
            accounts[from] -= amount;
            accounts[to] += amount;
            n_transacts++;

            if (n_transacts % N_TEST == 0) {
                test();
            }
        } finally {
            lock.unlock();
        }
    }

    // the second way to manage - tryLock
    public void tryLockTransfer(int from, int to, int amount) throws InterruptedException {
        if (lock.tryLock()) {
            try {
                accounts[from] -= amount;
                accounts[to] += amount;
                n_transacts++;

                if (n_transacts % N_TEST == 0) {
                    test();
                }
            } finally {
                lock.unlock();
            }
        } else {
            Thread.sleep(1);
            lockTransfer(from, to, amount);
        }
    }

    // the third way to manage - synchronization of blocks
    public void syncStatementTransfer(int from, int to, int amount) {
        synchronized (accounts) {
            accounts[from] -= amount;
            accounts[to] += amount;
            n_transacts++;
            if (n_transacts % N_TEST == 0) {
                test();
            }
        }
    }

    // the last option - synchronization of methods
    public synchronized void syncMethodTransfer(int from, int to, int amount) {
        while (accounts[from] < amount) {
            try {
                wait();
            } catch (InterruptedException e) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        accounts[from] -= amount;
        accounts[to] += amount;
        n_transacts++;
        notifyAll();
        if (n_transacts % N_TEST == 0) {
            test();
        }
    }

    public void test() {
        int sum = 0;
        for (int account : accounts) {
            sum += account;
        }
        System.out.println("Transactions:" + n_transacts + " Sum: " + sum);
    }

    public int size() {
        return this.accounts.length;
    }
}