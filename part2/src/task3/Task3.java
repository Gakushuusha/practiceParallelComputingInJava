package task3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Grade {
    private int points;
    private String grade;

    public Grade(int points) {
        this.points = points;
        this.grade = calculateGrade(points);
    }

    private String calculateGrade(int points) {
        if (points >= 95) return "A";
        else if (points >= 85) return "B";
        else if (points >= 75) return "C";
        else if (points >= 65) return "D";
        else if (points >= 60) return "E";
        else return "F";
    }

    @Override
    public String toString() {
        return grade + " (" + points + ")";
    }
}

class Student {
    private final String name;
    private final List<Grade> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public synchronized void addGrade(Grade grade) {
        grades.add(grade);
    }

    @Override
    public String toString() {
        return name + " Grades: " + grades;
    }
}

class Teacher implements Runnable {
    private final List<Student> students;

    public Teacher(List<Student> students) {
        this.students = students;
    }

    @Override
    public void run() {
        students.forEach(student -> {
            int points = (int) (Math.random() * 100);
            student.addGrade(new Grade(points));
        });
    }
}

class SimpleGradeBook {
    public static void main(String[] args) throws InterruptedException {
        List<Student> students = List.of(
                new Student("Alice"), new Student("Bob"), new Student("Charlie"));

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            executor.execute(new Teacher(students));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        students.forEach(System.out::println);
    }
}