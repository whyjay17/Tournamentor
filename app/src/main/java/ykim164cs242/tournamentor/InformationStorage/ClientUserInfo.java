package ykim164cs242.tournamentor.InformationStorage;

import java.util.List;

/**
 * UserInfo is a Model for users' profile data.
 * It contains githubID, name, avatarURL, numRepos,
 * numFollowers, numFollowing, bio, webstie, and createdDate
 * of the profile.
 */

public class ClientUserInfo {

    private String deviceID;
    private List<GameInfo> starredGames;

    public ClientUserInfo(String deviceID, List<GameInfo> starredGames) {
        this.deviceID = deviceID;
        this.starredGames = starredGames;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public List<GameInfo> getStarredGames() {
        return starredGames;
    }

    public void setStarredGames(List<GameInfo> starredGames) {
        this.starredGames = starredGames;
    }
}
