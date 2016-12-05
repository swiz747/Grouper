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
            @Field("username") String username,
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
    //public Buoys will only last some arbitrary timespan we should decide
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

    //Buoys in specific area
    @FormUrlEncoded
    @POST("nucleus/db/requestBuoysInArea.php")
    Call<List<ExternalDBResponse>> requestLocalBuoys(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("gridsize") double gridsize);

    //request buoy by buoy ID
    @FormUrlEncoded
    @POST("nucleus/db/requestBuoyByID.php")
    Call<List<ExternalDBResponse>> requestBuoy(
            @Field("buoyID") String buoyID);


    //for more info on this go to the Groupie class comments -AB
    @FormUrlEncoded
    @POST("nucleus/db/createEditGroupie.php")
    Call<List<ExternalDBResponse>> createEditGroupie(
            @Field("groupieCreationString") String groupieCreationString);

    //Groupies in specific area
    @FormUrlEncoded
    @POST("nucleus/db/requestBuoysInArea.php")
    Call<List<ExternalDBResponse>> requestLocalGroupies(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("gridsize") double gridsize);

    //request groupie based on location
    @FormUrlEncoded
    @POST("nucleus/db/requestBuoysInArea.php")
    Call<List<ExternalDBResponse>> requestGroupieByLocation(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude);

    //request groupie based on groupieID
    @FormUrlEncoded
    @POST("nucleus/db/requestGroupieByID.php")
    Call<List<ExternalDBResponse>> requestGroupieByID(
            @Field("GroupieID") double groupieID);


    //create profile based on creation string
    @FormUrlEncoded
    @POST("nucleus/db/createEditProfile.php")
    Call<List<ExternalDBResponse>> createEditProfile(
            @Field("profileCreationString") String profileCreationString);

    //request profile based on username
    @FormUrlEncoded
    @POST("nucleus/db/requestProfile.php")
    Call<List<ExternalDBResponse>> requestProfile(
            @Field("username") String username);

    //TODO request profiles based on friendslist -AB
    @FormUrlEncoded
    @POST("nucleus/db/requestProfile.php")
    Call<List<ExternalDBResponse>> requestProfileByFriendslist(
            @Field("username") String username);

    //in theory we can use this to upload any picture
    @Multipart
    @POST("nucleus/db/uploadPicture.php")
    Call<List<ExternalDBResponse>> uploadProfilePicture (
            @Part("username") RequestBody username,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part file);

    //can use this but wont return anything of use
    @FormUrlEncoded
    @POST("nucleus/db/requestPicture.php")
    Call<List<ExternalDBResponse>> requestProfilePicture(
            @Field("username") String username);

    //TODO rmember to change all of these IPs when we move to a domain name -AB
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://45.35.4.171/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
