package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.Product;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class ProductFragment extends BaseFragment {

    Bundle bundle;


    Product product;
    public static final String companyId = "companyId";
    public static final String productId = "productId";

    String paramCompanyId = "";
    String paramProductId = "";


    //  int ImageObjectSize;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;


    @BindView(R.id.mainlayout)
    ConstraintLayout mainlayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @BindView(R.id.indicator)
    CircleIndicator indicator;


    List<String> ImageList = new ArrayList<>();


    @BindView(R.id.pro_name)
    EditText proName;
    @BindView(R.id.pro_desc)
    EditText proDesc;
    @BindView(R.id.pro_price)
    EditText proPrice;
    @BindView(R.id.attribute_layout)
    LinearLayout attributeLayout;
    @BindView(R.id.add_att)
    ImageButton addAtt;

    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.product_desc_view)
    ScrollView productDescView;



    @Override
    public void onCustomBackPressed() {


        activityReference.onPageBack();

    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_product;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        getBaseActivity().toolbar.setTitle("Product");
        setArguments();
        initializeViews();


    }

    private void setArguments() {
        bundle = getArguments();
        if (bundle != null) {
            paramCompanyId = bundle.getString(companyId);
            paramProductId = bundle.getString(productId);
        }
    }

    private void initializeViews() {
        mShimmerViewContainer.startShimmerAnimation();

        mainlayout.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);


        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", paramCompanyId);
        params.put("productId", paramProductId);

/*

        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));
        params.put("productCategoryId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));

*/

        WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_DETAIL, false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                mShimmerViewContainer.stopShimmerAnimation();
                mainlayout.setVisibility(View.VISIBLE);
                mShimmerViewContainer.setVisibility(View.GONE);

                product = GsonHelper.GsonToProduct(activityReference, response);

                // ImageList = product.getImages();

                //  List<String> ImageList;
                ImageList.add("http://api.bizlinked.lumaque.pk/rest/Product/Image?companyId=1&imageId=1");
                ImageList.add("http://api.bizlinked.lumaque.pk/rest/Product/Image?companyId=1&imageId=2");
                ImageList.add("http://api.bizlinked.lumaque.pk/rest/Product/Image?companyId=1&imageId=3");


                viewpager.setAdapter(new CustomPagerAdapter(activityReference));

                indicator.setViewPager(viewpager);


                proName.setText(product.getProductName());
                proDesc.setText(product.getProductDescription());
                proPrice.setText(String.valueOf(product.getPrice()));

                
            }

            @Override
            public void onError(String response) {

                mShimmerViewContainer.stopShimmerAnimation();
onCustomBackPressed();

            }

            @Override
            public void onNoNetwork() {

            }
        });


    }





    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            // ModelObject modelObject = ModelObject.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_product_header, collection, false);
            ImageView headerView = layout.findViewById(R.id.imageView);

            Glide.with(activityReference).load(ImageList.get(position))
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())).centerCrop())
                    .into(headerView);


            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {

            return ImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // ModelObject customPagerEnum = ModelObject.values()[position];
            return ImageList.get(position);
        }

    }
}
