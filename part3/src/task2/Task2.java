package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

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

class GradeAssignment extends RecursiveAction {
    private final List<Student> students;

    GradeAssignment(List<Student> students) {
        this.students = students;
    }

    @Override
    protected void compute() {
        students.forEach(student -> {
            int points = (int) (Math.random() * 100);
            student.addGrade(new Grade(points));
        });
    }
}

class SimpleGradeBook {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        List<Student> students = List.of(new Student("Alice"), new Student("Bob"), new Student("Charlie"));

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        GradeAssignment task = new GradeAssignment(students);

        forkJoinPool.invoke(task);

        students.forEach(System.out::println);

        long endTime = System.nanoTime();
        long durationInMilliseconds = (endTime - startTime) / 1_000_000;
        System.out.println("Execution time: " + durationInMilliseconds + " ms");

        forkJoinPool.shutdown();
    }
}