package com.mg.androidapp.utility;

import android.graphics.Color;

import com.dd.plist.NSArray;
import com.dd.plist.NSDate;
import com.dd.plist.NSDictionary;
import com.mg.objects.Actuality;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.Image;
import com.mg.objects.MGEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods for parsing all types of plist files:
 * Buildings, Actulalities, Exhibitions (Events are processed the same way as Exhibitions)
 */
public class PlistParser{
    // =============================================================================
    // Constructors
    // =============================================================================

    public PlistParser(){}

    // =============================================================================
    // Methods
    // =============================================================================

   static public List <? extends MGEntry> parseBuildingsArray(NSArray buildingsArray){
        List <Building> buildingList = new ArrayList<Building>();

        int i = 0;
        while (i != buildingsArray.count()){
            Building current_building = new Building();
            NSDictionary current_building_dict = (NSDictionary) buildingsArray.getArray()[i];
            //appManager.log("test", "name = " + name);

            // setting String values of the building
            current_building.setName(current_building_dict.objectForKey("Name").toString());
            current_building.setKind(current_building_dict.objectForKey("Kind").toString());
            current_building.setEngName(current_building_dict.objectForKey("EngName").toString());
            current_building.setAddress(current_building_dict.objectForKey("Address").toString());
            current_building.setInfo(current_building_dict.objectForKey("Info").toString());
            current_building.setEngInfo(current_building_dict.objectForKey("EngInfo").toString());
            current_building.setGps(current_building_dict.objectForKey("GPS").toString());

            // setting Color of the building
            NSDictionary color_dict = (NSDictionary) (current_building_dict.objectForKey("Color"));
            int red = Integer.parseInt(color_dict.objectForKey("Red").toString());
            int green = Integer.parseInt(color_dict.objectForKey("Green").toString());
            int blue = Integer.parseInt(color_dict.objectForKey("Blue").toString());
            int buildingColor = Color.rgb(red, green, blue);
            current_building.setColor(buildingColor);

            // setting informations about images of the building
            NSArray imagesArray = (NSArray) current_building_dict.objectForKey("Images");
            int j = 0;
            while (j != imagesArray.count()){
                NSDictionary current_image_dict = (NSDictionary) imagesArray.getArray()[j];
                Image current_image = new Image();
                current_image.setDescription(current_image_dict.objectForKey("Description").toString());
                current_image.setEngDescription(current_image_dict.objectForKey("EngDescription").toString());
                current_image.setId(current_image_dict.objectForKey("Identifier").toString());
                current_building.addImage(current_image);
                j++;
            }

            buildingList.add(current_building);
            i++;
        }
        return buildingList;
    }

    static public List <Actuality> parseActualitiesArray(NSArray actArray){
        List <Actuality> actList = new ArrayList<Actuality>();

        int i = 0;
        while (i != actArray.count()){
            Actuality current_actuality = new Actuality();
            NSDictionary current_actuality_dict = (NSDictionary) actArray.getArray()[i];

            current_actuality.setName(current_actuality_dict.objectForKey("Name").toString());
            current_actuality.setKind(current_actuality_dict.objectForKey("Kind").toString());
            current_actuality.setEngName(current_actuality_dict.objectForKey("EngName").toString());
            current_actuality.setInfo(current_actuality_dict.objectForKey("Info").toString());
            current_actuality.setEngInfo(current_actuality_dict.objectForKey("EngInfo").toString());
            current_actuality.setFbLink(current_actuality_dict.objectForKey("FbLink").toString());
            current_actuality.setYtLink(current_actuality_dict.objectForKey("YtLink").toString());
            current_actuality.setTwLink(current_actuality_dict.objectForKey("TwLink").toString());

            // setting dates
            NSDate insertionDate = (NSDate) current_actuality_dict.objectForKey("InsertionDate");
            NSDate removeDate = (NSDate) current_actuality_dict.objectForKey("RemoveDate");

            current_actuality.setInsertionDate(insertionDate.getDate());
            current_actuality.setRemoveDate(removeDate.getDate());

            // setting  images of the actuality
            NSArray imagesArray = (NSArray) current_actuality_dict.objectForKey("Images");
            int j = 0;
            while (j != imagesArray.count()){
                NSDictionary current_image_dict = (NSDictionary) imagesArray.getArray()[j];
                Image current_image = new Image();
                current_image.setDescription(current_image_dict.objectForKey("Description").toString());
                current_image.setEngDescription(current_image_dict.objectForKey("EngDescription").toString());
                current_image.setId(current_image_dict.objectForKey("Identifier").toString());
                current_actuality.addImage(current_image);
                j++;
            }

            //setting list of buildings
            NSArray buildingArray = (NSArray) current_actuality_dict.objectForKey("Buildings");
            j = 0;
            while (j != buildingArray.count()){
                String current_bldgId = new String();
                current_bldgId =  buildingArray.getArray()[j].toString();
                current_actuality.addBuildingId(current_bldgId);
                j++;
            }

            actList.add(current_actuality);
            i++;
        }
        return actList;
    }

    static public List <? extends MGEntry> parseExhibitionArray(NSArray expositionArray, boolean areExpositionsConstant){
        List <Exhibition> exhibitionList = new ArrayList<Exhibition>();

        int i = 0;
        while (i != expositionArray.count()){
            Exhibition current_exhibition = new Exhibition();
            NSDictionary current_exhibition_dict = (NSDictionary) expositionArray.getArray()[i];

            current_exhibition.setName(current_exhibition_dict.objectForKey("Name").toString());
            current_exhibition.setKind(current_exhibition_dict.objectForKey("Kind").toString());
            current_exhibition.setEngName(current_exhibition_dict.objectForKey("EngName").toString());
            current_exhibition.setInfo(current_exhibition_dict.objectForKey("Info").toString());
            current_exhibition.setEngInfo(current_exhibition_dict.objectForKey("EngInfo").toString());
            current_exhibition.setFbLink(current_exhibition_dict.objectForKey("FbLink").toString());
            current_exhibition.setYtLink(current_exhibition_dict.objectForKey("YtLink").toString());
            current_exhibition.setTwLink(current_exhibition_dict.objectForKey("TwLink").toString());

            // setting dates
            NSDate insertionDate = (NSDate) current_exhibition_dict.objectForKey("InsertionDate");
            NSDate removeDate = (NSDate) current_exhibition_dict.objectForKey("RemoveDate");
            NSDate dateFrom = (NSDate) current_exhibition_dict.objectForKey("DateFrom");
            NSDate dateTo = (NSDate) current_exhibition_dict.objectForKey("DateTo");


            if (!areExpositionsConstant){
                current_exhibition.setInsertionDate(insertionDate.getDate());
                current_exhibition.setRemoveDate(removeDate.getDate());
            }

            if (dateFrom != null)
                current_exhibition.setDateFrom(dateFrom.getDate());
            if (dateTo != null)
            current_exhibition.setDateTo(dateTo.getDate());

            // setting  images of the exhibition
            NSArray imagesArray = (NSArray) current_exhibition_dict.objectForKey("Images");
            int j = 0;
            while (j != imagesArray.count()){
                NSDictionary current_image_dict = (NSDictionary) imagesArray.getArray()[j];
                Image current_image = new Image();
                current_image.setDescription(current_image_dict.objectForKey("Description").toString());
                current_image.setEngDescription(current_image_dict.objectForKey("EngDescription").toString());
                current_image.setId(current_image_dict.objectForKey("Identifier").toString());
                current_exhibition.addImage(current_image);
                j++;
            }

            //setting list of buildings
            NSArray buildingArray = (NSArray) current_exhibition_dict.objectForKey("Buildings");
            j = 0;
            while (j != buildingArray.count()){
                String current_bldgId;
                current_bldgId =  buildingArray.getArray()[j].toString();
                current_exhibition.addBuildingId(current_bldgId);
                j++;
            }

            exhibitionList.add(current_exhibition);
            i++;
        }
        return exhibitionList;
    }
}
