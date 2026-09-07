import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Main
 * ------------------------------------------------------------
 * Mini Hospital Emergency Management System
 * CIT300 - Data Structures and Algorithms - Individual Mid Assignment
 *
 * Ties together all four required data structures:
 *   1. PatientBST      - patient records
 *   2. EmergencyQueue   - waiting patients (FIFO)
 *   3. TreatmentStack   - completed treatment history (LIFO)
 *   4. VisitHistory     - per-patient past-visit linked list
 * ------------------------------------------------------------
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final PatientBST patientBST = new PatientBST();
    private static final EmergencyQueue emergencyQueue = new EmergencyQueue();
    private static final TreatmentStack treatmentStack = new TreatmentStack();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        boolean running = true;

        ConsoleUtil.printHeader("MINI HOSPITAL EMERGENCY MANAGEMENT SYSTEM");
        seedSampleData();

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> searchPatient();
                case 3 -> deletePatient();
                case 4 -> displayAllPatients();
                case 5 -> viewQueue();
                case 6 -> treatNextPatient();
                case 7 -> viewTreatmentHistory();
                case 8 -> undoLastTreatment();
                case 9 -> addVisitRecord();
                case 10 -> viewVisitHistory();
                case 11 -> removeVisitRecord();
                case 12 -> searchVisitRecord();
                case 0 -> {
                    ConsoleUtil.info("Thank you for using the Hospital ER System. Goodbye!");
                    running = false;
                }
                default -> ConsoleUtil.error("Invalid choice. Please select a valid menu option.");
            }
        }
        sc.close();
    }

    // ==========================================================
    // MENU
    // ==========================================================
    private static void printMenu() {
        System.out.println();
        ConsoleUtil.printBox(
                ConsoleUtil.BOLD + "PATIENT RECORDS (BST)" + ConsoleUtil.RESET,
                " 1. Register New Patient",
                " 2. Search Patient by ID",
                " 3. Delete Patient",
                " 4. Display All Patients (sorted by ID)",
                "",
                ConsoleUtil.BOLD + "EMERGENCY QUEUE" + ConsoleUtil.RESET,
                " 5. View Waiting Queue",
                " 6. Treat Next Patient (Dequeue)",
                "",
                ConsoleUtil.BOLD + "TREATMENT HISTORY (STACK)" + ConsoleUtil.RESET,
                " 7. View Treatment History",
                " 8. Undo Last Treatment (Pop)",
                "",
                ConsoleUtil.BOLD + "PATIENT VISIT HISTORY (LINKED LIST)" + ConsoleUtil.RESET,
                " 9. Add Visit Record",
                "10. View Visit History",
                "11. Remove Visit Record",
                "12. Search Visit Record",
                "",
                " 0. Exit"
        );
    }

    // ==========================================================
    // 1. BST - REGISTER PATIENT (also enqueues into ER queue)
    // ==========================================================
    private static void registerPatient() {
        ConsoleUtil.printHeader("REGISTER NEW PATIENT");
        int id = readInt("Patient ID: ");
        if (patientBST.search(id) != null) {
            ConsoleUtil.error("A patient with ID " + id + " already exists.");
            return;
        }
        String name = readLine("Patient Name: ");
        int age = readInt("Age: ");
        String contact = readLine("Contact Number: ");
        String condition = readLine("Medical Condition: ");

        Patient patient = new Patient(id, name, age, contact, condition);
        patientBST.insert(patient);
        emergencyQueue.enqueue(patient); // newly arrived patient joins the ER queue

        ConsoleUtil.success("Patient '" + name + "' (ID: " + id + ") registered and added to the ER queue.");
    }

    // ==========================================================
    // 1. BST - SEARCH
    // ==========================================================
    private static void searchPatient() {
        ConsoleUtil.printHeader("SEARCH PATIENT");
        int id = readInt("Enter Patient ID to search: ");
        Patient p = patientBST.search(id);
        if (p == null) {
            ConsoleUtil.error("No patient found with ID " + id + ".");
        } else {
            ConsoleUtil.printTable(
                    new String[]{"ID", "Name", "Age", "Contact", "Condition"},
                    List.<String[]>of(new String[]{
                            String.valueOf(p.getPatientId()), p.getName(), String.valueOf(p.getAge()),
                            p.getContactNumber(), p.getMedicalCondition()
                    })
            );
        }
    }

    // ==========================================================
    // 1. BST - DELETE
    // ==========================================================
    private static void deletePatient() {
        ConsoleUtil.printHeader("DELETE PATIENT");
        int id = readInt("Enter Patient ID to delete: ");
        if (patientBST.delete(id)) {
            ConsoleUtil.success("Patient ID " + id + " deleted from records.");
        } else {
            ConsoleUtil.error("No patient found with ID " + id + ".");
        }
    }

    // ==========================================================
    // 1. BST - IN-ORDER DISPLAY
    // ==========================================================
    private static void displayAllPatients() {
        ConsoleUtil.printHeader("ALL PATIENTS (In-Order Traversal — Ascending ID)");
        List<Patient> patients = patientBST.inOrderTraversal();
        String[][] rowsArr = patients.stream()
                .map(p -> new String[]{
                        String.valueOf(p.getPatientId()), p.getName(), String.valueOf(p.getAge()),
                        p.getContactNumber(), p.getMedicalCondition()
                }).toArray(String[][]::new);
        ConsoleUtil.printTable(new String[]{"ID", "Name", "Age", "Contact", "Condition"}, List.of(rowsArr));
        ConsoleUtil.info("Total patients: " + patientBST.size());
    }

    // ==========================================================
    // 2. QUEUE - VIEW
    // ==========================================================
    private static void viewQueue() {
        ConsoleUtil.printHeader("EMERGENCY WAITING QUEUE (FIFO)");
        if (emergencyQueue.isEmpty()) {
            ConsoleUtil.info("The emergency queue is currently empty.");
            return;
        }
        List<Patient> waiting = emergencyQueue.toList();
        String[][] rowsArr = waiting.stream()
                .map(p -> new String[]{String.valueOf(p.getPatientId()), p.getName(), p.getMedicalCondition()})
                .toArray(String[][]::new);
        ConsoleUtil.printTable(new String[]{"ID", "Name", "Condition"}, List.of(rowsArr));
        ConsoleUtil.info("Patients waiting: " + emergencyQueue.size());
    }

    // ==========================================================
    // 2. QUEUE - DEQUEUE + push into stack + add visit
    // ==========================================================
    private static void treatNextPatient() {
        ConsoleUtil.printHeader("TREAT NEXT PATIENT");
        if (emergencyQueue.isEmpty()) {
            ConsoleUtil.error("Cannot treat: the emergency queue is empty.");
            return;
        }
        Patient patient = emergencyQueue.dequeue();
        String now = LocalDateTime.now().format(FMT);

        // Move to treatment history (Stack)
        treatmentStack.push(new TreatmentRecord(
                patient.getPatientId(), patient.getName(), patient.getMedicalCondition(), now));

        ConsoleUtil.success("Now treating: " + patient.getName() + " (ID: " + patient.getPatientId() + ")");

        // Optionally record this as a visit in the patient's history
        if (readLine("Add this as a visit record for the patient? (y/n): ").equalsIgnoreCase("y")) {
            int visitId = readInt("Visit ID: ");
            String doctor = readLine("Doctor Name: ");
            String diagnosis = readLine("Diagnosis: ");
            String treatment = readLine("Treatment Given: ");
            patient.getVisitHistory().addVisit(new Visit(visitId, now, doctor, diagnosis, treatment));
            ConsoleUtil.success("Visit record added to patient's history.");
        }
    }

    // ==========================================================
    // 3. STACK - VIEW
    // ==========================================================
    private static void viewTreatmentHistory() {
        ConsoleUtil.printHeader("TREATMENT HISTORY (LIFO — most recent first)");
        if (treatmentStack.isEmpty()) {
            ConsoleUtil.info("No treatments have been completed yet.");
            return;
        }
        List<TreatmentRecord> records = treatmentStack.toList();
        String[][] rowsArr = records.stream()
                .map(r -> new String[]{
                        String.valueOf(r.getPatientId()), r.getPatientName(), r.getMedicalCondition(), r.getCompletedAt()
                }).toArray(String[][]::new);
        ConsoleUtil.printTable(new String[]{"ID", "Name", "Condition", "Completed At"}, List.of(rowsArr));
    }

    // ==========================================================
    // 3. STACK - POP
    // ==========================================================
    private static void undoLastTreatment() {
        ConsoleUtil.printHeader("UNDO LAST TREATMENT");
        TreatmentRecord popped = treatmentStack.pop();
        if (popped == null) {
            ConsoleUtil.error("Cannot undo: the treatment history stack is empty.");
        } else {
            ConsoleUtil.success("Removed most recent treatment record for: " + popped.getPatientName()
                    + " (ID: " + popped.getPatientId() + ")");
        }
    }

    // ==========================================================
    // 4. LINKED LIST - ADD VISIT
    // ==========================================================
    private static void addVisitRecord() {
        ConsoleUtil.printHeader("ADD VISIT RECORD");
        Patient p = findPatientOrWarn();
        if (p == null) return;

        int visitId = readInt("Visit ID: ");
        String date = readLine("Visit Date (e.g. 2026-09-06): ");
        String doctor = readLine("Doctor Name: ");
        String diagnosis = readLine("Diagnosis: ");
        String treatment = readLine("Treatment: ");

        p.getVisitHistory().addVisit(new Visit(visitId, date, doctor, diagnosis, treatment));
        ConsoleUtil.success("Visit record added for " + p.getName() + ".");
    }

    // ==========================================================
    // 4. LINKED LIST - VIEW
    // ==========================================================
    private static void viewVisitHistory() {
        ConsoleUtil.printHeader("PATIENT VISIT HISTORY");
        Patient p = findPatientOrWarn();
        if (p == null) return;

        List<Visit> visits = p.getVisitHistory().toList();
        if (visits.isEmpty()) {
            ConsoleUtil.info(p.getName() + " has no recorded visits.");
            return;
        }
        String[][] rowsArr = visits.stream()
                .map(v -> new String[]{
                        String.valueOf(v.getVisitId()), v.getVisitDate(), v.getDoctorName(),
                        v.getDiagnosis(), v.getTreatment()
                }).toArray(String[][]::new);
        ConsoleUtil.printTable(new String[]{"Visit ID", "Date", "Doctor", "Diagnosis", "Treatment"}, List.of(rowsArr));
    }

    // ==========================================================
    // 4. LINKED LIST - REMOVE
    // ==========================================================
    private static void removeVisitRecord() {
        ConsoleUtil.printHeader("REMOVE VISIT RECORD");
        Patient p = findPatientOrWarn();
        if (p == null) return;

        int visitId = readInt("Visit ID to remove: ");
        if (p.getVisitHistory().removeVisit(visitId)) {
            ConsoleUtil.success("Visit ID " + visitId + " removed from " + p.getName() + "'s history.");
        } else {
            ConsoleUtil.error("Visit ID " + visitId + " not found in " + p.getName() + "'s history.");
        }
    }

    // ==========================================================
    // 4. LINKED LIST - SEARCH
    // ==========================================================
    private static void searchVisitRecord() {
        ConsoleUtil.printHeader("SEARCH VISIT RECORD");
        Patient p = findPatientOrWarn();
        if (p == null) return;

        int visitId = readInt("Visit ID to search: ");
        Visit v = p.getVisitHistory().searchVisit(visitId);
        if (v == null) {
            ConsoleUtil.error("Visit ID " + visitId + " not found.");
        } else {
            ConsoleUtil.printTable(
                    new String[]{"Visit ID", "Date", "Doctor", "Diagnosis", "Treatment"},
                    List.<String[]>of(new String[]{
                            String.valueOf(v.getVisitId()), v.getVisitDate(), v.getDoctorName(),
                            v.getDiagnosis(), v.getTreatment()
                    })
            );
        }
    }

    // ==========================================================
    // Helpers
    // ==========================================================
    private static Patient findPatientOrWarn() {
        int id = readInt("Enter Patient ID: ");
        Patient p = patientBST.search(id);
        if (p == null) {
            ConsoleUtil.error("No patient found with ID " + id + ".");
        }
        return p;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsoleUtil.error("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /** Pre-loads a couple of sample patients so the demo has data immediately. */
    private static void seedSampleData() {
        Patient p1 = new Patient(101, "Nimal Perera", 34, "0771234567", "Fracture - Left Arm");
        Patient p2 = new Patient(102, "Kamala Silva", 58, "0719876543", "Chest Pain");
        patientBST.insert(p1);
        patientBST.insert(p2);
        emergencyQueue.enqueue(p1);
        emergencyQueue.enqueue(p2);
        ConsoleUtil.info("Sample data loaded: 2 patients registered and waiting in queue.");
    }
}
