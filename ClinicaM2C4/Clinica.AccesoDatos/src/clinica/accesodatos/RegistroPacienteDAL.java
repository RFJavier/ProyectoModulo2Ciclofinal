/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.accesodatos;
import java.util.*; 
import java.sql.*;
import clinica.entidadesdenegocio.RegistroPaciente;
/**
 *
 * @author Javier Rivera
 */
public class RegistroPacienteDAL {
    
 static String obtenerCampos() {
        return "r.idpaciente,r.Nombre,r.apellido,r.telefono,r.direccion,r.dui";
    }

    
    private static String obtenerSelect(RegistroPaciente pregistro) {
        String sql;
        sql = "SELECT ";
        if (pregistro.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
            
            sql += "TOP " + pregistro.getTop_Aux() + " ";
        }
        sql += (obtenerCampos() + " FROM controlhorario c");
        return sql;
    }
    
    private static String agregarOrderBy(RegistroPaciente pregistro) {
        String sql = " ORDER BY r.idpaciente DESC";
        if (pregistro.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            
            sql += " LIMIT " + pregistro.getTop_Aux() + " ";
        }
        return sql;
    }
    
     public static int crear(RegistroPaciente pregistro) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO registropaciente(nombre,apellido,telefono,direccion,dui) VALUES(?,?,?,?,?)"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pregistro.getNombre());
                ps.setString(2, pregistro.getApellido());
                ps.setString(3, pregistro.getTelefono());
                ps.setString(4, pregistro.getDireccion());
                ps.setString(5, pregistro.getDui());
                result = ps.executeUpdate(); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        } catch (SQLException ex) {
            throw ex; 
        }
        return result; 
    }
     
     public static int modificar(RegistroPaciente pregistro) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "UPDATE controlhorario SET nombre=?,apellido=?,Telefono=?,telefono=?,direccion=?,dui=? WHERE idhorario=?"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pregistro.getIdpaciente());
                ps.setString(2, pregistro.getNombre()); 
                ps.setString(3, pregistro.getApellido());
                ps.setString(4, pregistro.getTelefono());
                ps.setString(5, pregistro.getDireccion());
                ps.setString(6, pregistro.getDui());
                result = ps.executeUpdate(); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;   
            }
            conn.close(); 
        } catch (SQLException ex) {
            throw ex; 
        }
        return result; 
    }
  
     public static int eliminar(RegistroPaciente pregistro) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "DELETE FROM registropaciente WHERE idpaciente=?";  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pregistro.getIdpaciente());  
                result = ps.executeUpdate();  
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close();  
        } catch (SQLException ex) {
            throw ex; 
        }
        return result;  
    }
     
     static int asignarDatosResultSet(RegistroPaciente pregistro, ResultSet pResultSet, int pIndex) throws Exception {
        
        pIndex++;
        pregistro.setIdpaciente(pResultSet.getInt(pIndex)); 
        pIndex++;
        pregistro.setNombre(pResultSet.getString(pIndex)); 
        return pIndex;
    }
     
     private static void obtenerDatos(PreparedStatement pPS, ArrayList<RegistroPaciente> Horarios) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            while (resultSet.next()) { 
                RegistroPaciente control = new RegistroPaciente(); 
                asignarDatosResultSet(control, resultSet, 0); 
                Horarios.add(control); 
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex; 
        }
    }
     
     public static RegistroPaciente obtenerPorId(RegistroPaciente pregistro) throws Exception {
        RegistroPaciente control = new RegistroPaciente();
        ArrayList<RegistroPaciente> Horarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pregistro); 
            sql += " WHERE r.idpaciente,r.Nombre,r.apellido,r.telefono,r.direccion,r.dui"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pregistro.getIdpaciente()); 
               ps.setString(2, pregistro.getNombre()); 
                ps.setString(3, pregistro.getApellido());
                ps.setString(4, pregistro.getTelefono());
                ps.setString(5, pregistro.getDireccion());
                ps.setString(6, pregistro.getDui());
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;  
            }
            conn.close();  
        }
        catch (SQLException ex) {
            throw ex; 
        }
        if (Horarios.size() > 0) { 
            control = Horarios.get(0); 
        }
        return control; 
    }

    
    public static ArrayList<RegistroPaciente> obtenerTodos() throws Exception {
        ArrayList<RegistroPaciente> controles;
        controles = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new RegistroPaciente());  
            sql += agregarOrderBy(new RegistroPaciente());  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                obtenerDatos(ps, controles); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        } 
        catch (SQLException ex) {
            throw ex; 
        }
        return controles; 
    }
    
    static void querySelect(RegistroPaciente pregistro, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); 
        if (pregistro.getIdpaciente() > 0) { 
            pUtilQuery.AgregarWhereAnd(" r.idpaciente=? "); 
            if (statement != null) { 
                
                statement.setInt(pUtilQuery.getNumWhere(), pregistro.getIdpaciente()); 
            }
        }
        
        if (pregistro.getNombre() != null && pregistro.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.nombre, r.apellido, r.telefono, r.direccion, r.dui LIKE ?,?,?,?,? "); 
            if (statement != null) {
                
                statement.setString(pUtilQuery.getNumWhere(), "%" + pregistro.getNombre() + "%"); 
            }
        }
    }
    
    public static ArrayList<RegistroPaciente> buscar(RegistroPaciente pregistro) throws Exception {
        ArrayList<RegistroPaciente> Controles = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pregistro); 
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0); 
            querySelect(pregistro, utilQuery); 
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pregistro); 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pregistro, utilQuery); 
                obtenerDatos(ps, Controles); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;  
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex; 
        }
        return Controles; 
    }    
    
}
