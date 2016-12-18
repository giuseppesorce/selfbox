package com.docgenerici.selfbox.android.sync;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.async.XmlTaskParse;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.debug.Dbg;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class SyncPresenterImpl implements SyncPresenter {
    private ApiInteractor apiInteractor;
    private SyncView view;



    public SyncPresenterImpl(ApiInteractor loginInteractor) {
        this.apiInteractor = loginInteractor;
    }

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof SyncView)) {
            throw new IllegalArgumentException("View must extend SyncPresenter.View");
        }
        this.view = (SyncView) view;
    }

    @Override
    public void setup() {
        view.setup();
    }

    @Override
    public void startSync() {
        view.onStartSync();
    }

    @Override
    public void stopSync() {
        view.onStopSync();
    }


    private void getProduct() {


        Environment environment = SelfBoxApplicationImpl.appComponent.environment();
        environment.setBaseUrl("http://www.docgenerici.it/app/app.php");
        apiInteractor.getProduct("20161010")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            parseXML(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE getProduct: " + throwable.getLocalizedMessage());

                    }
                });


    }

    private void parseXML(String xmlString) {

        XmlTaskParse parseTask = new XmlTaskParse();
        parseTask.execute(xmlString);


    }
}
