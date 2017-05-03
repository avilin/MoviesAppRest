/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.services;

import es.avilin.moviesapp.entities.User;
import es.avilin.moviesapp.responses.AppResponse;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andresvicentelinares
 */
@Stateless
@Path("user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "MoviesAppRestPU")
    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User entity) {
        Response response;
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByUsername", User.class);
        List<User> users = query.setParameter("username", entity.getUsername()).getResultList();
        if (users.isEmpty()) {
            super.create(entity);
            response = Response.status(Response.Status.OK).entity(new AppResponse("OK", "", entity)).build();
        } else {
            response = Response.status(Response.Status.OK).entity(new AppResponse("ERROR", "Username already exists")).build();
        }
        return response;
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User entity) {
        Response response;
        String username = entity.getUsername();
        String password = entity.getPassword();
        try {
            User user = authenticate(username, password);
            response = Response.status(Response.Status.OK).entity(new AppResponse("OK", "", user)).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.OK).entity(new AppResponse("ERROR", ex.getMessage())).build();
        }

        return response;
    }

    private User authenticate(String username, String password) throws Exception {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByUsername", User.class);
        List<User> users = query.setParameter("username", username).getResultList();
        User user = null;
        if (users.isEmpty()) {
            throw new Exception("Incorrect username");
        } else {
            user = users.get(0);
            if (user.getPassword().compareTo(password) == 0) {
                user.setToken(issueToken(username));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 30);
                user.setExpiredDate(calendar.getTime());
                super.edit(user);
            } else {
                throw new Exception("Incorrect password");
            }
        }
        return user;
    }

    private String issueToken(String username) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
