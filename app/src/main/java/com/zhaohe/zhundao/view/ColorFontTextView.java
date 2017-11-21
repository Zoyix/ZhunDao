package com.zhaohe.zhundao.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;

import com.zhaohe.app.utils.StringUtils;
import com.zhaohe.zhundao.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/11/17 16:23
 */
public class ColorFontTextView extends AppCompatTextView {
    private ArrayList<String> mTexts = new ArrayList<>();
    private ArrayList<String> mColors = new ArrayList<>();
    private ArrayList<String> mTextSizes = new ArrayList<>();
    private ArrayList<String> mLines = new ArrayList<>();
    private ArrayList<String> mStyles = new ArrayList<>();
    private String mCurrentText;
    private Context mContext;
    private String styledTextString;
    private String styleTexts;
    private int mStart = 0, mEnd = 0;

    public ColorFontTextView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ColorFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    public ColorFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    public void setPositon(int start, int end) {
        this.mStart = start;
        this.mEnd = end;
    }

    //获得我们所定义的自定义样式属性
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ColorFountTextView);
        String colors = typedArray.getString(R.styleable.ColorFountTextView_color_arrays);
        String texts = typedArray.getString(R.styleable.ColorFountTextView_text_arrays);
        String textsizes = typedArray.getString(R.styleable.ColorFountTextView_textsize_arrays);
        String lines = typedArray.getString(R.styleable.ColorFountTextView_line_arrays);
        String styles = typedArray.getString(R.styleable.ColorFountTextView_style_arrays);
        typedArray.recycle();
        initDate(getText().toString(), colors, texts, textsizes, lines, styles);
        setTextDate();
    }

    //截取字符串添加到对应的集合
    private void initDate(String str, String colors, String texts, String textsizes, String lines, String styles) {
        try {
            if (!StringUtils.isEmpty(texts)) {
                String[] split = texts.split("\\|");
                for (int i = 0; i < split.length; i++) {
                    mTexts.add(split[i]);
                }
            }
            if (!StringUtils.isEmpty(colors)) {
                String[] split = colors.split("\\|");
                for (int i = 0; i < split.length; i++) {
                    mColors.add(split[i]);
                }
            }
            if (!StringUtils.isEmpty(textsizes)) {
                String[] split = textsizes.split("\\|");
                for (int i = 0; i < split.length; i++) {
                    mTextSizes.add(split[i]);
                }
            }
            if (!StringUtils.isEmpty(lines)) {
                String[] split = lines.split("\\|");
                for (int i = 0; i < split.length; i++) {
                    mLines.add(split[i]);
                }
            }
            if (!StringUtils.isEmpty(styles)) {
                String[] split = styles.split("\\|");
                for (int i = 0; i < split.length; i++) {
                    mStyles.add(split[i]);
                }
            }
            mCurrentText = str;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据相关属性赋值
     */
    private void setTextDate() {
        try {
            if (!StringUtils.isEmpty(mCurrentText)) {
                SpannableString styledText = new SpannableString(mCurrentText);
                if (mTextSizes != null && mTextSizes.size() != 0) {
                    int currentSize = mTextSizes.size() <= mTexts.size() ? mTextSizes.size() : mTexts.size();
                    for (int i = 0; i < currentSize; i++) {
                        String size = mTexts.get(i);
                        int startPostion = mCurrentText.indexOf(size);

                        int endPostion = mCurrentText.indexOf(size) + size.length();
                        if (startPostion < 0) {
                            startPostion = mStart;
                            endPostion = mEnd;
                        }
                        if (!mTextSizes.get(i).contains("null")) {
                            styledText.setSpan(new AbsoluteSizeSpan(sp2px(mContext, Integer.valueOf(mTextSizes.get(i)))), startPostion, endPostion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                if (mColors != null && mColors.size() != 0) {
                    int currentSize = mColors.size() <= mTexts.size() ? mColors.size() : mTexts.size();
                    for (int i = 0; i < currentSize; i++) {
                        String size = mTexts.get(i);
                        int startPostion = mCurrentText.indexOf(size);

                        int endPostion = mCurrentText.indexOf(size) + size.length();
                        if (startPostion < 0) {
                            startPostion = mStart;
                            endPostion = mEnd;
                        }
                        if (!mColors.get(i).contains("null")) {
                            styledText.setSpan(new ForegroundColorSpan(Color.parseColor(mColors.get(i))), startPostion, endPostion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                if (mLines != null && mLines.size() != 0) {
                    int currentSize = mLines.size() <= mTexts.size() ? mLines.size() : mTexts.size();
                    for (int i = 0; i < currentSize; i++) {
                        String size = mTexts.get(i);
                        int startPostion = mCurrentText.indexOf(size);
                        int endPostion = mCurrentText.indexOf(size) + size.length();
                        if (!mLines.get(i).contains("null")) {
                            if (mLines.get(i).contains("undeline")) {
                                styledText.setSpan(new UnderlineSpan(), startPostion, endPostion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else if (mLines.get(i).contains("deleteline")) {
                                styledText.setSpan(new StrikethroughSpan(), startPostion, endPostion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else {
                                styledText.setSpan(new UnderlineSpan(), startPostion, endPostion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                }
                if (mStyles != null && mStyles.size() != 0) {
                    int currentSize = mStyles.size() <= mTexts.size() ? mStyles.size() : mTexts.size();
                    for (int i = 0; i < currentSize; i++) {
                        String size = mTexts.get(i);
                        int startPostion = mCurrentText.indexOf(size);
                        int endPostion = mCurrentText.indexOf(size) + size.length();
                        if (!mStyles.get(i).contains("null")) {
                            StyleSpan styleSpan;
                            if (("normal").equals(mStyles.get(i))) {
                                styleSpan = new StyleSpan(Typeface.NORMAL);
                            } else if (("bold").equals(mStyles.get(i))) {
                                styleSpan = new StyleSpan(android.graphics.Typeface.BOLD);
                            } else if (("italic").equals(mStyles.get(i))) {
                                styleSpan = new StyleSpan(android.graphics.Typeface.ITALIC);
                            } else if (("bold_italic").equals(mStyles.get(i))) {
                                styleSpan = new StyleSpan(android.graphics.Typeface.BOLD_ITALIC);
                            } else {
                                styleSpan = new StyleSpan(Typeface.NORMAL);
                            }
                            styledText.setSpan(styleSpan, startPostion, endPostion, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                setText(styledText);
                styledTextString = getText().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ColorFontTextView setTextStyle(String str, String texts, String all) {
        if (!StringUtils.isEmpty(all) && !StringUtils.isEmpty(texts)) {
            styleTexts = texts;
            String[] split = all.split("\\|");
            if (all.contains("#")) {
                initDate(str, all, texts, null, null, null);
            } else if (all.contains("undeline") || all.contains("deleteline")) {
                initDate(str, null, texts, null, all, null);
            } else if (all.contains("normal") || all.contains("bold") || all.contains("italic") || all.contains("bold_italic")) {
                initDate(str, null, texts, null, null, all);
            } else if (isNumeric(all)) {
                initDate(str, null, texts, all, null, null);
            } else {
                initDate(str, null, texts, null, null, null);
            }
            setTextDate();
        } else {
            setText(str);
        }
        return this;
    }

    public ColorFontTextView setTextStyle(String all) {
        if (!StringUtils.isEmpty(styledTextString) && !StringUtils.isEmpty(styleTexts)) {
            setTextStyle(styledTextString, styleTexts, all);
        }
        return this;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, int spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 判断字符串是是数字
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.replace("|", ""));
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}