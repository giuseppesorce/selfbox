package com.docgenerici.selfbox.models;

import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.medico.Medico;

import java.util.List;

import io.realm.RealmObject;

/**
 * @uthor giuseppesorce
 */

public class MedicalList{

   public List<Medico> medici;
    public List<Farmacia> farmacie;
}
