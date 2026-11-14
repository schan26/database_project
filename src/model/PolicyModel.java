package ca.ubc.cs304.src.model;

import java.time.LocalDate;

public class PolicyModel {

    private int policyID;
    private String policyName;
    private String licenseNumber;
    private int coverageLimit;
    private LocalDate startDate;
    private LocalDate endDate;
    private String policyStatus;
    private int premium;
    private int deductible;

    public PolicyModel(int policyID, String policyName, String licenseNumber, int coverageLimit, LocalDate startDate, LocalDate endDate, String policyStatus, int premium, int deductible) {
        this.policyID = policyID;
        this.policyName = policyName;
        this.licenseNumber = licenseNumber;
        this.coverageLimit = coverageLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.policyStatus = policyStatus;
        this.premium = premium;
        this.deductible = deductible;
    }

    public String getPolicyName() {
        return policyName;
    }

    public int getPolicyID() {
        return policyID;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public int getCoverageLimit() {
        return coverageLimit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public int getPremium() {
        return premium;
    }

    public int getDeductible() {
        return deductible;
    }
}