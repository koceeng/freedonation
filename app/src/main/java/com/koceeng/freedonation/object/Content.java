package com.koceeng.freedonation.object;

import com.koceeng.freedonation.base.FirebaseObject;

public class Content extends FirebaseObject {

    private Long timestamp;
    private String title;
    private String subtitle;
    private String text;
    private String footer;

    public Content() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return trim(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return trim(subtitle);
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getText() {
        return trim(text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFooter() {
        return trim(footer);
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    private String trim(String value) {
        if (value != null)
            value = value
                    .replaceAll("\n", "")
                    .replaceAll("\\n", "")
                    .replaceAll("^\\s+|\\s+$", "")
                    .replaceAll("\\u00A0", "")
                    .replaceAll("\\u00A0", "")
                    .trim();
        return value;
    }
}
