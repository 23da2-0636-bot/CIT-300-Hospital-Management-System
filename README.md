# Mini Hospital Emergency Management System

**Course:** CIT300 – Data Structures and Algorithms
**Assignment:** Individual Mid Assignment

A Java console application that simulates patient registration, emergency
treatment queueing, treatment history, and per‑patient visit history using
four hand-built data structures.

## Data Structures Used

| # | Requirement | Data Structure | File(s) |
|---|-------------|-----------------|---------|
| 1 | Patient Records | Binary Search Tree (keyed by Patient ID) | `PatientBST.java` |
| 2 | Emergency Patient Queue | Custom FIFO Queue (linked nodes) | `EmergencyQueue.java` |
| 3 | Treatment History | Custom LIFO Stack (linked nodes) | `TreatmentStack.java`, `TreatmentRecord.java` |
| 4 | Patient Visit History | Singly Linked List (per patient) | `VisitHistory.java`, `Visit.java` |

Supporting files:
- `Patient.java` – patient record model (owns its own `VisitHistory`)
- `ConsoleUtil.java` – boxed/table console output helper (cosmetic only)
- `Main.java` – menu-driven program tying everything together

All four structures are implemented **from scratch** (no `java.util.Stack`,
`java.util.Queue`, or `java.util.LinkedList`) so the underlying node logic is
fully visible for grading and for the demonstration video.

## How to Compile & Run

Requires **JDK 17+**.

```bash
cd src
javac *.java -d ../bin
cd ../bin
java Main
```

> **Windows Command Prompt users:** if the box-drawing characters look like
> `?` symbols, run `chcp 65001` before `java Main` to switch the console to
> UTF‑8, or run it from IntelliJ/Eclipse's built-in console instead.

## Menu Overview

```
PATIENT RECORDS (BST)              EMERGENCY QUEUE
 1. Register New Patient            5. View Waiting Queue
 2. Search Patient by ID            6. Treat Next Patient (Dequeue)
 3. Delete Patient
 4. Display All Patients            TREATMENT HISTORY (STACK)
                                     7. View Treatment History
PATIENT VISIT HISTORY (LIST)        8. Undo Last Treatment (Pop)
 9. Add Visit Record
10. View Visit History              0. Exit
11. Remove Visit Record
12. Search Visit Record
```

## Design Notes

- **BST delete** uses the standard in-order-successor replacement strategy
  for nodes with two children.
- **Queue / Stack** are implemented with singly linked nodes rather than
  arrays, so there is no fixed capacity and no "queue is full" case to
  handle — only the empty case, as required.
- **Registering** a patient inserts into the BST *and* enqueues them into the
  ER queue in one step, modelling a patient who has just arrived.
- **Treating** a patient dequeues them, pushes a `TreatmentRecord` onto the
  stack, and optionally appends a `Visit` to that patient's own linked list —
  showing how all four structures cooperate in one workflow.

## Sample Data

Two sample patients (IDs 101, 102) are pre-loaded on startup purely so the
menu has data to demonstrate immediately; feel free to remove
`seedSampleData()` in `Main.java` before final submission if you'd rather
start from an empty system.
