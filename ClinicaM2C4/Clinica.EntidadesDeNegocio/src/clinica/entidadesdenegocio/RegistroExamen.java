/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.entidadesdenegocio;

/**
 *
 * @author Javier Rivera
 */
public class RegistroExamen {
    
    private int idexamen;
    private int idpaciente_fore;
    private String examen;
    private String Observacion;
    private int Top_Aux;
    private RegistroPaciente registropaciente;

    public RegistroExamen() {
    }

    public RegistroExamen(int idexamen, int idpaciente_fore, String examen, String Observacion) {
        this.idexamen = idexamen;
        this.idpaciente_fore = idpaciente_fore;
        this.examen = examen;
        this.Observacion = Observacion;
    }

    public int getIdexamen() {
        return idexamen;
    }

    public void setIdexamen(int idexamen) {
        this.idexamen = idexamen;
    }

    public int getIdpaciente_fore() {
        return idpaciente_fore;
    }

    public void setIdpaciente_fore(int idpaciente_fore) {
        this.idpaciente_fore = idpaciente_fore;
    }

    public String getExamen() {
        return examen;
    }

    public void setExamen(String examen) {
        this.examen = examen;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String Observacion) {
        this.Observacion = Observacion;
    }

    public int getTop_Aux() {
        return Top_Aux;
    }

    public void setTop_Aux(int Top_Aux) {
        this.Top_Aux = Top_Aux;
    }

    public RegistroPaciente getRegistropaciente() {
        return registropaciente;
    }

    public void setRegistropaciente(RegistroPaciente registropaciente) {
        this.registropaciente = registropaciente;
    }

   
    
    
    
    
}
