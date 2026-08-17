/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.balafamily.st10474385.prog6112.assignment1;

/**
 *
 * @author KhanyisaB
 */
import java.util.ArrayList;
import java.util.Scanner;

public class ST10474385PROG6112Assignment1 {

    static ArrayList<clsPatient> patients = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    // ==========================================
    // BED MANAGEMENT
    // ==========================================

    // 20 beds arranged in a 4 x 5 layout
    static String[][] beds = {
        {"B01", "B02", "B03", "B04", "B05"},
        {"B06", "B07", "B08", "B09", "B10"},
        {"B11", "B12", "B13", "B14", "B15"},
        {"B16", "B17", "B18", "B19", "B20"}
    };

    // Stores the Patient ID occupying each bed.
    // null means the bed is available.
    static String[][] bedOccupants = new String[4][5];

    static final int TOTAL_BEDS = 20;

    // ==========================================
    // MAIN
    // ==========================================

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n=================================");
            System.out.println("PATIENT MANAGEMENT SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Register New Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete / Discharge Patient");
            System.out.println("5. Display All Patients");
            System.out.println("---------------------------------");
            System.out.println("6. Allocate Bed");
            System.out.println("7. Release Bed");
            System.out.println("8. Display Ward Layout");
            System.out.println("9. Display Available Beds");
            System.out.println("10. Display Occupied Beds");
            System.out.println("---------------------------------");
            System.out.println("11. Reports");
            System.out.println("12. Exit");
            System.out.println("=================================");

            choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    registerPatient();
                    break;

                case 2:
                    searchPatient();
                    break;

                case 3:
                    updatePatient();
                    break;

                case 4:
                    deletePatient();
                    break;

                case 5:
                    displayAllPatients();
                    break;

                case 6:
                    allocateBed();
                    break;

                case 7:
                    releaseBed();
                    break;

                case 8:
                    displayWardLayout();
                    break;

                case 9:
                    displayAvailableBeds();
                    break;

                case 10:
                    displayOccupiedBeds();
                    break;

                case 11:
                    displayReports();
                    break;

                case 12:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 12);

        scanner.close();
    }

    // ==========================================
    // 1. REGISTER NEW PATIENT
    // ==========================================

    public static void registerPatient() {

        System.out.println("\n--- Register New Patient ---");

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        // Check if ID already exists
        if (findPatient(patientId) != null) {
            System.out.println("A patient with this ID already exists.");
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        int age = readInt("Enter Age: ");

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Enter Medical Condition: ");
        String medicalCondition = scanner.nextLine();

        String patientCategory = getPatientCategory();

        clsPatient patient = new clsPatient(
                patientId,
                firstName,
                lastName,
                age,
                gender,
                medicalCondition,
                patientCategory
        );

        patients.add(patient);

        System.out.println("Patient registered successfully!");

        // Only Inpatients can be allocated beds
        if (patientCategory.equalsIgnoreCase("Inpatient")) {

            System.out.println("\nThis patient is an Inpatient.");

            if (getAvailableBedCount() > 0) {

                System.out.print("Would you like to allocate a bed now? (Y/N): ");
                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("Y")) {
                    allocateBedToPatient(patient);
                }

            } else {
                System.out.println("WARNING: No beds are currently available.");
                System.out.println("The patient has been registered but no bed was allocated.");
            }
        }
    }

    // ==========================================
    // 2. SEARCH PATIENT
    // ==========================================

    public static void searchPatient() {

        System.out.println("\n--- Search Patient ---");

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        clsPatient patient = findPatient(patientId);

        if (patient != null) {
            System.out.println("\nPatient found:");
            patient.displayPatient();

            if (patient.getBedNumber() != null) {
                System.out.println("Allocated Bed: " + patient.getBedNumber());
            } else {
                System.out.println("Allocated Bed: None");
            }

        } else {
            System.out.println("Patient not found.");
        }
    }

    // ==========================================
    // 3. UPDATE PATIENT
    // ==========================================

    public static void updatePatient() {

        System.out.println("\n--- Update Patient ---");

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        clsPatient patient = findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nCurrent patient details:");
        patient.displayPatient();

        String oldCategory = patient.getPatientCategory();

        System.out.println("\nEnter new details:");

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        int age = readInt("Enter Age: ");

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Enter Medical Condition: ");
        String medicalCondition = scanner.nextLine();

        String newCategory = getPatientCategory();

        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalCondition(medicalCondition);
        patient.setPatientCategory(newCategory);

        // ==========================================
        // BED MANAGEMENT DURING UPDATE
        // ==========================================

        // If patient was previously an inpatient
        // but is now another category, release their bed.
        if (oldCategory.equalsIgnoreCase("Inpatient")
                && !newCategory.equalsIgnoreCase("Inpatient")) {

            if (patient.getBedNumber() != null) {

                releaseBedFromPatient(patient);

                System.out.println("Patient category changed.");
                System.out.println("Their previous bed has been released.");
            }
        }

        // If patient changes from another category
        // to Inpatient, offer bed allocation.
        if (!oldCategory.equalsIgnoreCase("Inpatient")
                && newCategory.equalsIgnoreCase("Inpatient")) {

            if (patient.getBedNumber() == null) {

                if (getAvailableBedCount() > 0) {

                    System.out.print(
                            "Patient is now an Inpatient. "
                            + "Would you like to allocate a bed? (Y/N): "
                    );

                    String answer = scanner.nextLine();

                    if (answer.equalsIgnoreCase("Y")) {
                        allocateBedToPatient(patient);
                    }

                } else {
                    System.out.println("No beds are currently available.");
                }
            }
        }

        System.out.println("Patient details updated successfully!");
    }

    // ==========================================
    // 4. DELETE / DISCHARGE PATIENT
    // ==========================================

    public static void deletePatient() {

        System.out.println("\n--- Delete / Discharge Patient ---");

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        clsPatient patient = findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\nPatient to be deleted:");
        patient.displayPatient();

        if (patient.getBedNumber() != null) {
            System.out.println("Allocated Bed: " + patient.getBedNumber());
        }

        System.out.print(
                "Are you sure you want to delete/discharge this patient? (Y/N): "
        );

        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Y")) {

            // Release bed before deleting patient
            if (patient.getBedNumber() != null) {
                releaseBedFromPatient(patient);
            }

            patients.remove(patient);

            System.out.println("Patient discharged/deleted successfully!");

        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    // ==========================================
    // 5. DISPLAY ALL PATIENTS
    // ==========================================

    public static void displayAllPatients() {

        System.out.println("\n--- All Registered Patients ---");

        if (patients.isEmpty()) {
            System.out.println("No patients are currently registered.");
            return;
        }

        System.out.println("Total Patients: " + patients.size());

        for (clsPatient patient : patients) {
            patient.displayPatient();

            if (patient.getBedNumber() != null) {
                System.out.println("Allocated Bed: "
                        + patient.getBedNumber());
            } else {
                System.out.println("Allocated Bed: None");
            }

            System.out.println("-----------------------------------");
        }
    }

    // ==========================================
    // 6. ALLOCATE BED
    // ==========================================

    public static void allocateBed() {

        System.out.println("\n--- Allocate Bed ---");

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        clsPatient patient = findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        allocateBedToPatient(patient);
    }

    // ==========================================
    // ALLOCATE BED TO PATIENT
    // ==========================================

    public static void allocateBedToPatient(clsPatient patient) {

        // Only Inpatients may have beds
        if (!patient.getPatientCategory().equalsIgnoreCase("Inpatient")) {

            System.out.println(
                    "ERROR: Only Inpatients may be allocated a hospital bed."
            );

            return;
        }

        // Check if patient already has a bed
        if (patient.getBedNumber() != null) {

            System.out.println(
                    "Patient already has bed: "
                    + patient.getBedNumber()
            );

            return;
        }

        // Check if beds are available
        if (getAvailableBedCount() == 0) {

            System.out.println(
                    "ERROR: No beds are available."
            );

            return;
        }

        // Display available beds
        displayAvailableBeds();

        System.out.print("Enter bed number to allocate: ");
        String bedNumber = scanner.nextLine().toUpperCase();

        if (!isValidBed(bedNumber)) {

            System.out.println("Invalid bed number.");
            return;
        }

        if (isBedOccupied(bedNumber)) {

            System.out.println(
                    "ERROR: " + bedNumber + " is already occupied."
            );

            return;
        }

        // Occupy bed
        setBedOccupant(bedNumber, patient.getPatientId());

        // Store bed number in patient
        patient.setBedNumber(bedNumber);

        System.out.println(
                "Bed " + bedNumber
                + " allocated successfully to "
                + patient.getFirstName() + " "
                + patient.getLastName() + "."
        );
    }

    // ==========================================
    // 7. RELEASE BED
    // ==========================================

    public static void releaseBed() {

        System.out.println("\n--- Release Bed ---");

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        clsPatient patient = findPatient(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        if (patient.getBedNumber() == null) {

            System.out.println(
                    "This patient does not currently have a bed."
            );

            return;
        }

        releaseBedFromPatient(patient);
    }

    // ==========================================
    // RELEASE BED FROM PATIENT
    // ==========================================

    public static void releaseBedFromPatient(clsPatient patient) {

        String bedNumber = patient.getBedNumber();

        if (bedNumber == null) {
            return;
        }

        // Find bed and make it available
        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (beds[row][col].equalsIgnoreCase(bedNumber)) {

                    bedOccupants[row][col] = null;

                    patient.setBedNumber(null);

                    System.out.println(
                            "Bed " + bedNumber
                            + " has been released."
                    );

                    return;
                }
            }
        }
    }

    // ==========================================
    // 8. DISPLAY COMPLETE WARD LAYOUT
    // ==========================================

    public static void displayWardLayout() {

        System.out.println("\n--- COMPLETE WARD LAYOUT ---");

        System.out.println(
                "Available = [Available]"
        );

        System.out.println(
                "Occupied  = [Occupied]"
        );

        System.out.println();

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                String bedNumber = beds[row][col];

                if (bedOccupants[row][col] == null) {

                    System.out.printf(
                            "%-18s",
                            bedNumber + " [Available]"
                    );

                } else {

                    System.out.printf(
                            "%-18s",
                            bedNumber + " [Occupied]"
                    );
                }
            }

            System.out.println();
        }

        System.out.println(
                "\nAvailable Beds: " + getAvailableBedCount()
                + " / " + TOTAL_BEDS
        );

        System.out.println(
                "Occupied Beds: " + getOccupiedBedCount()
                + " / " + TOTAL_BEDS
        );
    }

    // ==========================================
    // 9. DISPLAY AVAILABLE BEDS
    // ==========================================

    public static void displayAvailableBeds() {

        System.out.println("\n--- AVAILABLE BEDS ---");

        int available = 0;

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (bedOccupants[row][col] == null) {

                    System.out.print(
                            beds[row][col] + "  "
                    );

                    available++;

                    if (available % 5 == 0) {
                        System.out.println();
                    }
                }
            }
        }

        if (available == 0) {
            System.out.println("No beds are currently available.");
        } else {
            System.out.println(
                    "\nTotal Available Beds: " + available
            );
        }
    }

    // ==========================================
    // 10. DISPLAY OCCUPIED BEDS
    // ==========================================

    public static void displayOccupiedBeds() {

        System.out.println("\n--- OCCUPIED BEDS ---");

        int occupied = 0;

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (bedOccupants[row][col] != null) {

                    String patientId = bedOccupants[row][col];

                    clsPatient patient = findPatient(patientId);

                    System.out.print(
                            beds[row][col]
                            + " -> Patient ID: "
                            + patientId
                    );

                    if (patient != null) {
                        System.out.print(
                                " | Name: "
                                + patient.getFirstName()
                                + " "
                                + patient.getLastName()
                        );
                    }

                    System.out.println();

                    occupied++;
                }
            }
        }

        if (occupied == 0) {
            System.out.println("No beds are currently occupied.");
        } else {
            System.out.println(
                    "\nTotal Occupied Beds: " + occupied
            );
        }
    }

    // ==========================================
    // 11. REPORTS
    // ==========================================

    public static void displayReports() {

        int choice;

        do {

            System.out.println("\n=================================");
            System.out.println("REPORTS");
            System.out.println("=================================");
            System.out.println("1. Display All Registered Patients");
            System.out.println("2. Display All Available Beds");
            System.out.println("3. Display All Occupied Beds");
            System.out.println("4. Display Total Registered Patients");
            System.out.println("5. Display Total Occupied Beds");
            System.out.println("6. Display Ward Occupancy Percentage");
            System.out.println("7. Display All Reports");
            System.out.println("8. Return to Main Menu");
            System.out.println("=================================");

            choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    displayAllPatients();
                    break;

                case 2:
                    displayAvailableBeds();
                    break;

                case 3:
                    displayOccupiedBeds();
                    break;

                case 4:
                    displayTotalRegisteredPatients();
                    break;

                case 5:
                    displayTotalOccupiedBeds();
                    break;

                case 6:
                    displayOccupancyPercentage();
                    break;

                case 7:
                    displayAllReports();
                    break;

                case 8:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }

        } while (choice != 8);
    }

    // ==========================================
    // REPORT: TOTAL REGISTERED PATIENTS
    // ==========================================

    public static void displayTotalRegisteredPatients() {

        System.out.println("\n--- TOTAL REGISTERED PATIENTS ---");

        System.out.println(
                "Total number of registered patients: "
                + patients.size()
        );
    }

    // ==========================================
    // REPORT: TOTAL OCCUPIED BEDS
    // ==========================================

    public static void displayTotalOccupiedBeds() {

        System.out.println("\n--- TOTAL OCCUPIED BEDS ---");

        System.out.println(
                "Total number of occupied beds: "
                + getOccupiedBedCount()
        );
    }

    // ==========================================
    // REPORT: WARD OCCUPANCY PERCENTAGE
    // ==========================================

    public static void displayOccupancyPercentage() {

        System.out.println("\n--- WARD OCCUPANCY ---");

        int occupiedBeds = getOccupiedBedCount();

        double occupancyPercentage =
                ((double) occupiedBeds / TOTAL_BEDS) * 100;

        System.out.printf(
                "Ward Occupancy: %.2f%%%n",
                occupancyPercentage
        );

        System.out.println(
                "Occupied Beds: "
                + occupiedBeds
                + " / "
                + TOTAL_BEDS
        );

        System.out.println(
                "Available Beds: "
                + getAvailableBedCount()
                + " / "
                + TOTAL_BEDS
        );
    }

    // ==========================================
    // DISPLAY ALL REPORTS
    // ==========================================

    public static void displayAllReports() {

        System.out.println("\n");
        System.out.println("========================================");
        System.out.println("         HOSPITAL SYSTEM REPORTS");
        System.out.println("========================================");

        // Report 1
        System.out.println("\n1. REGISTERED PATIENTS");
        System.out.println(
                "Total Registered Patients: "
                + patients.size()
        );

        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
        } else {

            for (clsPatient patient : patients) {

                System.out.println(
                        patient.getPatientId()
                        + " - "
                        + patient.getFirstName()
                        + " "
                        + patient.getLastName()
                        + " - "
                        + patient.getPatientCategory()
                );

                if (patient.getBedNumber() != null) {
                    System.out.println(
                            "   Bed: "
                            + patient.getBedNumber()
                    );
                }
            }
        }

        // Report 2
        System.out.println("\n2. AVAILABLE BEDS");

        int available = getAvailableBedCount();

        System.out.println(
                "Total Available Beds: " + available
        );

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (bedOccupants[row][col] == null) {

                    System.out.print(
                            beds[row][col] + "  "
                    );
                }
            }
        }

        // Report 3
        System.out.println("\n\n3. OCCUPIED BEDS");

        int occupied = getOccupiedBedCount();

        System.out.println(
                "Total Occupied Beds: " + occupied
        );

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (bedOccupants[row][col] != null) {

                    System.out.println(
                            beds[row][col]
                            + " -> "
                            + bedOccupants[row][col]
                    );
                }
            }
        }

        // Report 4
        System.out.println("\n4. TOTAL REGISTERED PATIENTS");

        System.out.println(
                patients.size()
        );

        // Report 5
        System.out.println("\n5. TOTAL OCCUPIED BEDS");

        System.out.println(
                occupied
        );

        // Report 6
        System.out.println("\n6. WARD OCCUPANCY PERCENTAGE");

        double percentage =
                ((double) occupied / TOTAL_BEDS) * 100;

        System.out.printf(
                "Occupancy: %.2f%%%n",
                percentage
        );

        System.out.println(
                "\n========================================"
        );
    }

    // ==========================================
    // FIND PATIENT BY ID
    // ==========================================

    public static clsPatient findPatient(String patientId) {

        for (clsPatient patient : patients) {

            if (patient.getPatientId()
                    .equalsIgnoreCase(patientId)) {

                return patient;
            }
        }

        return null;
    }

    // ==========================================
    // PATIENT CATEGORY
    // ==========================================

    public static String getPatientCategory() {

        while (true) {

            System.out.println("\nSelect Patient Category:");
            System.out.println("1. Inpatient");
            System.out.println("2. Outpatient");
            System.out.println("3. Emergency");

            int choice = readInt("Enter category: ");

            switch (choice) {

                case 1:
                    return "Inpatient";

                case 2:
                    return "Outpatient";

                case 3:
                    return "Emergency";

                default:
                    System.out.println(
                            "Invalid category. Please try again."
                    );
            }
        }
    }

    // ==========================================
    // CHECK IF BED NUMBER IS VALID
    // ==========================================

    public static boolean isValidBed(String bedNumber) {

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (beds[row][col].equalsIgnoreCase(bedNumber)) {
                    return true;
                }
            }
        }

        return false;
    }

    // ==========================================
    // CHECK IF BED IS OCCUPIED
    // ==========================================

    public static boolean isBedOccupied(String bedNumber) {

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (beds[row][col].equalsIgnoreCase(bedNumber)) {

                    return bedOccupants[row][col] != null;
                }
            }
        }

        return false;
    }

    // ==========================================
    // SET BED OCCUPANT
    // ==========================================

    public static void setBedOccupant(
            String bedNumber,
            String patientId) {

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (beds[row][col].equalsIgnoreCase(bedNumber)) {

                    bedOccupants[row][col] = patientId;
                    return;
                }
            }
        }
    }

    // ==========================================
    // GET AVAILABLE BED COUNT
    // ==========================================

    public static int getAvailableBedCount() {

        int count = 0;

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (bedOccupants[row][col] == null) {
                    count++;
                }
            }
        }

        return count;
    }

    // ==========================================
    // GET OCCUPIED BED COUNT
    // ==========================================

    public static int getOccupiedBedCount() {

        int count = 0;

        for (int row = 0; row < beds.length; row++) {

            for (int col = 0; col < beds[row].length; col++) {

                if (bedOccupants[row][col] != null) {
                    count++;
                }
            }
        }

        return count;
    }

    // ==========================================
    // READ INTEGER SAFELY
    // ==========================================

    public static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}

