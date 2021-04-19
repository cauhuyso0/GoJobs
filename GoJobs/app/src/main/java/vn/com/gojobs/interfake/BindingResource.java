package vn.com.gojobs.interfake;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.com.gojobs.Model.Binding;
import vn.com.gojobs.Model.CreateBindingResponse;

public interface BindingResource {
    @POST("/register")
    Call<CreateBindingResponse> createBinding(@Body Binding binding);
}
