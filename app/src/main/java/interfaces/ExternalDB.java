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
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
    @FormUrlEncoded
    @POST("nucleus/db/updatelocation.php")
    Call<List<ExternalDBResponse>> getAreaCount(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("gridsize") double gridsize);

    //This updates user location in the DB
    //Should be used whenever new location info
    //is returned from location retriever
    // -AB
    @FormUrlEncoded
    @POST("nucleus/db/updatelocation.php")
    Call<List<ExternalDBResponse>> setLocation(
            @Field("username") String username,
            @Field("longitude") double longitude,
            @Field("latitude") double latitude);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://45.35.4.171/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();



}
