/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.dtos;

/**
 *
 * @author andresvicentelinares
 */
public class MovieDTO {

    private Integer id;
    private String name;
    private String synopsis;
    private Integer movieLength;
    private Integer releaseDate;
    private String genre;
    private String imageURL;
    private String thumbnailImageURL;
    private Integer author;

    public MovieDTO() {

    }

    public MovieDTO(Integer id, String name, String synopsis, Integer movieLength, Integer releaseDate, String genre, String imageURL, String thumbnailImageURL, Integer author) {
        this.id = id;
        this.name = name;
        this.synopsis = synopsis;
        this.movieLength = movieLength;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.imageURL = imageURL;
        this.thumbnailImageURL = thumbnailImageURL;
        this.author = author;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getThumbnailImageURL() {
        return thumbnailImageURL;
    }

    public void setThumbnailImageURL(String thumbnailImageURL) {
        this.thumbnailImageURL = thumbnailImageURL;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

}
