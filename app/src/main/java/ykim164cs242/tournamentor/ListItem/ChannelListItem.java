package ykim164cs242.tournamentor.ListItem;

/**
 * MatchListItem class represents a component of the ListView in the Math List Page.
 * It contains the id (or index), fieldName, game time, live status (boolean), team names,
 * and scores for each team.
 */
public class ChannelListItem {

    private int id;
    private String channelName;
    private String competitionTerm;
    private String hostOrganization;


    public ChannelListItem(int id, String channelName, String competitionTerm, String hostOrganization) {
        this.id = id;
        this.channelName = channelName;
        this.competitionTerm = competitionTerm;
        this.hostOrganization = hostOrganization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCompetitionTerm() {
        return competitionTerm;
    }

    public void setCompetitionTerm(String competitionTerm) {
        this.competitionTerm = competitionTerm;
    }

    public String getHostOrganization() {
        return hostOrganization;
    }

    public void setHostOrganization(String hostOrganization) {
        this.hostOrganization = hostOrganization;
    }
}
