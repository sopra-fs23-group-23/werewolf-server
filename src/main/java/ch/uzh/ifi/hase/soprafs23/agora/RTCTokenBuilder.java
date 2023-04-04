package ch.uzh.ifi.hase.soprafs23.agora;

import ch.uzh.ifi.hase.soprafs23.constant.VoiceChatRole;

public class RTCTokenBuilder {



    public String buildTokenFromUid(String appId, String appCertificate, String channelName, int uid, VoiceChatRole role, int privilegeTs){
        String account = uid == 0 ? "" : String.valueOf(uid);
        return buildTokenWithUserAccount(appId, appCertificate, channelName,
                account, role, privilegeTs);
    }
    public String buildTokenWithUserAccount(String appId, String appCertificate, String channelName, String account, VoiceChatRole role, int privilegeTs) {

        // Assign appropriate access privileges to each role.
        AccessToken builder = new AccessToken(appId, appCertificate, channelName, account);
        builder.addPrivilege(AccessToken.Privileges.kJoinChannel, privilegeTs);
        if (role == VoiceChatRole.Role_Publisher || role == VoiceChatRole.Role_Subscriber || role == VoiceChatRole.Role_Admin) {
            builder.addPrivilege(AccessToken.Privileges.kPublishAudioStream, privilegeTs);
            // not needed
            /*
            builder.addPrivilege(AccessToken.Privileges.kPublishVideoStream, privilegeTs);
            builder.addPrivilege(AccessToken.Privileges.kPublishDataStream, privilegeTs);
             */
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
