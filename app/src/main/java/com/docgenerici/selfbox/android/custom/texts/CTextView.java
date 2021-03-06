package com.docgenerici.selfbox.android.custom.texts;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.custom.UtilFont;


/**
 * @author Giuseppe Sorce
 */
public class CTextView extends TextView {

    protected String font = "";

    public CTextView(Context context) {
        super(context);
    }

    public CTextView(Context context, AttributeSet attrs) {
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
        setFont();

    }


    public CTextView(Context context, AttributeSet attrs, int defStyle) {
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

            case "helveticaltpro":
                UtilFont.setTypeface(this, "HelveticaNeueLTPro-Lt.otf");
                break;
            case "helveticalt":
                UtilFont.setTypeface(this, "HelveticaNeueLTStd-Lt.otf");
                break;
            case "helveticaltcn":
                UtilFont.setTypeface(this, "HelveticaNeueLTStd-LtCn.otf");
                break;
            case "helveticaltcnroman":
                UtilFont.setTypeface(this, "HelveticaNeueLTStd-Roman.otf");
                break;
            case "helveticabold":
                UtilFont.setTypeface(this, "HelveticaNeueLTPro-Bd.otf");
                break;
            case "helveticaltst":
                UtilFont.setTypeface(this, "HelveticaNeueLTStd-LtIt.otf");
                break;
            case "medium":
                UtilFont.setTypeface(this, "Roboto-Medium.ttf");
                break;
            case "robotobold":
                UtilFont.setTypeface(this, "Roboto-Bold.ttf");
                break;
            case "robotoitalic":
                UtilFont.setTypeface(this, "Roboto-Italic.ttf");
                break;
            case "roboto":
                UtilFont.setTypeface(this, "Roboto-Regular.ttf");
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
