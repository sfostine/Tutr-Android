package com.example.android.tutr;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

/**
 * Used to update user password and name on the Parse database.
 */
public class profileEditActivity extends AppCompatActivity {
    ParseUser currentUser = ParseUser.getCurrentUser();
    Button saveChangesButton;
    // UI references.
    private EditText wage;
    private EditText description;
    private EditText phone;
    private EditText subjects;
    private TextView desc;
    private TextView rating_title;
    // Keep track of whether registering has been cancelled
    private boolean cancel = false;
    private View focusView = null;
    /**
     * drop down menu.
     * if user selects nothing. spinner.getValue is equal to String "Select"
     * however, when user attempts to select anything only "yes" and "no" options are available
     */
    Spinner availability_spinner;
    final String[] spinner_options = new String[]{"Yes", "No", "Select"};

    RatingBar rating_bar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Overidden definition of the default onCreate method.
     * Opens "edit account" window; listens to clicks on button to save changes to the user account.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        if (currentUser == null) {
            return;
        }
        setUpUIelements();
        saveChangesButton.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View view) {
                        try {
                            saveChanges();
                        } catch (Exception e) {
                            startActivity(new Intent(profileEditActivity.this, LoginActivity.class));
                        }
                    }
                });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "profileEdit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.android.tutr/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "profileEdit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.android.tutr/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * does not display the last option when user attempts to select from the menu. last option is used as used as a hint.
     */
    class CustomArrayAdapter extends ArrayAdapter {
        /**
         * basic constructor
         *
         * @param context
         * @param resource
         * @param objects
         */
        public CustomArrayAdapter(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return super.getCount() - 1; // you dont display last item. It is used as hint.
        }
    }

    /**
     * initialize and link all UI elements and fields
     */
    protected void setUpUIelements() {
        try {
            currentUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(profileEditActivity.this, "Unable to access Parse Server", Toast.LENGTH_LONG).show();
        }
        saveChangesButton = (Button) findViewById(R.id.profileEditSaveChangesButton);
        wage = (EditText) findViewById(R.id.enter_hourly_rate);
        phone = (EditText) findViewById(R.id.enter_phone);
        subjects = (EditText) findViewById(R.id.enter_subjects);
        desc = (TextView) findViewById(R.id.descTextView);
        description = (EditText) findViewById(R.id.enter_description);

        if (currentUser.getList("courses") != null) {
            List<String> courses = currentUser.getList("courses");
            StringBuilder stringBuilder = new StringBuilder();
            for (String course : courses) {
                stringBuilder.append(",").append(course);
            }
            //remove the starting ','
            stringBuilder.deleteCharAt(0);
            subjects.setText(stringBuilder.toString());
        }
        if (currentUser.getString("phone") != null) {
            phone.setText(currentUser.getString("phone"));
        }
        if (currentUser.getDouble("hourlyRate") != 0) {
            wage.setText(String.valueOf(currentUser.getDouble("hourlyRate")));
        }
        if (currentUser.getString("description") != null) {
            description.setText(currentUser.getString("description"));
        }
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                desc.setText("Description " + (400 - s.toString().length()) + "/400");
            }
        });
        // init rating bar
        rating_bar = (RatingBar) findViewById(R.id.ratingBar);
        Log.w("rating", String.valueOf(currentUser.getDouble("rating")));
        rating_title = (TextView) findViewById(R.id.rating_title);
        rating_title.setText("Rating (" + currentUser.getDouble("rating") + " / 5.0)");
        rating_bar.setRating((float) currentUser.getDouble("rating"));
        rating_bar.setIsIndicator(true);
        // init text fields
        availability_spinner = (Spinner) findViewById(R.id.availability_spinner);
        ArrayAdapter<String> availability_menu_adapter = new CustomArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinner_options);
        // link spinner and adapters
        availability_menu_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availability_spinner.setAdapter(availability_menu_adapter);
        availability_spinner.setSelection(availability_menu_adapter.getCount()); //display hint
    }

    /**
     * Acts on press of "Save Changes" button. Checks inputs and saves to Parse database if valid.
     */
    protected void saveChanges() {
        final String wageStr = wage.getText().toString();
        double wageDouble = 0;
        String[] courses = subjects.getText().toString().toLowerCase().split(",");
        for (String c : courses) {
            if (!CourseValidator.isValidCourse(c)) {
                cancel = true;
                focusView = subjects;
            }
        }
        // Reset errors.
        wage.setError(null);
        if (!TextUtils.isEmpty(wageStr)) {
            wageDouble = Double.parseDouble(wageStr);

            // Check for a valid email address.
            if (wageDouble < 10.35) {
                wage.setError("The minimum wage is $10.35");
                focusView = wage;
                cancel = true;
            }
        }
        if (availability_spinner.getSelectedItem().toString().equalsIgnoreCase("select")) {
            focusView = availability_spinner;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            cancel = false;
            Toast.makeText(profileEditActivity.this, "Changed not successful. Verify your input values again!!!!", Toast.LENGTH_LONG).show();
            focusView.requestFocus();
            return;
        } else {
            currentUser.addAllUnique("courses", Arrays.asList(courses));
            currentUser.put("description", description.getText().toString());
            currentUser.put("hourlyRate", Double.parseDouble(wage.getText().toString()));
            currentUser.put("phone", phone.getText().toString());
            currentUser.put("available", availability_spinner.getSelectedItem().toString());
            Toast.makeText(profileEditActivity.this, "Changed profile successfully", Toast.LENGTH_LONG).show();
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    finish();
                }
            });
        }
    }
}
