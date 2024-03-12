package task6;

public class Main {
    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                sharedClass.increment();
                System.out.println(sharedClass.y);

            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                sharedClass.decrement();
                System.out.println(sharedClass.y);
            }

        });

        thread1.start();
        thread2.start();
    }

    public static class SharedClass {
        private int y = 0;

        public synchronized void increment() {

            y++;

        }

        public synchronized void decrement() {
            y--;
        }

//        public void increment() {
//
//            y++;
//
//        }
//
//        public void decrement() {
//            y--;
//        }


    }
}
