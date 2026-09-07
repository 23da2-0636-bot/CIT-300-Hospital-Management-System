import java.util.ArrayList;
import java.util.List;

/**
 * PatientBST  (Requirement 1 — Binary Search Tree, 20 Marks)
 * ------------------------------------------------------------
 * Stores Patient records keyed by patientId.
 * Supports insert, search, delete, and in-order traversal
 * (which naturally returns patients sorted by ascending ID).
 * ------------------------------------------------------------
 */
public class PatientBST {

    /** Internal BST node. */
    private static class Node {
        Patient patient;
        Node left, right;
        Node(Patient patient) { this.patient = patient; }
    }

    private Node root;
    private int count;

    // ---------------------------------------------------------
    // INSERT
    // ---------------------------------------------------------
    public boolean insert(Patient patient) {
        if (search(patient.getPatientId()) != null) {
            return false; // duplicate ID not allowed
        }
        root = insertRec(root, patient);
        count++;
        return true;
    }

    private Node insertRec(Node node, Patient patient) {
        if (node == null) return new Node(patient);
        if (patient.getPatientId() < node.patient.getPatientId()) {
            node.left = insertRec(node.left, patient);
        } else {
            node.right = insertRec(node.right, patient);
        }
        return node;
    }

    // ---------------------------------------------------------
    // SEARCH
    // ---------------------------------------------------------
    public Patient search(int patientId) {
        Node current = root;
        while (current != null) {
            if (patientId == current.patient.getPatientId()) return current.patient;
            current = (patientId < current.patient.getPatientId()) ? current.left : current.right;
        }
        return null;
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------
    public boolean delete(int patientId) {
        if (search(patientId) == null) return false;
        root = deleteRec(root, patientId);
        count--;
        return true;
    }

    private Node deleteRec(Node node, int patientId) {
        if (node == null) return null;

        if (patientId < node.patient.getPatientId()) {
            node.left = deleteRec(node.left, patientId);
        } else if (patientId > node.patient.getPatientId()) {
            node.right = deleteRec(node.right, patientId);
        } else {
            // Node found
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Two children: replace with in-order successor (smallest in right subtree)
            Node successor = findMin(node.right);
            node.patient = successor.patient;
            node.right = deleteRec(node.right, successor.patient.getPatientId());
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ---------------------------------------------------------
    // IN-ORDER TRAVERSAL -> ascending order of Patient ID
    // ---------------------------------------------------------
    public List<Patient> inOrderTraversal() {
        List<Patient> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node node, List<Patient> result) {
        if (node == null) return;
        inOrderRec(node.left, result);
        result.add(node.patient);
        inOrderRec(node.right, result);
    }

    public boolean isEmpty() { return root == null; }
    public int size() { return count; }
}
