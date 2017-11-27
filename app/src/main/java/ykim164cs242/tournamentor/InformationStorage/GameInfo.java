package ykim164cs242.tournamentor.InformationStorage;

/**
 * UserInfo is a Model for users' profile data.
 * It contains githubID, name, avatarURL, numRepos,
 * numFollowers, numFollowing, bio, webstie, and createdDate
 * of the profile.
 */

public class GameInfo {

    private String ID;
    private String fieldName; //could be combined with competition name
    private String gameTime;
    private String gameDate;
    private String teamA;
    private String teamB;
    private String scoreA;
    private String scoreB;
    private boolean isLive;
    private String tournamentName;
    private PlayerInfo[] scorers;

    private String winner;


    public GameInfo(String ID, String fieldName, String gameTime, String gameDate, String teamA, String teamB, String scoreA, String scoreB, boolean isLive, String tournamentName) {
        this.ID = ID;
        this.fieldName = fieldName;
        this.gameTime = gameTime;
        this.gameDate = gameDate;
        this.teamA = teamA;
        this.teamB = teamB;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.isLive = isLive;
        this.tournamentName = tournamentName;
        this.winner = "draw"; //Default is draw
    }

    public GameInfo(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
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

    public String getScoreA() {
        return scoreA;
    }

    public void setScoreA(String scoreA) {
        this.scoreA = scoreA;
    }

    public String getScoreB() {
        return scoreB;
    }

    public void setScoreB(String scoreB) {
        this.scoreB = scoreB;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public PlayerInfo[] getScorers() {
        return scorers;
    }

    public void setScorers(PlayerInfo[] scorers) {
        this.scorers = scorers;
    }
}
