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
        return "c.idhorario,c.idpaciente_fore,c.idexamen, c.nombre, c.horacita, c.nuevacita";
    }
 private static String obtenerSelect(ControlHorario pControlHorario) {
        String sql;
        sql = "SELECT ";
        if (pControlHorario.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
             
            sql += "TOP " + pControlHorario.getTop_Aux() + " ";
        }
        sql += (obtenerCampos() + " FROM ControlHorario c");
        return sql;
    }   
 private static String agregarOrderBy(ControlHorario pControlHorario) {
        String sql = " ORDER BY u.Id DESC";
        if (pControlHorario.getTop_Aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            
            
            sql += " LIMIT " + pControlHorario.getTop_Aux() + " ";
        }
        return sql;
    }
 
  public static int crear(ControlHorario pControlHorario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "INSERT INTO ControlHorario(idhorario,idpaciente_fore,idexamen_fore,nombre,horacita,nuevacita) VALUES(?,?,?,?,?,?)"; 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pControlHorario.getIdhorario()); //  
                ps.seString(2, pControlHorario.getIdpaciente_fore());
                 ps.setString(3, pControlHorario.getIdexamen_fore());  
                ps.seString(4, pControlHorario.getNombre());
                 ps.setString(5, pControlHorario.getHoracita());   
                ps.seString(6, pControlHorario.getNuevacita());
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
  
  public static int modificar(ControlHorario pControlHorario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "UPDATE ControlHorario SET idhorario=?, idpaciente_fore=?,idexamen_fore=?,nombre=?,horacita=?,nuevacita=? WHERE idhorario=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                 ps.setString(1, pControlHorario.getIdhorario());   
                ps.seString(2, pControlHorario.getIdpaciente_fore());
                 ps.setString(3, pControlHorario.getIdexamen_fore());   
                ps.seString(4, pControlHorario.getNombre());
                 ps.setString(5, pControlHorario.getJHoracita());  
                ps.seString(6, pControlHorario.getNuevacita());
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
  
  public static int eliminar(ControlHorario pControlHorario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { 
            sql = "DELETE FROM ControlHorario WHERE idhorario=?";  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pControlHorario.getIdhorario());   
                ps.seString(2, pControlHorario.getIdopaciente_fore());
                 ps.setString(3, pControlHorario.getIdexamen_fore());  
                ps.seString(4, pControlHorario.getNombre());
                 ps.setString(5, pControlHorario.getHoracita());   
                ps.seString(6, pControlHorario.getNuevacita());
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
  
   static int asignarDatosResultSet(ControlHorario pControlHorario, ResultSet pResultSet, int pIndex) throws Exception {
        
        pIndex++;
        pRol.setIdhorario(pResultSet.getInt(pIndex)); 
        pIndex++;
        pRol.setIdpaciente_fore(pResultSet.getString(pIndex));
        pIndex++;
        pRol.setIdexamen_fore(pResultSet.getInt(pIndex)); 
        pIndex++;
        pRol.setNombre(pResultSet.getString(pIndex)); 
        pIndex++;
        pRol.setHoracita(pResultSet.getInt(pIndex)); 
        pIndex++;
        pRol.setNuevacita(pResultSet.getString(pIndex)); 
        pIndex++;
        return pIndex;
    }
     private static void obtenerDatos(PreparedStatement pPS, ArrayList<ControlHorario> pHorarios) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { 
            while (resultSet.next()) { 
                ControlHorario controlhorario = new ControlHorario(); 
                asignarDatosResultSet(controlhorario, resultSet, 0); 
                pHorarios.add(controlhorario);
            }
            resultSet.close(); 
        } catch (SQLException ex) {
            throw ex;  
        }
    }
     public static ControlHorario obtenerPoridhorario(ControlHorario pControlHorario) throws Exception {
        ContolHorario controlhorario = new ControlHorario();
        ArrayList<ControlHorario> horarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pControlHorario);
            sql += " WHERE c.idhorario=?";  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                ps.setString(1, pControlHorario.getIdhorario()); 
                ps.seString(2, pControlHorario.getIdpaciente_fore());
                 ps.setString(3, pControlHorario.getIdexamen_fore());   
                ps.seString(4, pControlHorario.getNombre());
                 ps.setString(5, pControlHorario.getHoracita());   
                ps.seString(6, pControlHorario.getNuevacita());  
                obtenerDatos(ps, horarios); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex;  
            }
            conn.close(); 
        }
        catch (SQLException ex) {
            throw ex; 
        }
        if (horarios.size() > 0) { 
            controlhorario = horarios.get(0);  
        }
        return horarios;  
    }
     public static ArrayList<ControlHorario> obtenerTodos() throws Exception {
        ArrayList<ControlHorario> horarios;
        horarios = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = obtenerSelect(new ControlHorario()); 
            sql += agregarOrderBy(new ControlHorario());  
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                obtenerDatos(ps, horarios); 
                ps.close();
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        } 
        catch (SQLException ex) {
            throw ex; 
        }
        return horarios; 
    }

     static void querySelect(ControlHorario pControlHorario, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); 
        if (pControlhorario.getIdhorario() > 0) {
            pUtilQuery.AgregarWhereAnd(" c.idhorario=?"); 
            if (statement != null) { 
                
                statement.setInt(pUtilQuery.getNumWhere(), pControlHorario.getIdhorario());
 
            }
        } 
        if (pControlHorario.getIdpaciente_fore() > 0) {
            pUtilQuery.AgregarWhereAnd(" c.idpaciente_fore=? "); 
            if (statement != null) {
           
                statement.setInt(pUtilQuery.getNumWhere(), pControlHorario.getIdpaciente_fore());
            }
        }
        
        if (pControlHorario.getIdexamen_fore() != null && pControlHorario.getIdpaciente().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" c.idexamen_fore= ? "); 
            if (statement != null) {
                 
                statement.setString(pUtilQuery.getNumWhere(), "%" + pControlHorario.getIdexamen_fore() + "%");
            }
        }
        
        if (pControlHorario.getNombre() != null && pControlHorario.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" c.Nombre=? "); 
            if (statement != null) {
               
                statement.setString(pUtilQuery.getNumWhere(), "%" + pControlHorario.getNombre() + "%"); 
            }
        } 
        
        if (pControlHorario.getHoracita() > 0) {
            pUtilQuery.AgregarWhereAnd(" c.Horacita=? "); 
            if (statement != null) {
                 
                statement.setInt(pUtilQuery.getNumWhere(), pControlHorario.getHoracita());
            }
        }
        
        if (pControlHorario.getNuevacita() != null && pControlHorario.getNuevacita().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" c.Nuevacita=? "); 
            if (statement != null) {
                 
                statement.setString(pUtilQuery.getNumWhere(), "%" + pControlHorario.getNuevacita() + "%");
            }
        }
    } 
      public static ArrayList<ControlHorario> buscar(ControlHorario pControlHorario) throws Exception {
        ArrayList<ControlHorario> horarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = obtenerSelect(pControlHorario); 
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pControlhorario, utilQuery); 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pControlHorario); 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pControlHorario, utilQuery); 
                obtenerDatos(ps, horarios); 
                ps.close(); 
            } catch (SQLException ex) {
                throw ex; 
            }
            conn.close(); 
        } 
        catch (SQLException ex) {
            throw ex; 
        }
        return horarios; 
    }
       public static ArrayList<ControlHorario> buscarIncluirRegistropaciente(ControlHorario pControlHorario) throws Exception {
        ArrayList<ControlHorario> horarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = "SELECT "; 
            if (pControlHorario.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
                sql += "TOP " + pControlHorario.getTop_aux() + " "; 
            }
            sql += obtenerCampos(); 
            sql += ",";
            sql += ResgistroPacienteDAL.obtenerCampos();
            sql += " FROM controlhorario c";
            sql += " JOIN registropaciente r on (r.idpaciente_fore=c.idhorario)"; 
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pControlHorario, utilQuery); 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pControlHorario); 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { 
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pControlHorarios, utilQuery);
                obtenerDatosIncluirRegistropaciente(ps, horarios);
                ps.close(); 
            } catch (SQLException ex) {
                 throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return horarios; 
    }
       public static ArrayList<ControlHorario> buscarIncluirRegistroexamen(ControlHorario pControlHorario) throws Exception {
        ArrayList<ControlHorario> horarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { 
            String sql = "SELECT "; 
            if (pControlHorario.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
                sql += "TOP " + pControlHorario.getTop_aux() + " "; 
            }
            sql += obtenerCampos(); 
            sql += ",";
            sql += ResgistroExamenDAL.obtenerCampos(); 
            sql += " FROM controlhorario c";
            sql += " JOIN registroexamen r on (r.idexamen_fore=c.idhorario)"; 
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pControlHorario, utilQuery);  
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pControlHorario); 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pControlHorario, utilQuery);
                obtenerDatosIncluirRegistroexamen(ps, horarios);
                ps.close(); 
            } catch (SQLException ex) {
                 throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return horarios;
    }
    
}
