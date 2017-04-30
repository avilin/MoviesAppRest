/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.services;

/**
 *
 * @author andresvicentelinares
 */
public class Response {
    
    private String status;
    private String errorMessage;
    private Object data;

    public Response(String status, String errorMessage, Object data) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
