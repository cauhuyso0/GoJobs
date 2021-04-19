package vn.com.gojobs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static vn.com.gojobs.Model.GojobConfig.ROOT_URL;

public class RetrofitClient {
    public static Retrofit retrofit = null;

    public static Retrofit getClien() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
