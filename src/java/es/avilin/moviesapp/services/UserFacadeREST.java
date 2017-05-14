/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.services;

import es.avilin.moviesapp.dtos.LoginDTO;
import es.avilin.moviesapp.dtos.RegisterDTO;
import es.avilin.moviesapp.entities.User;
import es.avilin.moviesapp.dtos.ResponseDTO;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author andresvicentelinares
 */
@Stateless
@Path("user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "MoviesAppRestPU")
    private EntityManager entityManager;

    public UserFacadeREST() {
        super(User.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterDTO registerDTO) {
        Response response;
        if (registerDTO == null) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "No data received")).build();
            return response;
        }
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "We need a username and a password to register you")).build();
            return response;
        }

        TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsername", User.class);
        List<User> users = query.setParameter("username", registerDTO.getUsername()).getResultList();
        if (users.isEmpty()) {
            try {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setToken(issueToken(username, password));
                user.setExpiredDate(expiredDate());
                super.create(user);
                response = Response.status(Response.Status.OK).entity(new ResponseDTO("OK", "", user)).build();
            } catch (Exception ex) {
                response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "The application has encountered an unknown error.")).build();
            }
        } else {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "Username already exists")).build();
        }
        return response;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {
        Response response;
        if (loginDTO == null) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "No data received")).build();
            return response;
        }
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", "You need a username and a password to log in")).build();
            return response;
        }
        try {
            User user = authenticate(username, password);
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("OK", "", user)).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.OK).entity(new ResponseDTO("ERROR", ex.getMessage())).build();
        }

        return response;
    }

    private User authenticate(String username, String password) throws Exception {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsername", User.class);
        List<User> users = query.setParameter("username", username).getResultList();
        User user = null;
        if (users.isEmpty()) {
            throw new Exception("Incorrect username");
        } else {
            user = users.get(0);
            if (user.getPassword().compareTo(password) == 0) {
                user.setToken(issueToken(username, password));
                user.setExpiredDate(expiredDate());
                super.edit(user);
            } else {
                throw new Exception("Incorrect password");
            }
        }
        return user;
    }

    private String issueToken(String username, String password) throws Exception {
        String token = username + ":" + password;
        try {
            return DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new Exception("Cannot encode with UTF-8");
        }
    }

    private Date expiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

}
