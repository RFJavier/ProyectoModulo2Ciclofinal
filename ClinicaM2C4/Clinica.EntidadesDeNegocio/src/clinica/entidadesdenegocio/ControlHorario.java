/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.entidadesdenegocio;

/**
 *
 * @author Javier Rivera
 */
public class ControlHorario {
    
    private int idhorario;
    private int idpaciente_fore;
    private int idexamen_fore;
    private String Nombre;
    private String Horacita;
    private String Nuevacita;
    private int Top_Aux;
    private RegistroExamen registroexamen;
    private RegistroPaciente registropaciente;

    public ControlHorario() {
    }

    public ControlHorario(int idhorario, int idpaciente_fore, int idexamen_fore, String Nombre, String Horacita, String Nuevacita) {
        this.idhorario = idhorario;
        this.idpaciente_fore = idpaciente_fore;
        this.idexamen_fore = idexamen_fore;
        this.Nombre = Nombre;
        this.Horacita = Horacita;
        this.Nuevacita = Nuevacita;
    }

    public int getIdhorario() {
        return idhorario;
    }

    public void setIdhorario(int idhorario) {
        this.idhorario = idhorario;
    }

    public int getIdpaciente_fore() {
        return idpaciente_fore;
    }

    public void setIdpaciente_fore(int idpaciente_fore) {
        this.idpaciente_fore = idpaciente_fore;
    }

    public int getIdexamen_fore() {
        return idexamen_fore;
    }

    public void setIdexamen_fore(int idexamen_fore) {
        this.idexamen_fore = idexamen_fore;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getHoracita() {
        return Horacita;
    }

    public void setHoracita(String Horacita) {
        this.Horacita = Horacita;
    }

    public String getNuevacita() {
        return Nuevacita;
    }

    public void setNuevacita(String Nuevacita) {
        this.Nuevacita = Nuevacita;
    }

    public int getTop_Aux() {
        return Top_Aux;
    }

    public void setTop_Aux(int Top_Aux) {
        this.Top_Aux = Top_Aux;
    }

    public RegistroExamen getRegistroexamen() {
        return registroexamen;
    }

    public void setRegistroexamen(RegistroExamen registroexamen) {
        this.registroexamen = registroexamen;
    }

    public RegistroPaciente getRegistropaciente() {
        return registropaciente;
    }

    public void setRegistropaciente(RegistroPaciente registropaciente) {
        this.registropaciente = registropaciente;
    }
    
    
 
    
}
