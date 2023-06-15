package sg.edu.np.mad.madasgcacheflash;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private final GetDataService myApi;
    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetDataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(GetDataService.class);
    }
    public static synchronized RetrofitClient getInstance(){
        if (instance == null){
            instance = new RetrofitClient();

        }
        return instance;
    }

    public GetDataService getMyApi() {
        return myApi;
    }
}
