package com.docgenerici.selfbox.models.persistence;

import com.docgenerici.selfbox.debug.Dbg;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2017.
 *         <p>......</p>
 */

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        Dbg.p("oldVersion: "+oldVersion);
        Dbg.p("newVersion: "+newVersion);

        if(oldVersion ==0){
           schema.create("MedicalView").addField("idadd", int.class, FieldAttribute.PRIMARY_KEY).addField("code", String.class).addField("selectionDate", Date.class);
            schema.create("PharmaView").addField("idadd", int.class, FieldAttribute.PRIMARY_KEY).addField("code", String.class).addField("selectionDate", Date.class);
            schema.get("ContentBox").addField("newcontent", boolean.class);
            oldVersion++;
        }
        if(oldVersion == 1){
            schema.create("ItemShared").addField("id", String.class, FieldAttribute.PRIMARY_KEY).addField("name", String.class).addField("path", String.class).addField("type", String.class);

//
//            public class ItemShared extends RealmObject {
//
//                @PrimaryKey
//                private String id;
//                private String name;
//                private String path;
//                private String type;
//            }
        }


    }
}

