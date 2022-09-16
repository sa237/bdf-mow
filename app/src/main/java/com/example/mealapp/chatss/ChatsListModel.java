package com.example.mealapp.chatss;

public class ChatsListModel {
    String chatListId,lastMessage,member;

    public ChatsListModel() {
    }

    public ChatsListModel(String chatListId,String lastMessage, String member) {

        this.chatListId = chatListId;
        this.lastMessage = lastMessage;
        this.member = member;
    }


    public String getChatListId() {
        return chatListId;
    }

    public void setChatListId(String chatListId) {
        this.chatListId = chatListId;
    }



    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
