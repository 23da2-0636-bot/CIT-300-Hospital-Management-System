import java.util.ArrayList;
import java.util.List;

/**
 * VisitHistory  (Requirement 4 — Singly Linked List, 20 Marks)
 * ------------------------------------------------------------
 * A custom singly linked list (no java.util.LinkedList used)
 * that stores a patient's past visits in chronological order
 * (newest visit added at the tail).
 *
 * Supported operations: add, remove(by visitId), search(by visitId),
 * display (traverse from head to tail).
 * ------------------------------------------------------------
 */
public class VisitHistory {

    /** Internal node of the singly linked list. */
    private static class Node {
        Visit data;
        Node next;
        Node(Visit data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size;

    /** Adds a new visit at the end of the list. O(1) thanks to tail pointer. */
    public void addVisit(Visit visit) {
        Node newNode = new Node(visit);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /** Removes a visit by its visitId. Returns true if removed. */
    public boolean removeVisit(int visitId) {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current.data.getVisitId() == visitId) {
                if (previous == null) {
                    head = current.next;       // removing the head
                } else {
                    previous.next = current.next;
                }
                if (current == tail) {
                    tail = previous;           // removing the tail
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false; // not found
    }

    /** Searches for a visit by visitId. Returns null if not found. */
    public Visit searchVisit(int visitId) {
        Node current = head;
        while (current != null) {
            if (current.data.getVisitId() == visitId) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /** Returns all visits as a List, in list order (head to tail), for display. */
    public List<Visit> toList() {
        List<Visit> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }
}
