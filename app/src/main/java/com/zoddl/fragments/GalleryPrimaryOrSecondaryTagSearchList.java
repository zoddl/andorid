package com.zoddl.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.activities.HomeActivity;
import com.zoddl.adapters.gallery.GalleryClickDetailsGallerytag;
import com.zoddl.adapters.gallery.GalleryTagAdapter1;
import com.zoddl.adapters.gallery.GalleryTagAdapter2;
import com.zoddl.model.commommodel.PrimaryTagTypeModel;
import com.zoddl.databinding.FragmentGalleryBinding;
import com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel.GalleryprimaryorsecondaryResponse;
import com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel.PayloadItem;
//import com.clickaccounting.model.primarytaggallerydetails.PrimaryGalleryResponse;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.PrefManager;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.utils.UrlConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoddl.utils.AppConstant.HOME_TAG_DETAILS_ID;
import static com.zoddl.utils.AppConstant.HOME_TAG__DETAILS_FROM;

public class GalleryPrimaryOrSecondaryTagSearchList extends BaseFragment implements StringConstant{

    private FragmentGalleryBinding binding;
    private Context context;
    private String _id,fromWhere;

    ArrayList<PrimaryTagTypeModel> primarytagtype = new ArrayList<>();
    ArrayList<SecondaryTag> secondarytagtype = new ArrayList<>();

    GalleryTagAdapter1 galleryTagAdapter1;
    GalleryTagAdapter2 galleryTagAdapter2;

    GalleryClickDetailsGallerytag galleryClickDetailsGallerytag;

    PrefManager manager;
    private List<PayloadItem> payloadItems1=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = PrefManager.getInstance(context);
        if (getArguments() != null) {
            _id = getArguments().getString(HOME_TAG_DETAILS_ID);
            fromWhere = getArguments().getString(HOME_TAG__DETAILS_FROM);
            //Log.e("AviName",details.getPrimeName());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_gallery, container, false);
        setRecyclerView();
        getprimarytag();
        getsecondarytag();
        getgalleryDetailstag(fromWhere,_id);
        return binding.getRoot();
    }


    private void setRecyclerView() {

        galleryTagAdapter1 = new GalleryTagAdapter1(context,primarytagtype);
        binding.rvTag1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvTag1.setAdapter(galleryTagAdapter1);

        galleryTagAdapter1.setItemClickListener(new GalleryTagAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ((HomeActivity) context).callForTagClick(StringConstant._PRIMARY,primarytagtype.get(position).getprimaryName(),primarytagtype.get(position).getPrimaryid());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);
            }
        });

        galleryTagAdapter2 = new GalleryTagAdapter2(getContext(),secondarytagtype);
        binding.rvTag2.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        binding.rvTag2.setAdapter(galleryTagAdapter2);
        galleryTagAdapter2.setItemClickListener(new GalleryTagAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ((HomeActivity) context).callForTagClick(StringConstant._SECONDARY,secondarytagtype.get(position).getSecondaryName(),secondarytagtype.get(position).getId());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);
            }
        });


        galleryClickDetailsGallerytag = new GalleryClickDetailsGallerytag (getContext(),fromWhere,payloadItems1);
        binding.rvMainGallery.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rvMainGallery.setAdapter(galleryClickDetailsGallerytag);
        binding.rvMainGallery.setNestedScrollingEnabled(false);

    }

    @Override
    public void setListeners() {
    }

    @Override
    public String getTagName() {
        return GalleryFragment.class.getSimpleName();
    }

    //Primary Tag List
    public void getprimarytag() {

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        primarytagtype.addAll(VolleyApiRequest.parsePrimaryList(object.getJSONArray("PrimaryTag")));
                        //Toast.makeText(context, primarytagtype.size()+"", Toast.LENGTH_SHORT).show();
                        galleryTagAdapter1.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {

                    e.printStackTrace();
                }
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        String url = UrlConstants.ZODDL_PRIMARY_TAG;
        String fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_PRIMARY_TAG;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken",manager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }
    //Secondary Tag List
    public void getsecondarytag() {
        // progressDialog = DialogUtils.getProgressDialog(getActivity(), "Please wait . . .");
        // progressDialog.show();
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        secondarytagtype.addAll(VolleyApiRequest.parseSecondaryList(object.getJSONArray("SecondaryTag")));
                        galleryTagAdapter2.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    //progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();

            }
        };

        String url = UrlConstants.ZODDL_SECONDARY_TAG;
        String fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_SECONDARY_TAG;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", manager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void  getgalleryDetailstag(String mFromWhere, String tagid)
    {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        payloadItems1.clear();
                        GalleryprimaryorsecondaryResponse priamrydetails1=VolleyApiRequest.GalleryprimaryorsecondaryRes(object);
                        payloadItems1.addAll(priamrydetails1.getPayload());
                        // payloadItems1.get(0).setImages(priamrydetails1.getPayload().get(0).getImages());
                        galleryClickDetailsGallerytag.notifyDataSetChanged();

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();

            }
        };

        String url;

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", manager.getKeyAuthtoken());
        if (mFromWhere.equalsIgnoreCase("Primary")){
            params.put("Primary_Tag", tagid);
            url = UrlConstants.ZODDL_GALLERY_TAG_BASED_ON_PRIMARY_TAG;
            String fragName = ((HomeActivity) getActivity()).getFragmentName();
            if (fragName.equalsIgnoreCase(_DOCUMENT)){
                url = UrlConstants.ZODDL_DOC_TAG_BASED_ON_PRIMARY_TAG;
            }

        }else{
            params.put("Secondary_Tag", tagid);
            url = UrlConstants.ZODDL_GALLERY_TAG_BASED_ON_SECONDARY_TAG;
            String fragName = ((HomeActivity) getActivity()).getFragmentName();
            if (fragName.equalsIgnoreCase(_DOCUMENT)){
                url = UrlConstants.ZODDL_DOC_TAG_BASED_ON_SECONDARY_TAG;
            }
        }

        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);

    }

    @Override
    public void onClick(View v) {

    }
}
