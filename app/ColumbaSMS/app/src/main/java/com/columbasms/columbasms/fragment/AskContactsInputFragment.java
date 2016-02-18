package com.columbasms.columbasms.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.columbasms.columbasms.R;
import com.columbasms.columbasms.adapter.ContactsAdapter;
import com.columbasms.columbasms.model.Contact;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/16/16.
 */
public class AskContactsInputFragment extends DialogFragment {

    private ContactsAdapter adapter;
    private List<Contact> contactList;
    private ImageView sab;
    private String assName;
    private String key;
    private String message;
    private String CAMPAIGN_ID;
    private String USER_ID;
    private static Resources res;
    private static Activity activity;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        res = getActivity().getResources();
        activity = getActivity();

        //GET ASSOCIATION NAME FOR THIS CAMPAIGN AND CREATE KEY
        assName = getTag();
        key =  assName + "_contacts";
        message = getArguments().getString("message");

        final String flag = getArguments().getString("flag");

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());


        CAMPAIGN_ID = getArguments().getString("campaign_id");
        USER_ID = p.getString("user_id","NOID");

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setCustomTitle(inflater.inflate(R.layout.dialog_ask_contacts_input, null));
        builder.setItems(R.array.ci_type, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putString("message", message);
                bundle.putString("campaign_id", CAMPAIGN_ID);
                bundle.putString("ass_id", getArguments().getString("ass_id"));
                bundle.putString("flag",flag);
                switch (which){
                    case 0:
                        ChooseContactsFragment newFragment0 = new ChooseContactsFragment();
                        newFragment0.setArguments(bundle);
                        newFragment0.show(getFragmentManager(), assName);
                        break;
                    case 1:
                        ChooseGroupFragment newFragment1 = new ChooseGroupFragment ();
                        newFragment1.setArguments(bundle);
                        newFragment1.show(getFragmentManager(), assName);
                        break;
                };
            }
        });

        return builder.create();
    }
}
