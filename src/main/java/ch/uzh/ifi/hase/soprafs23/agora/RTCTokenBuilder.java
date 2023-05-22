package ch.uzh.ifi.hase.soprafs23.agora;

import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;

public class RTCTokenBuilder {

    private static String appId = "2d64cdbec0324225b28f83ed19f75397";
    private static String appCertificate = "8214500e1ebd4e10bcd411dd4df44395";
    private static int expirationTimeInSeconds = 7200;

    public String buildTokenWithUserAccount(String channelName, String account, VoiceChatRole role) {

        int privilegeTs = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        // Assign appropriate access privileges to each role.
        AccessToken builder = new AccessToken(appId, appCertificate, channelName, account);
        builder.addPrivilege(AccessToken.Privileges.kJoinChannel, privilegeTs);
        if (role == VoiceChatRole.Role_Publisher || role == VoiceChatRole.Role_Subscriber || role == VoiceChatRole.Role_Admin) {
            builder.addPrivilege(AccessToken.Privileges.kPublishAudioStream, privilegeTs);
            builder.addPrivilege(AccessToken.Privileges.kPublishVideoStream, privilegeTs);
        }
        try {
            return builder.build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
