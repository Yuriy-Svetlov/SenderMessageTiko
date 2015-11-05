package loaders;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

/**
 * Created by Юрий on 26.07.2015.
 */
public class CustomJsonObject extends JsonObjectRequest {

    private final String PROTOCOL_CONTENT_TYPE = String.format("application/json");


    public CustomJsonObject(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }


    @Override
    public String getBodyContentType()
    {
        return PROTOCOL_CONTENT_TYPE;
    }

}