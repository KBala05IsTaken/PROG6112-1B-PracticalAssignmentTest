/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balafamily.st10474385.prog6112.assignment1;

/**
 *
 * @author KhanyisaB
 */
class clsInpatient extends clsPatient {

    private String wardNumber;
    private String bedNumber;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public clsInpatient(
            String patientId,
            String firstName,
            String lastName,
            int age,
            String gender,
            String medicalCondition,
            String wardNumber,
            String bedNumber) {

        // Initialise inherited attributes
        super(
                patientId,
                firstName,
                lastName,
                age,
                gender,
                medicalCondition,
                PatientCategory.INPATIENT
        );

        this.wardNumber = wardNumber;
        this.bedNumber = bedNumber;
    }

    // ==========================================
    // GETTERS
    // ==========================================

    public String getWardNumber() {
        return wardNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    // ==========================================
    // SETTERS
    // ==========================================

    public void setWardNumber(String wardNumber) {
        this.wardNumber = wardNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    // ==========================================
    // OVERRIDE DISPLAY
    // ==========================================

    @Override
    public void displayPatient() {

        // Display inherited patient information
        super.displayPatient();

        // Display additional inpatient information
        System.out.println("Ward Number: " + wardNumber);

        if (bedNumber != null) {
            System.out.println("Bed Number: " + bedNumber);
        } else {
            System.out.println("Bed Number: None");
        }

        System.out.println("-----------------------------------");
    }
}