package com.liufeismart.editable.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TextUtil {





    public static int getTextWidth(String content, EditText textView,
            Context context) {
        TextPaint paint = textView.getPaint();
        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        int width = rect.width();
        return width;
    }


}
