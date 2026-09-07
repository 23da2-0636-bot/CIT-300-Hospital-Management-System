/**
 * Patient
 * ------------------------------------------------------------
 * Represents a single patient record stored in the BST.
 * Each patient owns its own Visit History (Singly Linked List).
 * ------------------------------------------------------------
 */
public class Patient {

    private final int patientId;
    private String name;
    private int age;
    private String contactNumber;
    private String medicalCondition;

    // Requirement 4: each patient has a singly linked list of visits
    private final VisitHistory visitHistory;

    public Patient(int patientId, String name, int age, String contactNumber, String medicalCondition) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.contactNumber = contactNumber;
        this.medicalCondition = medicalCondition;
        this.visitHistory = new VisitHistory();
    }

    public int getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContactNumber() { return contactNumber; }
    public String getMedicalCondition() { return medicalCondition; }
    public VisitHistory getVisitHistory() { return visitHistory; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }

    @Override
    public String toString() {
        return "Patient{id=" + patientId + ", name='" + name + "', age=" + age
                + ", contact='" + contactNumber + "', condition='" + medicalCondition + "'}";
    }
}
