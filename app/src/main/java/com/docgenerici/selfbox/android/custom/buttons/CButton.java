package com.docgenerici.selfbox.android.custom.buttons;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.docgenerici.selfbox.android.custom.UtilFont;

import com.docgenerici.selfbox.R;



/**
 * @author Giuseppe Sorce
 */
public class CButton extends Button {

    protected String font = "";

    public CButton(Context context) {
        super(context);
    }

    public CButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            TypedArray styles_array = context.obtainStyledAttributes(attrs,
                    R.styleable.CTextView);
            try {
                font = styles_array.getString(R.styleable.CTextView_font);
            } finally {
                styles_array.recycle();
            }
        }
        setTransformationMethod(null);

        setFont();

    }


    public CButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            TypedArray styles_array = context.obtainStyledAttributes(attrs,
                    R.styleable.CTextView);
            try {
                font = styles_array.getString(R.styleable.CTextView_font);
            } finally {
                styles_array.recycle();
            }
        }
        setTransformationMethod(null);

        setFont();

    }


    /**
     * Set custom font
     *
     * @param f
     */
    public void setFont(String f) {
        font = f;
        setFont();
    }

    protected void setFont() {

        if (!(font != null && font.length() > 0)) {
            font = "helveticalt";
        }
        switch (font) {

            case "helveticalt":
                UtilFont.setTypeface(this, "HelveticaNeueLTPro-Lt.otf");
                break;
            case "medium":
                UtilFont.setTypeface(this, "Roboto-Medium.ttf");
                break;

        }

    }

    /**
     * Change font and style programmatically
     *
     * @param fontchange
     * @param style
     */
    public void changeFontStyle(String fontchange, int style) {

        if (fontchange.length() > 0) {
            font = fontchange;
        }
        Typeface typeFace = UtilFont.getTypeFace(getContext(), font);
        this.setTypeface(UtilFont.getTypeFace(getContext(), font),
                style >= 0 ? style : typeFace.getStyle());

    }

}
