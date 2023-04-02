package com.example.termux_spring.accessingdatajpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import java.text.SimpleDateFormat;

@Entity
public class TrendingVideos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String channelName;
    private String url;
    private Long viewCount;
    private Long viewerCount;
    private Date trendingTime;
    private String title;
    private Long videoType;

    protected TrendingVideos(){}
    public TrendingVideos(String channelName, String url, Long viewCount, Long viewerCount, Date trendingTime, String title, Long videoType){
        this.channelName=channelName;
        this.url=url;
        this.viewCount=viewCount;
        this.viewerCount=viewerCount;
        this.trendingTime=trendingTime;
        this.title=title;
        this.videoType=videoType;
    }

    @Override
    public String toString(){
        return String.format(
            "Trending: [id=%d, channel_name=%s, url=%s, view_count=%d, viewer_count=%d, trending_time=%s, title=%s, videoType=%d]",
            this.id,
            this.channelName,
            this.url,
            this.viewCount,
            this.viewerCount,
            this.getTrendingTimeString(),
            this.title,
            this.videoType
        );
    }

    public Long getId(){
        return id;
    }

    public String getChannelName(){
        return channelName;
    }
    
    public String getUrl(){
        return url;
    }

    public Long getViewCount(){
        return viewCount;
    }

    public Long getViewerCount(){
        return viewerCount;
    }

    public Date getTrendingTime(){
        return trendingTime;
    }

    public String getTrendingTimeString(){
        return (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(this.trendingTime).toString();
    }

    public String getTitle(){
        return title;
    }

    public Long getVideoType(){
        return videoType;
    }
}
