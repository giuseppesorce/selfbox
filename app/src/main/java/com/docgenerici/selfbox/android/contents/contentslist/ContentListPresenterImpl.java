package com.docgenerici.selfbox.android.contents.contentslist;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.models.contents.ContentBox;
import com.docgenerici.selfbox.models.contents.Filters;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.contents.Target;
import com.docgenerici.selfbox.models.contents.TypeContent;
import com.docgenerici.selfbox.models.persistence.InfoApp;
import com.docgenerici.selfbox.models.persistence.ItemShared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
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
    private String userTarget;
    private int lastIdFolder;
    private Filters lastFiltersType;
    private long lastUpdate = 0;

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
        lastFiltersType = null;
        view.showSelectAz();
        view.updateContents();
    }

    @Override
    public void selectDate() {
        FILTER_NOW = FILTER_BY_DATE;
        lastFiltersType = null;
        view.showSelectDate();
        view.updateContents();
    }

    @Override
    public void onSelecteFilter() {
        view.openFilterDialog();
    }

    @Override
    public void setup(int folder) {

        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp infoApp = realm.where(InfoApp.class).findFirst();
        if (infoApp != null) {
            lastUpdate = infoApp.lastUpdate;
        }
        this.folderSample = folder;
        userTarget = infoApp.lineShortCode;
        view.setup();
        view.showSelectAz();
    }

    @Override
    public List<ContentDoc> getContents(String categoryContent) {
        contents.clear();
        ArrayList<Folder> folder = getRealmFolder();
        for (int i = 0; i < folder.size(); i++) {
            if (folder.get(i).name != null && !folder.get(i).name.equalsIgnoreCase("root")) {
                ContentDoc contentDoc = new ContentDoc();
                contentDoc.id = folder.get(i).id;
                contentDoc.lastUpdate = folder.get(i).creationDate;
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
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        Folder folderRoot = realm.where(Folder.class).equalTo("name", "Root").findFirst();
        if (folderRoot != null) {

            for (int j = 0; j < folderRoot.contents.size(); j++) {
                ArrayList<Target> allTargets = new ArrayList<>(folderRoot.contents.get(j).targets);

                if (checkCategory(allTargets) && contentToView(folderRoot.contents.get(j))) {
                    ContentDoc contentDoc = new ContentDoc();
                    contentDoc.id = folderRoot.contents.get(j).id;
                    contentDoc.lastUpdate = folderRoot.contents.get(j).lastUpdate;
                    contentDoc.keywords = folderRoot.contents.get(j).keywords;
                    contentDoc.viewed = folderRoot.contents.get(j).isViewed();
                    if (folderRoot.contents.get(j).isNewcontent() && !contentDoc.viewed) {
                        if (folderRoot.contents.get(j).alertHighlight) {
                            contentDoc.typeview = SelfBoxConstants.TypeViewContent.IMPORTANT_NEW;
                        } else {
                            contentDoc.typeview = SelfBoxConstants.TypeViewContent.NEW;
                        }
                    } else {

                        if (folderRoot.contents.get(j).alertHighlight) {
                            contentDoc.typeview = SelfBoxConstants.TypeViewContent.IMPORTANT;
                        }
                    }

                    contentDoc.alertHighlight = folderRoot.contents.get(j).alertHighlight;
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
                        Dbg.p("uriCover id["+contentDoc.id+"]="+uriCover);
                        contentDoc.cover = uriCover;
                    }
                    contentDoc.name = folderRoot.contents.get(j).name;
                    contents.add(contentDoc);
                }
            }
        }
        if (FILTER_NOW == FILTER_BY_AZ) {
            Collections.sort(contents, new Comparator<ContentDoc>() {
                @Override
                public int compare(ContentDoc o1, ContentDoc o2) {
                    return o1.name.compareTo(o2.name);
                }
            });
        } else if (FILTER_NOW == FILTER_BY_DATE) {
            Collections.sort(contents, new Comparator<ContentDoc>() {
                @Override
                public int compare(ContentDoc o1, ContentDoc o2) {
                    return Long.compare(o1.lastUpdate, o2.lastUpdate);
                }
            });
        }


        return contents;
    }

    private boolean contentToView(ContentBox contentBox) {
        boolean visible = true;
        if (!contentBox.visible || !contentBox.active && !contentBox.approved) {
            visible = false;
        }
        return visible;
    }


    private ArrayList<Folder> getRealmFolder() {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Folder> folder = realm.where(Folder.class).findAll();
        return new ArrayList<Folder>(folder);
    }

    private boolean checkCategory(ArrayList<Target> allTargets) {
        boolean inTarget = false;
        ArrayList<String> tagetId = new ArrayList<>();
        for (int i = 0; i < allTargets.size(); i++) {
            tagetId.add(allTargets.get(i).code.toLowerCase());
        }
        switch (categoryContent.toLowerCase()) {

            case "isf":
                if (userTarget.equalsIgnoreCase("s")) {
                    if (tagetId.contains("isf") || tagetId.contains("isf_specia")) {
                        inTarget = true;
                    }
                } else {
                    if (tagetId.contains("isf")) {
                        inTarget = true;
                    }
                }
                break;
            case "pharma":
                if (tagetId.contains("farmacia")) {
                    inTarget = true;
                }
                break;
            case "medico":
                if (tagetId.contains("medico")) {
                    inTarget = true;
                }
                break;
        }
        Dbg.p("checkCategory inTarget: " + inTarget);

        return inTarget;
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
        lastIdFolder = id;
        folderContentFolderList.clear();
        folderContentFolderList = new ArrayList<ContentDoc>();
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        Folder folder = realm.where(Folder.class).equalTo("id", id).findFirst();
        if (folder != null) {

            for (int j = 0; j < folder.contents.size(); j++) {
                ArrayList<Target> allTargets = new ArrayList<>(folder.contents.get(j).targets);
                if (checkCategory(allTargets) && contentToView(folder.contents.get(j))) {
                    ContentDoc contentDoc = new ContentDoc();
                    contentDoc.id = folder.contents.get(j).id;
                    contentDoc.keywords = folder.contents.get(j).keywords;
                    contentDoc.alertHighlight = folder.contents.get(j).alertHighlight;
                    contentDoc.name = folder.contents.get(j).name;
                    contentDoc.viewed = folder.contents.get(j).isViewed();
//
                    if (folder.contents.get(j).isNewcontent() && !folder.contents.get(j).isViewed()) {
                        if (folder.contents.get(j).alertHighlight) {
                            contentDoc.typeview = SelfBoxConstants.TypeViewContent.IMPORTANT_NEW;
                        } else {
                            contentDoc.typeview = SelfBoxConstants.TypeViewContent.NEW;
                        }
                    } else {

                        if (folder.contents.get(j).alertHighlight) {
                            contentDoc.typeview = SelfBoxConstants.TypeViewContent.IMPORTANT;
                        }
                    }

                    TypeContent typeContent = folder.contents.get(j).type;
                    if (typeContent.descr.equalsIgnoreCase("pdf")) {
                        contentDoc.type = SelfBoxConstants.TypeContent.PDF;
                    } else if (typeContent.descr.equalsIgnoreCase("evisual")) {
                        contentDoc.type = SelfBoxConstants.TypeContent.VISUAL;
                    } else if (typeContent.descr.equalsIgnoreCase("video")) {
                        contentDoc.type = SelfBoxConstants.TypeContent.VIDEO;
                    }
                    contentDoc.content = folder.contents.get(j).getLocalfilePath();
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

        if (FILTER_NOW == FILTER_BY_AZ) {
            Collections.sort(folderContentFolderList, new Comparator<ContentDoc>() {
                @Override
                public int compare(ContentDoc o1, ContentDoc o2) {
                    return o1.name.compareTo(o2.name);
                }
            });
        } else if (FILTER_NOW == FILTER_BY_DATE) {
            Collections.sort(folderContentFolderList, new Comparator<ContentDoc>() {
                @Override
                public int compare(ContentDoc o1, ContentDoc o2) {
                    return Long.compare(o1.lastUpdate, o2.lastUpdate);
                }
            });
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
        lastFiltersType = null;
        ArrayList<ContentDoc> filtered = new ArrayList<>();
        if (levelView == 0) {
            if (contents != null) {
                for (int i = 0; i < contents.size(); i++) {
                    if (filterText != null) {

                        if (contents.get(i).name.toLowerCase().contains(filterText.toLowerCase())) {
                            filtered.add(contents.get(i));
                        } else {
                            ArrayList<String> keyword = new ArrayList<>(Arrays.asList(contents.get(i).keywords.split(",")));
                            if (keyword.contains(filterText)) {
                                filtered.add(contents.get(i));
                            }
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
                        } else {
                            ArrayList<String> keyword = new ArrayList<>(Arrays.asList(folderContentFolderList.get(i).keywords.split(",")));
                            if (keyword.contains(filterText)) {
                                filtered.add(folderContentFolderList.get(i));
                            }
                        }
                    }
                }
            }
        }
        view.applyFilter(filtered);
    }

    @Override
    public void selectLastFilter() {
        lastFiltersType = null;
        if (levelView == 0) {
            view.applyFilter(contents);
        } else if (levelView == 1) {
            view.applyFilter(folderContentFolderList);
        }

    }

    @Override
    public void setCategory(String categoryContent) {
        this.categoryContent = categoryContent;
    }

    @Override
    public void getUpdateContents() {
        if (levelView == 0) {
            contents = (ArrayList<ContentDoc>) getContents(categoryContent);
            view.applyFilter(contents);
        } else if (levelView == 1) {
            folderContentFolderList = getContentsByFolder(lastIdFolder);
            view.applyFilter(folderContentFolderList);
        }

    }

    @Override
    public void filterTypes(Filters filterList) {
        ArrayList<ContentDoc> contentDocs = new ArrayList<>();
        lastFiltersType = filterList;
        if (levelView == 0) {
            if (filterList.all) {
                view.applyFilter(contents);
            } else {
                for (int i = 0; i < contentDocs.size(); i++) {
                    ContentDoc contentDoc = contentDocs.get(i);
                    if (contentDoc.type == SelfBoxConstants.TypeContent.PDF && filterList.documents) {
                        contentDocs.add(contentDoc);
                    } else if (contentDoc.type == SelfBoxConstants.TypeContent.VISUAL && filterList.eVisual) {
                        contentDocs.add(contentDoc);
                    } else if (contentDoc.alertHighlight && filterList.alertHighlight) {
                        contentDocs.add(contentDoc);
                    } else if (contentDoc.type == SelfBoxConstants.TypeContent.VIDEO && filterList.video) {
                        contentDocs.add(contentDoc);
                    }
                }
                view.applyFilter(contentDocs);
            }
        } else if (levelView == 1) {
            if (filterList.all) {
                view.applyFilter(folderContentFolderList);
            } else {
                Dbg.p("filterList.alertHighlight: " + filterList.alertHighlight);
                for (int i = 0; i < folderContentFolderList.size(); i++) {
                    ContentDoc contentDoc = folderContentFolderList.get(i);
                    if (filterList.alertHighlight) {
                        if (contentDoc.type == SelfBoxConstants.TypeContent.PDF && filterList.documents) {
                            contentDocs.add(contentDoc);
                        } else if (contentDoc.type == SelfBoxConstants.TypeContent.VISUAL && filterList.eVisual) {
                            contentDocs.add(contentDoc);
                        } else if (contentDoc.type == SelfBoxConstants.TypeContent.VIDEO && filterList.video) {
                            contentDocs.add(contentDoc);
                        }
                    } else {
                        if (contentDoc.type == SelfBoxConstants.TypeContent.PDF && filterList.documents && !contentDoc.alertHighlight) {
                            contentDocs.add(contentDoc);
                        } else if (contentDoc.type == SelfBoxConstants.TypeContent.VISUAL && filterList.eVisual && !contentDoc.alertHighlight) {
                            contentDocs.add(contentDoc);
                        } else if (contentDoc.type == SelfBoxConstants.TypeContent.VIDEO && filterList.video && !contentDoc.alertHighlight) {
                            contentDocs.add(contentDoc);
                        }
                    }
                }
                view.applyFilter(contentDocs);
            }
        }
    }

    @Override
    public Filters getLastFilter() {
        if (lastFiltersType != null) {
            return lastFiltersType;
        }
        return new Filters(true);
    }

    @Override
    public void setContentViewed(int id) {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        realm.beginTransaction();
        ContentBox content = realm.where(ContentBox.class).equalTo("id", id).findFirst();
        content.setViewed(true);
        content.setNewcontent(false);
        realm.copyToRealmOrUpdate(content);
        realm.commitTransaction();
        if (levelView == 1) {
            getContentsByFolder(lastIdFolder);
            view.applyFilter(folderContentFolderList);
        } else if (levelView == 0) {
            getContents(categoryContent);
            view.applyFilter(contents);
        }
    }

    @Override
    public ContentBox getContentDataFromId(int id) {
        Realm realm= SelfBoxApplicationImpl.appComponent.realm();
        ContentBox contentBox= realm.where(ContentBox.class).equalTo("id", id).findFirst();
        return contentBox;
    }

    @Override
    public void addOrDeleteShare(ContentDoc contentSelectShare) {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        String id= String.valueOf(contentSelectShare.id);

       final ItemShared sharedItem = realm.where(ItemShared.class).equalTo("id", id).findFirst();
        if(sharedItem !=null){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    sharedItem.deleteFromRealm();
                }
            });
        }else {
            try{

            realm.beginTransaction();

                ItemShared  newSharedItem= new ItemShared();
                newSharedItem.setId(id);
                newSharedItem.setName(contentSelectShare.name);
                newSharedItem.setType("content");
                realm.copyToRealmOrUpdate(newSharedItem);
        }catch (Exception ex){

            }
            finally {
                realm.commitTransaction();
            }
        }
        view.refreshContents();


    }

    @Override
    public ArrayList<ItemShared> getItemsShared() {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<ItemShared> sharedItems = realm.where(ItemShared.class).findAll();
        return new ArrayList<>(sharedItems);
    }
}
