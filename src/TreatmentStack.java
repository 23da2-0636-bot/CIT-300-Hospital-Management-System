import java.util.ArrayList;
import java.util.List;

/**
 * TreatmentStack  (Requirement 3 — Stack, 20 Marks)
 * ------------------------------------------------------------
 * A custom singly linked LIFO stack (no java.util.Stack) that
 * stores completed treatment records. The most recently
 * completed treatment is always at the top.
 * ------------------------------------------------------------
 */
public class TreatmentStack {

    private static class Node {
        TreatmentRecord data;
        Node next;
        Node(TreatmentRecord data) { this.data = data; }
    }

    private Node top;
    private int size;

    /** Push: add a newly completed treatment record on top. */
    public void push(TreatmentRecord record) {
        Node newNode = new Node(record);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /** Pop: remove and return the most recently completed record. */
    public TreatmentRecord pop() {
        if (isEmpty()) {
            return null; // caller must handle empty-stack case
        }
        TreatmentRecord record = top.data;
        top = top.next;
        size--;
        return record;
    }

    /** Peek at the top record without removing it. */
    public TreatmentRecord peek() {
        return isEmpty() ? null : top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    /** Returns records top-to-bottom (most recent first) for display. */
    public List<TreatmentRecord> toList() {
        List<TreatmentRecord> result = new ArrayList<>();
        Node current = top;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
