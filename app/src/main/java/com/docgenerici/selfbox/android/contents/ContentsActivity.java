package com.docgenerici.selfbox.android.contents;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.contents.contentslist.ContentsListFragment;
import com.docgenerici.selfbox.android.contents.productlist.ProductListFragment;
import com.docgenerici.selfbox.android.contents.share.ShareContentsDialogFragment;
import com.docgenerici.selfbox.android.contents.share.ShareInterface;
import com.docgenerici.selfbox.android.home.help.HelpDialogFragment;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.shares.ShareData;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentsActivity extends AppCompatActivity implements MainContentPresenter.MainContentView, ViewPager.OnPageChangeListener, ContentActivityInterface, ShareInterface{

    @BindColor(R.color.orange)
    int orange;
    @BindColor(R.color.orange_dark)
    int orange_dark;
    @BindColor(R.color.green)
    int green;
    @BindColor(R.color.green_dark)
    int green_dark;
    @BindColor(R.color.blu)
    int blu;
    @BindColor(R.color.blu)
    int blu_dark;
    @BindView(R.id.btContent)
    Button btContent;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.btProducts)
    Button btProducts;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.vPager)
    ViewPager vPager;
    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.btHelp)
    Button btHelp;

    private MainContentPresenter presenter;
    private final int NAVIGATION_CONTENT = 0;
    private final int NAVIGATION_PRODUCTS = 1;
    @BindColor(R.color.orange)
    int orangeColor;
    @BindColor(R.color.color_text_tab_button)
    int colorButtonSelect;
    private ShareContentsDialogFragment shareDialog;
    private String category;
    private ContentsListFragment contentFrag;
    private MedicoDto medicoSelected;
    private FarmaciaDto lastPharmaUser;
    private boolean training;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        ButterKnife.bind(this);
        presenter = SelfBoxApplicationImpl.appComponent.mainContentPresenter();
        presenter.setView(this);
        vPager.setAdapter(new ContentsAdapter(getFragmentManager()));
        vPager.addOnPageChangeListener(this);
        if (getIntent() != null) {
            category = getIntent().getStringExtra("category");
            training = getIntent().getBooleanExtra("training", false);
            medicoSelected = getIntent().getParcelableExtra("medico");

            if (medicoSelected != null) {
            }
            lastPharmaUser = getIntent().getParcelableExtra("lastPharmaUser");
            presenter.setCategories(category, medicoSelected, lastPharmaUser);
        }
        presenter.setup(category);
    }

    @OnClick(R.id.ivLogo)
    void onTapLogo() {
        finish();
    }

    @OnClick(R.id.btContent)
    void onSelectContents() {
        setNavigation(NAVIGATION_CONTENT);
        presenter.onSelectContents();
    }

    @OnClick(R.id.btProducts)
    void onSelectProoducts() {
        setNavigation(NAVIGATION_PRODUCTS);
        presenter.onSelectProducts();
    }

    @OnClick(R.id.btHelp)
    public void showHelp() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        HelpDialogFragment helpDialog = HelpDialogFragment.createInstance();
        helpDialog.show(ft, "helpDialog");
    }

    @OnClick(R.id.tvShare)
    void onTapShareButton() {
        presenter.onSelectShare();
    }

    @Override
    public void setupView() {
        changeStatusBar(presenter.getContentDarkColor());
        rlToolbar.setBackgroundColor(presenter.getContentColor());
        btHelp.setBackground(presenter.getBackGroundhelp());
        btContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                int w = btContent.getWidth();
                int h = btContent.getHeight();
                if (w > 0 && h > 0) {
                    setNavigation(NAVIGATION_CONTENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        btContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
        vPager.setCurrentItem(0);
    }

    @Override
    public void showContents() {
        vPager.setCurrentItem(NAVIGATION_CONTENT);
    }

    @Override
    public void showProducts() {
        vPager.setCurrentItem(NAVIGATION_PRODUCTS);
    }

    @Override
    public void showShareContents(ArrayList<ContentDoc> contentsShared) {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        shareDialog = ShareContentsDialogFragment.createInstance(contentsShared, category, medicoSelected, lastPharmaUser, training);
        shareDialog.setShare(this);
        shareDialog.show(ft, "shareDialog");
    }

    @Override
    public void refreshContentShare(int size) {
        tvShare.setVisibility(size > 0 ? View.VISIBLE : View.GONE);
        tvShare.setText("CONDIVIDI " +size + " CONTENUTI");

    }

    private void setNavigation(int nav) {

        switch (nav) {
            case NAVIGATION_CONTENT:
                //  ViewCompat.animate(vLine).scaleX(presenter.getScale(vLine.getWidth(), btContent.getWidth())).x(btContent.getX()- btContent.getLeft());
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btContent.getLayoutParams();
                RelativeLayout.LayoutParams paramsView = (RelativeLayout.LayoutParams) vLine.getLayoutParams();
                paramsView.leftMargin = params.leftMargin;
                paramsView.width = params.width;
                vLine.setLayoutParams(paramsView);
                btContent.setTextColor(colorButtonSelect);
                btProducts.setTextColor(presenter.getContentColor());
                break;
            case NAVIGATION_PRODUCTS:
                RelativeLayout.LayoutParams paramsP = (RelativeLayout.LayoutParams) btProducts.getLayoutParams();
                //  ViewCompat.animate(vLine).scaleX(presenter.getScale(vLine.getWidth(), btProducts.getWidth())).x(btProducts.getLeft()+paramsP.leftMargin);
                RelativeLayout.LayoutParams paramsViewL = (RelativeLayout.LayoutParams) vLine.getLayoutParams();
                paramsViewL.leftMargin = btProducts.getLeft();
                paramsViewL.width = paramsP.width;
                vLine.setLayoutParams(paramsViewL);
                btContent.setTextColor(presenter.getContentColor());
                btProducts.setTextColor(colorButtonSelect);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setNavigation(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void setShared(ArrayList<ContentDoc> contentsShared) {
         presenter.refreshContents();
    }

    @Override
    public void onShareData(ShareData shareData) {
        presenter.shareData(shareData);
    }

    @Override
    public void onChangeShareData() {
        presenter.refreshContents();
        if(contentFrag !=null){
            contentFrag.refreshContents();
        }
    }


    private class ContentsAdapter extends FragmentStatePagerAdapter {


        public ContentsAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    contentFrag = ContentsListFragment.createInstance(category, training);
                    return contentFrag;
                case 1:
                    return ProductListFragment.createInstance(training);

                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        if (vPager.getCurrentItem() == 1) {
            vPager.setCurrentItem(0);
        } else {

            if (contentFrag != null && contentFrag.canBack()) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        if(contentFrag !=null){
            contentFrag.refreshContents();
        }
        super.onResume();
    }

    private void changeStatusBar(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
