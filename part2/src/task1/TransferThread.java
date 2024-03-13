package task1;

public class TransferThread extends Thread {
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;
    private static final int REPLICATIONS = 1000;

    public TransferThread(Bank b, int from, int max) {
        this.bank = b;
        this.fromAccount = from;
        this.maxAmount = max;
    }

    public void run() {
        while (!interrupted()) {
            for (int i = 0; i < REPLICATIONS; i++) {
                int toAccount = (int) (bank.size() * Math.random());
                int amount = (int) (500 * maxAmount * Math.random() / REPLICATIONS);

                // use lock
                //this.bank.lockTransfer(fromAccount, toAccount, amount);

                // synchronized block
                //this.bank.syncStatementTransfer(fromAccount, toAccount, amount);

                // tryLock
                // this.bank.tryLockTransfer(fromAccount, toAccount, amount);

                // synchronized method
                this.bank.syncMethodTransfer(fromAccount, toAccount, amount);

            }
        }

    }
}
