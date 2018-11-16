package com.zoddl.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.activities.HomeActivity;
import com.zoddl.adapters.HomePostSortAdapter;
import com.zoddl.adapters.gallery.GalleryTagAdapter1;
import com.zoddl.adapters.gallery.GalleryTagAdapter2;
import com.zoddl.databinding.FragmentPostSortBinding;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.model.commommodel.PrimaryTagTypeModel;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.postdetials.Payload;
import com.zoddl.model.postdetials.PostDetailsModel;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_ID;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_SOURCE_TYPE;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_TYPE;

/**
 * Created by Abhishek Tiwari on 13/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class HomePostSortFragment extends BaseFragment{

    private Context context;
    private FragmentPostSortBinding binding;

    ArrayList<PrimaryTagTypeModel> primarytagtype = new ArrayList<>();
    ArrayList<SecondaryTag> secondarytagtype = new ArrayList<>();
    GalleryTagAdapter1 galleryTagAdapter1;
    GalleryTagAdapter2 galleryTagAdapter2;

    HomePostSortAdapter tagMainAdapter;

    private PrefManager prefManager;

    String primaryType,primaryTagId,sourceType;

    LinearLayoutManager linearLayoutManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = PrefManager.getInstance(context);

        if (getArguments()!=null){
            primaryType = getArguments().getString(HOME_POST_SORT_SEEALL_DETAILS_TYPE);
            primaryTagId = getArguments().getString(HOME_POST_SORT_SEEALL_DETAILS_ID);
            sourceType = getArguments().getString(HOME_POST_SORT_SEEALL_DETAILS_SOURCE_TYPE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_post_sort, container, false);

        setRecyclerView();
        getprimarytag();
        getsecondarytag();

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

        tagMainAdapter = new HomePostSortAdapter(getContext());

        linearLayoutManager = new LinearLayoutManager(context);
        binding.rvMainGallery.setLayoutManager(linearLayoutManager);
        binding.rvMainGallery.setItemAnimator(new DefaultItemAnimator());
        binding.rvMainGallery.setAdapter(tagMainAdapter);

        loadFirstPage();
    }




    @Override
    public String getTagName() {
        return PostFragment.class.getSimpleName();
    }

    public void getprimarytag() {

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        primarytagtype.addAll(VolleyApiRequest.parsePrimaryList(object.getJSONArray("Primary_Tag")));
                        //Toast.makeText(context, primarytagtype.size()+"", Toast.LENGTH_SHORT).show();
                        galleryTagAdapter1.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response",error.toString());
            }
        };

        String url = UrlConstants.ZODDL_REPORT_PRIMARY_TAG;

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }
    public void getsecondarytag() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        secondarytagtype.addAll(VolleyApiRequest.parseSecondaryList(object.getJSONArray("Secondary_Tag")));
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

        String url = UrlConstants.ZODDL_REPORT_SECONDARY_TAG;


        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void loadFirstPage() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {

                        PostDetailsModel mJSONObject = VolleyApiRequest.parsePostDetails(object);
                        List<Payload> payLoad = new ArrayList<>();
                        payLoad.addAll(mJSONObject.getPayload());
                        if (payLoad.size()!=0){
                            tagMainAdapter.addAll(payLoad);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        };

        String url = UrlConstants.ZODDL_SEE_ALL_DETAILS_BY_MONTH;


        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Tag_Type",primaryType);
        params.put("Primary_Tag", ""+primaryTagId);
        params.put("Source_Type", ""+sourceType);
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }


    @Override
    public void setListeners() {
    }

    @Override
    public void onClick(View view) {
    }

}
