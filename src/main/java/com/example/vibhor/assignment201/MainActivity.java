package com.example.vibhor.assignment201;

import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//creating class by extending Activity class.
public class MainActivity extends Activity
{
    //Creating references of classes whose elements are used in the layout.
    Button saveBtn;
    EditText nameET,phoneET;

    @Override
    //oncreate method.
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    //Setting Content View.

        //Setting references with their IDs.
        saveBtn = (Button)findViewById(R.id.save_btn);
        nameET = (EditText)findViewById(R.id.name_et);
        phoneET = (EditText)findViewById(R.id.phon_et);

        //Setting onClick Listener to the Button.
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Checking that both fields are filled.
                if(!nameET.getText().toString().isEmpty() && !phoneET.getText().toString().isEmpty())
                {
                    //ArrayList of ContentProviderOperation.
                    ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();

                    //Assigning int variable to ArrayList Size.
                    int rawContactID = contentProviderOperations.size();

                    //creating new row to add new contact in the Content Provider.
                    contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
                            .withValue(RawContacts.ACCOUNT_NAME,null)
                            .build());

                    //Creating Field of name in the row created above to store the name of Person.
                    contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactID)
                            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(StructuredName.DISPLAY_NAME,nameET.getText().toString())
                            .build());

                    //Creating Field of Phone in row created above to store the Phone number of person.
                    contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactID)
                            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(Phone.NUMBER,phoneET.getText().toString())
                            .withValue(Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build());


                    try
                    {
                        //Applying Batch to arrayList to Save in the Contacts Permanently.
                        getContentResolver().applyBatch(ContactsContract.AUTHORITY,contentProviderOperations);
                        Toast.makeText(getBaseContext(),"Contact Saved.",Toast.LENGTH_SHORT).show();
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                    catch (OperationApplicationException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    //Displaying Toast.
                    Toast.makeText(getBaseContext(),"Please Fill All Details.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
