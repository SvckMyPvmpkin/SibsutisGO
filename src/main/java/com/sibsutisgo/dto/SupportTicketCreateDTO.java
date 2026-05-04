package com.sibsutisgo.dto;

public class SupportTicketCreateDTO {
    private String message;

    public SupportTicketCreateDTO() {}

    public SupportTicketCreateDTO (String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
