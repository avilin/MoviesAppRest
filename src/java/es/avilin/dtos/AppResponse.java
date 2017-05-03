/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.avilin.moviesapp.responses;

/**
 *
 * @author andresvicentelinares
 */
public class AppResponse {
    
    private String status;
    private String message;
    private Object data;

    public AppResponse(String status) {
        this.status = status;
        this.message = "";
        this.data = null;
    }
    
    public AppResponse(String status, String errorMessage) {
        this.status = status;
        this.message = errorMessage;
        this.data = null;
    }
    
    public AppResponse(String status, String errorMessage, Object data) {
        this.status = status;
        this.message = errorMessage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}
