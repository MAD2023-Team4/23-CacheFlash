package sg.edu.np.mad.madasgcacheflash;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    String BASE_URL = "https://npfirebasemad-262a1-default-rtdb.asia-southeast1.firebasedatabase.app/";
    @GET("users/Jack.json")
    Call<JsonObject> getUserCredentials();
}
