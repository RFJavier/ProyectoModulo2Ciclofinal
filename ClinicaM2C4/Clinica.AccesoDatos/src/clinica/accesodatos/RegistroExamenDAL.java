/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.accesodatos;



import java.util.*;
import java.sql.*;
import clinica.entidadesdenegocio.RegistroPaciente;
import clinica.entidadesdenegocio.RegistroExamen;



/**
 *
 * @author Javier Rivera
 */
public class RegistroExamenDAL {
    
    static String obtenerCampos() {
        return "u.Id, u.IdRol, u.Nombre, u.Apellido, u.Login, u.Estatus, u.FechaRegistro";
    }
    
    private static String obtenerSelect(RegistroExamen pexamen) {
        String sql;
        sql = "SELECT ";
        if (pexamen.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {  
            sql += "TOP " + pexamen.getTop_Aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Usuario u");
        return sql;
    }
    
    private static String agregarOrderBy(RegistroExamen pexamen) {
        String sql = " ORDER BY u.Id DESC";
        if (pexamen.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL){
            sql += " LIMIT " + pexamen.getTop_Aux() + " ";
        }
        return sql;
    }
    
     public static int crear(RegistroExamen pregistro) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO registroexamen(idpaciente_fore,examen,observacion) VALUES(?,?,?)"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pregistro.getIdpaciente_fore());
                ps.setString(2, pregistro.getExamen());
                ps.setString(3, pregistro.getObservacion());
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
     
     public static int modificar(RegistroExamen pUsuario) throws Exception {
        int result;
        String sql;
        
            try (Connection conn = ComunDB.obtenerConexion();) { 
                sql = "UPDATE Usuario SET idpaciente_fore=?, examen=?, observacion=? WHERE Id=?";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                    ps.setInt(1, pUsuario.getIdpaciente_fore()); 
                    ps.setString(2, pUsuario.getExamen());  
                    ps.setString(3, pUsuario.getObservacion()); 
                    ps.setInt(4, pUsuario.getIdexamen()); 
                       
                    result = ps.executeUpdate(); 
                    ps.close(); 
                } catch (SQLException ex) {
                    throw ex; 
                }
                conn.close(); 
            }
            catch (SQLException ex) {
                throw ex; 
            }
        
            return result;
        }
     public static int eliminar(RegistroExamen pregistro) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "DELETE FROM registroexamen WHERE idexamen=?"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {  
                ps.setInt(1, pregistro.getIdexamen()); 
                result = ps.executeUpdate(); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex;  
        }
        return result; 
    }
     
     static int asignarDatosResultSet(RegistroExamen pregistro, ResultSet pResultSet, int pIndex) throws Exception {
        
        pIndex++;
        pregistro.setIdexamen(pResultSet.getInt(pIndex)); 
        pIndex++;
        pregistro.setIdpaciente_fore(pResultSet.getInt(pIndex));
        pIndex++;
        pregistro.setExamen(pResultSet.getString(pIndex));
        pIndex++;
        pregistro.setObservacion(pResultSet.getString(pIndex));
        pIndex++;
       
        return pIndex;
    }
     
     private static void obtenerDatos(PreparedStatement pPS, ArrayList<RegistroExamen> examenes) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            while (resultSet.next()) { 
                RegistroExamen examen = new RegistroExamen(); 
                asignarDatosResultSet(examen, resultSet, 0); 
                examenes.add(examen); 
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex; 
        }
    }
     
    
   public static RegistroExamen obtenerPorId(RegistroExamen pregistro) throws Exception {
        RegistroExamen examen = new RegistroExamen();
        ArrayList<RegistroExamen > examenes = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pregistro); 
            sql += " WHERE r.idexamen,r.idpaciente_fore,r.examen,r.observacion"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setInt(1, pregistro.getIdexamen()); 
               ps.setInt(2, pregistro.getIdpaciente_fore()); 
                ps.setString(3, pregistro.getExamen());
                ps.setString(4, pregistro.getObservacion());
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;  
            }
            conn.close();  
        }
        catch (SQLException ex) {
            throw ex; 
        }
        if (examenes.size() > 0) { 
            examen = examenes.get(0); 
        }
        return examen; 
    }
  
 public static ArrayList<RegistroExamen> obtenerTodos() throws Exception {
        ArrayList<RegistroExamen> examenes;
        examenes = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new RegistroExamen());  
            sql += agregarOrderBy(new RegistroExamen());  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                obtenerDatos(ps, examenes); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        } 
        catch (SQLException ex) {
            throw ex; 
        }
        return examenes; 
    }
    
    static void querySelect(RegistroExamen pregistro, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); 
        if (pregistro.getIdexamen() > 0) { 
            pUtilQuery.AgregarWhereAnd(" r.idexamen=? "); 
            if (statement != null) { 
                
                statement.setInt(pUtilQuery.getNumWhere(), pregistro.getIdexamen()); 
            }
        }
        
        if (pregistro.getIdRol() > 0) {
            pUtilQuery.AgregarWhereAnd(" u.IdRol=? "); // agregar el campo IdRol al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo IdRol a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pregistro.getIdRol());
            }
        }
        
        if (pregistro.getExamen() != null && pregistro.getExamen().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.examen LIKE ? "); 
            if (statement != null) {
                
                statement.setString(pUtilQuery.getNumWhere(), "%" + pregistro.getExamen() + "%"); 
            }
        }
        
        if (pregistro.getObservacion() != null && pregistro.getObservacion().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd("r.observacion LIKE ?"); 
            if (statement != null) {
                
                statement.setString(pUtilQuery.getNumWhere(), "%" + pregistro.getObservacion() + "%"); 
            }
        }
    }
    
    public static ArrayList<RegistroExamen> buscar(RegistroExamen pregistro) throws Exception {
        ArrayList<RegistroExamen> Controles = new ArrayList();
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
    
}

