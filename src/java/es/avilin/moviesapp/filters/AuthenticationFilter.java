/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.filters;

import es.avilin.moviesapp.entities.User;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andresvicentelinares
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @PersistenceContext(unitName = "MoviesAppRestPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (token == null) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        try {
            validateToken(token);
        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) throws Exception {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByToken", User.class);
        List<User> users = query.setParameter("token", token).getResultList();
        if (users.isEmpty() || users.get(0).getExpiredDate() == null 
                || users.get(0).getExpiredDate().before(Calendar.getInstance().getTime())) {
            throw new Exception();
        }
    }

}
