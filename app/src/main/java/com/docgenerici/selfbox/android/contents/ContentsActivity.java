package com.docgenerici.selfbox.android.contents;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v13.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.contents.contentslist.ContentsListFragment;
import com.docgenerici.selfbox.android.contents.productlist.ProductListFragment;
import com.docgenerici.selfbox.debug.Dbg;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContentsActivity extends AppCompatActivity implements MainContentPresenter.MainContentView {

    @BindView(R.id.btContent)
    Button btContent;
    @BindView(R.id.btProducts)
    Button btProducts;
    @BindView(R.id.vLine)
    View vLine;
    private MainContentPresenter presenter;
    private final int NAVIGATION_CONTENT = 1;
    private final int NAVIGATION_PRODUCTS = 2;
    @BindColor(R.color.green)
    int greenColor;
    @BindColor(R.color.color_text_tab_button)
    int colorButtonSelect;
    private ContentsListFragment contentsList;
    private ProductListFragment productsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        ButterKnife.bind(this);
        presenter = SelfBoxApplicationImpl.appComponent.mainContentPresenter();
        presenter.setView(this);
        presenter.setup();

    }

    @OnClick(R.id.btContent)
    void onSelectContents() {
        setNavigation(NAVIGATION_CONTENT);
        presenter.onSelectContents();
    }

    @OnClick(R.id.btProducts)
    void onSelectCProoducts() {
        setNavigation(NAVIGATION_PRODUCTS);
        presenter.onSelectProducts();
    }

    @Override
    public void setupView() {

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
        onSelectContents();
    }

    @Override
    public void showContents() {
        if(contentsList ==null) {
            contentsList = new ContentsListFragment();
        }
        showFragment(contentsList, "contentList", R.id.rmContainer);
    }

    @Override
    public void showProducts() {
        if(productsListFragment ==null) {
            productsListFragment = new ProductListFragment();
        }
        showFragment(productsListFragment, "productsListFragment", R.id.rmContainer);
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
                btProducts.setTextColor(greenColor);
                break;
            case NAVIGATION_PRODUCTS:
                RelativeLayout.LayoutParams paramsP = (RelativeLayout.LayoutParams) btProducts.getLayoutParams();
                //  ViewCompat.animate(vLine).scaleX(presenter.getScale(vLine.getWidth(), btProducts.getWidth())).x(btProducts.getLeft()+paramsP.leftMargin);
                RelativeLayout.LayoutParams paramsViewL = (RelativeLayout.LayoutParams) vLine.getLayoutParams();
                paramsViewL.leftMargin = btProducts.getLeft();
                paramsViewL.width = paramsP.width;
                vLine.setLayoutParams(paramsViewL);
                btContent.setTextColor(greenColor);
                btProducts.setTextColor(colorButtonSelect);
                break;
        }
    }

    private void showFragment(Fragment frag, String tag, int container) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(container, frag, tag);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }
}
