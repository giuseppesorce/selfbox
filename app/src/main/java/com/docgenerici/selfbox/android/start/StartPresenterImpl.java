package com.docgenerici.selfbox.android.start;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.comm.LoginInteractor;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.LoginResponse;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class StartPresenterImpl implements StartPresenter {
    private final LoginInteractor loginInteractor;
    private StartView view;

    public StartPresenterImpl(LoginInteractor loginInteractor) {
        this.loginInteractor = loginInteractor;

    }

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof StartView)) {
            throw new IllegalArgumentException("View must extend StartPresenter.View");
        }
        this.view = (StartView) view;
    }

    @Override
    public void chekActivation() {
        //TODO this method checks if there is a previous activation
        if(hereActivation()){



        }else{
            String appLicence="77750";
            String isfCode="77750";
            String appVer="1.0.0-beta";
            String devId="gfgfgfg";
            loginInteractor.login(appVer, devId, isfCode,  appLicence)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<LoginResponse>() {
                        @Override
                        public void call(LoginResponse loginResponse) {
                            Dbg.p("CALL OK");

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE");

                        }
                    });

        }
    }

    private boolean hereActivation() {

        return false;
    }
}
