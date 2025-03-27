package com.belaku.naveenprakash.npstreetmap;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    // as we are making get request
    // so we are displaying GET as annotation.
    // and inside we are passing
    // last parameter for our url.
    @Headers({"Authorization: fsq3sTeV2SE6RX43BJE0vC+PxSZJArvmyLqsn4iqfQF4Dvs=", "accept: application/json", "query: Temple"})
    @GET("v3/places/{nearby}")
    Call<NearbyPlacesPojo> getPlaces(
            @Path("nearby") String nearby,
            @Query("ll") String latlng,
            @Query("query") String place
    );
}