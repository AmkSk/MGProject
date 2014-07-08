package com.mg.androidapp.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dd.plist.NSArray;
import com.dd.plist.PropertyListParser;
import com.mg.androidapp.activities.ActualActivity;
import com.mg.androidapp.activities.ExhibitonsListActivity;
import com.mg.androidapp.activities.ExtraActivity;
import com.mg.androidapp.activities.HomeActivity;
import com.mg.androidapp.R;
import com.mg.androidapp.activities.VisitActivity;
import com.mg.androidapp.utility.PlistParser;
import com.mg.objects.Building;
import com.mg.objects.MGEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Singleton class
 * - Manages the navigation
 * - Stores urls, fb links
 * - Stores fonts
 * - Stores lists of buildings, actualities, exhibitions, constant exhibitions
 */
public class AppManager extends Activity {

    // =============================================================================
    // Fields
    // =============================================================================
    private static AppManager instance;

//    private static String serverURL = "http://www.fi.muni.cz/~xziman/MG/Plists/";
//    private static String imagesURL = "http://www.fi.muni.cz/~xziman/MG/Images/";
    private static String serverURL = "http://mgapp.azurewebsites.net/Plists/";
    private static String imagesURL = "http://mgapp.azurewebsites.net/Images/";

    private static String youtubeURL = "http://www.youtube.com/videaMG";
    private static String fbId = "312236305130";

    private static String bienaleURL = "http://www.moravska-galerie.cz/bienale-brno.aspx";
    private static String bienaleFbId = "299416523410256";

    static private final String DEVELOPER_KEY = "AIzaSyCWqiQH_jd4dxuggWWxR1iI5GQb7MIxNjc";

    private Typeface font = null;
    private Typeface fontBold = null;
    private int themeButtonColor;
    private String appLanguage = Locale.getDefault().getLanguage();
    private Building selectedBuilding;
    private MGEntry selectedEntry;

    private static boolean printLogMessages = true; // change this value to turn on/off global log messages

    private List<? extends MGEntry> buildingList;
    private List<? extends MGEntry> actList;
    private List<? extends MGEntry> eventList;
    private List<? extends MGEntry> exhibitionsList;
    private List<? extends MGEntry> constantExhibitionsList;


    // =============================================================================
    // Constructors
    // =============================================================================
    private AppManager(){}

    // =============================================================================
    // Getters
    // =============================================================================

    public String getServerURL(){
        return serverURL;
    }

    public static String getYoutubeURL() {
        return youtubeURL;
    }

    public static String getFbId() {
        return fbId;
    }

    public static String getBienaleURL() {
        return bienaleURL;
    }

    public static String getBienaleFbId() {
        return bienaleFbId;
    }

    public Typeface getFont(){
        return this.font;
    }

    public Typeface getFontBold(){
        return this.fontBold;
    }

    public static String getImagesURL() {
        return imagesURL;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public MGEntry getSelectedEntry() {
        return selectedEntry;
    }

    public List<? extends MGEntry> getBuildingList() {
        return buildingList;
    }

    public List<? extends MGEntry> getActList() {
        return actList;
    }

    public List<? extends MGEntry> getEventList() {
        return eventList;
    }

    public List<? extends MGEntry> getExhibitionsList() {
        return exhibitionsList;
    }

    public List<? extends MGEntry> getConstantExhibitionsList() {
        return constantExhibitionsList;
    }

    public static String getDeveloperKey() {
        return DEVELOPER_KEY;
    }

    public int getThemeButtonColor() {
        return themeButtonColor;
    }

    // =============================================================================
    // Setters
    // =============================================================================


    public void setSelectedBuilding(Building selectedBuilding) {
        this.selectedBuilding = selectedBuilding;
    }

    public void setSelectedEntry(MGEntry selectedEntry) {
        this.selectedEntry = selectedEntry;
    }

    public void setBuildingList(List<? extends MGEntry> buildingList) {
        this.buildingList = buildingList;
    }

    public void setActList(List<? extends MGEntry> actList) {
        this.actList = actList;
    }

    public void setEventList(List<? extends MGEntry> eventList) {
        this.eventList = eventList;
    }

    public void setExhibitionsList(List<? extends MGEntry> exhibitionsList) {
        this.exhibitionsList = exhibitionsList;
    }

    public void setConstantExhibitionsList(List<? extends MGEntry> constantExhibitionsList) {
        this.constantExhibitionsList = constantExhibitionsList;
    }

    public void setThemeButtonColor(int themeButtonColor) {
        this.themeButtonColor = themeButtonColor;
    }

    // =============================================================================
    //  Methods
    // =============================================================================
    public static AppManager getInstance()
    {
        if (instance == null)
        {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * Method called in every activity, initializes the menu navigation on the bottom
     * 5 buttons: Actualities, Visit, Permanent Exhibitions, Extra, Logo
     * @param activity - current activity
     */
    public void initNavigation(final Activity activity){
        final ImageButton logoButton = (ImageButton) activity.findViewById(R.id.logoButton);
        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoButtonClicked(activity);
            }
        });

        final Button actualButton = (Button) activity.findViewById(R.id.actualButton);
        actualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               actualButtonClicked(activity);
            }
        });

        final Button visitButton = (Button) activity.findViewById(R.id.visitButton);
        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitButtonClicked(activity);
            }
        });

        final Button exhibitionsButton = (Button) activity.findViewById(R.id.exhibitionsButton);
        exhibitionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exhibitionsButtonClicked(activity);
            }
        });

        final Button extraButton = (Button) activity.findViewById(R.id.extraButton);
        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extraButtonClicked(activity);
            }
        });


        if (this.font == null){
            this.font = Typeface.createFromAsset(activity.getAssets(), "fonts/ArialNarrow.ttf");
            this.fontBold = Typeface.createFromAsset(activity.getAssets(), "fonts/ArialNarrowBold.ttf");
        }

        actualButton.setTypeface(this.fontBold);
        visitButton.setTypeface(this.fontBold);
        exhibitionsButton.setTypeface(this.fontBold);
        extraButton.setTypeface(this.fontBold);
    }

    public void logoButtonClicked(Activity activity){
        this.log("Activity launch", "INFO: Launching HOME Activity");
        Intent intent = new Intent(activity, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }

    public void visitButtonClicked(Activity activity){
        this.log("Activity launch", "INFO: Launching VISIT Activity");
        Intent intent = new Intent(activity, VisitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }

    public void actualButtonClicked(Activity activity){
        this.log("Activity launch", "INFO: Launching ACTUAL Activity");
        Intent intent = new Intent(activity, ActualActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }

    public void exhibitionsButtonClicked(Activity activity){
        this.log("Activity launch", "INFO: CONSTANT EXHIBITIONS Activity");
        Intent intent = new Intent(activity, ExhibitonsListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("constant", true);
        activity.startActivity(intent);
    }

    public void extraButtonClicked(Activity activity){
        this.log("Activity launch", "INFO: Launching EXTRA Activity");
        Intent intent = new Intent(activity, ExtraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }

    /**
     * Checks, if the image is already downloaded on the external memory
     * @param imageId - string identifier of the image
     * @return boolean true/false, depending if the image is downloaded or not
     */
    public boolean isImageDownloaded(String imageId){
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard.getAbsolutePath() + "/MG/images/"+ imageId + ".png");
        return file.exists();
    }

    public String getImagePathFromId(String imageId){
        File sdCard = Environment.getExternalStorageDirectory();
        String path = new String(sdCard.getAbsolutePath() + "/MG/images/"+ imageId + ".png");
        return path;
    }

    public String createImageURI(String imageId){
        return getImagesURL() + imageId + ".png";
    }

    /**
     *
     * @param bitmap
     * @param imageId
     * @throws FileNotFoundException
     */
    public void saveBitmapToSDcard(Bitmap bitmap, String imageId) throws FileNotFoundException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MG/images");
        dir.mkdirs();

        File file = new File(dir, imageId + ".png");
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        File output = new File(dir, ".nomedia");
        try {
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param drawable
     * @param drawableId - String identifier of the image
     * @throws FileNotFoundException
     */
    public void saveDrawableToSDcard(Drawable drawable, String drawableId) throws FileNotFoundException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MG/images");
        dir.mkdirs();

        if (drawable != null){
            Bitmap b = ((BitmapDrawable)drawable).getBitmap();
            File file = new File(dir, drawableId + ".png");
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);

            File output = new File(dir, ".nomedia");
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context - current application context
     * @param imageId - String identifier of the image
     * @return - the requested drawable
     */
    public Drawable getDrawableFromSDcard(Context context, String imageId){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MG/images");
        Bitmap bmp = BitmapFactory.decodeFile(dir + "/" + imageId + ".png");
        Drawable drawable = new BitmapDrawable(context.getResources(), bmp);
        return drawable;
    }

    /**
     * Changes the current activity title (next to the logo)
     * @param activity - current activity
     * @param resource
     */
    public void changeActivityTitle(Activity activity, int resource){
        TextView headerText = (TextView) activity.findViewById(R.id.headerText);
        headerText.setText(activity.getResources().getString(resource));
    }

    /**
     * Adds a string to the current activity title (next to the logo)
     * @param activity - current activity
     * @param message - text added (below) to the activity
     */
    public void addToTitle(Activity activity, String message){
        TextView headerText = (TextView) activity.findViewById(R.id.headerText);
        headerText.setText(headerText.getText() + System.getProperty("line.separator") + "/ " + message);
    }

    /**
     *
     * @param plistFilePath - a path to the .plist file
     * @return the top <array> tag containing all the dictionaries <dict>
     * @throws Exception
     */
    public NSArray getArrayFromFile(String plistFilePath) throws Exception {
        File file = new File(plistFilePath);

        FileInputStream is = new FileInputStream(file);
        NSArray array = (NSArray) PropertyListParser.parse(is);
        return array;
    }


    public void log(String type, String message){
        if (this.printLogMessages)
            Log.i(type, message);
    }

    /**
     * @param date
     * @return date String in format DD/MM/YYYY
     */
    public String getDateString(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.DAY_OF_MONTH)
                + "/" + (cal.get(Calendar.MONTH) + 1)
                + "/" + cal.get(Calendar.YEAR));
    }

    /**
     * @param date
     * @return date String in format DD/MM
     */
    public String getDateStringShort(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.DAY_OF_MONTH)
                + "/" + (cal.get(Calendar.MONTH) + 1));
    }

    public void parseExhibitionPlist(String dir) throws Exception {
        if (this.exhibitionsList == null){
            NSArray array = this.getArrayFromFile(dir + "/" + Constants.EXHIBITIONS);
            List<? extends MGEntry> list = PlistParser.parseExhibitionArray(array, false);
            this.exhibitionsList = list;
        }
    }

    public void parseConstantExhibitionPlist(String dir) throws Exception {
        if (this.constantExhibitionsList == null){
            NSArray array = this.getArrayFromFile(dir + "/" + Constants.CONSTANT_EXHIBITIONS);
            List<? extends MGEntry> list = PlistParser.parseExhibitionArray(array, true);
            this.constantExhibitionsList = list;
        }
    }

    public void parseEventPlist(String dir) throws Exception {
        if (this.eventList == null){
            NSArray array = this.getArrayFromFile(dir + "/" + Constants.EVENTS);
            List<? extends MGEntry> list = PlistParser.parseExhibitionArray(array, false);
            this.eventList = list;
        }
    }

    public void parseBuildingPlist(String dir) throws Exception {
        if (this.buildingList == null){
            NSArray array = this.getArrayFromFile(dir + "/" + Constants.BUILDINGS);
            List<? extends MGEntry> list = PlistParser.parseBuildingsArray(array);
            this.buildingList = list;
        }
    }

    public void parseActualitiesPlist(String dir) throws Exception {
        if (this.actList == null){
            NSArray array = this.getArrayFromFile(dir + "/" + Constants.ACTUALITIES);
            List<? extends MGEntry> list = PlistParser.parseActualitiesArray(array);
            this.actList = list;
        }
    }


    // =============================================================================
    //  Subclasses
    // =============================================================================
}
