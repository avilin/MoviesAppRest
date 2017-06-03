/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.services;

import es.avilin.moviesapp.dtos.MovieResponseDTO;
import es.avilin.moviesapp.dtos.ResponseDTO;
import es.avilin.moviesapp.dtos.UploadImageDTO;
import es.avilin.moviesapp.dtos.UploadsImResponseDTO;
import es.avilin.moviesapp.entities.Movie;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andresvicentelinares
 */
@Stateless
@Path("movie")
public class MovieFacadeREST extends AbstractFacade<Movie> {

    @PersistenceContext(unitName = "MoviesAppRestPU")
    private EntityManager entityManager;

    public MovieFacadeREST() {
        super(Movie.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie entity) {
        super.create(entity);
        Response response = Response.status(Response.Status.OK).entity(new ResponseDTO("OK", "", entity.getId())).build();
        return response;
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, Movie entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public Response removeMovie(@PathParam("id") Integer id) {
        super.remove(super.find(id));
        Response response = Response.status(Response.Status.OK).entity(new ResponseDTO("OK", "")).build();
        return response;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Movie find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllMovies() {
        TypedQuery<MovieResponseDTO> query = entityManager.createQuery("SELECT new es.avilin.moviesapp.dtos.MovieResponseDTO(m.id, m.name, m.synopsis, m.movieLength, m.releaseDate, m.genre, m.imageUrl, m.thumbnailImageUrl, m.author.id) FROM Movie m", MovieResponseDTO.class);
        List<MovieResponseDTO> movies = query.getResultList();
        Response response = Response.status(Response.Status.OK).entity(new ResponseDTO("OK", "", movies)).build();
        return response;
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @POST
    @Path("uploadImageURL")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadImageURL(UploadImageDTO uploadImageDTO) {
        Response response;
        if (uploadImageDTO == null) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "No data received")).build();
            return response;
        }
        Integer id = uploadImageDTO.getId();
        String imageURL = uploadImageDTO.getImageURL();
        if (id == null || imageURL == null || imageURL.isEmpty()) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "You need to send an image URL for the movie")).build();
            return response;
        }
        try {
            TypedQuery<Movie> query = entityManager.createNamedQuery("Movie.findById", Movie.class);
            Movie movie = query.setParameter("id", id).getSingleResult();
            if (movie == null) {
                throw new Exception("Movie doesn't exist");
            } else {
                UploadsImResponseDTO clientResponse = ClientBuilder.newClient().target("http://uploads.im/api").path("upload=" + imageURL).request(MediaType.APPLICATION_JSON).get(UploadsImResponseDTO.class);
                movie.setImageUrl(clientResponse.getData().getImg_url());
                movie.setThumbnailImageUrl(clientResponse.getData().getThumb_url());
                super.edit(movie);
            }
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("OK", "")).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", ex.getMessage())).build();
        }

        return response;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
