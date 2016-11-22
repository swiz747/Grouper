package objects;

/**
 * Created by Arthur on 11/21/2016.
 *
 * this is a generic class that should be used whenever you're pulling from the external database
 * the php on the server side will be formatted to fit into this class.
 *
 */




public class ExternalDBResponse {

    //this is the data that you requested
    private String mainResponse;
    //this will be either true for successful or false for failure
    private String responseCode;
    //this will tell you what happened (error messages will be here)
    private String responseMessage;
    // this is for debugging but it echos back what you put in to make sure youre not retarded
    private String echoInput;


    //to be honest i dont think this class even needs a constructor but heres the default constructor
    public ExternalDBResponse() {
        this.echoInput = "default";
        this.mainResponse = "default";
        this.responseCode = "default";
        this.responseMessage = "default";
    }

    public String getEchoInput() {
        return echoInput;
    }

    public void setEchoInput(String echoInput) {
        this.echoInput = echoInput;
    }

    public String getMainResponse() {
        return mainResponse;
    }

    public void setMainResponse(String mainResponse) {
        this.mainResponse = mainResponse;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "ExternalDBResponse{" +
                "echoInput='" + echoInput + '\'' +
                ", mainResponse='" + mainResponse + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
