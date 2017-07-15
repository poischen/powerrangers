package msp.powerrangers.logic;


public class Donation {

    private String donationDbId;
    private String userDbId;
    private double value;

    public Donation(){
        // Needed for Firebase
    };

    public Donation(String donationDbId,String userDbId, double value){
        this.donationDbId = donationDbId;
        this.userDbId = userDbId;
        this.value = value;
    }


    // getter
    public String getDonationDbId() {
        return donationDbId;
    }

    public String getUserDbId() {
        return userDbId;
    }

    public double getValue() {
        return value;
    }

    // setter
    public void setDonationDbId(String donationDbId) {
        this.donationDbId = donationDbId;
    }

    public void setUserDbId(String userDbId) {
        this.userDbId = userDbId;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
