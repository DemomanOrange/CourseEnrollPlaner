import java.util.*;

public class Main {

    // Course - set of prerequisites
    static HashMap<String, HashSet<String>> coursePrereqs = new HashMap<>();

    // Student - set of completed courses
    static HashMap<String, HashSet<String>> studentCompleted = new HashMap<>();


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        printHelp();

        // small empirical timing experiment
        timingTest();

        while (true) {

            System.out.print("> ");
            String line = sc.nextLine().trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split(" ");
            String cmd = parts[0].toUpperCase();

            try {

                switch (cmd) {

                    case "HELP":
                        printHelp();
                        break;

                    case "ADD_COURSE":
                        addCourse(parts);
                        break;

                    case "ADD_PREREQ":
                        addPrereq(parts);
                        break;

                    case "PREREQS":
                        showPrereqs(parts);
                        break;

                    case "COMPLETE":
                        completeCourse(parts);
                        break;

                    case "DONE":
                        showDone(parts);
                        break;

                    case "CAN_TAKE":
                        canTake(parts);
                        break;

                    case "EXIT":
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Unknown command. Type HELP.");

                }

            } catch (Exception e) {
                System.out.println("Invalid command format.");
            }

        }
    }


    static void printHelp() {

        System.out.println("Course Enrollment Planner — Commands:");
        System.out.println("HELP");
        System.out.println("ADD_COURSE <C>");
        System.out.println("ADD_PREREQ <C> <P>");
        System.out.println("PREREQS <C>");
        System.out.println("COMPLETE <student> <C>");
        System.out.println("DONE <student>");
        System.out.println("CAN_TAKE <student> <C>");
        System.out.println("EXIT");
        System.out.println();
    }


    static void addCourse(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: ADD_COURSE <C>");
            return;
        }

        String course = parts[1];

        coursePrereqs.putIfAbsent(course, new HashSet<>());

        System.out.println("Added course: " + course);
    }


    static void addPrereq(String[] parts) {

        if (parts.length < 3) {
            System.out.println("Usage: ADD_PREREQ <C> <P>");
            return;
        }

        String course = parts[1];
        String prereq = parts[2];

        coursePrereqs.putIfAbsent(course, new HashSet<>());
        coursePrereqs.putIfAbsent(prereq, new HashSet<>());

        coursePrereqs.get(course).add(prereq);

        System.out.println("Added prereq: " + prereq + " -> " + course);
    }


    static void showPrereqs(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: PREREQS <C>");
            return;
        }

        String course = parts[1];

        HashSet<String> prereqs = coursePrereqs.get(course);

        if (prereqs == null || prereqs.isEmpty()) {
            System.out.println("Prereqs for " + course + ": []");
        } else {
            System.out.println("Prereqs for " + course + ": " + prereqs);
        }
    }


    static void completeCourse(String[] parts) {

        if (parts.length < 3) {
            System.out.println("Usage: COMPLETE <student> <C>");
            return;
        }

        String student = parts[1];
        String course = parts[2];

        studentCompleted.putIfAbsent(student, new HashSet<>());

        studentCompleted.get(student).add(course);

        System.out.println(student + " completed " + course);
    }


    static void showDone(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: DONE <student>");
            return;
        }

        String student = parts[1];

        HashSet<String> done = studentCompleted.get(student);

        if (done == null || done.isEmpty()) {
            System.out.println("No record");
        } else {
            System.out.println("Completed courses: " + done);
        }
    }


    static void canTake(String[] parts) {

        if (parts.length < 3) {
            System.out.println("Usage: CAN_TAKE <student> <C>");
            return;
        }

        String student = parts[1];
        String course = parts[2];

        HashSet<String> prereqs = coursePrereqs.get(course);

        if (prereqs == null || prereqs.isEmpty()) {
            System.out.println("YES");
            return;
        }

        HashSet<String> completed = studentCompleted.getOrDefault(student, new HashSet<>());

        for (String p : prereqs) {

            if (!completed.contains(p)) {
                System.out.println("NO");
                return;
            }

        }

        System.out.println("YES");
    }


    // small empirical timing experiment
    static void timingTest() {

        System.out.println("Running small timing experiment...");

        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < 100000; i++) {
            set.add("Course" + i);
        }

        long total = 0;

        for (int run = 1; run <= 5; run++) {

            long start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                set.contains("Course50000");
            }

            long end = System.nanoTime();

            long time = end - start;

            System.out.println("Run " + run + ": " + time + " ns");

            total += time;
        }

        System.out.println("Average time: " + (total / 5) + " ns");
        System.out.println();
    }
}
//> ADD_COURSE CS101
//Added course: CS101
//
//> ADD_PREREQ CS102 CS101
//Added prereq: CS101 -> CS102
//
//> COMPLETE Daniel CS101
//Daniel completed CS101
//
//> CAN_TAKE Daniel CS102
//YES