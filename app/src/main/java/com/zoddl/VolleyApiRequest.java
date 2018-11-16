package com.zoddl;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.commommodel.TagDetailsModel;
import com.zoddl.model.commommodel.PrimaryTagTypeModel;
import com.zoddl.model.home.HomeDataResponse;
import com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel.GalleryprimaryorsecondaryResponse;
import com.zoddl.model.home.HomeDataModel.CashItem;
import com.zoddl.model.gallery.GalleryDetails;
import com.zoddl.model.home.HomeDetailTagWise.HomeSeeAllModel;
import com.zoddl.model.home.HomePrimaryTagPayload;
import com.zoddl.model.postdetials.Payload;
import com.zoddl.model.postdetials.PostDetailsModel;
import com.zoddl.model.report.ReportPayload;
import com.zoddl.model.report.ReportPrimaryTag;
import com.zoddl.model.report.ReportSecondaryTag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by garuv on 2/5/18.
 */

public class VolleyApiRequest extends BaseStringRequest {
    private Map<String, String> bodyParams;

    public VolleyApiRequest(String Url, @Nullable Map<String, String> bodyParams, Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Request.Method.POST, Url, listener, errorListener);
        this.bodyParams = bodyParams;
        if (bodyParams != null)
            Log.e("Request Details", "API: " + Url + "\nBody Params: " + new Gson().toJson(bodyParams).toString());
    }

    @Override
    public Map<String, String> getParams() {
        return bodyParams;
    }


    public static List<PrimaryTagTypeModel> parsePrimaryList(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PrimaryTagTypeModel>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static List<HomePrimaryTagPayload> parseHomePrimaryList(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<HomePrimaryTagPayload>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static List<SecondaryTag> parseSecondaryList(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<SecondaryTag>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static List<ReportPrimaryTag> parseReportPrimaryList(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReportPrimaryTag>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static List<ReportPayload> parseReport(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReportPayload>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static List<ReportSecondaryTag> parseReportSecondaryList(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReportSecondaryTag>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static List<TagDetailsModel> parseGalleryList(JSONArray jsonArray) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<TagDetailsModel>>() {
        }.getType();
        return gson.fromJson(jsonArray.toString(), type);
    }

    public static GalleryDetails parseGalleryDetails(JSONObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<GalleryDetails>() {
        }.getType();
        return gson.fromJson(object.toString(), type);
    }

    public static PostDetailsModel parsePostDetails(JSONObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<PostDetailsModel>() {
        }.getType();
        return gson.fromJson(object.toString(), type);
    }
    public static Payload parsePostDetailsPayload(JSONObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<Payload>() {
        }.getType();
        return gson.fromJson(object.toString(), type);
    }

    public static GalleryprimaryorsecondaryResponse GalleryprimaryorsecondaryRes(JSONObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<GalleryprimaryorsecondaryResponse>() {
        }.getType();
        return gson.fromJson(object.toString(), type);
    }


    public static HomeDataResponse parseHomedataResponse(JSONObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<HomeDataResponse>() {
        }.getType();
        return gson.fromJson(object.toString(), type);
    }

    public static List<CashItem> parseList(JSONArray array) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CashItem>>() {
        }.getType();
        return gson.fromJson(array.toString(), type);
    }

    public static HomeSeeAllModel parseHomeDetails(JSONObject object) {
        Gson gson = new Gson();
        Type type = new TypeToken<HomeSeeAllModel>() {
        }.getType();
        return gson.fromJson(object.toString(), type);
    }




}
