package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException  {

        List<String> subBreeds = new ArrayList<>();
        Request request = new Request.Builder()
                .url("https://dog.ceo/api/breed/" + breed + "/list")
                .build();



        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (!responseBody.getString("status").equals("success")) {
                throw new BreedFetcher.BreedNotFoundException("Breed not found: " + breed);
            }

            final JSONArray breeds = responseBody.getJSONArray("message");
            for (int i = 0; i < breeds.length(); i++) {
                final String subBreed = breeds.getString(i);
                subBreeds.add(subBreed);
            }


        } catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
        return subBreeds;
    }
}