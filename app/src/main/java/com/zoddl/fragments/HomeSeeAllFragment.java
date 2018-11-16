package com.zoddl.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.activities.HomeActivity;
import com.zoddl.adapters.gallery.GalleryTagAdapter2;
import com.zoddl.adapters.home.HomePrimaryTagAdapter;
import com.zoddl.adapters.hometagslist.HomeSeeAllMainAdapter;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.databinding.FragmentGalleryBinding;
import com.zoddl.interfaces.OnEventListenerTollbar;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.home.HomeDetailTagWise.HomeSeeAllModel;
import com.zoddl.model.home.HomeDetailTagWise.Payload;
import com.zoddl.model.home.HomePrimaryTagPayload;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoddl.interfaces.StringConstant._PRIMARY;
import static com.zoddl.interfaces.StringConstant._SECONDARY;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_SEEALL_DETAILS_TYPE;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_NONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSeeAllFragment extends BaseFragment implements OnEventListenerTollbar{

    private FragmentGalleryBinding binding;
    private Context context;
    private PrefManager prefManager;
    private MyRoomDatabase db;

    ArrayList<HomePrimaryTagPayload> primarytagtype = new ArrayList<>();
    ArrayList<SecondaryTag> secondarytagtype = new ArrayList<>();

    HomePrimaryTagAdapter galleryTagAdapter1;
    GalleryTagAdapter2 galleryTagAdapter2;

    List<Payload> taglistItems=new ArrayList<>();
    HomeSeeAllMainAdapter galleryMainAdapter;

    String tagType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = PrefManager.getInstance(context);
        db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();

        assert ((HomeActivity) getActivity()) != null;
        ((HomeActivity) getActivity()).setOnEventListenerTollbar(HomeSeeAllFragment.this);

        if (getArguments()!=null){
            tagType = getArguments().getString(HOME_POST_SORT_SEEALL_DETAILS_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_gallery, container, false);

        setRecyclerView();
        setCalls();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        galleryTagAdapter1 = new HomePrimaryTagAdapter(context,primarytagtype);
        binding.rvTag1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvTag1.setAdapter(galleryTagAdapter1);

        galleryTagAdapter1.setItemClickListener(new HomePrimaryTagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ((HomeActivity) context).callForTagClick(_PRIMARY,primarytagtype.get(position).getPrime_Name(),primarytagtype.get(position).getId());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);
            }
        });

        galleryTagAdapter2 = new GalleryTagAdapter2(getContext(),secondarytagtype);
        binding.rvTag2.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        binding.rvTag2.setAdapter(galleryTagAdapter2);

        galleryTagAdapter2.setItemClickListener(new GalleryTagAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ((HomeActivity) context).callForTagClick(_SECONDARY,secondarytagtype.get(position).getSecondaryName(),secondarytagtype.get(position).getId());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);
            }
        });

        galleryMainAdapter = new HomeSeeAllMainAdapter(getContext(),taglistItems);
        binding.rvMainGallery.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rvMainGallery.setAdapter(galleryMainAdapter);
        binding.rvMainGallery.setNestedScrollingEnabled(false);
    }

    public void getprimarytag() {
        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        primarytagtype.addAll(VolleyApiRequest.parseHomePrimaryList(object.getJSONArray("Primary_Tag")));
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
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_PRIMARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }
    public void getsecondarytag() {

        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

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
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_SECONDARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    public void getAllDetailsGallery() {
        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        HomeSeeAllModel details=VolleyApiRequest.parseHomeDetails(object);
                        taglistItems.addAll(details.getPayload());

                        if (taglistItems.size()==0){
                            //showAlertDialog(_GALLERY, _DATA_NOT_FOUND);
                        }else {
                            galleryMainAdapter.notifyDataSetChanged();
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
                //progressDialog.dismiss();
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Tag_Type",tagType);
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_SEE_ALL_DETAILS, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    private void setCalls() {
        getprimarytag();
        getsecondarytag();
        getAllDetailsGallery();

    }

    @Override
    public String getTagName() {
        return HomeSeeAllFragment.class.getSimpleName();
    }

    @Override
    public void setListeners() {}

    @Override
    public void onClick(View view) {}

    @Override
    public void onEvent(String query, int pos) {
        if (pos==HOME_TOLLBAR_FILLTER_NONE){
            getAllDetailsGallery();
            return;
        }
       /* switch (pos){
            case HOME_TOLLBAR_FILLTER_BY_AMOUNT:
                Collections.sort(taglistItems,TaglistItem.SortWithAmount);
                break;
            case HOME_TOLLBAR_FILLTER_BY_COUNT:
                Collections.sort(taglistItems,TaglistItem.SortWithCount);
                break;
            case HOME_TOLLBAR_FILLTER_BY_USES:
                Collections.sort(taglistItems,TaglistItem.SortWithCount);
                break;
            case HOME_TOLLBAR_FILLTER_BY_ALPHABET:
                Collections.sort(taglistItems,TaglistItem.SortWithAlphabet);
                break;
            case HOME_TOLLBAR_SEARCH_EVENT:
                galleryMainAdapter.getFilter().filter(query);
                return;

        }
        binding.rvMainGallery.setLayoutManager(new LinearLayoutManager(context));
        galleryMainAdapter = new GalleryMainAdapter(context,taglistItems);
        binding.rvMainGallery.setAdapter(galleryMainAdapter);
        galleryMainAdapter.notifyDataSetChanged();*/

    }

    @Override
    public void onEventForObject(SycnTags obj) {

    }


}