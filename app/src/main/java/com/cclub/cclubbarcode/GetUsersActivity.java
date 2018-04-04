package com.cclub.cclubbarcode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by ivahmet on 02/04/2018.
 */

public class GetUsersActivity extends Activity {
    ArrayList<String> usersList;

    // We are getting users registered to server and showing them in a listview.
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_getusers);

        usersList = new ArrayList<>();

        final ListView users_lv = findViewById(R.id.users_lv);

        Ion.with(this)
                .load(Config.SERVER_URL + "/users")
                .setHeader(Config.HEADER_NAME, Config.HEADER_CONTENT)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e == null) {
                            JsonArray users_jsonarray = result.getAsJsonArray("users");
                            for(int i = 0; i<users_jsonarray.size(); i++) {
                                JsonElement user = users_jsonarray.get(i);

                                usersList.add(user.getAsJsonObject().get("name").getAsString() + " " + user.getAsJsonObject().get("surname").getAsString());
                            }

                            ArrayAdapter adapter = new ArrayAdapter(GetUsersActivity.this, android.R.layout.simple_list_item_1, usersList);
                            users_lv.setAdapter(adapter);
                        }
                    }
                });
    }

}
