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
           schema.create("MedicalView").addField("idadd", int.class, FieldAttribute.PRIMARY_KEY).addField("id", String.class).addField("d", Date.class);
            schema.create("PharmaView").addField("idadd", int.class, FieldAttribute.PRIMARY_KEY).addField("id", String.class).addField("d", Date.class);
            schema.get("ContentBox").addField("newcontent", boolean.class);
            oldVersion++;
        }


    }
}

