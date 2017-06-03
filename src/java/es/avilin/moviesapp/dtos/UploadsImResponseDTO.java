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
public class UploadsImResponseDTO {
    
    private Integer status_code;
    private String status_txt;
    private UploadsImDataDTO data;

    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }

    public UploadsImDataDTO getData() {
        return data;
    }

    public void setData(UploadsImDataDTO data) {
        this.data = data;
    }
            
}
