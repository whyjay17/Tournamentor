package ykim164cs242.tournamentor.InformationStorage;

/**
 * UserInfo is a Model for users' profile data.
 * It contains githubID, name, avatarURL, numRepos,
 * numFollowers, numFollowing, bio, webstie, and createdDate
 * of the profile.
 */

public class PlayerInfo {

    private String ID;
    private String playerName;
    private String teamName;
    private int goals;

    public PlayerInfo(String ID, String playerName, String teamName, int goals) {
        this.ID = ID;
        this.playerName = playerName;
        this.teamName = teamName;
        this.goals = goals;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }
}
