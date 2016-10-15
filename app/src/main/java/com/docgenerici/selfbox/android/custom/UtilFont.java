package com.docgenerici.selfbox.android.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.debug.Dbg;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Giuseppe Sorce
 */
public class UtilFont {


  public static Map<String, Typeface> typefaceCache = new HashMap<>();

  public static void setTypeface(TextView textView, String typefaceName) {

    Context context = textView.getContext();
    if (typefaceCache.containsKey(typefaceName)) {
      textView.setTypeface(typefaceCache.get(typefaceName));
    } else {
      Typeface typeface;
      try {
        typeface = Typeface.createFromAsset(textView.getContext()
                .getAssets(),
            context.getString(R.string.assets_fonts_folder)
                + typefaceName);
      } catch (Exception e) {
      Dbg.e( "Couldn't set TypeFace: %s"+e.getMessage());
        return;
      }
      typefaceCache.put(typefaceName, typeface);
      textView.setTypeface(typeface);
    }
  }

  public static void setTypeface(Button button, String typefaceName) {
    Context context = button.getContext();
    if (typefaceCache.containsKey(typefaceName)) {
      button.setTypeface(typefaceCache.get(typefaceName));
    } else {
      Typeface typeface;
      try {
        typeface = Typeface.createFromAsset(button.getContext()
                .getAssets(),
            context.getString(R.string.assets_fonts_folder)
                + typefaceName);
      } catch (Exception e) {
        Dbg.e(  "Couldn't set TypeFace: %s"+e.getMessage());
        return;
      }
      typefaceCache.put(typefaceName, typeface);
      button.setTypeface(typeface);
    }
  }

  public static void setTypeface(EditText edit, String typefaceName) {
    Context context = edit.getContext();

    if (typefaceCache.containsKey(typefaceName)) {
      edit.setTypeface(typefaceCache.get(typefaceName));
    } else {
      Typeface typeface;
      try {
        typeface = Typeface.createFromAsset(edit.getContext()
                .getAssets(),
            context.getString(R.string.assets_fonts_folder)
                + typefaceName);
      } catch (Exception e) {
        Dbg.e( "Couldn't set TypeFace: %s"+e.getMessage());
        return;
      }
      typefaceCache.put(typefaceName, typeface);
      edit.setTypeface(typeface);
    }
  }

  public static Typeface getTypeFace(Context context, String typefaceName) {
    if (typefaceCache.containsKey(typefaceName)) {
      return (typefaceCache.get(typefaceName));
    } else {
      Typeface typeface;
      try {
        typeface = Typeface.createFromAsset(context.getAssets(),
            context.getString(R.string.assets_fonts_folder)
                + typefaceName);
      } catch (Exception e) {

        return null;
      }
      typefaceCache.put(typefaceName, typeface);
      return typeface;
    }
  }
}