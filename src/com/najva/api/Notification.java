package com.najva.api;

import java.text.SimpleDateFormat;
import java.util.*;

public class Notification {

    private static final String[] onClickActions = {"open-link", "open-sms", "open-call", "open-app", "open-activity",
            "open-telegram-channel", "join-telegram-channel"};
    private static final String SEND_TO_ALL_URL = "https://app.najva.com/api/v1/notifications/";
    private static final String SEND_TO_USERS_URL = "https://app.najva.com/notification/api/v1/notifications/";

    Map<String, Object> data = new HashMap<>();
    String url;

    public static class NotificationBuilder {

        public static final int OPEN_LINK = 0;
        public static final int OPEN_SMS = 1;
        public static final int OPEN_CALL = 2;
        public static final int OPEN_APP = 3;
        public static final int OPEN_ACTIVITY = 4;
        public static final int OPEN_TELEGRAM_CHANNEL = 5;
        public static final int JOIN_TELEGRAM_CHANNEL = 6;

        private String title;
        private String body;
        private int onClickAction;
        private String url;
        private String content;
        private String icon;
        private String image;
        private Map<String, String> json = new HashMap<>();
        private String jsonString;
        private Date sendTime;
        private List<Integer> segmentInclude = new ArrayList<>();
        private List<Integer> segmentExclude = new ArrayList<>();
        private boolean oneSignalEnabled;
        private List<Integer> oneSignalAccounts = new ArrayList<>();
        private boolean sendToAll;
        private List<String> subscriberTokens = new ArrayList<>();

        public NotificationBuilder(String title, String body, int onClickAction, String url, boolean sendToAll) {
            this.title = title;
            this.body = body;
            this.onClickAction = onClickAction;
            this.url = url;
            this.sendToAll = sendToAll;
        }

        public NotificationBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public NotificationBuilder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public NotificationBuilder setImage(String image) {
            this.image = image;
            return this;
        }

        public NotificationBuilder addJsonParameter(String key, String value) {
            json.put(key, value);
            return this;
        }

        public NotificationBuilder setJsonString(String json) {
            jsonString = json;
            return this;
        }

        public NotificationBuilder setSentTime(Date date) {
            this.sendTime = date;
            return this;
        }

        public NotificationBuilder includeSegment(int segmentId) {
            segmentInclude.add(segmentId);
            return this;
        }

        public NotificationBuilder excludeSegment(int segmentId) {
            segmentExclude.add(segmentId);
            return this;
        }

        public NotificationBuilder enableOneSignal() {
            oneSignalEnabled = true;
            return this;
        }

        public NotificationBuilder includeOneSignalAccount(int accountId) {
            oneSignalAccounts.add(accountId);
            return this;
        }

        public NotificationBuilder setSubscriberTokens(List<String> subscriberTokens) {
            this.subscriberTokens = subscriberTokens;
            return this;
        }

        public NotificationBuilder addSubscriberTokens(String token) {
            subscriberTokens.add(token);
            return this;
        }

        public Notification build() {
            Notification notification = new Notification();

            if (sendToAll) {
                notification.url = SEND_TO_ALL_URL;

                if (subscriberTokens != null && !subscriberTokens.isEmpty()) {
                    throw new IllegalArgumentException("Cannot send notification to specific users when 'sendToAll' is true");
                }

            } else {
                notification.url = SEND_TO_USERS_URL;
            }

            notification.data.put("title", this.title);
            notification.data.put("body", this.body);
            notification.data.put("content", this.content);
            notification.data.put("onClickAction", onClickActions[this.onClickAction]);
            notification.data.put("url", this.url);
            notification.data.put("icon", this.icon);
            notification.data.put("image", this.image);
            if (jsonString != null) {
                notification.data.put("json", jsonString);
            } else {
                notification.data.put("json", json);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            notification.data.put("sent_time", dateFormat.format(sendTime) + "T" + timeFormat.format(sendTime));
            notification.data.put("segment_include", segmentInclude);
            notification.data.put("segment_exclude", segmentExclude);
            notification.data.put("one_signal_enabled", String.valueOf(oneSignalEnabled));
            notification.data.put("one_signal_accounts", oneSignalAccounts);

            return notification;
        }
    }
}
