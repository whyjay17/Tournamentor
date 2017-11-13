package ykim164cs242.tournamentor.ListItem;

/**
 * MatchListItem class represents a component of the ListView in the Math List Page.
 * It contains the id (or index), fieldName, game time, live status (boolean), team names,
 * and scores for each team.
 */
public class LeagueTableItem {

    private int id;
    private String fieldName;
    private String gameTime; // Live-time of the game
    private String gameDate; // Date of the game
    private String teamA;
    private String teamB;
    private int scoreA;
    private int scoreB;
    private boolean isLive;
    private boolean isStarred;

    public LeagueTableItem(int id, String fieldName, String gameTime, String gameDate, String teamA, String teamB, int scoreA, int scoreB, boolean isLive, boolean isStarred) {
        this.id = id;
        this.fieldName = fieldName;
        this.gameTime = gameTime;
        this.gameDate = gameDate;
        this.teamA = teamA;
        this.teamB = teamB;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.isLive = isLive;
        this.isStarred = isStarred;
    }

    // Constructor


    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }
}
