package com.busap.vcs.webcomn.controller;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by
 * User: djyin
 * Date: 12/10/13
 * Time: 2:28 PM
 */
public class MutliFormatDateEditor extends PropertyEditorSupport {

    private final boolean allowEmpty;

    /**
     * The Date formats. 多种格式.默认支持 "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd" 二种格式
     */
    List<String> dateFormats = null;

    List<SimpleDateFormat> simpleFormats = null;

    public MutliFormatDateEditor(boolean allowEmpty) {
        super();
        this.allowEmpty = allowEmpty;
        if (dateFormats == null) {
            dateFormats = new ArrayList<String>();
            dateFormats.add("yyyy-MM-dd HH:mm:ss");
            dateFormats.add("yyyy-MM-dd");
            //dateFormats.add("HH:mm:ss");
        }
        simpleFormats = new ArrayList<SimpleDateFormat>();
        for (String df : dateFormats) {
            simpleFormats.add(new SimpleDateFormat(df));
        }

    }
    public MutliFormatDateEditor(List<String> dateFormats, boolean allowEmpty) {
        super();
        this.allowEmpty = allowEmpty;
        this.dateFormats = dateFormats;
        simpleFormats = new ArrayList<SimpleDateFormat>();
        for (String df : dateFormats) {
            simpleFormats.add(new SimpleDateFormat(df));
        }

    }
    public Date parse(String input) throws ParseException {
        ParseException pe = null;
        for (int i = 0; i < this.simpleFormats.size(); i++) {
            try {
                SimpleDateFormat sf = simpleFormats.get(i);
                return sf.parse(input);
            } catch (ParseException e) {
                //ignored
                pe = e;
            }
        }
        throw pe;
    }
    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            try {
                setValue(parse(text));
            }
            catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? this.simpleFormats.get(0).format(value) : "");
    }

}
