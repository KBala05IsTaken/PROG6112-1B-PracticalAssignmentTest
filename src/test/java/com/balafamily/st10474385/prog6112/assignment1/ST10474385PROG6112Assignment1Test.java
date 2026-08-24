/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.balafamily.st10474385.prog6112.assignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ST10474385PROG6112Assignment1Test {

    private final PrintStream originalOut = System.out;

    private ByteArrayOutputStream output;


    // ==========================================
    // SETUP BEFORE EACH TEST
    // ==========================================

    @BeforeEach
    public void setUp() {

        // Clear all patients
        ST10474385PROG6112Assignment1.patients.clear();

        // Clear all bed occupants
        for (int row = 0;
                row < ST10474385PROG6112Assignment1.bedOccupants.length;
                row++) {

            for (int col = 0;
                    col < ST10474385PROG6112Assignment1.bedOccupants[row].length;
                    col++) {

                ST10474385PROG6112Assignment1.bedOccupants[row][col] = null;
            }
        }

        // Create fresh output stream
        output = new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));
    }


    // ==========================================
    // CLEAN UP AFTER EACH TEST
    // ==========================================

    @AfterEach
    public void tearDown() {

        System.setOut(originalOut);
    }


    // ==========================================
    // HELPER METHOD
    // ==========================================

    private void provideInput(String input) {

        ST10474385PROG6112Assignment1.scanner =
                new java.util.Scanner(
                        new ByteArrayInputStream(
                                input.getBytes()
                        )
                );
    }


    // ==========================================
    // TEST 1
    // REGISTER A PATIENT
    // ==========================================

    @Test
    public void testRegisterPatient() {

        provideInput(
                "P001\n"
                + "John\n"
                + "Smith\n"
                + "30\n"
                + "Male\n"
                + "Flu\n"
                + "2\n"
        );

        ST10474385PROG6112Assignment1.registerPatient();

        assertEquals(
                1,
                ST10474385PROG6112Assignment1.patients.size()
        );

        clsPatient patient =
                ST10474385PROG6112Assignment1.findPatient("P001");

        assertNotNull(patient);

        assertEquals(
                "John",
                patient.getFirstName()
        );

        assertEquals(
                "Smith",
                patient.getLastName()
        );

        assertEquals(
                PatientCategory.OUTPATIENT,
                patient.getPatientCategory()
        );
    }


    // ==========================================
    // TEST 2
    // SEARCH FOR A PATIENT
    // ==========================================

    @Test
    public void testSearchPatient() {

        clsPatient patient =
                new clsPatient(
                        "P002",
                        "Mary",
                        "Jones",
                        25,
                        "Female",
                        "Headache",
                        PatientCategory.OUTPATIENT
                );

        ST10474385PROG6112Assignment1.patients.add(patient);

        provideInput("P002\n");

        ST10474385PROG6112Assignment1.searchPatient();

        String result = output.toString();

        assertTrue(
                result.contains("Patient found")
        );

        assertTrue(
                result.contains("P002")
        );

        assertTrue(
                result.contains("Mary")
        );

        assertTrue(
                result.contains("Jones")
        );
    }


    // ==========================================
    // TEST 3
    // UPDATE PATIENT DETAILS
    // ==========================================

    @Test
    public void testUpdatePatient() {

        clsPatient patient =
                new clsPatient(
                        "P003",
                        "Peter",
                        "Brown",
                        40,
                        "Male",
                        "Back Pain",
                        PatientCategory.OUTPATIENT
                );

        ST10474385PROG6112Assignment1.patients.add(patient);

        /*
         * Input:
         * Patient ID
         * New first name
         * New last name
         * New age
         * New gender
         * New medical condition
         * New category
         */

        provideInput(
                "P003\n"
                + "Michael\n"
                + "Brown\n"
                + "45\n"
                + "Male\n"
                + "Arthritis\n"
                + "2\n"
        );

        ST10474385PROG6112Assignment1.updatePatient();

        clsPatient updated =
                ST10474385PROG6112Assignment1.findPatient("P003");

        assertNotNull(updated);

        assertEquals(
                "Michael",
                updated.getFirstName()
        );

        assertEquals(
                "Brown",
                updated.getLastName()
        );

        assertEquals(
                45,
                updated.getAge()
        );

        assertEquals(
                "Arthritis",
                updated.getMedicalCondition()
        );
    }


    // ==========================================
    // TEST 4
    // DELETE / DISCHARGE PATIENT
    // ==========================================

    @Test
    public void testDeletePatient() {

        clsPatient patient =
                new clsPatient(
                        "P004",
                        "Sarah",
                        "Wilson",
                        35,
                        "Female",
                        "Migraine",
                        PatientCategory.OUTPATIENT
                );

        ST10474385PROG6112Assignment1.patients.add(patient);

        provideInput(
                "P004\n"
                + "Y\n"
        );

        ST10474385PROG6112Assignment1.deletePatient();

        assertNull(
                ST10474385PROG6112Assignment1.findPatient("P004")
        );

        assertEquals(
                0,
                ST10474385PROG6112Assignment1.patients.size()
        );
    }


    // ==========================================
    // TEST 5
    // ALLOCATE A BED
    // ==========================================

    @Test
    public void testAllocateBed() {

        clsInpatient patient =
                new clsInpatient(
                        "P005",
                        "David",
                        "Miller",
                        50,
                        "Male",
                        "Pneumonia",
                        "W01",
                        null
                );

        ST10474385PROG6112Assignment1.patients.add(patient);

        provideInput("B01\n");

        ST10474385PROG6112Assignment1.allocateBedToPatient(
                patient
        );

        assertEquals(
                "B01",
                patient.getBedNumber()
        );

        assertEquals(
                "P005",
                ST10474385PROG6112Assignment1.bedOccupants[0][0]
        );

        assertEquals(
                19,
                ST10474385PROG6112Assignment1.getAvailableBedCount()
        );

        assertEquals(
                1,
                ST10474385PROG6112Assignment1.getOccupiedBedCount()
        );
    }


    // ==========================================
    // TEST 6
    // RELEASE A BED
    // ==========================================

    @Test
    public void testReleaseBed() {

        clsInpatient patient =
                new clsInpatient(
                        "P006",
                        "Linda",
                        "Taylor",
                        60,
                        "Female",
                        "Heart Problem",
                        "W01",
                        "B02"
                );

        ST10474385PROG6112Assignment1.patients.add(patient);

        // Manually occupy B02
        ST10474385PROG6112Assignment1.bedOccupants[0][1] =
                "P006";

        assertEquals(
                1,
                ST10474385PROG6112Assignment1.getOccupiedBedCount()
        );

        ST10474385PROG6112Assignment1.releaseBedFromPatient(
                patient
        );

        assertNull(
                patient.getBedNumber()
        );

        assertNull(
                ST10474385PROG6112Assignment1.bedOccupants[0][1]
        );

        assertEquals(
                0,
                ST10474385PROG6112Assignment1.getOccupiedBedCount()
        );

        assertEquals(
                20,
                ST10474385PROG6112Assignment1.getAvailableBedCount()
        );
    }


    // ==========================================
    // TEST 7
    // PREVENT DUPLICATE PATIENT IDs
    // ==========================================

    @Test
    public void testPreventDuplicatePatientIds() {

        clsPatient firstPatient =
                new clsPatient(
                        "P007",
                        "John",
                        "Smith",
                        30,
                        "Male",
                        "Flu",
                        PatientCategory.OUTPATIENT
                );

        ST10474385PROG6112Assignment1.patients.add(
                firstPatient
        );

        // Try registering another patient
        // with the same ID.

        provideInput(
                "P007\n"
        );

        ST10474385PROG6112Assignment1.registerPatient();

        // Only one patient should exist.
        assertEquals(
                1,
                ST10474385PROG6112Assignment1.patients.size()
        );

        String result =
                output.toString();

        assertTrue(
                result.contains(
                        "already exists"
                )
        );
    }


    // ==========================================
    // TEST 8
    // PREVENT ALLOCATING AN OCCUPIED BED
    // ==========================================

    @Test
    public void testPreventAllocatingOccupiedBed() {

        clsInpatient firstPatient =
                new clsInpatient(
                        "P008",
                        "James",
                        "Smith",
                        45,
                        "Male",
                        "Infection",
                        "W01",
                        "B01"
                );

        clsInpatient secondPatient =
                new clsInpatient(
                        "P009",
                        "Robert",
                        "Jones",
                        50,
                        "Male",
                        "Injury",
                        "W01",
                        null
                );

        ST10474385PROG6112Assignment1.patients.add(
                firstPatient
        );

        ST10474385PROG6112Assignment1.patients.add(
                secondPatient
        );

        // B01 is already occupied.
        ST10474385PROG6112Assignment1.bedOccupants[0][0] =
                "P008";

        // Second patient attempts to take B01.
        provideInput("B01\n");

        ST10474385PROG6112Assignment1.allocateBedToPatient(
                secondPatient
        );

        // Second patient must NOT receive B01.
        assertNull(
                secondPatient.getBedNumber()
        );

        // B01 must still belong to P008.
        assertEquals(
                "P008",
                ST10474385PROG6112Assignment1.bedOccupants[0][0]
        );

        String result =
                output.toString();

        assertTrue(
                result.contains("already occupied")
        );
    }


    // ==========================================
    // TEST 9
    // PREVENT BED ALLOCATION WHEN
    // ALL BEDS ARE OCCUPIED
    // ==========================================

    @Test
    public void testPreventAllocationWhenAllBedsOccupied() {

        // Occupy all 20 beds.
        for (int row = 0;
                row < ST10474385PROG6112Assignment1.bedOccupants.length;
                row++) {

            for (int col = 0;
                    col < ST10474385PROG6112Assignment1.bedOccupants[row].length;
                    col++) {

                ST10474385PROG6112Assignment1.bedOccupants[row][col] =
                        "PATIENT-" + row + "-" + col;
            }
        }

        assertEquals(
                0,
                ST10474385PROG6112Assignment1.getAvailableBedCount()
        );

        assertEquals(
                20,
                ST10474385PROG6112Assignment1.getOccupiedBedCount()
        );

        clsInpatient patient =
                new clsInpatient(
                        "P010",
                        "Andrew",
                        "Davis",
                        55,
                        "Male",
                        "Broken Leg",
                        "W01",
                        null
                );

        ST10474385PROG6112Assignment1.patients.add(
                patient
        );

        // No bed number should be requested because
        // the method should detect that all beds are occupied.
        provideInput("");

        ST10474385PROG6112Assignment1.allocateBedToPatient(
                patient
        );

        assertNull(
                patient.getBedNumber()
        );

        assertEquals(
                0,
                ST10474385PROG6112Assignment1.getAvailableBedCount()
        );

        String result =
                output.toString();

        assertTrue(
                result.contains(
                        "No beds are available"
                )
        );
    }


    // ==========================================
    // TEST 10
    // SORT PATIENTS BY SURNAME
    // ==========================================

    @Test
    public void testSortPatientsBySurname() {

        clsPatient patient1 =
                new clsPatient(
                        "P011",
                        "John",
                        "Zulu",
                        30,
                        "Male",
                        "Flu",
                        PatientCategory.OUTPATIENT
                );

        clsPatient patient2 =
                new clsPatient(
                        "P012",
                        "Mary",
                        "Adams",
                        25,
                        "Female",
                        "Flu",
                        PatientCategory.OUTPATIENT
                );

        clsPatient patient3 =
                new clsPatient(
                        "P013",
                        "Peter",
                        "Brown",
                        40,
                        "Male",
                        "Injury",
                        PatientCategory.EMERGENCY
                );

        ST10474385PROG6112Assignment1.patients.add(
                patient1
        );

        ST10474385PROG6112Assignment1.patients.add(
                patient2
        );

        ST10474385PROG6112Assignment1.patients.add(
                patient3
        );

        ST10474385PROG6112Assignment1.sortPatientsBySurname();

        assertEquals(
                "Adams",
                ST10474385PROG6112Assignment1.patients
                        .get(0)
                        .getLastName()
        );

        assertEquals(
                "Brown",
                ST10474385PROG6112Assignment1.patients
                        .get(1)
                        .getLastName()
        );

        assertEquals(
                "Zulu",
                ST10474385PROG6112Assignment1.patients
                        .get(2)
                        .getLastName()
        );
    }


    // ==========================================
    // TEST 11
    // SORT PATIENTS BY PATIENT ID
    // ==========================================

    @Test
    public void testSortPatientsByPatientId() {

        clsPatient patient1 =
                new clsPatient(
                        "P300",
                        "John",
                        "Smith",
                        30,
                        "Male",
                        "Flu",
                        PatientCategory.OUTPATIENT
                );

        clsPatient patient2 =
                new clsPatient(
                        "P100",
                        "Mary",
                        "Jones",
                        25,
                        "Female",
                        "Flu",
                        PatientCategory.OUTPATIENT
                );

        clsPatient patient3 =
                new clsPatient(
                        "P200",
                        "Peter",
                        "Brown",
                        40,
                        "Male",
                        "Injury",
                        PatientCategory.EMERGENCY
                );

        ST10474385PROG6112Assignment1.patients.add(
                patient1
        );

        ST10474385PROG6112Assignment1.patients.add(
                patient2
        );

        ST10474385PROG6112Assignment1.patients.add(
                patient3
        );

        ST10474385PROG6112Assignment1.sortPatientsByPatientId();

        assertEquals(
                "P100",
                ST10474385PROG6112Assignment1.patients
                        .get(0)
                        .getPatientId()
        );

        assertEquals(
                "P200",
                ST10474385PROG6112Assignment1.patients
                        .get(1)
                        .getPatientId()
        );

        assertEquals(
                "P300",
                ST10474385PROG6112Assignment1.patients
                        .get(2)
                        .getPatientId()
        );
    }
}
