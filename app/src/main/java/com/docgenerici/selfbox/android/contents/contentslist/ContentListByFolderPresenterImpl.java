package com.docgenerici.selfbox.android.contents.contentslist;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.contents.ContentBox;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.contents.TypeContent;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class ContentListByFolderPresenterImpl implements ContentListByFolderPresenter {
    private ContentView view;
    private final int FILTER_BY_DATE = 1;
    private final int FILTER_BY_AZ = 2;
    private int FILTER_NOW = FILTER_BY_AZ;
    private ArrayList<ContentDoc> contents = new ArrayList<>();
    private int sample1;
    private int sample2;
    private int sample3;
    private int folderSample;
    private ArrayList<ContentDoc> folderContentList;


    @Override
    public void setView(BaseView view) {
        if (!(view instanceof ContentView)) {
            throw new IllegalArgumentException("View must extend ContentListPresenter.View");
        }
        this.view = (ContentView) view;
    }

    @Override
    public void selectAZ() {
        FILTER_NOW = FILTER_BY_AZ;
        view.showSelectAz();
    }

    @Override
    public void selectDate() {
        FILTER_NOW = FILTER_BY_DATE;
        view.showSelectDate();
    }

    @Override
    public void onSelecteFilter() {
        view.openFilterDialog();
    }

    @Override
    public void setup(int folder, int sample1, int sample2, int sample3) {

        this.sample1 = sample1;
        this.sample2 = sample1;
        this.sample3 = sample1;
        this.folderSample = folder;
    }

    @Override
    public List<ContentDoc> getContents(String categoryContent) {
        contents.clear();
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Folder> folder = realm.where(Folder.class).findAll();
        for (int i = 0; i < folder.size(); i++) {
            ContentDoc contentDoc = new ContentDoc();
            contentDoc.id = folder.get(i).id;
            contentDoc.type = SelfBoxConstants.TypeContent.FOLDER;
            if (folder.get(i).getCoverUri() == null || folder.get(i).getCoverUri().length() < 2 || folder.get(i).getCoverUri().contains("error_")) {
                contentDoc.image = folderSample;
            } else {
                String uri = folder.get(i).getCoverUri();

                if (uri.contains("file://")) {
                    uri = uri.replace("file://", "");
                }
                contentDoc.cover = uri;
            }
            contentDoc.isFolder = true;
            contentDoc.name = folder.get(i).name;
            contents.add(contentDoc);
        }
        Folder folderRoot = realm.where(Folder.class).equalTo("name", "Root").findFirst();
        if (folderRoot != null) {
            Dbg.p("folderRoot: " + folderRoot.name);
            Dbg.p("folderRoot: " + folderRoot.contents.size());
            for (int j = 0; j < folderRoot.contents.size(); j++) {
                ContentDoc contentDoc = new ContentDoc();
                contentDoc.id = folderRoot.contents.get(j).id;
                TypeContent typeContent = folderRoot.contents.get(j).type;
                if (typeContent.descr.equalsIgnoreCase("pdf")) {
                    contentDoc.type = SelfBoxConstants.TypeContent.PDF;
                } else if (typeContent.descr.equalsIgnoreCase("video")) {
                    contentDoc.type = SelfBoxConstants.TypeContent.VISUAL;
                }
                contentDoc.content= folderRoot.contents.get(j).getLocalfilePath();
                if (folderRoot.contents.get(j).getLocalthumbnailPath() == null || folderRoot.contents.get(j).getLocalthumbnailPath().length() < 2 || folderRoot.contents.get(j).getLocalthumbnailPath().contains("error_")) {
                    contentDoc.image = folderSample;
                } else {
                    String uriCover = folderRoot.contents.get(j).getLocalthumbnailPath();
                    if (uriCover.contains("file://")) {
                        uriCover = uriCover.replace("file://", "");
                    }
                    contentDoc.cover = uriCover;
                }

                contents.add(contentDoc);

            }

        }


        return contents;
    }

    @Override
    public void setShare(int position) {
        contents.get(position).shared = !contents.get(position).shared;
        view.refreshContents();
    }

    @Override
    public ArrayList<ContentDoc> getContentsShared() {
        ArrayList<ContentDoc> contentsShared = new ArrayList<>();
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).shared) {
                contentsShared.add(contents.get(i));
            }
        }
        return contentsShared;
    }

    @Override
    public ArrayList<ContentDoc> getContentFolder(ContentDoc contentSelect) {

        folderContentList = new ArrayList<ContentDoc>();
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Folder> folders = realm.where(Folder.class).findAll();
        Folder folder = null;
        for (int i = 0; i < folders.size(); i++) {
            if (folders.get(i).id == contentSelect.id) {
                folder = folders.get(i);
            }
        }

        if (folder != null) {
            RealmList<ContentBox> folderDocs = folder.contents;
            for (int i = 0; i < folderDocs.size(); i++) {

                ContentDoc contentDoc = new ContentDoc();
                contentDoc.id = folderDocs.get(i).id;
                contentDoc.type = getType(folderDocs.get(i).type.name);
                contentDoc.name = folderDocs.get(i).name;
//               if(folderDocs.get(i).cover ==null || folder.get(i).cover.length() < 2){
//                   contentDoc.image= folderSample;
//               }else{
//                   contentDoc.cover= folder.get(i).path+folder.get(i).cover;
//               }
//               contentDoc.name =folder.get(i).name;
                contentDoc.image = folderSample;
                folderContentList.add(contentDoc);
            }

        }


        return folderContentList;
    }

    private int getType(String name) {

        if (name.equalsIgnoreCase("pdf")) {
            return SelfBoxConstants.TypeContent.PDF;
        } else if (name.equalsIgnoreCase("video")) {
            return SelfBoxConstants.TypeContent.VIDEO;
        }
        return SelfBoxConstants.TypeContent.PDF;

    }


}
