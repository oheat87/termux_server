package com.example.termux_spring.accessingdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TrendingVideosRepository extends JpaRepository<TrendingVideos, Long> {
    TrendingVideos findById(long id);
    List<TrendingVideos> findTop20ByVideoTypeOrderByTrendingTimeDesc(Long videoType);
}
