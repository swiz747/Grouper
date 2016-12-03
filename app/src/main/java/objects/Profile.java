package objects;

/**
 * Created by Arthur on 12/2/2016.
 */

public class Profile {

   private String username;
   private String firstName;
   private String lastName;
   private String phone;
   private String email;
   private String dob;
   private String state;
   private String zip;
   private String bio;
   private String genderName;
   private String genderMale;
   private String genderFemale;


    public Profile(String username, String firstName, String lastName, String phone, String email, String dob, String state, String zip, String bio, String genderName, String genderMale, String genderFemale)
    {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.state = state;
        this.zip = zip;
        this.bio = commaReplacer(bio);
        this.genderName = genderName;
        this.genderMale = genderMale;
        this.genderFemale = genderFemale;
    }

    //use when getting profile back from DB
    public Profile(String creationString)
    {

    }

    public Profile()
    {

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderMale() {
        return genderMale;
    }

    public void setGenderMale(String genderMale) {
        this.genderMale = genderMale;
    }

    public String getGenderFemale() {
        return genderFemale;
    }

    public void setGenderFemale(String genderFemale) {
        this.genderFemale = genderFemale;
    }

    private String commaReplacer(String inString)
    {
        String returnString = "";
        returnString = inString;
        returnString = returnString.replaceAll("%\\$&69%",",");
        returnString = returnString.replaceAll("[,]","dildoinmyass");


        return returnString;
    }

    public String profileCreationString()
    {
        String returnString = "";


         returnString += this.username + ",";
         returnString += this.firstName + ",";
         returnString += this.lastName + ",";
         returnString += this.phone + ",";
         returnString += this.email + ",";
         returnString += this.dob + ",";
         returnString += this.state + ",";
         returnString += this.zip + ",";
         returnString += commaReplacer(this.bio) + ","; //TODO remove any commas in here and replace them -AB
         returnString += this.genderName + ",";
         returnString += this.genderMale + ",";
         returnString += this.genderFemale;
        return returnString;
    }
}
