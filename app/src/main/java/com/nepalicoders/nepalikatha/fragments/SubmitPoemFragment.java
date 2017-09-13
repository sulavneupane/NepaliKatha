package com.nepalicoders.nepalikatha.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.nepalicoders.nepalikatha.R;
import com.nepalicoders.nepalikatha.constants.ApiUrl;
import com.nepalicoders.nepalikatha.interfaces.Callback;
import com.nepalicoders.nepalikatha.utils.BackgroundThread;
import com.nepalicoders.nepalikatha.utils.Connection;
import com.nepalicoders.nepalikatha.utils.Messages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitPoemFragment extends Fragment {

    AppCompatButton submitButton;


    public SubmitPoemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_submit_story, container, false);

        submitButton = (AppCompatButton) view.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submittedBy = ((EditText)view.findViewById(R.id.poem_submitted_by)).getText().toString();
                String email = ((EditText)view.findViewById(R.id.poem_email)).getText().toString();
                String title = ((EditText)view.findViewById(R.id.poem_title)).getText().toString();
                String writer = ((EditText)view.findViewById(R.id.poem_writer)).getText().toString();
                String content = ((EditText)view.findViewById(R.id.poem_content)).getText().toString();

                if(submittedBy.equals("") || email.equals("") || title.equals("") || writer.equals("") || content.equals("")){

                    Messages.snackBarShort(view, getString(R.string.fill_all_fields));

                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("title", title);
                    map.put("writer", writer);
                    map.put("content", content);
                    map.put("submitted_by", submittedBy);
                    map.put("email", email);

                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setTitle("Submitting");
                    dialog.setMessage("Submitting Your Story...");
                    dialog.show();

                    BackgroundThread thread = new BackgroundThread(ApiUrl.NEPALI_KABIT_SUBMIT_POEM_URL, map, Connection.POST);
                    thread.setBackgroundListener(getActivity(), new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            dialog.dismiss();
                            Messages.snackBarShort(view, getString(R.string.no_internet_connection));
                        }

                        @Override
                        public void onResponse(Response response, String result) throws IOException {
                            dialog.dismiss();
                            if (response.isSuccessful()) {
                                Messages.alertDialogSimple("Story Submitted!", "Thank you for submitting your story which will be published in this app once approved by the admin.", getActivity());
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new LatestPoemsFragment()).commit();
                            } else {
                                Messages.snackBarShort(view, getString(R.string.action_wrong));
                            }
                        }
                    });
                    thread.execute();
                }
            }
        });

        return view;
    }

}
