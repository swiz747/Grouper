package objects;

/**
 * Created by Arthur on 11/22/2016.
 * 
 * This is a helper class for creating a groupie
 * you create it using the constructor like normal
 * but when you want to talk to the external database just reference
 * the object and call the groupieCreationString() function.
 * that function will vomit out a string that is formatted the way the receiving php page on the server expects
 *
 * DO NOT FORGET TO FILL OUT ANY FIELD HERE EXCEPT ADDRESS!!!!
 */

public class Groupie {

    private String groupieName;
    private String groupieCreator;
    private String groupieDescription;
    private boolean privateIndicator;
    private String groupieStartDate;
    private String groupieStartTime;
    private String groupieEndDate;
    private String groupieEndTime;
    private double groupieLat;
    private double groupieLong;
    private String groupieAddress;

    public Groupie(String groupieName, String groupieCreator, String groupieDescription, boolean privateIndicator, String groupieStartDate, String groupieStartTime, String groupieEndDate, String groupieEndTime, double groupieLat, double groupieLong, String groupieAddress) {
        this.groupieName = groupieName;
        this.groupieCreator = groupieCreator;
        this.groupieDescription = groupieDescription;
        this.privateIndicator = privateIndicator;
        this.groupieStartDate = groupieStartDate;
        this.groupieStartTime = groupieStartTime;
        this.groupieEndDate = groupieEndDate;
        this.groupieEndTime = groupieEndTime;
        this.groupieLat = groupieLat;
        this.groupieLong = groupieLong;
        this.groupieAddress = groupieAddress;
    }

    public Groupie() {

    }

    public void setGroupieName(String groupieName) {
        this.groupieName = groupieName;
    }

    public void setGroupieCreator(String groupieCreator) {
        this.groupieCreator = groupieCreator;
    }

    public void setGroupieDescription(String groupieDescription) {
        this.groupieDescription = groupieDescription;
    }

    public void setPrivateIndicator(boolean privateIndicator) {
        this.privateIndicator = privateIndicator;
    }

    public void setGroupieStartDate(String groupieStartDate) {
        this.groupieStartDate = groupieStartDate;
    }

    public void setGroupieStartTime(String groupieStartTime) {
        this.groupieStartTime = groupieStartTime;
    }

    public void setGroupieEndDate(String groupieEndDate) {
        this.groupieEndDate = groupieEndDate;
    }

    public void setGroupieEndTime(String groupieEndTime) {
        this.groupieEndTime = groupieEndTime;
    }

    public void setGroupieLat(double groupieLat) {
        this.groupieLat = groupieLat;
    }

    public void setGroupieLong(double groupieLong) {
        this.groupieLong = groupieLong;
    }

    public void setGroupieAddress(String groupieAddress) {
        this.groupieAddress = groupieAddress;
    }

    public String groupieCreationString()
    {
        String returnString = "";

        returnString += this.groupieName + ",";
        returnString += this.groupieCreator + ",";
        returnString += this.groupieDescription + ",";
        if (this.privateIndicator)
        {
            returnString += "Y" + ",";
        }
        else
        {
            returnString += "N" + ",";
        }
        returnString += this.groupieStartDate + ",";
        returnString += this.groupieStartTime + ",";
        returnString += this.groupieEndDate + ",";
        returnString += this.groupieEndTime + ",";
        returnString += this.groupieLat + ",";
        returnString += this.groupieLong + ",";

        if(this.groupieAddress == null)
        {
            returnString += "null" ;
        }
        else
        {
            returnString += this.groupieAddress ;
        }
        return returnString;
    }
}
