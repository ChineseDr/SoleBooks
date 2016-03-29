package com.blanke.solebook.core.userhome;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.blanke.solebook.R;
import com.blanke.solebook.base.BaseSwipeBackActivity;
import com.blanke.solebook.bean.SoleUser;
import com.blanke.solebook.constants.Constants;
import com.blanke.solebook.utils.BitmapUtils;
import com.jaeger.library.StatusBarUtil;
import com.neu.refresh.NeuSwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.qiujuer.genius.blur.StackBlur;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;

@EActivity(R.layout.activity_user_home)
public class UserHomeActivity extends BaseSwipeBackActivity {

    private static String ARG_NAME_BEAN = "UserHomeActivity_bean";
    @ViewById(R.id.toolbar3)
    Toolbar toolbar;
    @ViewById(R.id.activity_details_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @ViewById(R.id.activity_details_appbar)
    AppBarLayout mAppBarLayout;
    @ViewById(R.id.activity_details_coordlayout)
    CoordinatorLayout mCoordinatorLayout;
    @ViewById(R.id.activity_userhome_icon)
    ImageView mIcon;
    @ViewById(R.id.activity_userhome_location)
    TextView mTextLocation;
    @ViewById(R.id.activity_userhome_head)
    LinearLayout mLayoutHead;
    @ViewById(R.id.activity_userhome_tablayout)
    TabLayout mTabLayout;
    @ViewById(R.id.activity_userhome_viewpager)
    ViewPager mViewPager;

    @StringRes(R.string.title_newly_like)
    String titleLike;
    @StringRes(R.string.title_newly_comment)
    String titleComment;

    private AVUser user;
    private double h;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void start(Activity activity, ImageView imageview, SoleUser user) {
        Intent intent2 = new Intent(activity, UserHomeActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_NAME_BEAN, user);
        intent2.putExtras(bundle);
        if (imageview != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    imageview, activity.getResources().getString(R.string.share_img3));
            activity.startActivity(intent2, options.toBundle());
        } else {
            activity.startActivity(intent2);
        }
    }

    @AfterViews
    void init() {
        StatusBarUtil.setTransparent(this);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable(ARG_NAME_BEAN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbarLayout.setTitle(SoleUser.getNickname(user));
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_expanded_text);//展开后的字体大小等
//        mTextLocation.setText("武汉");
        ImageLoader.getInstance().displayImage(SoleUser.getIconurl(user), mIcon, Constants.getImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                getMyColor(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                h = mCollapsingToolbarLayout.getMeasuredHeight()
                        - mCollapsingToolbarLayout.getMinimumHeight();
                h *= 0.9;
                float a = (float) ((h + verticalOffset) / h);
                mIcon.setAlpha(a);
            }
        });
        mTabLayout.addTab(mTabLayout.newTab().setText(titleLike));
        mTabLayout.addTab(mTabLayout.newTab().setText(titleComment));

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return UserNewlyFragment.newInstance(position,user.getObjectId());
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void changeColor(int color) {
//        mLayoutHead.setBackgroundColor(Color.BLUE);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getMyColor(Bitmap bitmap) {
//        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                Palette.Swatch swatch = palette.getVibrantSwatch();
//                if (swatch != null) {
//                    changeColor(swatch.getBodyTextColor());
//                }
//            }
//        });
        mCollapsingToolbarLayout.setBackground(new BitmapDrawable(getResources(),
                StackBlur.blurNativelyPixels(BitmapUtils.addBlackBitmap(bitmap), Constants.BLUE_VALUE, false)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}