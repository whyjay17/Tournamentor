package ykim164cs242.tournamentor.ListItem;

/**
 * MatchListItem class represents a component of the ListView in the Math List Page.
 * It contains the id (or index), fieldName, game time, live status (boolean), team names,
 * and scores for each team.
 */
public class TeamListItem {

    private int id;
    private String teamName;
    private String foundationYear;
    private String captainName;

    public TeamListItem(int id, String teamName, String foundationYear, String captainName) {
        this.id = id;
        this.teamName = teamName;
        this.foundationYear = foundationYear;
        this.captainName = captainName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(String foundationYear) {
        this.foundationYear = foundationYear;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }
}
