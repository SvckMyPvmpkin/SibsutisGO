package com.sibsutisgo.dto;

public class ReviewRequestDTO {
    private Long id;
    private Integer rating;
    private String description;

    public ReviewRequestDTO(){}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Integer getRating() { return rating; }

    public void setRating(Integer rating) { this.rating = rating; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}
