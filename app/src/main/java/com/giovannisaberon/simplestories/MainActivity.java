package com.giovannisaberon.simplestories;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ImageData imageData;
    private ImageJson imageJson;
    final static public ArrayList<ImageData> slides = new ArrayList<>();
    public BibleJson bibleJson;
    private BibleData bibleData;
    ImageView imageView;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public void showTitle(String title){
        setTitle(title);
    }

    public static class ImageLoader extends AsyncTask <Void, Void, byte[]>{

        private String URL;
        private Context context;
        private InputStream in;

        ImageLoader(Context context, String Url)
        {
            URL = Url;
            ImageLoader.this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            AssetManager assetMgr = context.getAssets();

            try {

                in = assetMgr.open(URL);
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

        @Override
        protected byte[] doInBackground(Void... arg0) {

            Bitmap bitmap = null;
            byte[] b = new byte[]{};
            try {
                bitmap = BitmapFactory.decodeStream(in);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                b = baos.toByteArray();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        }

        @Override
        protected void onPostExecute( byte[] result )  {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageJson = new ImageJson(this);
        bibleJson = new BibleJson(this);
        JSONObject jsonBible = new JSONObject();
        try {
            jsonBible = bibleJson.getJsonBible("filename.json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = imageJson.getSlides("imagedata.json", "slides");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x=0; x<jsonArray.length(); x++){
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject = (JSONObject) jsonArray.getJSONObject(x);

                String title = jsonObject.getString("title");
                String filename = "file:///android_asset/";
                Log.i("title", title);
                filename = filename + jsonObject.getString("filename");
//                caption = jsonObject.getString("caption");

                String caption = jsonObject.getString("caption");
                JSONObject biblereference = jsonObject.getJSONObject("bible");
                String book = biblereference.getString("book");
                String chapter = biblereference.getString("chapter");
                int verse = biblereference.getInt("verse");
                String word = bibleJson.getVerse(jsonBible,book, chapter, verse);
                Log.i("word", word);
                BibleData bibleData = new BibleData(book, Integer.parseInt(chapter), verse, word);
                Log.i("caption", caption);
                imageData = new ImageData(title, filename, caption, bibleData);
                slides.add(imageData);
                Log.i("imagedata arraylist", slides.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Resources res;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, ArrayList<ImageData> slides) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            int slidenumber = getArguments().getInt(ARG_SECTION_NUMBER) -1;
            ImageData imagedata = slides.get(slidenumber);
            String title = imagedata.getTitle();
            String imageFilePath = imagedata.getImagefilename();
            Log.i("imagefilepath", imageFilePath);
            String caption = imagedata.getCaption();
            BibleData bibleData = imagedata.getBibleData();
            String word = bibleData.getWord();


            // get input stream
//                InputStream ims = getContext().getAssets().open(url);
//                // load image as Drawable
//                Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
//                imcassoageView.setImageDrawable(d);
            Picasso picasso = new Picasso.Builder(getContext()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {

                }
            }).build();
            picasso.load(imageFilePath)
                    .error(R.drawable.ic_foreground_error_black_36dp) // On error image
                    .placeholder(R.drawable.ic_foreground_error_black_36dp) // Place holder image
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Do something
                            Log.d("test","Success image loading...");
                        }

                        @Override
                        public void onError() {
                            // Do something
                            Log.d("test","Error!!!");
                        }
                    });
            textView.setText( caption + " - " + word);
            getActivity().setTitle(caption);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1, slides);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return slides.size();
        }
    }
}
