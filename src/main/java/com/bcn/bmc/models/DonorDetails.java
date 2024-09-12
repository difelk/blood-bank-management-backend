package com.bcn.bmc.models;

import java.util.List;
import java.util.Optional;

public class DonorDetails {

    private Donor donor;

    private DonorAddress donorAddress;

    private List<DonorDocument> donorDocument;

    private List<Donation> donations;

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    public DonorDetails(Donor donor, DonorAddress donorAddress, List<DonorDocument> donorDocument, List<Donation> donations){
        this.donor = donor;
        this.donorAddress = donorAddress;
        this.donorDocument = donorDocument;
        this.donations = donations;

    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public DonorAddress getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(DonorAddress donorAddress) {
        this.donorAddress = donorAddress;
    }

    public List<DonorDocument> getDonorDocument() {
        return donorDocument;
    }

    public void setDonorDocument(List<DonorDocument> donorDocument) {
        this.donorDocument = donorDocument;
    }



}
