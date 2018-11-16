package com.zoddl.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.zoddl.adapters.PostSubMainAdapter;
import com.zoddl.adapters.gallery.GalleryTagAdapter1;
import com.zoddl.adapters.gallery.GalleryTagAdapter2;
import com.zoddl.databinding.FragmentPostBinding;
import com.zoddl.model.commommodel.PrimaryTagTypeModel;
import com.zoddl.model.commommodel.SecondaryTag;
import com.zoddl.model.postdetials.Image;
import com.zoddl.model.postdetials.Payload;
import com.zoddl.model.postdetials.PostDetailsModel;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.PrefManager;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.utils.UrlConstants;
import com.zoddl.utils.paging.PaginationAdapterCallback;
import com.zoddl.utils.paging.PaginationScrollListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.zoddl.utils.AppConstant.HOME_POST_SORT_NEXT_NAV_DETAILS_DATE;
import static com.zoddl.utils.AppConstant.HOME_POST_SORT_NEXT_NAV_DETAILS_ID;
import static com.zoddl.utils.CommonUtils.getMonthName;
import static com.zoddl.interfaces.StringConstant._DOCUMENT;

/**
 * Created by Abhishek Tiwari on 10/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class PostFragment extends BaseFragment implements PaginationAdapterCallback{

    private Context context;
    private FragmentPostBinding binding;

    ArrayList<PrimaryTagTypeModel> primarytagtype = new ArrayList<>();
    ArrayList<SecondaryTag> secondarytagtype = new ArrayList<>();
    GalleryTagAdapter1 galleryTagAdapter1;
    GalleryTagAdapter2 galleryTagAdapter2;


    //private List<Payload> payLoad = new ArrayList<>();
    //private List<Image> images = new ArrayList<>();

    private PrefManager prefManager;
    //private ProgressDialog progressDialog;
    private String primaryTagId,primaryDate;
    private PostSubMainAdapter untagedSubMainAdapter;


    LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 100;
    private int currentPage = PAGE_START;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = PrefManager.getInstance(context);
        if (getArguments() != null) {
            primaryTagId = getArguments().getString(HOME_POST_SORT_NEXT_NAV_DETAILS_ID);
            primaryDate = getArguments().getString(HOME_POST_SORT_NEXT_NAV_DETAILS_DATE);
            //Log.e("AviName",details.getPrimeName());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_post, container, false);

        setRecyclerView();
        getprimarytag();
        getsecondarytag();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        binding.tvSeeAll.setVisibility(View.INVISIBLE);

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

        untagedSubMainAdapter = new PostSubMainAdapter(getContext());
        untagedSubMainAdapter.setOnEventListener(this);

        linearLayoutManager = new GridLayoutManager(context, 3);
        binding.rvSubGallery.setLayoutManager(linearLayoutManager);
        binding.rvSubGallery.setItemAnimator(new DefaultItemAnimator());
        binding.rvSubGallery.setAdapter(untagedSubMainAdapter);

        binding.rvSubGallery.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }
    public void getprimarytag() {

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

        String url = UrlConstants.ZODDL_PRIMARY_TAG;
        String fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_PRIMARY_TAG;
        }

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
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void loadFirstPage() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
               // progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {

                        PostDetailsModel mJSONObject = VolleyApiRequest.parsePostDetails(object);
                        List<Payload> payLoad = new ArrayList<>();

                        payLoad.addAll(mJSONObject.getPayload());

                        List<Image> images = null;
                        if (payLoad.size()!=0){
                            images = payLoad.get(0).getImages();
                        }

                        String month = getMonthName(payLoad.get(0).getDate());
                        boolean recent = getCurrent(month);
                        if (recent){
                            binding.tvTagName.setText("Recent"+" ("+payLoad.get(0).getCount()+")");
                        }else{
                            binding.tvTagName.setText(month+" ("+payLoad.get(0).getCount()+")");
                        }

                        binding.tvPrice.setText("Rs."+payLoad.get(0).getTotal());

                        if (images!=null){
                            untagedSubMainAdapter.addAll(images);
                            if (currentPage <= TOTAL_PAGES) untagedSubMainAdapter.addLoadingFooter();
                            else isLastPage = true;
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

        String url = UrlConstants.ZODDL_GALLERY_DETAILS_BASED_ON_PRIMARY_TAG_AND_MONTH;
        String fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_DETAILS_BASED_ON_PRIMARY_TAG_AND_MONTH;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Primary_Tag",primaryTagId);
        params.put("Month",getMonthName(primaryDate));
        params.put("Page", ""+currentPage);
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }
    private void loadNextPage() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                // progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {

                        untagedSubMainAdapter.removeLoadingFooter();
                        isLoading = false;

                        PostDetailsModel mJSONObject = VolleyApiRequest.parsePostDetails(object);
                        List<Payload> payLoad = new ArrayList<>();
                        payLoad.addAll(mJSONObject.getPayload());

                        List<Image> images = null;
                        if (payLoad.size()!=0){
                            images = payLoad.get(0).getImages();
                        }

                        if (currentPage != TOTAL_PAGES && images!=null && images.size()!=0) {
                            untagedSubMainAdapter.addAll(images);
                            untagedSubMainAdapter.addLoadingFooter();
                        } else isLastPage = true;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                untagedSubMainAdapter.showRetry(true, fetchErrorMessage(error));
            }
        };

        String url = UrlConstants.ZODDL_GALLERY_DETAILS_BASED_ON_PRIMARY_TAG_AND_MONTH;
        String fragName = ((HomeActivity) getActivity()).getFragmentName();
        if (fragName.equalsIgnoreCase(_DOCUMENT)){
            url = UrlConstants.ZODDL_DOC_DETAILS_BASED_ON_PRIMARY_TAG_AND_MONTH;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", prefManager.getKeyAuthtoken());
        params.put("Primary_Tag",primaryTagId);
        params.put("Month",getMonthName(primaryDate));
        params.put("Page", ""+currentPage);
        VolleyApiRequest request = new VolleyApiRequest(url, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }
    @Override
    public void retryPageLoad() {
        loadNextPage();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean getCurrent(String mMonth) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        String value = getMonthName(formattedDate);
        if (mMonth.equalsIgnoreCase(value)){
            return true;
        }
        return false;
    }
    @Override
    public void setListeners() {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public String getTagName() {
        return PostFragment.class.getSimpleName();
    }


}
