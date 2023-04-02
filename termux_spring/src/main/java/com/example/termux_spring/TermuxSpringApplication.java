package com.example.termux_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.termux_spring.accessingdatajpa.TrendingVideos;
import com.example.termux_spring.accessingdatajpa.TrendingVideosRepository;

import java.util.List;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class TermuxSpringApplication {
	private static final Logger log= LoggerFactory.getLogger(TermuxSpringApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TermuxSpringApplication.class, args);
	}

	@Autowired
	private TrendingVideosRepository repository;

	@GetMapping("/")
	public String index(){
		List<TrendingVideos> youtube_trending= new ArrayList<TrendingVideos>();
		List<TrendingVideos> twitch_trending= new ArrayList<TrendingVideos>();
		for(TrendingVideos video: repository.findTop20ByVideoTypeOrderByTrendingTimeDesc(1L)){
			youtube_trending.add(video);
		}
		for(TrendingVideos video: repository.findTop20ByVideoTypeOrderByTrendingTimeDesc(2L)){
			twitch_trending.add(video);
		}
		String youtube_table_update_time="";
		if(youtube_trending.size()>0) youtube_table_update_time=youtube_trending.get(0).getTrendingTimeString();
		String youtube_trending_table=
		"""
			<table>
				<caption style=\"caption-side:bottom\"> last update: 
		""" + youtube_table_update_time +

		"""
				</caption>
					<thead>
						<th>rank</th>
						<th>channel</th>
						<th>title</th>
						<th>view</th>
						<th>-</th>
					</thead>
					<tbody>
			""";
		for(int i=0; i<youtube_trending.size(); i++){
			TrendingVideos video=youtube_trending.get(i);
			youtube_trending_table+="<tr>";
			youtube_trending_table+="<td>"+Long.toString(i+1)+"</td>";
			youtube_trending_table+="<td>"+video.getChannelName()+"</td>";
			youtube_trending_table+="<td>"+video.getTitle()+"</td>";
			youtube_trending_table+="<td>"+Long.toString(video.getViewCount())+"</td>";
			youtube_trending_table+="<td><a href=\'"+video.getUrl()+"\'>watch</a></td>";
			youtube_trending_table+="</tr>";
		}
		youtube_trending_table+=
			"""
					</tbody>
				</table>
			""";
		String twitch_table_update_time="";
		if(twitch_trending.size()>0) twitch_table_update_time=twitch_trending.get(0).getTrendingTimeString();
		String twitch_trending_table=
			"""
				<table>
					<caption style=\"caption-side:bottom\"> last update: 
			""" + twitch_table_update_time +

			"""
					</caption>
					<thead>
						<th>rank</th>
						<th>channel</th>
						<th>title</th>
						<th>viewer</th>
						<th>-</th>
					</thead>
					<tbody>
			""";
		for(int i=0; i<twitch_trending.size(); i++){
			TrendingVideos video=twitch_trending.get(i);
			twitch_trending_table+="<tr>";
			twitch_trending_table+="<td>"+Long.toString(i+1)+"</td>";
			twitch_trending_table+="<td>"+video.getChannelName()+"</td>";
			twitch_trending_table+="<td>"+video.getTitle()+"</td>";
			twitch_trending_table+="<td>"+Long.toString(video.getViewerCount())+"</td>";
			twitch_trending_table+="<td><a href=\'"+video.getUrl()+"\'>watch</a></td>";
			twitch_trending_table+="</tr>";
		}
		
		twitch_trending_table+=
			"""
					</tbody>
				</table>
			""";
		return  """
				<h1>top trending youtube videos</h1>
				
				""" + youtube_trending_table +
				"""
				<h1>top trending twitch streams</h1>	
				""" + twitch_trending_table +
				"""
				<h1>Hello from Home-Brewed Web Service</h1>
				<h3>wanna go to</h3>
				<ul>
					<li><a href='https://www.google.com'>Google?</a></li>
					<li><a href='https://www.youtube.com'>Youtube?</a></li>
					<li><a href='https://www.naver.com'>Naver?</a></li>
					<li><a href='https://www.facebook.com'>Facebook?</a></li>
				</ul>
				<h3><a href='/hello'>hello page</a></h3>
				<span>this web site is powered by
					<img width='150' height='150' src='https://spring.io/img/logos/spring-initializr.svg'/>
				</span>
				""";
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value="name",defaultValue = "World")String name){
		return "<h1>Hello " + name + "!</h1>";
	}

	@Bean
	public CommandLineRunner termuxDBConnectTest(TrendingVideosRepository repository){
		return (args)->{
			log.info("Trending Videos find all:");
			log.info("-------------------------");
			int count=0;
			for (TrendingVideos video : repository.findAll()){
				// log.info(video.toString());
				count++;
			}
			log.info(String.format("total rows: %d",count));
			log.info("");

			log.info("Trending Youtube Videos most recent:");
			log.info("-------------------------");
			for (TrendingVideos video : repository.findTop20ByVideoTypeOrderByTrendingTimeDesc(1L)){
				log.info(video.toString());
			}
			log.info("");

			log.info("Trending Twitch Streams most recent:");
			log.info("-------------------------");
			for (TrendingVideos video : repository.findTop20ByVideoTypeOrderByTrendingTimeDesc(2L)){
				log.info(video.toString());
			}
			log.info("");
		};
	}
}
