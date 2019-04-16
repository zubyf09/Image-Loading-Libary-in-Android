package com.mindvally.mindvellytest.interfaces;



import com.mindvally.mindvellytest.model.PinDetails;
import com.mindvally.mindvellytest.utils.C;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface UserInterface {
    @GET(C.REQ_PINS)
    Call<List<PinDetails>> getAllPins();

}


