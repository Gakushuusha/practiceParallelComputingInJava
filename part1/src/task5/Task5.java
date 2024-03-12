package task5;

public class Task5 {
    public static void main(String[] args) {
        System.out.println("Technologies of parallel computing\nPart 1");
        System.out.println("Task 5");
//
//        Thread first = new Thread(new SymbolAsync('-'));
//        Thread second = new Thread(new SymbolAsync('|'));

        Manager manager = new Manager(80, 10);

        Thread first = new Thread(new SymbolSync('-', manager, true));
        Thread second = new Thread(new SymbolSync('|', manager, false));

        first.start();
        second.start();
    }
}