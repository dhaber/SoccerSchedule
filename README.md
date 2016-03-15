# SoccerSchedule

Displays a list of upcoming soccer games for teams that I'm interested in (Bayern Munich, Barcelona, Manchester United, etc)

##Build
1. Install Maven
2. mvn clean package

##Run
java -jar target/soccerschedule-0.0.1-SNAPSHOT.jar --weeks=2 --teams="barcelona,manchester united,argentina"

##Options
 --weeks - number of weeks to get data for - default: 1  
 --teams - the names of the teams to search for split by comma - default: `Bayern Munich,Barcelona,Manchester United,United States,Germany,England,Argentina,Brazil,Spain,Netherlands,France,Belgium,Italy`
