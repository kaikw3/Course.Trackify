package ui;

/* Citation

Title: TellerApp Source Code
Author: CPSC 210 Team
Date: July 2023
Availability: https://github.students.cs.ubc.ca/CPSC210/TellerApp

 */


import model.Course;
import model.CoursesTaken;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


// Represents the user interface console
public class CourseListApp {
    private static final String JSON_STORAGE = "./data/coursesTaken.json";
    private CoursesTaken coursestaken;
    private Scanner input;
    private Course removeCourse;
    private Course changedCourse;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private Event event;
    private EventLog events;


    public static void main(String[] args) throws FileNotFoundException {
        new CourseListApp();
    }


    // EFFECTS: runs the course list Application
    public CourseListApp() throws FileNotFoundException {
        runCourseList();
    }

    // MODIFIES: this
    // EFFECTS: processes the user input
    private void runCourseList() {
        boolean appRun = true;
        String action;

        initialize();

        while (appRun) {
            displayCourseListMenu();
            action = input.next();
            action = action.toLowerCase();

            if (action.equals("quit")) {
                System.out.println("\t");
                System.out.println("Thanks for using CourseTracker!");

                for (Event e : EventLog.getInstance()) {
                    System.out.println(e);
                }

                appRun = false;
            } else {
                processAction(action);
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: processes the user command
    public void processAction(String action) {
        if (action.equals("add")) {
            doAddCourse();
        } else if (action.equals("remove")) {
            doRemoveCourse();
        } else if (action.equals("view")) {
            doViewCourseList();
        } else if (action.equals("change grade")) {
            doSetCourseGrade();
        } else if (action.equals("save")) {
            doSaveList();
        } else if (action.equals("load")) {
            doLoadList();
        } else {
            System.out.println("Sorry, please input a valid selection.");
        }
        System.out.println("\t");
    }

    // MODIFIES: this
    // EFFECTS: initializes course and courses taken list
    private void initialize() {
        coursestaken = new CoursesTaken("Kai's Course List");
        input = new Scanner(System.in).useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORAGE);
        jsonReader = new JsonReader(JSON_STORAGE);

    }

    // EFFECTS: Displays the main menu option to user
    private void displayCourseListMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("\t \"add\" -> Add a course");
        System.out.println("\t \"remove\" -> Remove a course");
        System.out.println("\t \"view\" -> View your course list");
        System.out.println("\t \"change grade\" -> Change grade of a course");
        System.out.println("\t \"save\" -> save course list to file");
        System.out.println("\t \"load\" -> load course list from file");
        System.out.println("\t \"quit\" -> Quit Application");
        System.out.println("\nWelcome to CourseTracker, your personal course tracking application! "
                + "Please select a menu option from above by typing in the word of the action: ");

    }

    // REQUIRES: 0 <= course grade entered <= 100
    // MODIFIES: this
    // EFFECTS: adds a course to courses taken
    private void doAddCourse() {
        System.out.println("Please type the Course Code of the Course you would lie to add (ie. CPSC 103): ");
        String courseCode = input.next();
        System.out.println("Please type the name of the Course (ie. Introduction to Systematic Program Design): ");
        String courseName = input.next();
        System.out.println("Enter the grade you got in the course. "
                + "If you haven't received a grade yet, put 0 as a placeholder: ");
        Double courseGrade = input.nextDouble();
        Course course = new Course(courseCode, courseName, courseGrade);
        if (coursestaken.addCourse(course)) {
            coursestaken.addCourse(course);

            System.out.println("\t");
            System.out.println(course.getCourseCode() + ": "
                    + course.getCourseName() + " has been added to your list!");
        } else {
            System.out.println("\t");
            System.out.println("Sorry, this course has already been added to your list!");
        }
    }

    // MODIFIES: this
    // EFFECTS: removes a course from courses taken
    private void doRemoveCourse() {

        if (coursestaken.getList().isEmpty()) {
            System.out.println("Sorry, you have not added any courses.");
        } else {
            doViewCourseList();
            System.out.println("\nPlease type the Course Code of the Course you would lie to remove"
                    + " from the list above (ie. CPSC 103): ");
            String courseInput = input.next();
            for (Course c : coursestaken.getList()) {
                if (courseInput.toLowerCase().equals(c.getCourseCode().toLowerCase())) {

                    System.out.println(c.getCourseCode()
                            + ": " + c.getCourseName() + " has been removed!");

                    removeCourse = c;
                }
            }
            if (coursestaken.removeCourse(removeCourse)) {
                coursestaken.removeCourse(removeCourse);
            } else {
                System.out.println("\nSorry your course list does not contain that course. Please make sure the "
                        + "course code is spelt correctly!");
            }
        }
    }

    // EFFECTS: displays the courses in courses taken list
    private void doViewCourseList() {
        System.out.println("\nYOUR COURSES:");

        if (coursestaken.getList().isEmpty()) {
            System.out.println("Sorry, you Courses Taken list is empty.");
        } else {
            for (Course c : coursestaken.getList()) {
                System.out.println(c.getCourseCode() + " | "
                        + c.getCourseName() + " | " + c.getCourseGrade());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the course grade of a specified course in course taken
    private void doSetCourseGrade() {
        if (coursestaken.getList().isEmpty()) {
            System.out.println("Sorry, you have not added any courses to your list.");
        } else {
            doViewCourseList();
            System.out.println("\nFrom the list above, enter the course code of the"
                    + " course you would like to change the grade of (ie. CPSC 103):");
            String courseInput = input.next();
            System.out.println("Enter the grade you would like to change to.");
            double courseGradeInput = input.nextDouble();
            for (Course c : coursestaken.getList()) {
                if (courseInput.toLowerCase().equals(c.getCourseCode().toLowerCase())) {

                    c.setCourseGrade(courseGradeInput);
                    changedCourse = c;

                }
            }
            if (coursestaken.getList().contains(changedCourse)) {
                System.out.println("Course Grade successfully changed!");
                changedCourse = null;
            } else {
                System.out.println("\nSorry your course list does not contain that course. Please make sure the "
                        + "course code is spelt correctly!");
            }
        }

    }

    // EFFECTS: saves current courses taken list to file
    private void doSaveList() {
        try {
            jsonWriter.open();
            jsonWriter.write(coursestaken);
            jsonWriter.close();
            System.out.println("Course list saved to" + JSON_STORAGE);
        } catch (FileNotFoundException e) {
            System.out.println("Saved failed.");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads courses taken list from file
    private void doLoadList() {
        try {
            coursestaken = jsonReader.reader();
            System.out.println("Loaded course list from" + JSON_STORAGE);
        } catch (IOException e) {
            System.out.println("Load Failed.");
        }
    }
}
