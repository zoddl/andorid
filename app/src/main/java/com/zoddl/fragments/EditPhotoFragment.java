package com.zoddl.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zoddl.R;
import com.zoddl.activities.EditPhotoActivity;
import com.zoddl.adapters.adapter.EditAdapter;
import com.zoddl.model.EditType;
import com.zoddl.utils.StartSnapHelper;
import com.zoddl.utils.ImageUtils;
import com.hoanganhtuan95ptit.brightness.BrightnessFragment;
import com.hoanganhtuan95ptit.brightness.OnBrightnessListener;
import com.hoanganhtuan95ptit.contrast.ContrastFragment;
import com.hoanganhtuan95ptit.contrast.OnContrastListener;
import com.hoanganhtuan95ptit.crop.CropFragment;
import com.hoanganhtuan95ptit.crop.OnCropListener;
import com.hoanganhtuan95ptit.fillter.FilterFragment;
import com.hoanganhtuan95ptit.fillter.OnFilterListener;
import com.hoanganhtuan95ptit.rotate.OnRotateListener;
import com.hoanganhtuan95ptit.rotate.RotateFragment;
import com.hoanganhtuan95ptit.saturation.OnSaturationListener;
import com.hoanganhtuan95ptit.saturation.SaturationFragment;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Hoang Anh Tuan on 11/23/2017.
 */

public class EditPhotoFragment extends Fragment implements
        EditAdapter.OnItemEditPhotoClickedListener,
        OnCropListener,
        OnFilterListener,
        OnRotateListener,
        OnSaturationListener,
        OnBrightnessListener,
        OnContrastListener {

    private static final String INPUT_URL = "inputUrl";

    @BindView(R.id.ivPhotoView)
    ImageView ivPhotoView;
    @BindView(R.id.ivCheck)
    ImageView ivCheck;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;
    @BindView(R.id.ivLoadingEdit)
    AVLoadingIndicatorView ivLoading;
    @BindView(R.id.listEdit)
    RecyclerView listEdit;
    Unbinder unbinder;

    public static EditPhotoFragment create(String inputUrl) {
        EditPhotoFragment fragment = new EditPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INPUT_URL, inputUrl);
        fragment.setArguments(bundle);
        return fragment;
    }


    private String outputUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)ivPhotoView.getDrawable()).getBitmap();

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                String filePath = ImageUtils.createImageFileForBitmap(getContext(),bytes);
                /*File direct = new File(Environment.getExternalStorageDirectory() + LOCAL_FOLDER_LOCATION_DATA);
                if(!direct.exists()) {
                    direct.mkdir();
                }

   File f = new File(Environment.getExternalStorageDirectory() + LOCAL_FOLDER_LOCATION_DATA+"temp.jpg");
                try {

                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                ((EditPhotoActivity)getActivity()).setResultData(filePath);
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditPhotoActivity)getActivity()).finish();
            }
        });
    }

    private void initView() {

        EditAdapter editAdapter = new EditAdapter(getActivity());
        editAdapter.setOnItemEditPhotoClickedListener(this);
        listEdit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listEdit.setAdapter(editAdapter);
        new StartSnapHelper().attachToRecyclerView(listEdit);

        editAdapter.add(EditType.Crop);
        editAdapter.add(EditType.Filter);
        editAdapter.add(EditType.Rotate);
        editAdapter.add(EditType.Saturation);
        editAdapter.add(EditType.Brightness);
        editAdapter.add(EditType.Contrast);

        if (getArguments() != null) {

            outputUrl = getArguments().getString(INPUT_URL);
            showPhoto(outputUrl);
        }
    }


    private void showPhoto(String outputUrl) {
        this.outputUrl = outputUrl;
        Observable
                .just(outputUrl)
                .map(new Function<String, Bitmap>() {
                    public Bitmap apply(String url) throws Exception {
                        return getBitmap(url);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    public void onSubscribe(Disposable d) {
                        showLoading();
                    }

                    public void onNext(Bitmap bitmap) {
                        ivPhotoView.setImageBitmap(bitmap);
                    }

                    public void onError(Throwable e) {
                    }

                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    private Bitmap getBitmap(String inputUrl) {
        Bitmap bitmap = ImageUtils.getBitmapSdcard(inputUrl);
        bitmap = ImageUtils.scaleDown(bitmap);
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemEditPhotoClicked(EditType type) {
        switch (type) {
            case Crop:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(CropFragment.create(outputUrl, this));
                break;
            case Filter:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(FilterFragment.create(outputUrl, this));
                break;
            case Rotate:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(RotateFragment.create(outputUrl, this));
                break;
            case Saturation:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(SaturationFragment.create(outputUrl, this));
                break;
            case Brightness:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(BrightnessFragment.create(outputUrl, this));
                break;
            case Contrast:
                ((EditPhotoActivity) getActivity()).addFragmentToStack(ContrastFragment.create(outputUrl, this));
                break;
        }
    }

    private void hideLoading() {
        if (ivLoading != null)
            ivLoading.smoothToHide();
    }

    private void showLoading() {
        if (ivLoading != null)
            ivLoading.smoothToShow();
    }

    @Override
    public void onBrightnessPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onContrastPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onCropPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onFilterPhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onRotatePhotoCompleted(String s) {
        showPhoto(s);
    }

    @Override
    public void onSaturationPhotoCompleted(String s) {
        showPhoto(s);
    }
}
