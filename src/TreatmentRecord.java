/**
 * TreatmentRecord
 * ------------------------------------------------------------
 * Represents one completed treatment event, stored in the
 * Treatment History Stack.
 * ------------------------------------------------------------
 */
public class TreatmentRecord {

    private final int patientId;
    private final String patientName;
    private final String medicalCondition;
    private final String completedAt;

    public TreatmentRecord(int patientId, String patientName, String medicalCondition, String completedAt) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.medicalCondition = medicalCondition;
        this.completedAt = completedAt;
    }

    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getMedicalCondition() { return medicalCondition; }
    public String getCompletedAt() { return completedAt; }
}
