package com.docgenerici.selfbox.android.contents.contentslist;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.contents.Target;
import com.docgenerici.selfbox.models.contents.TypeContent;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class ContentListPresenterImpl implements ContentListPresenter {
    private ContentView view;
    private final int FILTER_BY_DATE = 1;
    private final int FILTER_BY_AZ = 2;
    private int FILTER_NOW = FILTER_BY_AZ;
    private ArrayList<ContentDoc> contents = new ArrayList<>();
    private int folderSample;
    private ArrayList<ContentDoc> folderContentList;
    private int levelView;
    private ArrayList<ContentDoc> folderContentFolderList = new ArrayList<>();
    private String categoryContent;


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
    public void setup(int folder) {
        this.folderSample = folder;
        view.setup();
    }

    @Override
    public List<ContentDoc> getContents(String categoryContent) {
        contents.clear();
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Folder> folder = realm.where(Folder.class).findAll();
        for (int i = 0; i < folder.size(); i++) {
            if (folder.get(i).name != null && !folder.get(i).name.equalsIgnoreCase("root")) {
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
        }
        Folder folderRoot = realm.where(Folder.class).equalTo("name", "Root").findFirst();
        if (folderRoot != null) {

            for (int j = 0; j < folderRoot.contents.size(); j++) {
              ArrayList<Target> allTargets= new ArrayList<>(folderRoot.contents.get(j).targets);
               if(checkCategory(allTargets)) {
                   ContentDoc contentDoc = new ContentDoc();
                   contentDoc.id = folderRoot.contents.get(j).id;
                   TypeContent typeContent = folderRoot.contents.get(j).type;
                   if (typeContent.descr.equalsIgnoreCase("pdf")) {
                       contentDoc.type = SelfBoxConstants.TypeContent.PDF;
                   } else if (typeContent.descr.equalsIgnoreCase("evisual")) {
                       contentDoc.type = SelfBoxConstants.TypeContent.VISUAL;
                   } else if (typeContent.descr.equalsIgnoreCase("video")) {
                       contentDoc.type = SelfBoxConstants.TypeContent.VIDEO;
                   }
                   contentDoc.content = folderRoot.contents.get(j).getLocalfilePath();
                   if (folderRoot.contents.get(j).getLocalthumbnailPath() == null || folderRoot.contents.get(j).getLocalthumbnailPath().length() < 2 || folderRoot.contents.get(j).getLocalthumbnailPath().contains("error_")) {
                       contentDoc.image = folderSample;
                   } else {
                       String uriCover = folderRoot.contents.get(j).getLocalthumbnailPath();
                       if (uriCover.contains("file://")) {
                           uriCover = uriCover.replace("file://", "");
                       }
                       contentDoc.cover = uriCover;
                   }
                   contentDoc.name = folderRoot.contents.get(j).name;

                   contents.add(contentDoc);
               }
            }

        }


        return contents;
    }

    private boolean checkCategory(ArrayList<Target> allTargets) {
        ArrayList<Integer> tagetId= new ArrayList<>();
        for (int i = 0; i < allTargets.size(); i++) {
            tagetId.add(allTargets.get(i).id);
        }
        if(categoryContent.equalsIgnoreCase("isf") || tagetId.size()==0){
            return true;
        }else if(categoryContent.equalsIgnoreCase("pharma")){
            if(tagetId.contains(4)){
                return true;
            }
        }else if(categoryContent.equalsIgnoreCase("medico")){
            if(tagetId.contains(3)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setShare(ContentDoc contentDoc) {
        contentDoc.shared = !contentDoc.shared;
        view.refreshContents();
    }

    @Override
    public ArrayList<ContentDoc> getContentsShared() {
        ArrayList<ContentDoc> contentsShared = new ArrayList<>();
        if (levelView == 0) {
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).shared) {
                    contentsShared.add(contents.get(i));
                }
            }
        } else if (levelView == 1) {
            for (int i = 0; i < folderContentFolderList.size(); i++) {
                if (folderContentFolderList.get(i).shared) {
                    contentsShared.add(folderContentFolderList.get(i));
                }
            }
        }
        return contentsShared;
    }

    @Override
    public ArrayList<ContentDoc> getContentsByFolder(int id) {
        folderContentFolderList.clear();
        folderContentFolderList = new ArrayList<ContentDoc>();
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        Folder folder = realm.where(Folder.class).equalTo("id", id).findFirst();
        if (folder != null) {

            for (int j = 0; j < folder.contents.size(); j++) {
                ArrayList<Target> allTargets= new ArrayList<>(folder.contents.get(j).targets);
                if(checkCategory(allTargets)) {
                    ContentDoc contentDoc = new ContentDoc();
                    contentDoc.id = folder.contents.get(j).id;

                    contentDoc.name = folder.contents.get(j).name;
                    TypeContent typeContent = folder.contents.get(j).type;
                    if (typeContent.descr.equalsIgnoreCase("pdf")) {
                        contentDoc.type = SelfBoxConstants.TypeContent.PDF;
                    } else if (typeContent.descr.equalsIgnoreCase("evisual")) {
                        contentDoc.type = SelfBoxConstants.TypeContent.VISUAL;
                    } else if (typeContent.descr.equalsIgnoreCase("video")) {
                        contentDoc.type = SelfBoxConstants.TypeContent.VIDEO;
                    }
                    contentDoc.content = folder.contents.get(j).getLocalfilePath();
                    Dbg.p("getContentsByFolder contentDoc.content: " + contentDoc.content);
                    if (folder.contents.get(j).getLocalthumbnailPath() == null || folder.contents.get(j).getLocalthumbnailPath().length() < 2 || folder.contents.get(j).getLocalthumbnailPath().contains("error_")) {
                        contentDoc.image = folderSample;
                    } else {
                        String uriCover = folder.contents.get(j).getLocalthumbnailPath();
                        if (uriCover.contains("file://")) {
                            uriCover = uriCover.replace("file://", "");
                        }
                        contentDoc.cover = uriCover;
                    }
                    folderContentFolderList.add(contentDoc);
                }
            }
        }
        return folderContentFolderList;
    }

    @Override
    public void setLevelView(int level) {
        levelView = level;
    }

    @Override
    public int getLevelView() {
        return levelView;
    }

    @Override
    public void selectFilter(String filterText) {
        ArrayList<ContentDoc> filtered = new ArrayList<>();
        if (levelView == 0) {
            if (contents != null) {
                for (int i = 0; i < contents.size(); i++) {
                    if (filterText != null) {

                        if (contents.get(i).name.toLowerCase().contains(filterText.toLowerCase())) {
                            filtered.add(contents.get(i));
                        }
                    }
                }
            }
        } else if (levelView == 1) {
            if (folderContentFolderList != null) {
                for (int i = 0; i < folderContentFolderList.size(); i++) {
                    if (folderContentFolderList.get(i).name != null) {
                        if (folderContentFolderList.get(i).name.toLowerCase().contains(filterText.toLowerCase())) {
                            filtered.add(folderContentFolderList.get(i));
                        }
                    }
                }
            }
        }
        view.applyFilter(filtered);
    }

    @Override
    public void selectLastFilter() {
        if (levelView == 0) {
            view.applyFilter(contents);
        } else if (levelView == 1) {
            view.applyFilter(folderContentFolderList);
        }

    }

    @Override
    public void setCategory(String categoryContent) {
        this.categoryContent= categoryContent;
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
