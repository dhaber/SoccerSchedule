package com.fawnanddoug.soccerschedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fawnanddoug.soccerschedule.domain.Game;
import com.fawnanddoug.soccerschedule.service.PrinterService;
import com.fawnanddoug.soccerschedule.service.SoccerScheduleService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(Application.class.getCanonicalName() + ".output");
	
	@Autowired
	private SoccerScheduleService scheduleService;
	
	@Autowired
	private PrinterService printerService;
	
	@Value("${weeks}")
	private int weeks;
	
	@Value("#{'${teams}'.split(',')}") 
	private List<String> teams;

	@Override
	public void run(String... args) {
		logger.warn("Searching " + this.weeks + " weeks for " + this.teams);
		List<Game> games = this.scheduleService.getSchedule(this.weeks);
		games = this.scheduleService.filterByTeams(games, teams);
		this.printerService.print(games);
	}

	public static void main(String[] args) throws Exception {
		 SpringApplication app = new SpringApplication(Application.class);
		 app.setShowBanner(false);
		 app.run(args);		
	}
}
