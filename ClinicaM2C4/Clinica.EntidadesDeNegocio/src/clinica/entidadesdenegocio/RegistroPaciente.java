/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.entidadesdenegocio;

/**
 *
 * @author Javier Rivera
 */
public class RegistroPaciente {
    
    private int idpaciente;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String dui;
    private int Top_Aux;
    private RegistroExamen examen;
    private ControlHorario horario;

    public RegistroPaciente() {
    }

    public RegistroPaciente(int idpaciente, String nombre, String apellido, String telefono, String direccion, String dui) {
        this.idpaciente = idpaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.dui = dui;
    }

    public int getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(int idpaciente) {
        this.idpaciente = idpaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public int getTop_Aux() {
        return Top_Aux;
    }

    public void setTop_Aux(int Top_Aux) {
        this.Top_Aux = Top_Aux;
    }
    
     public RegistroExamen getExamen() {
        return examen;
    }

    public void setExamen(RegistroExamen examen) {
        this.examen = examen;
    }

    public ControlHorario getHorario() {
        return horario;
    }

    public void setHorario(ControlHorario horario) {
        this.horario = horario;
    }
    
}
