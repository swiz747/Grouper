package interfaces;

/**
 * Created by Arthur on 11/21/2016.(9/21/2016.)
 *
 * this is the interface you will use to interact with the external DB
 *
 */

//TODO add secure API calls otherwise russian hacker (or scriptkiddies) will rek our shit -AB
import java.util.List;


import objects.ExternalDBResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ExternalDB
{

    /** these look ugly but its pretty simple
     * '@'typeOfFormEncoding
     * '@'POST("baseURL+restofwebAddress.anythingyouwant")
     * Call<List<YourReturnObjectHere>> methodNameHere(
     * '@'Field("NameOfParameterInPHPFile") double longitude,
     * '@'Field("NameOfParameterInPHPFile") double latitude;)
     *
     * -AB
    */

    //this finds out how many niggas are in your
    //defined grid size based on current location
    //please note that the grid size is IN MILES
    //
    //-AB
    // changed name from niggacount to people count as of 11/22/2016 :(
    @FormUrlEncoded
    @POST("nucleus/db/requestPeopleCount.php")
    Call<List<ExternalDBResponse>> getAreaCount(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("gridsize") double gridsize);

    //This updates user location in the DB
    //Should be used whenever new location info
    //is returned from location retriever
    // -AB
    @FormUrlEncoded
    @POST("nucleus/db/updateLocation.php")
    Call<List<ExternalDBResponse>> setLocation(
            @Field("username") String username,
            @Field("longitude") double longitude,
            @Field("latitude") double latitude);

    //this will place a public Buoy at the specified coords with the specified grid size
    @FormUrlEncoded
    @POST("nucleus/db/placePublicBuoy.php")
    Call<List<ExternalDBResponse>> placePublicBuoy(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude);

    //this will place a private Buoy at the specified coords
    //with the specified grid size and life span(measured in hours)
    //this gives admin control of the buoy to the creator
    @FormUrlEncoded
    @POST("nucleus/db/placePrivateBuoy.php")
    Call<List<ExternalDBResponse>> placePrivateBuoy(
            @Field("username") String username,
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("lifespan") int lifespan);


    //i know its a fuck load of info to put into a function and im trying to find a way to make it easier

    /**
    @FormUrlEncoded
    @POST("nucleus/db/createGroupie.php")
    Call<List<ExternalDBResponse>> createGroupie(
            @Field("groupieName") String groupieName,
            @Field("groupieCreator") String groupieCreator,
            @Field("groupieDescription") String groupieDescription,
            //String value should either be Y or N for indicator
            @Field("groupiePrivateIndicator") String privateIndicator,
            @Field("groupieStartDate") String groupieStartDate,
            @Field("groupieStartTime") String username,
            @Field("groupieEndDate") String groupieEndDate,
            @Field("groupieEndTime") String groupieEndTime,
            @Field("groupieLat") double groupieLat,
            @Field("groupieLong") double groupieLong,
            @Field("groupieAddress") String groupieAddress);
    */

    //for more info on this go to the Groupie class comments -AB
    @FormUrlEncoded
    @POST("nucleus/db/createGroupie.php")
    Call<List<ExternalDBResponse>> createGroupie(
            @Field("groupieCreationString") String groupieCreationString);

    @FormUrlEncoded
    @POST("nucleus/db/createEditProfile.php")
    Call<List<ExternalDBResponse>> editProfile(
            @Field("groupieCreationString") String groupieCreationString);


    //TODO dont use this yet -AB
    @Multipart
    @POST("nucleus/db/uploadPicture.php")
    Call<List<ExternalDBResponse>> uploadProfilePicture (
            @Part("username") RequestBody username,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part file);



    //can use this but wont return anything of use
    @FormUrlEncoded
    @POST("nucleus/db/uploadProfilePic.php")
    Call<List<ExternalDBResponse>> requestProfilePicture(
            @Field("username") String username);



    //TODO rmember to change all of these IPs when we move to a domain name -AB
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://45.35.4.171/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



}
