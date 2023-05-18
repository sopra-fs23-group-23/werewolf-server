package ch.uzh.ifi.hase.soprafs23.agora;

import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;

public class RTCTokenBuilder {

    private static String appId = "348d6a205d75436e916896366c5e315c";
    private static String appCertificate = "2e1e585ed3f74218ae249f7d14656fe2";
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
