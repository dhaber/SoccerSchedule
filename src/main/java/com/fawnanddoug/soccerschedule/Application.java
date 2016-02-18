package com.fawnanddoug.soccerschedule;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import com.fawnanddoug.soccerschedule.domain.Game;
import com.fawnanddoug.soccerschedule.service.SoccerScheduleService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(Application.class.getCanonicalName() + ".output");
	
	@Autowired
	private SoccerScheduleService scheduleService;
	
	@Value("${weeks}")
	private int weeks;
	
	private static final List<String> teams = Arrays.asList(
			"Bayern Munich", "Barcelona", "Manchester United", "United States", "Germany", "England", 
			"Argentina", "Brazil", "Spain", "Netherlands", "France", "Belgium", "Italy");

	@Override
	public void run(String... args) {
		List<Game> games = this.scheduleService.getSchedule(this.weeks);
		games = this.scheduleService.filterByTeams(games, teams);
		for (Game game : games) {
			String channel = game.getChannel();
			channel = StringUtils.hasText(channel) ? channel : " ";
			logger.warn(game.getStartTime().toString("yyyy MMM-dd E hh:mm a") + "| " + game.getLeague() + "| " + game.getAway().getName() + "| "  + game.getHome().getName() + "| " + channel);			
		}
	}

	public static void main(String[] args) throws Exception {
		 SpringApplication app = new SpringApplication(Application.class);
		 app.setShowBanner(false);
		 app.run(args);		
	}
}
