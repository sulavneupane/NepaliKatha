package com.nepalicoders.nepalikatha.json;

import com.nepalicoders.nepalikatha.objects.Poem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sulav on 1/3/16.
 */
public class JsonParser {

    public static List<Poem> parseAllPoems(String result) {
        List<Poem> poems = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Poem poem = new Poem();

                poem.setId(Long.parseLong(object.getString("id")));
                poem.setTitle(object.getString("title"));
                poem.setWriter(object.getString("writer"));
                poem.setContent(object.getString("content"));
                poem.setCategory(object.getString("category"));
                poem.setPublishedOn(object.getString("published_on"));
                poem.setSubmittedBy(object.getString("submitted_by"));
                poem.setEmail(object.getString("email"));

                poems.add(poem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return poems;
    }

}
