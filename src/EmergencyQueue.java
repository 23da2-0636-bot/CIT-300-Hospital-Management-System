import java.util.ArrayList;
import java.util.List;

/**
 * EmergencyQueue  (Requirement 2 — Queue, 20 Marks)
 * ------------------------------------------------------------
 * A custom singly linked, FIFO queue built from scratch
 * (no java.util.Queue) to hold patients waiting to be treated.
 *
 * front -> oldest patient (next to be treated)
 * rear  -> newest patient (most recently arrived)
 * ------------------------------------------------------------
 */
public class EmergencyQueue {

    private static class Node {
        Patient patient;
        Node next;
        Node(Patient patient) { this.patient = patient; }
    }

    private Node front;
    private Node rear;
    private int size;

    /** Enqueue: add a patient to the back of the waiting line. */
    public void enqueue(Patient patient) {
        Node newNode = new Node(patient);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /** Dequeue: remove and return the next patient to be treated. */
    public Patient dequeue() {
        if (isEmpty()) {
            return null; // caller must handle empty-queue case
        }
        Patient patient = front.patient;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return patient;
    }

    /** Peek at the next patient without removing them. */
    public Patient peekFront() {
        return isEmpty() ? null : front.patient;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    /** Returns waiting patients in queue order (front to rear) for display. */
    public List<Patient> toList() {
        List<Patient> result = new ArrayList<>();
        Node current = front;
        while (current != null) {
            result.add(current.patient);
            current = current.next;
        }
        return result;
    }
}
