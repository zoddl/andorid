package com.zoddl.fragments;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.zoddl.activities.BaseActivity;
import com.zoddl.activities.HomeActivity;
import com.zoddl.adapters.gallery.GalleryMainAdapter;
import com.zoddl.adapters.gallery.GalleryTagAdapter1;
import com.zoddl.adapters.gallery.GalleryTagAdapter2;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.database.SycnTags;
import com.zoddl.databinding.FragmentGalleryBinding;
import com.zoddl.interfaces.OnEventListenerTollbar;
import com.zoddl.model.commommodel.PrimaryTagTypeModel;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.gallery.GalleryDetails;
import com.zoddl.model.gallery.ImagesItem;
import com.zoddl.model.gallery.TaglistItem;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zoddl.interfaces.StringConstant._GALLERY;
import static com.zoddl.interfaces.StringConstant._MESSAGE;
import static com.zoddl.interfaces.StringConstant._PRIMARY;
import static com.zoddl.interfaces.StringConstant._SECONDARY;
import static com.zoddl.interfaces.StringConstant._SUCCESS;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_ALPHABET;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_AMOUNT;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_COUNT;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_BY_USES;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_FILLTER_NONE;
import static com.zoddl.utils.AppConstant.HOME_TOLLBAR_SEARCH_EVENT;
import static com.zoddl.utils.AppConstant.LOCAL_BROADCAST_FOR_GALARY_UPDATE_UI;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BaseFragment implements OnEventListenerTollbar{

    private FragmentGalleryBinding binding;
    private Context context;
    private PrefManager prefManager;
    private MyRoomDatabase db;

    ArrayList<PrimaryTagTypeModel> primarytagtype = new ArrayList<>();
    ArrayList<SecondaryTag> secondarytagtype = new ArrayList<>();

    GalleryTagAdapter1 galleryTagAdapter1;
    GalleryTagAdapter2 galleryTagAdapter2;

    List<TaglistItem> taglistItems=new ArrayList<>();
    GalleryMainAdapter galleryMainAdapter;

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

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter(LOCAL_BROADCAST_FOR_GALARY_UPDATE_UI));

        assert ((HomeActivity) getActivity()) != null;
        ((HomeActivity) getActivity()).setOnEventListenerTollbar(GalleryFragment.this);

        if (NetworkUtils.isOnline(context)){
            ((BaseActivity)getActivity()).checkAndStartService();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(_MESSAGE);
            if (message.equalsIgnoreCase(_SUCCESS)){
                setCalls();
            }
        }
    };
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_gallery, container, false);

        setRecyclerView();
        setCalls();

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setCalls();
            }

        });

        return binding.getRoot();
    }
    private void setRecyclerView() {
        galleryTagAdapter1 = new GalleryTagAdapter1(context,primarytagtype);
        binding.rvTag1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvTag1.setAdapter(galleryTagAdapter1);

        galleryTagAdapter1.setItemClickListener(new GalleryTagAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ((HomeActivity) context).callForTagClick(_PRIMARY,primarytagtype.get(position).getprimaryName(),primarytagtype.get(position).getPrimaryid());
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

        galleryMainAdapter = new GalleryMainAdapter(getContext(),taglistItems);
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
                        primarytagtype.addAll(VolleyApiRequest.parsePrimaryList(object.getJSONArray("PrimaryTag")));
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
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_PRIMARY_TAG, params, stringListener, errorListener);
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
        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_SECONDARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    public void getAllDetailsGallery() {
        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, context.getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

       // progressDialog = DialogUtils.getProgressDialog(getActivity(), "Please wait . . .");
       // progressDialog.show();

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        GalleryDetails details=VolleyApiRequest.parseGalleryDetails(object);
                        taglistItems.addAll(details.getTaglist());

                        if (taglistItems.size()==0){
                            //showAlertDialog(_GALLERY, _DATA_NOT_FOUND);
                            setLocalInItData();
                        }else {
                            galleryMainAdapter.notifyDataSetChanged();
                            setLocalInItData();
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
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_GALLERY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    private void setCalls() {

        if (primarytagtype.size()!=0){
            primarytagtype.clear();
        }

        if (secondarytagtype.size()!=0){
            secondarytagtype.clear();
        }

        if (taglistItems.size()!=0){
            taglistItems.clear();
        }

        getprimarytag();
        getsecondarytag();
        getAllDetailsGallery();

        binding.swipeContainer.setRefreshing(false);

    }


    private void setLocalInItData() {

        List<SycnTags> obj = db.getDao().getAll();
        if (obj.size() != 0) {

            //List<TaglistItem> data = new ArrayList<>();
            for (int i = 0; i < obj.size(); i++) {

                if (obj.get(i).getTypeOfFile().equalsIgnoreCase(_GALLERY)){

                    List<ImagesItem> imagesItems = new ArrayList<>();
                    PrimaryTagTypeModel primaryTagTypeModel = new PrimaryTagTypeModel(obj.get(i).getPrimaryTag());
                    for (PrimaryTagTypeModel temp: primarytagtype){
                        temp.getprimaryName().equalsIgnoreCase(primaryTagTypeModel.getprimaryName());
                    }
                    if (primarytagtype!=null){
                        if (!primaryTagTypeModel.getprimaryName().equalsIgnoreCase("")){
                            if (checkPrimaryDublication(primarytagtype,primaryTagTypeModel)) {
                            } else {
                                primarytagtype.add(primaryTagTypeModel);
                            }
                        }

                    }




                    String secTag = obj.get(i).getSecondaryTag();
                    Gson gsonObj = new Gson();
                    Type secTagListType = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> secTagList = gsonObj.fromJson(secTag, secTagListType);

                    List<SecondaryTag> secList = new ArrayList<>();

                    for (String val : secTagList) {
                        SecondaryTag secondaryTagTypeModel = new SecondaryTag(val);
                        secList.add(secondaryTagTypeModel);

                        if (secondarytagtype!=null){
                            if (checkSecDublication(secondarytagtype,secondaryTagTypeModel)) {
                            } else {
                                secondarytagtype.add(secondaryTagTypeModel);
                            }
                        }
                    }

                    ImagesItem imagesItem = new ImagesItem(obj.get(i).getPrimaryTag(),
                            obj.get(i).getDescription(),
                            obj.get(i).getDate(),
                            obj.get(i).getAmount(),
                            obj.get(i).getTagStatus(),
                            obj.get(i).getFlow(),
                            "0",
                            secList,
                            obj.get(i).getImageUrl(),
                            obj.get(i).getImageUrlThumb());

                    imagesItems.add(imagesItem);

                    TaglistItem tagListItem = new TaglistItem(obj.get(i).getPrimaryTag(), obj.get(i).getAmount(), imagesItems);
                    //for (int x = 0; x < taglistItems.size(); x++) {
                    if (checkDetailDublication(taglistItems,tagListItem,imagesItem)) {
                    } else {
                        taglistItems.add(0,tagListItem);
                    }
                    //}


                    //data.add(tagListItem);
                }

            }

            //mergeData(data);

            galleryTagAdapter1.notifyDataSetChanged();
            galleryTagAdapter2.notifyDataSetChanged();
            galleryMainAdapter.notifyDataSetChanged();

        }
    }

    private void setCurrerntData(SycnTags obj) {

        if (obj!=null) {

            List<ImagesItem> imagesItems = new ArrayList<>();
            PrimaryTagTypeModel primaryTagTypeModel = new PrimaryTagTypeModel(obj.getPrimaryTag());
            for (PrimaryTagTypeModel temp: primarytagtype){
                temp.getprimaryName().equalsIgnoreCase(primaryTagTypeModel.getprimaryName());
            }
            if (primarytagtype!=null){
                if (!primaryTagTypeModel.getprimaryName().equalsIgnoreCase("")){
                    if (checkPrimaryDublication(primarytagtype,primaryTagTypeModel)) {
                    } else {
                        primarytagtype.add(primaryTagTypeModel);
                    }
                }

            }
            String secTag = obj.getSecondaryTag();
            Gson gsonObj = new Gson();
            Type secTagListType = new TypeToken<List<String>>() {
            }.getType();
            List<String> secTagList = gsonObj.fromJson(secTag, secTagListType);

            List<SecondaryTag> secList = new ArrayList<>();

            if (secTagList!=null){
                for (String val : secTagList) {
                    SecondaryTag secondaryTagTypeModel = new SecondaryTag(val);
                    secList.add(secondaryTagTypeModel);

                    if (secondarytagtype!=null){
                        if (checkSecDublication(secondarytagtype,secondaryTagTypeModel)) {
                        } else {
                            secondarytagtype.add(secondaryTagTypeModel);
                        }
                    }
                }
            }


            ImagesItem imagesItem = new ImagesItem(obj.getPrimaryTag(),
                    obj.getDescription(),
                    obj.getDate(),
                    obj.getAmount(),
                    obj.getTagStatus(),
                    obj.getFlow(),
                    "0",
                    secList,
                    obj.getImageUrl(),
                    obj.getImageUrlThumb());

            imagesItems.add(imagesItem);

            TaglistItem tagListItem = new TaglistItem(obj.getPrimaryTag(), obj.getAmount(), imagesItems);

            if (checkDetailDublication(taglistItems,tagListItem,imagesItem)) {
            } else {
                taglistItems.add(0,tagListItem);
            }

            galleryTagAdapter1.notifyDataSetChanged();
            galleryTagAdapter2.notifyDataSetChanged();
            galleryMainAdapter.notifyDataSetChanged();

        }
    }
    @Override
    public String getTagName() {
        return GalleryFragment.class.getSimpleName();
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
        switch (pos){
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
        galleryMainAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEventForObject(SycnTags obj) {
        setCurrerntData(obj);
    }


    private boolean checkDetailDublication(List<TaglistItem> list, TaglistItem conta1, ImagesItem mImagesItem) {
        for(TaglistItem o : list) {
            if(o.getPrimeName().equalsIgnoreCase(conta1.getPrimeName())) {
                List<ImagesItem> tempImageItem = o.getImages();
                tempImageItem.add(0,mImagesItem);
                return true;
            }
        }
        return false;
    }

    private boolean checkSecDublication(List<SecondaryTag> lista,SecondaryTag conta1) {
        for(SecondaryTag o : lista) {
            if(o.getSecondaryName().equalsIgnoreCase(conta1.getSecondaryName())) {
                //lista.add(conta1);
                return true;
            }
        }
        return false;
    }

    private boolean checkPrimaryDublication(List<PrimaryTagTypeModel> lista,PrimaryTagTypeModel conta1) {
        for(PrimaryTagTypeModel o : lista) {
            if(o.getprimaryName().equalsIgnoreCase(conta1.getprimaryName())) {
                //lista.add(conta1);
                return true;
            }
        }
        return false;
    }

   /* private boolean checkPrimaryDublication(List<PrimaryTagTypeModel> lista,PrimaryTagTypeModel conta1) {
        lista = new ArrayList<>();
        return containsLocation(lista,conta1.getprimaryName());
    }

    public static boolean containsLocation(Collection<PrimaryTagTypeModel> c, String location) {
        for(PrimaryTagTypeModel o : c) {
            if(o != null && o.getprimaryName().equals(location)) {
                return true;
            }
        }
        return false;
    }*/
}