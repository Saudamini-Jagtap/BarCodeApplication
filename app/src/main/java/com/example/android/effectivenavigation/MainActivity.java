/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.effectivenavigation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    /**
     * The {@link PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link FragmentStatePagerAdapter}.
     */
    // Databases Name
    public static final String DATABASE_NAME = "ItemListManager";
    public static DatabaseHandler db;
    public static DisplayMetrics metrics;
    public static List<String> categoryList;
    public static HashMap<String, List<Items>> itemList_UnderCategory;

    public static ExpandableListAdapter listAdapter;


    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.effectivenavigation/http/host/path")
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
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.android.effectivenavigation/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new ExternalScanFragment();
                case 1:
                    return new CameraScanFragment();
                case 2:
                    return new VoiceScanFragment();
                case 3:
                    return new ItemListFragment();
                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
//                case 0:
//                    return "Launchpad";
                case 0:
                    return "External Scan";
                case 1:
                    return "Camera Scan";
                case 2:
                    return "Voice Scan";
                case 3:
                    return "Item List";
                default:
                    return "Dummy Section";
            }
        }
    }
    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    /**
     * A fragment that launches external Scanning  application.
     */
    public static class ExternalScanFragment extends Fragment {
        String database = "http://www.upcdatabase.com/xmlrpc";
        String fetchItem,status,getC = null;
        private TextView itemname,scanedcode;
        private EditText myeditText;
        private ImageButton addToItemList;
        private ProgressBar pbSearchDatabse;
        private int selectedCategory;
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_section_ext_scanner, container, false);
            myeditText = (EditText)rootView.findViewById(R.id.barcode);
            scanedcode = (TextView)rootView.findViewById(R.id.scancode);
            itemname = (TextView)rootView.findViewById(R.id.itemname);
            addToItemList = (ImageButton) rootView.findViewById(R.id.fab);
            pbSearchDatabse = (ProgressBar)rootView.findViewById(R.id.progressBar);
            pbSearchDatabse.setVisibility(View.INVISIBLE);
            final TextView getcode =(TextView) rootView.findViewById(R.id.getcode);
            //uncomment for testing purpose
            //pbSearchDatabse.setVisibility(View.VISIBLE);
            //String temp = "null8901803000162";
            //searchUPCdatabase(temp.substring(4));
            myeditText.setOnTouchListener(new View.OnTouchListener(){
                public boolean onTouch (View v, MotionEvent event) {
                    return true; // the listener has consumed the event
                }
            });
            myeditText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                        event.getKeyCode();
                        char pressed = (char) event.getUnicodeChar();
                        getC += pressed;
                        myeditText.setText(getC.substring(4).trim());
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_ENTER:
                                pbSearchDatabse.setVisibility(View.VISIBLE);
                                searchUPCdatabase(myeditText.getText().toString().trim());
                                return true;
                            default:
                                break;
                        }
                    }
                    return true;
                }
            });
            addToItemList.setEnabled(false);
            addToItemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status != "fail") {
                        //fetching all the category from the category database table
                        List<Category> catList = db.getAllCategories();
                        final List<CharSequence> categories = new ArrayList<>();
                        for(Category cat:catList) {
                            categories.add(new String(cat.getName()));
                        }
                        final CharSequence[] charSequenceArrayofCategories = categories.toArray(new
                                CharSequence[categories.size()]);
                        //dialog popup for category selection
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.dialogMessgage));
                        builder.setSingleChoiceItems(charSequenceArrayofCategories, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                //Toast.makeText(getContext(), charSequenceArrayofCategories[item], Toast.LENGTH_SHORT).show();
                                selectedCategory = item;
                            }
                        });
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // fetch the category by the ID
                                        Category identifiedCategory = db.getCategory(selectedCategory + 1);
                                        //add to the database
                                        //DatabaseHandler db = new DatabaseHandler(getActivity().getApplication());
                                        if (db.addItem(new Items(itemname.getText().toString(),myeditText.getText().toString(),identifiedCategory.getName()," - "))) {
                                            reloadAllData();
                                            Toast.makeText(getContext(),
                                                    "Adding item to database",
                                                    Toast.LENGTH_LONG).show();
                                            //Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(),
                                                    "Error while adding to database :(", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        builder.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        Toast.makeText(getContext(),
                                "No item to add to the database :(", Toast.LENGTH_LONG).show();
                    }
                }
            });

            ImageButton clear = (ImageButton) rootView.findViewById(R.id.btn_refresh);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanedcode.setText("");
                    itemname.setText("");
                    myeditText.setText("");
                    getC = null;
                    addToItemList.setEnabled(false);
                }
            });
            return rootView;
        }
        public void searchUPCdatabase(final String BarcodeData) {
            final Handler handler = new Handler();
            switch(BarcodeData.length()) {
                case 8:
                case 12:
                case 13:
                    new Thread(new Runnable() {
                        HashMap result;

                        public void run() {
                            try {
                                Map<String, String> params = new HashMap<String, String>();
                                if (BarcodeData.length() == 12 || BarcodeData.length() == 8)
                                    params.put("upc", BarcodeData);
                                else if (BarcodeData.length() == 13)
                                    params.put("ean", BarcodeData);
                                params.put("rpc_key", "2eb97972f2b9029836d7f8c526a48cb6db94745d");
                                try {
                                    URL url = new URL(database);
                                    XMLRPCClient client = new XMLRPCClient(url, XMLRPCClient.FLAGS_FORWARD | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_CERT | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_HOST);
                                    result = (HashMap) client.call("lookup", params);
                                    status = result.get("status").toString();
                                    fetchItem = result.get("description").toString();
                                    handler.post(new Runnable() {
                                        public void run() {
                                            pbSearchDatabse.setVisibility(View.INVISIBLE);
                                            scanedcode.setText(getString(R.string.scancode, myeditText.getText().toString()));
                                            itemname.setText(getString(R.string.found_item_name, fetchItem));
                                            addToItemList.setEnabled(true);
                                        }
                                    });
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    fetchItem = result.get("message").toString();
                                    handler.post(new Runnable() {
                                        public void run() {
                                            pbSearchDatabse.setVisibility(View.INVISIBLE);
                                            scanedcode.setText(getString(R.string.scancode, myeditText.getText().toString()));
                                            itemname.setText(fetchItem);
                                            status = "fail";

                                        }
                                    });
                                }
                            } catch (NullPointerException nl) {
                                nl.printStackTrace();
                                fetchItem = result.get("message").toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        pbSearchDatabse.setVisibility(View.INVISIBLE);
                                        scanedcode.setText(getString(R.string.scancode, myeditText.getText().toString()));
                                        itemname.setText(fetchItem);
                                        status = "fail";
                                    }
                                });
                            } catch (XMLRPCException e) {
                                e.printStackTrace();
                                final String err = e.getCause().toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        pbSearchDatabse.setVisibility(View.INVISIBLE);
                                        scanedcode.setText(getString(R.string.scancode, myeditText.getText().toString()));
                                        itemname.setText("Unable to connect to the host. Check you internet connection or try again later.");
                                        status = "fail";
                                    }
                                });
                            }
                        }
                    }).start();
                    break;
                default:
                    handler.post(new Runnable() {
                        public void run() {
                            pbSearchDatabse.setVisibility(View.INVISIBLE);
                            scanedcode.setText(getString(R.string.scancode, myeditText.getText().toString()));
                            itemname.setText("Invalid Barcode. Only UPC and EAN code allowed.");
                            status = "fail";
                        }
                    });
                    break;
            }
        }
    }
    /**
     * A fragment that launches camera Scanning  application.
     */
    public static class CameraScanFragment extends Fragment {
        private String codeContent;
        private TextView itemname,scanedcode;
        private ImageButton addToItemList;
        private int selectedCategory;
        String fetchItem,status;
        private ProgressBar pbSearchDatabse;
        String database = "http://www.upcdatabase.com/xmlrpc";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_camera_scanner, container, false);

            scanedcode = (TextView)rootView.findViewById(R.id.scancode);
            itemname = (TextView)rootView.findViewById(R.id.itemname);
            addToItemList = (ImageButton) rootView.findViewById(R.id.fab);
            pbSearchDatabse = (ProgressBar)rootView.findViewById(R.id.progressBar);
            pbSearchDatabse.setVisibility(View.INVISIBLE);
            rootView.findViewById(R.id.btn_scan_now)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scanedcode.setText("");
                            itemname.setText("");
                            pbSearchDatabse.setVisibility(View.VISIBLE);
                            scanNow(v);
                        }
                    });
            addToItemList.setEnabled(false);
            addToItemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status != "fail") {
                        //fetching all the category from the category database table
                        List<Category> catList = db.getAllCategories();
                        final List<CharSequence> categories = new ArrayList<>();
                        for(Category cat:catList) {
                            categories.add(new String(cat.getName()));
                        }
                        final CharSequence[] charSequenceArrayofCategories = categories.toArray(new
                                CharSequence[categories.size()]);

                        //dialog popup for category selection
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.dialogMessgage));
                        builder.setSingleChoiceItems(charSequenceArrayofCategories, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                //Toast.makeText(getContext(), charSequenceArrayofCategories[item], Toast.LENGTH_SHORT).show();
                                selectedCategory = item;
                            }
                        });
                        builder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // fetch the category by the ID
                                        Category identifiedCategory = db.getCategory(selectedCategory + 1);
                                        //add to the database
                                        //DatabaseHandler db = new DatabaseHandler(getActivity().getApplication());
                                        if (db.addItem(new Items(itemname.getText().toString(), codeContent,identifiedCategory.getName(), " - "))) {
                                            reloadAllData();
                                            Toast.makeText(getContext(),
                                                    "Adding item to database",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(),
                                                    "Error while adding to database :(",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        builder.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Toast.makeText(getContext(),
                                "No item to add to the database :(", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return rootView;
        }
        /**
         * event handler for scan button
         *
         * @param //view view of the activity
         */
        public void scanNow(View View){
            Context context = getContext();
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt(String.valueOf(R.string.scan_bar_code));
            integrator.setResultDisplayDuration(0);
            integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.initiateScan();
            try {
                startActivityForResult(integrator.createScanIntent(), IntentIntegrator.REQUEST_CODE);
            } catch (ActivityNotFoundException a) {
                a.printStackTrace();
                Toast.makeText(getContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        }
        /**
         * function handle scan result
         *
         * @param requestCode scanned code
         * @param resultCode  result of scanned code
         * @param intent      intent
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);
            switch (requestCode) {
                case IntentIntegrator.REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        // Parsing bar code reader result
                        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                        //if (DEBUG) Log.d(TAG, "Parsing bar code reader result: " + result.toString());
                        if (result != null) {
                            //we have a result
                            codeContent = result.getContents();
                            searchUPCdatabase(codeContent.toString());

                        } else {
                            Toast toast = Toast.makeText(getActivity(), "No scan data received!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    break;
            }
        }

        public void searchUPCdatabase(final String BarcodeData) {
            final Handler handler = new Handler();
            switch(BarcodeData.length()) {
                case 8:
                case 12:
                case 13:
                    new Thread(new Runnable() {
                        HashMap result;

                        public void run() {
                            try {
                                Map<String, String> params = new HashMap<String, String>();
                                if (BarcodeData.length() == 12 || BarcodeData.length() == 8)
                                    params.put("upc", BarcodeData);
                                else if (BarcodeData.length() == 13)
                                    params.put("ean", BarcodeData);
                                params.put("rpc_key", "2eb97972f2b9029836d7f8c526a48cb6db94745d");
                                try {
                                    URL url = new URL(database);
                                    XMLRPCClient client = new XMLRPCClient(url, XMLRPCClient.FLAGS_FORWARD | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_CERT | XMLRPCClient.FLAGS_SSL_IGNORE_INVALID_HOST);
                                    result = (HashMap) client.call("lookup", params);
                                    status = result.get("status").toString();
                                    fetchItem = result.get("description").toString();
                                    handler.post(new Runnable() {
                                        public void run() {
                                            pbSearchDatabse.setVisibility(View.INVISIBLE);
                                            scanedcode.setText(getString(R.string.scancode,codeContent));
                                            itemname.setText(getString(R.string.found_item_name, fetchItem));
                                            addToItemList.setEnabled(true);
                                        }
                                    });
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    fetchItem = result.get("message").toString();
                                    handler.post(new Runnable() {
                                        public void run() {
                                            pbSearchDatabse.setVisibility(View.INVISIBLE);
                                            scanedcode.setText(getString(R.string.scancode,codeContent));
                                            itemname.setText(fetchItem);
                                            status = "fail";

                                        }
                                    });
                                }
                            } catch (NullPointerException nl) {
                                nl.printStackTrace();
                                fetchItem = result.get("message").toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        pbSearchDatabse.setVisibility(View.INVISIBLE);
                                        scanedcode.setText(getString(R.string.scancode,codeContent));
                                        itemname.setText(fetchItem);
                                        status = "fail";
                                    }
                                });
                            } catch (XMLRPCException e) {
                                e.printStackTrace();
                                final String err = e.getCause().toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        pbSearchDatabse.setVisibility(View.INVISIBLE);
                                        scanedcode.setText(getString(R.string.scancode,codeContent));
                                        itemname.setText("Unable to connect to the host. Check you internet connection or try again later.");
                                        status = "fail";
                                    }
                                });
                            }
                        }
                    }).start();
                    break;
                default:
                    handler.post(new Runnable() {
                        public void run() {
                            pbSearchDatabse.setVisibility(View.INVISIBLE);
                            scanedcode.setText(getString(R.string.scancode,codeContent));
                            itemname.setText("Invalid Barcode. Only UPC and EAN code allowed.");
                            status = "fail";
                        }
                    });
                    break;
            }
        }
    }
    /**
     * A fragment that launches voice Scanning  application.
     */
    public static class VoiceScanFragment extends Fragment {
        private TextView txtSpeechInput;
        private ImageButton btnSpeak;
        private ImageButton addToItemList;
        private int selectedCategory;
        private final int REQ_CODE_SPEECH_INPUT = 100;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_voice_scanner, container, false);
            txtSpeechInput = (TextView) rootView.findViewById(R.id.txtSpeechInput);
            btnSpeak = (ImageButton) rootView.findViewById(R.id.btnSpeak);
//            btnSpeak.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    promptSpeechInput();
//                }
//            });
            btnSpeak.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int currState;
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            v.setPressed(true);
                            // Start action ...
                            promptSpeechInput();

                            currState = MotionEvent.ACTION_DOWN;
                        break;
                        case MotionEvent.ACTION_UP:
                            currState = MotionEvent.ACTION_UP;
                        case MotionEvent.ACTION_OUTSIDE:
                        case MotionEvent.ACTION_CANCEL:
                            v.setPressed(false);
                            // Stop action ...
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                    }

                    return true;
                    //return false;
                }
            });
            addToItemList = (ImageButton) rootView.findViewById(R.id.fab);
            addToItemList.setEnabled(false);
            addToItemList.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //fetching all the category from the category database table
                    List<Category> catList = db.getAllCategories();
                    final List<CharSequence> categories = new ArrayList<>();
                    for(Category cat:catList) {
                        categories.add(new String(cat.getName()));
                    }
                    final CharSequence[] charSequenceArrayofCategories = categories.toArray(new
                            CharSequence[categories.size()]);

                    //dialog popup for category selection
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.dialogMessgage));
                    builder.setSingleChoiceItems(charSequenceArrayofCategories, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            //Toast.makeText(getContext(), charSequenceArrayofCategories[item], Toast.LENGTH_SHORT).show();
                            selectedCategory = item;
                        }
                    });
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // fetch the category by the ID
                                    Category identifiedCategory = db.getCategory(selectedCategory + 1);
                                    //add to the database
                                    //DatabaseHandler db = new DatabaseHandler(getActivity().getApplication());
                                    //db.addItem(new Items(txtSpeechInput.getText().toString().trim(), " - ",identifiedCategory.getName().toString()," - "));
                                    if (db.addItem(new Items(txtSpeechInput.getText().toString().trim(), " - ",identifiedCategory.getName().toString()," - "))) {
                                        reloadAllData();
                                        Toast.makeText(getContext(),
                                                "Adding item to database",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "Error while adding to database :(",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    builder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            return rootView;
        }

        /**
         * Showing google speech input dialog
         */
        private void promptSpeechInput() {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,10000);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,10000);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.speech_prompt));
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                a.printStackTrace();
                Toast.makeText(getContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Receiving speech input
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        txtSpeechInput.setText(result.get(0));
                        addToItemList.setEnabled(true);
                    }
                    break;
                }
            }
        }

        public static void loadContainerOnTouchListener() {
            View.OnTouchListener listener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LinearLayout layout = (LinearLayout) v;
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View view = layout.getChildAt(i);
                        Rect outRect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                        if (outRect.contains((int) event.getX(), (int) event.getY())) {
                            Log.d(this.getClass().getName(), String.format("Over view.id[%d]", view.getId()));
                        }
                    }
                    return true;
                }
            };
        }





























    }
    /**
     * A fragment that launches Item List application.
     */
    public static class ItemListFragment extends Fragment {
        private TableLayout itemtable;

        ExpandableListView expListView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_section_item_list, container, false);

            //get the expandable list view from the layout
            expListView = (ExpandableListView) rootView.findViewById(R.id.expandable_list);

            int width = metrics.widthPixels;
            expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

            //prepare the list data ready for display
            prepareListData();

            listAdapter = new ExpandableListAdapter(getActivity(), categoryList,itemList_UnderCategory);

            // setting list adapter
            expListView.setAdapter(listAdapter);

//            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                public boolean onChildClick(ExpandableListView parent, View v,
//                                            int groupPosition, int childPosition, long id) {
//                    final Items selectedItem = listAdapter.getChild(
//                            groupPosition, childPosition);
//                    Toast.makeText(getContext(), selectedItem.getName(), Toast.LENGTH_LONG)
//                            .show();
//                    return true;
//                }
//            });
            return rootView;
        }

        public int GetPixelFromDips(float pixels) {
            // Get the screen's density scale
            final float scale = getResources().getDisplayMetrics().density;
            // Convert the dps to pixels, based on density scale
            return (int) (pixels * scale + 0.5f);
        }
    }
    /*
* Preparing the list data
*/
    public static void prepareListData() {
        categoryList = new ArrayList<String>();
        itemList_UnderCategory = new HashMap<String, List<Items>>();

        //fetching all the category from the category database table
        List<Category> catList = db.getAllCategories();

        //fetching all the items from the item database table
        List<Items> itemsList =db.getAllItems();

        for(Category category:catList){
            categoryList.add(category.getName());
            List<Items> sortItemList = new ArrayList<Items>();
            for(Items itm:itemsList){

                if(itm.getCategory().equalsIgnoreCase(category.getName()) ){
                    sortItemList.add(itm);
                }
            }
            itemList_UnderCategory.put(category.getName(),sortItemList);
        }
        //fetch the items with sorted categories
    }
    /**
     * helper to show what happens when all data is new
     */
    private static void reloadAllData(){
        categoryList.clear();
        itemList_UnderCategory.clear();
        //fetching all the category from the category database table
        List<Category> catList = db.getAllCategories();

        //fetching all the items from the item database table
        List<Items> itemsList =db.getAllItems();

        for(Category category:catList){
            categoryList.add(category.getName());
            List<Items> sortItemList = new ArrayList<Items>();
            for(Items itm:itemsList){

                if(itm.getCategory().equalsIgnoreCase(category.getName()) ){
                    sortItemList.add(itm);
                }
            }
            itemList_UnderCategory.put(category.getName(),sortItemList);
        }
        // fire the event
        listAdapter.notifyDataSetChanged();
    }
}
