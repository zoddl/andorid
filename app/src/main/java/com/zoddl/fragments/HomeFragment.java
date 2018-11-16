package com.zoddl.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zoddl.adapters.home.HomePrimaryTagAdapter;
import com.zoddl.database.MyRoomDatabase;
import com.zoddl.interfaces.StringConstant;
import com.zoddl.model.home.HomeCommonData;
import com.zoddl.model.home.HomeDataResponse;
import com.zoddl.model.home.HomePrimaryTagPayload;
import com.zoddl.R;
import com.zoddl.VolleyApiRequest;
import com.zoddl.activities.HomeActivity;
import com.zoddl.adapters.home.HomeTagDetailAdapter;
import com.zoddl.adapters.home.TablehomeAdapter;
import com.zoddl.databinding.FragmentHomeBinding;
import com.zoddl.model.home.HomeDataModel.CashItem;
import com.zoddl.model.home.HomeDataModel.PayloadItem;
import com.zoddl.model.homeReport.Reportdata;
import com.zoddl.model.report.ReportSecondaryTag;
import com.zoddl.utils.AppConstant;
import com.zoddl.utils.NetworkUtils;
import com.zoddl.utils.PrefManager;
import com.zoddl.utils.UrlConstants;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private Context context;
    private PrefManager manager;
    private FragmentHomeBinding binding;

    private TablehomeAdapter tablehomeAdapter;
    private HomeTagDetailAdapter homeTagDetailAdapter;

    private HomePrimaryTagAdapter primaryTags;
    private List<HomePrimaryTagPayload> primarytagtype = new ArrayList<>();

    private HomeDataResponse homedetails;

    private List<PayloadItem> payloaditemarraylist = new ArrayList<>();
    private List<PayloadItem> temp = new ArrayList<>();
    private MyRoomDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = PrefManager.getInstance(context);
        db = Room.databaseBuilder(context, MyRoomDatabase.class, AppConstant.DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflating view layout
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_home, container, false);

        primaryTagsSetup();
        tableSetup();
        getHomeData();

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getTopListItems();
                getHomeData();
            }

        });

        return binding.getRoot();
    }

    private void tableSetup() {
        //Table details layout
        tablehomeAdapter = new TablehomeAdapter(context, payloaditemarraylist);
        binding.rvTagTableHome.setLayoutManager(new GridLayoutManager(context, 6));
        binding.rvTagTableHome.setAdapter(tablehomeAdapter);
        binding.rvTagTableHome.setNestedScrollingEnabled(false);

        getTopListItems();
    }

    private void primaryTagsSetup() {
        primaryTags = new HomePrimaryTagAdapter(context, primarytagtype);
        binding.rvTagHome.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.rvTagHome.setAdapter(primaryTags);
        primaryTags.setItemClickListener(new HomePrimaryTagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                if (primarytagtype.get(position).getSource_Type().equalsIgnoreCase("document gallery")) {
                    ((HomeActivity) context).setFragmentName(StringConstant._DOCUMENT);
                } else {
                    ((HomeActivity) context).setFragmentName(StringConstant._GALLERY);
                }

                ((HomeActivity) context).callForTagClick(StringConstant._PRIMARY, primarytagtype.get(position).getPrime_Name(), primarytagtype.get(position).getId());
                ((HomeActivity) context).onDrawerItemSelected(null, AppConstant.FRAGMENT_GALLERY_Click);
            }
        });

        getPrimaryTag();
    }

    public void getPrimaryTag() {
        if (primarytagtype.size() != 0) {
            primarytagtype.clear();
        }
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response1", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        primarytagtype.addAll(VolleyApiRequest.parseHomePrimaryList(object.getJSONArray("Primary_Tag")));
                        primaryTags.notifyDataSetChanged();

                        if (manager.getKeyIsFirstTime()) {
                            for (HomePrimaryTagPayload obj : primarytagtype) {
                                db.getPrimaryTagsDao().insert(obj);
                            }

                            getsecondarytag();
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

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", manager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_PRIMARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);
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
                        List<ReportSecondaryTag> secondarytagtype = new ArrayList<>();

                        secondarytagtype.addAll(VolleyApiRequest.parseReportSecondaryList(object.getJSONArray("Secondary_Tag")));

                        for (ReportSecondaryTag obj : secondarytagtype) {
                            db.getSecTagsDao().insertAll(obj);
                        }


                        manager.setKeyIsFirstTime(false);
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
        params.put("Authtoken", manager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_SECONDARY_TAG, params, stringListener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }


    //Table data insert functionality

    public void getTopListItems() {
        if (payloaditemarraylist.size()!=0){
            payloaditemarraylist.clear();
        }
        if (temp.size()!=0){
            temp.clear();
        }

        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Top_list_response", response);

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        JSONArray payLoadArray = object.getJSONArray("Payload");
                        payloaditemarraylist.add(getDummyItem());

                        for (int p = 0; p < payLoadArray.length(); p++) {
                            PayloadItem payloadItem = new PayloadItem();
                            JSONArray array = payLoadArray.getJSONObject(p).getJSONArray(getJsonArrayName(p));
                            //Log.e("ArrayName/Size", array.length() + " " + getJsonArrayName(p));
                            List<CashItem> items = VolleyApiRequest.parseList(array);
                            payloadItem.setCash(items);

                            payloadItem.setColumnHeading(getJsonArrayName(p));

                            payloaditemarraylist.add(payloadItem);
                        }
                        temp.addAll(payloaditemarraylist);
                        temp.remove(0);

                        tablehomeAdapter.notifyDataSetChanged();
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
                Log.e("Top_list_error", error.toString());
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", manager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_HOME_FARGEMENT_DETAILS, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);

    }

    //Array Name In Android
    private String getJsonArrayName(int pos) {
        String arrayname = "";
        switch (pos) {
            case 0:
                arrayname = "cash+";
                break;
            case 1:
                arrayname = "cash-";
                break;
            case 2:
                arrayname = "bank+";
                break;
            case 3:
                arrayname = "bank-";
                break;
            case 4:
                arrayname = "other";
                break;
        }
        return arrayname;
    }

    //In Table colomu name set code in Android
    private PayloadItem getDummyItem() {
        PayloadItem payloadItem = new PayloadItem();
        List<CashItem> items = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            CashItem cashItem = new CashItem();
            cashItem.setTotalAmount(i + "");
            items.add(cashItem);
        }
        payloadItem.setCash(items);
        payloadItem.setColumnHeading("NO");
        return payloadItem;
    }


    JSONObject reportJson;

    // Home All Data From ApIi in android
    public void getHomeData() {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Home_data_response", response + " " + manager.getKeyAuthtoken());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("ResponseCode") == 200) {
                        homedetails = (VolleyApiRequest.parseHomedataResponse(object));

                        reportJson = object.getJSONObject("Payload").
                                getJSONObject("Reportdata");

                        setUpList(homedetails);
                    }
                } catch (Exception e) {
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Home_data_error", error.toString());
            }
        };

        Map<String, String> params = new HashMap<>();
        params.put("Authtoken", manager.getKeyAuthtoken());
        VolleyApiRequest request = new VolleyApiRequest(UrlConstants.ZODDL_REPORT_HOME_GALLERY_DATA, params, stringListener, errorListener);
        Volley.newRequestQueue(getActivity()).add(request);

    }

    List<HomeCommonData> list = new ArrayList<>();
    Reportdata reportdata;

    float strCashP, strCashM, strBankP, strBankM, other;

    private void setUpList(HomeDataResponse homedetails) {
        if (list.size()!=0){
            list.clear();
        }
        binding.swipeContainer.setRefreshing(false);

        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                HomeCommonData cashplusdata = homedetails.getPayload().getCashplusdata();
                list.add(cashplusdata);
                strCashP = cashplusdata.getTotal();
            } else if (i == 1) {
                HomeCommonData cashminusdata = homedetails.getPayload().getCashminusdata();
                list.add(cashminusdata);
                strCashM = cashminusdata.getTotal();
            } else if (i == 2) {
                HomeCommonData bankplusdata = homedetails.getPayload().getBankplusdata();
                list.add(bankplusdata);

                strBankM = bankplusdata.getTotal();
            } else if (i == 3) {
                HomeCommonData bankminusdata = homedetails.getPayload().getBankminusdata();
                list.add(bankminusdata);
                strBankP = bankminusdata.getTotal();
            } else if (i == 4) {
                HomeCommonData otherdata = homedetails.getPayload().getOtherdata();
                list.add(otherdata);
                other = otherdata.getTotal();
            } else if (i == 5) {
                HomeCommonData gallerydata = homedetails.getPayload().getGallerydata();
                list.add(gallerydata);
            } else if (i == 6) {
                HomeCommonData documentdata = homedetails.getPayload().getDocumentdata();
                list.add(documentdata);
            } else if (i == 7) {
                reportdata = homedetails.getPayload().getReportdata();
            }
        }

        binding.rvTagDetailsHome.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        homeTagDetailAdapter = new HomeTagDetailAdapter(context, list, reportJson);
        binding.rvTagDetailsHome.setAdapter(homeTagDetailAdapter);
        binding.rvTagDetailsHome.setNestedScrollingEnabled(false);

        setUpPieChart();

    }

    private void setUpPieChart() {
        binding.chartHome.setUsePercentValues(true);
        binding.chartHome.setExtraOffsets(5, 10, 5, 5);
        binding.chartHome.setCenterText("Highlight Tags");

        ArrayList<Entry> yvalues = new ArrayList<>();
        yvalues.add(new Entry(strCashP, 0));
        yvalues.add(new Entry(strCashM, 1));
        yvalues.add(new Entry(strBankP, 2));
        yvalues.add(new Entry(strBankM, 3));
        yvalues.add(new Entry(other, 4));

        PieDataSet dataSet = new PieDataSet(yvalues, "Tags");
        dataSet.setColors(VORDIPLOM_COLORS);

        ArrayList<String> xVals = new ArrayList<>();

        xVals.add("C+");
        xVals.add("C-");
        xVals.add("B+");
        xVals.add("B-");
        xVals.add("O");

        PieData data = new PieData(xVals, dataSet);

        data.setValueFormatter(new PercentFormatter());

        binding.chartHome.setData(data);
        binding.chartHome.animateXY(1400, 1400);
        binding.chartHome.invalidate();

    }

    public static final int[] VORDIPLOM_COLORS = {
            Color.rgb(36, 212, 203),
            Color.rgb(189, 240, 238),
            Color.rgb(20, 146, 204),
            Color.rgb(203, 232, 245),
            Color.rgb(240, 244, 245)
    };

    //Bar Chart Implement The In Android
    private void serBarChart(List<PayloadItem> data) {
        Log.e("bar", data.toString());
        ArrayList<ArrayList<BarEntry>> tempList = new ArrayList<>();

        int[][] dataArray = new int[5][5];

        for (int i = 0; i < data.size(); i++) {
            List<CashItem> dataList = new ArrayList<>();
            dataList.addAll(data.get(i).getCash());
            for (int j = 0; j < dataList.size(); j++) {
                int val = 0;
                try {
                    val = Integer.parseInt(dataList.get(j).getTotalAmount());
                } catch (Exception e) {
                }
                dataArray[i][j] = val;
            }
        }

        ArrayList<BarEntry> dataListFinal;
        for (int i = 0; i < 5; i++) {
            dataListFinal = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                BarEntry entry = new BarEntry(dataArray[j][i], j);
                dataListFinal.add(entry);
            }
            tempList.add(dataListFinal);
        }

        BarDataSet barDataSet1 = new BarDataSet(tempList.get(0), "");
        //barDataSet1.setColors(VORDIPLOM_COLORS);
        barDataSet1.setColors(new int[]{R.color.secondary_text}, context);

        BarDataSet barDataSet2 = new BarDataSet(tempList.get(1), "");
        barDataSet2.setColors(new int[]{R.color.colorLightBlue}, context);

        BarDataSet barDataSet3 = new BarDataSet(tempList.get(2), "");
        barDataSet3.setColors(new int[]{R.color.secondary_text}, context);

        BarDataSet barDataSet4 = new BarDataSet(tempList.get(3), "");
        barDataSet4.setColors(new int[]{R.color.colorLightBlue}, context);

        BarDataSet barDataSet5 = new BarDataSet(tempList.get(4), "");
        barDataSet5.setColors(new int[]{R.color.secondary_text}, context);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);
        dataSets.add(barDataSet5);

        List<String> lavels = new ArrayList<>();
        lavels.add("Cash+");
        lavels.add("Cash-");
        lavels.add("Bank+");
        lavels.add("Bank-");
        lavels.add("Other");

        BarData barData = new BarData(lavels, dataSets);

       /* binding.chartHome.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chartHome.getLegend().setEnabled(false);
        binding.chartHome.setData(barData);
        binding.chartHome.setPinchZoom(true);
        binding.chartHome.invalidate();*/
    }

}
