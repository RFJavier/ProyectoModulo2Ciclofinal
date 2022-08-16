/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.accesodatos;
import java.util.*; 
import java.sql.*; 
import clinica.entidadesdenegocio.ControlHorario;
/**
 *
 * @author Javier Rivera
 */
public class ControlHorarioDAL {
    
    static String obtenerCampos() {
        return "c.idhorario,c.Nombre,c.Horacita,c.nuevacita";
    }

    
    private static String obtenerSelect(ControlHorario pHorario) {
        String sql;
        sql = "SELECT ";
        if (pHorario.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
            
            sql += "TOP " + pHorario.getTop_Aux() + " ";
        }
        sql += (obtenerCampos() + " FROM controlhorario c");
        return sql;
    }
    
    private static String agregarOrderBy(ControlHorario pHorario) {
        String sql = " ORDER BY c.idhorario DESC";
        if (pHorario.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            
            sql += " LIMIT " + pHorario.getTop_Aux() + " ";
        }
        return sql;
    }
    
     public static int crear(ControlHorario pHorario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO controlhorario(nombre,Horacita,nuevacita) VALUES(?,?,?)"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pHorario.getNombre());   
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
     
     public static int modificar(ControlHorario pHorario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "UPDATE controlhorario SET nombre=?,Horacita=?,nuevacita=? WHERE idhorario=?"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pHorario.getNombre()); 
                ps.setInt(2, pHorario.getIdhorario());   
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
  
     public static int eliminar(ControlHorario pHorario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "DELETE FROM controlhorario WHERE idhorario=?";  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pHorario.getIdhorario());  
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
     
     static int asignarDatosResultSet(ControlHorario pHorario, ResultSet pResultSet, int pIndex) throws Exception {
        
        pIndex++;
        pHorario.setIdhorario(pResultSet.getInt(pIndex)); 
        pIndex++;
        pHorario.setNombre(pResultSet.getString(pIndex)); 
        return pIndex;
    }
     
     private static void obtenerDatos(PreparedStatement pPS, ArrayList<ControlHorario> Horarios) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            while (resultSet.next()) { 
                ControlHorario control = new ControlHorario(); 
                asignarDatosResultSet(control, resultSet, 0); 
                Horarios.add(control); 
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex; 
        }
    }
     
     public static ControlHorario obtenerPorId(ControlHorario pHorario) throws Exception {
        ControlHorario control = new ControlHorario();
        ArrayList<ControlHorario> Horarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pHorario); 
            sql += " WHERE c.idhorario=?"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pHorario.getIdhorario());  
                obtenerDatos(ps, Horarios); 
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

    
    public static ArrayList<ControlHorario> obtenerTodos() throws Exception {
        ArrayList<ControlHorario> controles;
        controles = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new ControlHorario());  
            sql += agregarOrderBy(new ControlHorario());  
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
    
    static void querySelect(ControlHorario pHorario, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); 
        if (pHorario.getIdhorario() > 0) { 
            pUtilQuery.AgregarWhereAnd(" c.idhorario=? "); 
            if (statement != null) { 
                
                statement.setInt(pUtilQuery.getNumWhere(), pHorario.getIdhorario()); 
            }
        }
        
        if (pHorario.getNombre() != null && pHorario.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" c.nombre LIKE ? "); 
            if (statement != null) {
                
                statement.setString(pUtilQuery.getNumWhere(), "%" + pHorario.getNombre() + "%"); 
            }
        }
    }
    
    public static ArrayList<ControlHorario> buscar(ControlHorario pHorario) throws Exception {
        ArrayList<ControlHorario> Controles = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pHorario); 
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0); 
            querySelect(pHorario, utilQuery); 
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pHorario); 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pHorario, utilQuery); 
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
