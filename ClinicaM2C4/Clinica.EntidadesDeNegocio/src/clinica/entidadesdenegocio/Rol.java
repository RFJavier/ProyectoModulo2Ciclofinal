
package clinica.entidadesdenegocio;
import java.util.ArrayList;
/**
 *
 * @author Javier Rivera
 */
public class Rol {
    private int id;
    private String nombre;
    private int top_aux;
    private ArrayList<Usuario> Usuario;

    public Rol() {
    }

    public Rol(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }

     public ArrayList<Usuario> getUsuario() {
        return Usuario;
    }

    public void setUsuario(ArrayList<Usuario> Usuario) {
        this.Usuario = Usuario;
    }
    
}
