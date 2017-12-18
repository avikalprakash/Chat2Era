package com.example.lue.erachat.Activity.Models;

/**
 * Created by lue on 17-06-2017.
 */

public class Messages {


    String tick;
    public boolean left;
    private String KeyId;
    private boolean isSelected;
    private String messageTime;
    private String messageText;
    private String messageId;
    private String messageType;
    private String messageSenderId;
    private String messageReciverId;
    private String messageInTime;
    private String messageCreationDate;
    private String Chatroom_Message_Text;
    private String Chatroom_Message_Image;
    private String Chatroom_Message_Video;
    private String Chatroom_Message_Voice;
    private String Chatroom_Message_ContactInfo_Image;
    private String Chatroom_Message_ContactOnfo_Number;
    private String Chatroom_Message_ContactInfo_Id;
    private String Chatroom_Message_id;
    private String Chatroom_Message_Senderid;
    private String ChatroomMessage_Reciverid;
    private String Chatroom_message_inTime;
    private String Chatroom_message_Type;
    private String Chatroom_CreationDate;
    private String Chatroom_Latitude;
    private String Chatroom_Longitude;



    private String Chatroom_FileName;
    private String ChatroomImagePath;
   private String  Sender_Name;
    private String Sender_chat_image;



    private String SenderMobile;
    private String GroupId;
    private String GroupMessageId;
    private String GroupName;
    private String GroupIcon;
    private String GroupCreatedDate;
    private String GroupMemberId;
    private String GroupMemberName;
    private String GroupMemberNumber;
    private String GroupMemberPhoto;
    private String GroupMemberStatus;
    private String GroupAdmin;
    private String GroupMessageSenderId;
    private String GroupMessageSendingTime;
    private String GroupMessageType;
    private String GroupMessageText;
    private String GroupMessageImage;
    private String InvitationId;
    private String InvitationSenderImage;
    private String InvitationSenderName;
    private String InvitationSenderId;
    private String InvitationReciverId;
    private String InvitationStatus;
    private String InvitationSenderMobile;
    private String UserId;
    private String UserName;
    private String UserMobile;
    private String UserStatus;
    private String UserImage;
    private String CheckContact;

   /* public String getBlueTick() {
        return blueTick;
    }

    public void setBlueTick(String blueTick) {
        this.blueTick = blueTick;
    }*/

    private String blueTick;

    private String GroupIdChat;
    private String GroupMessageChatId;
    private String GroupChatMessageType;
    private String GroupChatMessageText;
    private String GroupChatMessageSenderId;
    private String GroupChatMessageSenderName;
    private String GroupChatMessageIncomingTime;
    private String GroupChatMessageImage;
    private String GroupChatMessageVoice;
    private String GroupChatMessageContactInfo_Id;
    private String GroupChatMessageContactInfo_Image;
    private String GroupChatMessageContactInfo_Number;
    private String GroupChatMessageVideo;
    private String GroupChatMessageLattitude;
    private String GroupChatMessageLongitude;
    private String BlockedContactUserid;
    private String BlockedContactUserName;
    private String BlockedContactUserPhoto;
    private String BlockedContactUserMobile;
         public Messages(){

         }
    public Messages(String messageId,String GroupId,String GroupName,String GroupImage,String SenderId,String Messagetype,String MessageText,String MessageTime) {
      this.GroupMessageId=messageId;
        this.GroupId=GroupId;
        this.GroupName=GroupName;
        this.GroupIcon=GroupImage;
        this.GroupMessageSenderId=SenderId;
        this.GroupMessageType=Messagetype;
        this.GroupMessageText=MessageText;
        this.GroupMessageSendingTime=MessageTime;

    }
    public Messages(String GroupChatId,String GroupChatMessageId,String GroupChatMessageSenderId,String GroupChatMessageSendername,String GroupchatMessageIntime,String GCMessageType,String GCMessageText,String GCMessageImage,String GCMessageVoice,String GCMessageVideo,String GCMessageLat,String GCMessageLong,String GCMessageContInfoId,String GCMessageContInfoNumber,String GCMessageContInfoImage){
       this.GroupIdChat=GroupChatId;
        this.GroupMessageChatId=GroupChatMessageId;
        this.GroupChatMessageSenderId=GroupChatMessageSenderId;
        this.GroupChatMessageSenderName=GroupChatMessageSendername;
        this.GroupChatMessageIncomingTime=GroupchatMessageIntime;
        this.GroupChatMessageType=GCMessageType;
        this.GroupChatMessageText=GCMessageText;
        this.GroupChatMessageImage=GCMessageImage;
        this.GroupChatMessageVoice=GCMessageVoice;
        this.GroupChatMessageVideo=GCMessageVideo;
        this.GroupChatMessageLattitude=GCMessageLat;
        this.GroupChatMessageLongitude=GCMessageLong;
        this.GroupChatMessageContactInfo_Id=GCMessageContInfoId;
        this.GroupChatMessageContactInfo_Number=GCMessageContInfoNumber;
        this.GroupChatMessageContactInfo_Image=GCMessageContInfoImage;
    }

    public Messages( String senderId, String reciverid, String messageType, String messageText, String messageInTime,String senderName,String SenderImage) {

        this.messageSenderId=senderId;
        this.messageReciverId=reciverid;
        this.messageType=messageType;
        this.messageText=messageText;
        this.messageInTime=messageInTime;
        this.Sender_Name=senderName;
        this.Sender_chat_image=SenderImage;
        this.SenderMobile=SenderMobile;
    }
    public Messages(String messageId, String senderId, String reciverid, String messageText,String messageImage,String messageVideo,String messageVoice,String messageContactInfoImage,String messageContactInfoNumber,String messageContactInfoId, String messageInTime,String messageType, String messageCreationdate,String latitude,String longitude,String id, String files) {
        this.Chatroom_Message_id=messageId;
        this.Chatroom_Message_Senderid=senderId;
        this.ChatroomMessage_Reciverid=reciverid;
        this.Chatroom_Message_Text=messageText;
        this.Chatroom_Message_Image=messageImage;
        this.Chatroom_Message_Video=messageVideo;
        this.Chatroom_Message_Voice=messageVoice;
        this.Chatroom_Message_ContactInfo_Image=messageContactInfoImage;
        this.Chatroom_Message_ContactOnfo_Number=messageContactInfoNumber;
        this.Chatroom_Message_ContactInfo_Id=messageContactInfoId;
        this.Chatroom_message_inTime=messageInTime;
        this.Chatroom_message_Type=messageType;
        this.Chatroom_CreationDate=messageCreationdate;
        this.Chatroom_Latitude=latitude;
        this.Chatroom_Longitude=longitude;
        this.Chatroom_FileName=files;

    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageSenderId() {
        return messageSenderId;
    }

    public void setMessageSenderId(String messageSenderId) {
        this.messageSenderId = messageSenderId;
    }

    public String getMessageReciverId() {
        return messageReciverId;
    }

    public void setMessageReciverId(String messageReciverId) {
        this.messageReciverId = messageReciverId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageInTime() {
        return messageInTime;
    }

    public void setMessageInTime(String messageInTime) {
        this.messageInTime = messageInTime;
    }

    public String getMessageCreationDate() {
        return messageCreationDate;
    }

    public void setMessageCreationDate(String messageCreationDate) {
        this.messageCreationDate = messageCreationDate;
    }

    public String getChatroom_Message_Text() {
        return Chatroom_Message_Text;
    }

    public void setChatroom_Message_Text(String chatroom_Message_Text) {
        Chatroom_Message_Text = chatroom_Message_Text;
    }

    public String getChatroom_Message_id() {
        return Chatroom_Message_id;
    }

    public void setChatroom_Message_id(String chatroom_Message_id) {
        Chatroom_Message_id = chatroom_Message_id;
    }

    public String getChatroom_Message_Senderid() {
        return Chatroom_Message_Senderid;
    }

    public void setChatroom_Message_Senderid(String chatroom_Message_Senderid) {
        Chatroom_Message_Senderid = chatroom_Message_Senderid;
    }

    public String getChatroomMessage_Reciverid() {
        return ChatroomMessage_Reciverid;
    }

    public void setChatroomMessage_Reciverid(String chatroomMessage_Reciverid) {
        ChatroomMessage_Reciverid = chatroomMessage_Reciverid;
    }

    public String getChatroom_message_inTime() {
        return Chatroom_message_inTime;
    }

    public void setChatroom_message_inTime(String chatroom_message_inTime) {
        Chatroom_message_inTime = chatroom_message_inTime;
    }

    public String getChatroom_message_Type() {
        return Chatroom_message_Type;
    }

    public void setChatroom_message_Type(String chatroom_message_Type) {
        Chatroom_message_Type = chatroom_message_Type;
    }

    public String getChatroom_CreationDate() {
        return Chatroom_CreationDate;
    }

    public void setChatroom_CreationDate(String chatroom_CreationDate) {
        Chatroom_CreationDate = chatroom_CreationDate;
    }

    public String getSender_Name() {
        return Sender_Name;
    }

    public void setSender_Name(String sender_Name) {
        Sender_Name = sender_Name;
    }

    public String getChatroom_Message_Image() {
        return Chatroom_Message_Image;
    }

    public void setChatroom_Message_Image(String chatroom_Message_Image) {
        Chatroom_Message_Image = chatroom_Message_Image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }



    public String getSender_chat_image() {
        return Sender_chat_image;
    }

    public void setSender_chat_image(String sender_chat_image) {
        Sender_chat_image = sender_chat_image;
    }

    public String getSenderMobile() {
        return SenderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        SenderMobile = senderMobile;
    }

    public String getChatroom_Message_Video() {
        return Chatroom_Message_Video;
    }

    public void setChatroom_Message_Video(String chatroom_Message_Video) {
        Chatroom_Message_Video = chatroom_Message_Video;
    }

    public String getChatroom_Message_Voice() {
        return Chatroom_Message_Voice;
    }

    public void setChatroom_Message_Voice(String chatroom_Message_Voice) {
        Chatroom_Message_Voice = chatroom_Message_Voice;
    }

    public String getChatroom_Message_ContactInfo_Image() {
        return Chatroom_Message_ContactInfo_Image;
    }

    public void setChatroom_Message_ContactInfo_Image(String chatroom_Message_ContactInfo_Image) {
        Chatroom_Message_ContactInfo_Image = chatroom_Message_ContactInfo_Image;
    }

    public String getChatroom_Message_ContactOnfo_Number() {
        return Chatroom_Message_ContactOnfo_Number;
    }

    public void setChatroom_Message_ContactOnfo_Number(String chatroom_Message_ContactOnfo_Number) {
        Chatroom_Message_ContactOnfo_Number = chatroom_Message_ContactOnfo_Number;
    }

    public String getChatroom_Message_ContactInfo_Id() {
        return Chatroom_Message_ContactInfo_Id;
    }

    public void setChatroom_Message_ContactInfo_Id(String chatroom_Message_ContactInfo_Id) {
        Chatroom_Message_ContactInfo_Id = chatroom_Message_ContactInfo_Id;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupIcon() {
        return GroupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        GroupIcon = groupIcon;
    }

    public String getGroupCreatedDate() {
        return GroupCreatedDate;
    }

    public void setGroupCreatedDate(String groupCreatedDate) {
        GroupCreatedDate = groupCreatedDate;
    }

    public String getGroupMemberId() {
        return GroupMemberId;
    }

    public void setGroupMemberId(String groupMemberId) {
        GroupMemberId = groupMemberId;
    }

    public String getGroupMemberName() {
        return GroupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        GroupMemberName = groupMemberName;
    }

    public String getGroupMemberNumber() {
        return GroupMemberNumber;
    }

    public void setGroupMemberNumber(String groupMemberNumber) {
        GroupMemberNumber = groupMemberNumber;
    }

    public String getGroupMemberPhoto() {
        return GroupMemberPhoto;
    }

    public void setGroupMemberPhoto(String groupMemberPhoto) {
        GroupMemberPhoto = groupMemberPhoto;
    }

    public String getGroupMemberStatus() {
        return GroupMemberStatus;
    }

    public void setGroupMemberStatus(String groupMemberStatus) {
        GroupMemberStatus = groupMemberStatus;
    }

    public String getGroupAdmin() {
        return GroupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        GroupAdmin = groupAdmin;
    }

    public String getInvitationId() {
        return InvitationId;
    }

    public void setInvitationId(String invitationId) {
        InvitationId = invitationId;
    }


    public String getInvitationReciverId() {
        return InvitationReciverId;
    }

    public void setInvitationReciverId(String invitationReciverId) {
        InvitationReciverId = invitationReciverId;
    }

    public String getInvitationSenderId() {
        return InvitationSenderId;
    }

    public void setInvitationSenderId(String invitationSenderId) {
        InvitationSenderId = invitationSenderId;
    }

    public String getInvitationStatus() {
        return InvitationStatus;
    }

    public void setInvitationStatus(String invitationStatus) {
        InvitationStatus = invitationStatus;
    }

    public String getInvitationSenderImage() {
        return InvitationSenderImage;
    }

    public void setInvitationSenderImage(String invitationSenderImage) {
        InvitationSenderImage = invitationSenderImage;
    }

    public String getInvitationSenderName() {
        return InvitationSenderName;
    }

    public void setInvitationSenderName(String invitationSenderName) {
        InvitationSenderName = invitationSenderName;
    }

    public String getInvitationSenderMobile() {
        return InvitationSenderMobile;
    }

    public void setInvitationSenderMobile(String invitationSenderMobile) {
        InvitationSenderMobile = invitationSenderMobile;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getCheckContact() {
        return CheckContact;
    }

    public void setCheckContact(String checkContact) {
        CheckContact = checkContact;
    }

    public String getKeyId() {
        return KeyId;
    }

    public void setKeyId(String keyId) {
        KeyId = keyId;
    }

    public String getGroupMessageSenderId() {
        return GroupMessageSenderId;
    }

    public void setGroupMessageSenderId(String groupMessageSenderId) {
        GroupMessageSenderId = groupMessageSenderId;
    }

    public String getGroupMessageSendingTime() {
        return GroupMessageSendingTime;
    }

    public void setGroupMessageSendingTime(String groupMessageSendingTime) {
        GroupMessageSendingTime = groupMessageSendingTime;
    }

    public String getGroupMessageType() {
        return GroupMessageType;
    }

    public void setGroupMessageType(String groupMessageType) {
        GroupMessageType = groupMessageType;
    }

    public String getGroupMessageText() {
        return GroupMessageText;
    }

    public void setGroupMessageText(String groupMessageText) {
        GroupMessageText = groupMessageText;
    }

    public String getGroupMessageImage() {
        return GroupMessageImage;
    }

    public void setGroupMessageImage(String groupMessageImage) {
        GroupMessageImage = groupMessageImage;
    }

    public String getGroupMessageId() {
        return GroupMessageId;
    }

    public void setGroupMessageId(String groupMessageId) {
        GroupMessageId = groupMessageId;
    }


    public String getGroupMessageChatId() {
        return GroupMessageChatId;
    }

    public void setGroupMessageChatId(String groupMessageChatId) {
        GroupMessageChatId = groupMessageChatId;
    }

    public String getGroupChatMessageType() {
        return GroupChatMessageType;
    }

    public void setGroupChatMessageType(String groupChatMessageType) {
        GroupChatMessageType = groupChatMessageType;
    }

    public String getGroupChatMessageText() {
        return GroupChatMessageText;
    }

    public void setGroupChatMessageText(String groupChatMessageText) {
        GroupChatMessageText = groupChatMessageText;
    }

    public String getGroupChatMessageIncomingTime() {
        return GroupChatMessageIncomingTime;
    }

    public void setGroupChatMessageIncomingTime(String groupChatMessageIncomingTime) {
        GroupChatMessageIncomingTime = groupChatMessageIncomingTime;
    }

    public String getGroupChatMessageImage() {
        return GroupChatMessageImage;
    }

    public void setGroupChatMessageImage(String groupChatMessageImage) {
        GroupChatMessageImage = groupChatMessageImage;
    }

    public String getGroupChatMessageVoice() {
        return GroupChatMessageVoice;
    }

    public void setGroupChatMessageVoice(String groupChatMessageVoice) {
        GroupChatMessageVoice = groupChatMessageVoice;
    }

    public String getGroupChatMessageContactInfo_Id() {
        return GroupChatMessageContactInfo_Id;
    }

    public void setGroupChatMessageContactInfo_Id(String groupChatMessageContactInfo_Id) {
        GroupChatMessageContactInfo_Id = groupChatMessageContactInfo_Id;
    }

    public String getGroupChatMessageContactInfo_Image() {
        return GroupChatMessageContactInfo_Image;
    }

    public void setGroupChatMessageContactInfo_Image(String groupChatMessageContactInfo_Image) {
        GroupChatMessageContactInfo_Image = groupChatMessageContactInfo_Image;
    }



    public String getGroupChatMessageVideo() {
        return GroupChatMessageVideo;
    }

    public void setGroupChatMessageVideo(String groupChatMessageVideo) {
        GroupChatMessageVideo = groupChatMessageVideo;
    }

    public String getGroupChatMessageLattitude() {
        return GroupChatMessageLattitude;
    }

    public void setGroupChatMessageLattitude(String groupChatMessageLattitude) {
        GroupChatMessageLattitude = groupChatMessageLattitude;
    }

    public String getGroupChatMessageLongitude() {
        return GroupChatMessageLongitude;
    }

    public void setGroupChatMessageLongitude(String groupChatMessageLongitude) {
        GroupChatMessageLongitude = groupChatMessageLongitude;
    }

    public String getGroupChatMessageContactInfo_Number() {
        return GroupChatMessageContactInfo_Number;
    }

    public void setGroupChatMessageContactInfo_Number(String groupChatMessageContactInfo_Number) {
        GroupChatMessageContactInfo_Number = groupChatMessageContactInfo_Number;
    }

    public String getGroupChatMessageSenderId() {
        return GroupChatMessageSenderId;
    }

    public void setGroupChatMessageSenderId(String groupChatMessageSenderId) {
        GroupChatMessageSenderId = groupChatMessageSenderId;
    }

    public String getGroupIdChat() {
        return GroupIdChat;
    }

    public void setGroupIdChat(String groupIdChat) {
        GroupIdChat = groupIdChat;
    }

    public String getGroupChatMessageSenderName() {
        return GroupChatMessageSenderName;
    }

    public void setGroupChatMessageSenderName(String groupChatMessageSenderName) {
        GroupChatMessageSenderName = groupChatMessageSenderName;
    }

    public String getBlockedContactUserid() {
        return BlockedContactUserid;
    }

    public void setBlockedContactUserid(String blockedContactUserid) {
        BlockedContactUserid = blockedContactUserid;
    }

    public String getBlockedContactUserName() {
        return BlockedContactUserName;
    }

    public void setBlockedContactUserName(String blockedContactUserName) {
        BlockedContactUserName = blockedContactUserName;
    }

    public String getBlockedContactUserPhoto() {
        return BlockedContactUserPhoto;
    }

    public void setBlockedContactUserPhoto(String blockedContactUserPhoto) {
        BlockedContactUserPhoto = blockedContactUserPhoto;
    }

    public String getBlockedContactUserMobile() {
        return BlockedContactUserMobile;
    }

    public void setBlockedContactUserMobile(String blockedContactUserMobile) {
        BlockedContactUserMobile = blockedContactUserMobile;
    }

    public String getChatroom_Latitude() {
        return Chatroom_Latitude;
    }

    public void setChatroom_Latitude(String chatroom_Latitude) {
        Chatroom_Latitude = chatroom_Latitude;
    }

    public String getChatroom_Longitude() {
        return Chatroom_Longitude;
    }

    public void setChatroom_Longitude(String chatroom_Longitude) {
        Chatroom_Longitude = chatroom_Longitude;
    }

    public String getChatroomImagePath() {
        return ChatroomImagePath;
    }

    public void setChatroomImagePath(String chatroomImagePath) {
        ChatroomImagePath = chatroomImagePath;
    }

    public String getChatroom_FileName() {
        return Chatroom_FileName;
    }

    public void setChatroom_FileName(String chatroom_FileName) {
        Chatroom_FileName = chatroom_FileName;
    }

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
        this.tick = tick;
    }

}
