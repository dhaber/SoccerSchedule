package com.fawnanddoug.soccerschedule.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fawnanddoug.soccerschedule.domain.Game;
import com.fawnanddoug.soccerschedule.domain.Team;
import com.fawnanddoug.soccerschedule.service.SoccerScheduleService;

@Service
public class FoxSportsScheduleService implements SoccerScheduleService {

	private static final DateTimeFormatter DATE_FORMATTER_FROM_PAGE = DateTimeFormat.forPattern("EEEE, MMMM dd, yyyy h:mma");
	private static final DateTimeFormatter DATE_FORMATTER_FROM_URL = DateTimeFormat.forPattern("yyyy-MM-dd");

	private static Logger logger = LoggerFactory.getLogger(FoxSportsScheduleService.class);
		
	@Override
	public List<Game> getSchedule(int weeks) {
		logger.info("Getting schedule for {} weeks ",weeks);
		List<Game> games = new ArrayList<Game>();
		
		DateTime date = new DateTime();
		for (int i=0; i < weeks; i++) {
			date = date.plusWeeks(i);
			logger.info("Getting data for week {}. Start date: {} ", i+1, date);
			Document doc = getDocument(date);
			logger.info("Finding elements");
			Elements elements = doc.select("section.wisfb_scrollableContentX > div > div");
			
			logger.info("Generating games");
			games.addAll(generateGames(elements));
			
			logger.info("Done with week {}", i+1);
		}
		
		return games;
	}
	
	private List<Game> generateGames(Elements elements) {
		List<Game> games = new ArrayList<Game>();
		
		String day = "";
		String league = "";
		for (Element element : elements) {
			if (isDay(element) ) {
				day = element.ownText();
				
			} else if (isLeague(element)) {
				league = element.ownText();
				
			} else if (isPreGame(element)) {
				games.add(generateGames(day, league, element));
			}
		}
		
		return games;
	}

	private boolean isPreGame(Element element) {
		return element.classNames().contains("wisfb_preGame"); 
	}

	private Game generateGames(String day, String league, Element element) {
		Elements teams = element.select(".wisfb_teamInfo");
		if (teams.size() != 2) {
			throw new RuntimeException("Expected two teams in " + element);
		}
		
		Team home = generateTeam(teams.get(0));
		Team away = generateTeam(teams.get(1));
		
		String venue = generateVenue(element);
		String channel = generateChannel(element);
		DateTime startTime = generateStartTime(day, element);
		
		return new Game(home, away, venue, league, startTime, channel);
	}

	private String generateChannel(Element element) {
		Elements channels = element.select(".wisfb_secondary");
		
		if (channels.size() != 1) {
			throw new RuntimeException("Expected one channel in " + element);
		}

		return channels.get(0).text();
	}

	private DateTime generateStartTime(String day, Element element) {
		Elements times = element.select(".wisfb_primary");
		
		if (times.size() != 1) {
			throw new RuntimeException("Expected one time in " + element);
		}
		
		String time = times.get(0).text();
		if (!StringUtils.hasText(time) || "TBA".equals(time)) {
			time = "12:00a";
		}
		
		// remove the time zone
		time = time.replaceAll(" ET", "");
		
		// get format right
		time = time.replace("a", "AM");
		time = time.replace("p", "PM");
		
		return DATE_FORMATTER_FROM_PAGE.parseDateTime(day + " " + time);
	}

	private String generateVenue(Element element) {
		Elements venues = element.select(".wisfb_name");
		
		if (venues.size() != 1) {
			throw new RuntimeException("Expected one venue in " + element);
		}

		return venues.get(0).text();
	}

	private Team generateTeam(Element element) {
		Elements spans = element.select("span");
		if (spans.size() != 1 && spans.size() != 2 ) {
			throw new RuntimeException("Expected one or two spans in " + element);
		}
		
		String name = spans.get(0).text();
		String record = "";
		if (spans.size() == 2) {
			record = spans.get(1).text().replaceAll("[()]","");
		}
		
		Elements imgs = element.select("img");
		if (imgs.size() != 1) {
			throw new RuntimeException("Expected one img in " + element);
		}
		
		String logoUrl = imgs.get(0).attr("src");
		
		return new Team(name, record, logoUrl);
	}

	private boolean isLeague(Element element) {
		return element.classNames().contains("wisfb_scheduleGroupName");
	}

	private boolean isDay(Element element) {
		return element.classNames().contains("wisfb_scheduleGroupTitle");
	}

	private Document getDocument(DateTime date) {
		try {
			String dateString = DATE_FORMATTER_FROM_URL.print(date);
			return Jsoup.connect("http://www.foxsports.com/soccer/schedule?competition=0&season=0&date=" + dateString).timeout(0).get();
		} catch (IOException e) {
			throw new RuntimeException("Could not get document", e);
		}
		
	}

	@Override
	public List<Game> filterByTeams(List<Game> games, List<String> teams) {
		logger.info("Filtering {} games by {} teams: {}", games.size(), teams.size());
		List<Game> matches = new ArrayList<Game>();
		for (Game game : games) {
			if (matches(game, teams)) {
				matches.add(game);
			}
		}
		logger.info("Done filtering");
		return matches;
	}

	private boolean matches(Game game, List<String> teams) {
		return matches(game.getAway(), teams) || matches(game.getHome(), teams);
	}

	private boolean matches(Team team, List<String> teams) {
		String name = team.getName();
		for (String teamName : teams) {
			if (teamName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

}
