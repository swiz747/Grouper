package objects;

/**
 * Created by Arthur on 12/1/2016.
 */

public class Buoy {


    private String buoyChatID;
    private String buoyName;
    private String buoyCreator;
    private String buoyDescription;
    private boolean privateIndicator;
    private double buoyLatitude;
    private double buoyLongitude;
    private double buoyGridsize;


    public Buoy(String buoyChatID, String buoyName, String buoyCreator, String buoyDescription, boolean privateIndicator, double buoyLatitude, double buoyLongitude, double buoyGridsize) {
        this.buoyChatID = buoyChatID;
        this.buoyName = buoyName;
        this.buoyCreator = buoyCreator;
        this.buoyDescription = buoyDescription;
        this.privateIndicator = privateIndicator;
        this.buoyLatitude = buoyLatitude;
        this.buoyLongitude = buoyLongitude;
        this.buoyGridsize = buoyGridsize;
    }

    public Buoy(String creationString) {

    }

    public Buoy() {
    }


    public String getBuoyChatID() {
        return buoyChatID;
    }

    public void setBuoyChatID(String buoyChatID) {
        this.buoyChatID = buoyChatID;
    }

    public String getBuoyName() {
        return buoyName;
    }

    public void setBuoyName(String buoyName) {
        this.buoyName = buoyName;
    }

    public String getBuoyCreator() {
        return buoyCreator;
    }

    public void setBuoyCreator(String buoyCreator) {
        this.buoyCreator = buoyCreator;
    }

    public String getBuoyDescription() {
        return buoyDescription;
    }

    public void setBuoyDescription(String buoyDescription) {
        this.buoyDescription = buoyDescription;
    }

    public boolean isPrivateIndicator() {
        return privateIndicator;
    }

    public void setPrivateIndicator(boolean privateIndicator) {
        this.privateIndicator = privateIndicator;
    }

    public double getBuoyLatitude() {
        return buoyLatitude;
    }

    public void setBuoyLatitude(double buoyLatitude) {
        this.buoyLatitude = buoyLatitude;
    }

    public double getBuoyLongitude() {
        return buoyLongitude;
    }

    public void setBuoyLongitude(double buoyLongitude) {
        this.buoyLongitude = buoyLongitude;
    }

    public double getBuoyGridsize() {
        return buoyGridsize;
    }

    public void setBuoyGridsize(double buoyGridsize) {
        this.buoyGridsize = buoyGridsize;
    }

    public String buoyCreationString()
    {
        return null;
    }
}
