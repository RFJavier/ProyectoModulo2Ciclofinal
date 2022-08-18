/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.accesodatos;

import java.util.*;
import java.sql.*;
import clinica.entidadesdenegocio.Usuario;
import clinica.entidadesdenegocio.Rol;
import java.time.LocalDate;
/**
 *
 * @author Javier Rivera
 */
public class RegistroUsuarioDAL {
    
    public static String encriptarMD5(String txt) throws Exception {
        try {
            StringBuffer sb;
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(txt.getBytes());
            sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw ex;
        }
    }
    
    static String obtenerCampos() {
        return "u.Id, u.IdRol, u.Nombre, u.Apellido, u.Login, u.Estatus, u.FechaRegistro";
    }
    
    private static String obtenerSelect(Usuario pUsuario) {
        String sql;
        sql = "SELECT ";
        if (pUsuario.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
             // Agregar el TOP a la consulta SELECT si el gestor de base de datos es SQL SERVER y getTop_aux es mayor a cero
            sql += "TOP " + pUsuario.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Usuario u");
        return sql;
    }   
    
    private static String agregarOrderBy(Usuario pUsuario) {
        String sql = " ORDER BY u.Id DESC";
        if (pUsuario.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            // Agregar el LIMIT a la consulta SELECT de la tabla de Usuario en el caso que getTop_aux() sea mayor a cero y el gestor de base de datos
            // sea MYSQL
            sql += " LIMIT " + pUsuario.getTop_aux() + " ";
        }
        return sql;
    }
    
    private static boolean existeLogin(Usuario pUsuario) throws Exception {
        boolean existe = false;
        ArrayList<Usuario> usuarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pUsuario);  // Obtener la consulta SELECT de la tabla Usuario
            // Concatenar a la consulta SELECT de la tabla Usuario el WHERE y el filtro para saber si existe el login
            sql += " WHERE u.Id<>? AND u.Login=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pUsuario.getId());  // Agregar el parametros a la consulta donde estan el simbolo ? #1 
                ps.setString(2, pUsuario.getLogin());  // Agregar el parametros a la consulta donde estan el simbolo ? #2 
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de USuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement el en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (usuarios.size() > 0) { // Verificar si el ArrayList de Usuario trae mas de un registro en tal caso solo debe de traer uno
            Usuario usuario;
             // Se solucciono tenia valor de 1 cuando debe de ser cero
            usuario = usuarios.get(0); // Si el ArrayList de Usuario trae un registro o mas obtenemos solo el primero 
            if (usuario.getId() > 0 && usuario.getLogin().equals(pUsuario.getLogin())) {
                // Si el Id de Usuario es mayor a cero y el Login que se busco en la tabla de Usuario es igual al que solicitamos
                // en los parametros significa que el login ya existe en la base de datos y devolvemos true en la variable "existe"
                existe = true;
            }
        }
        return existe; //Devolver la variable "existe" con el valor true o false si existe o no el Login en la tabla de Usuario de la base de datos

    }
    
     public static int crear(Usuario pUsuario) throws Exception {
        int result;
        String sql;
        boolean existe = existeLogin(pUsuario); // verificar si el usuario que se va a crear ya existe en nuestra base de datos
        if (existe == false) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                 //Definir la consulta INSERT a la tabla de Usuario utilizando el simbolo "?" para enviar parametros
                sql = "INSERT INTO Usuario(IdRol,Nombre,Apellido,Login,Password,Estatus,FechaRegistro) VALUES(?,?,?,?,?,?,?)";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                    ps.setInt(1, pUsuario.getIdRol()); // Agregar el parametro a la consulta donde estan el simbolo "?" #1  
                    ps.setString(2, pUsuario.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo "?" #2 
                    ps.setString(3, pUsuario.getApellido()); // agregar el parametro a la consulta donde estan el simbolo "?" #3 
                    ps.setString(4, pUsuario.getLogin()); // agregar el parametro a la consulta donde estan el simbolo "?" #4 
                    ps.setString(5, encriptarMD5(pUsuario.getPassword())); // agregar el parametro a la consulta donde estan el simbolo "?" #5 
                    ps.setByte(6, pUsuario.getEstatus()); // agregar el parametro a la consulta donde estan el simbolo "?" #6 
                    ps.setDate(7, java.sql.Date.valueOf(LocalDate.now())); // agregar el parametro a la consulta donde estan el simbolo "?" #7 
                    result = ps.executeUpdate(); // ejecutar la consulta INSERT en la base de datos
                    ps.close(); // cerrar el PreparedStatement
                } catch (SQLException ex) {
                    throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda 
                }
                conn.close();
            } // Handle any errors that may have occurred.
            catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al obtener la conexion en el caso que suceda
            }
        } else {
            result = 0;
            throw new RuntimeException("Login ya existe"); // enviar una exception para notificar que el login existe
        }
        return result; // Retornar el numero de fila afectadas en el INSERT en la base de datos 
    }
     
     public static int modificar(Usuario pUsuario) throws Exception {
        int result;
        String sql;
        boolean existe = existeLogin(pUsuario); // verificar si el usuario que se va a modificar ya existe en nuestra base de datos
        if (existe == false) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                //Definir la consulta UPDATE a la tabla de Usuario utilizando el simbolo ? para enviar parametros
                sql = "UPDATE Usuario SET IdRol=?, Nombre=?, Apellido=?, Login=?, Estatus=? WHERE Id=?";
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                    ps.setInt(1, pUsuario.getIdRol()); // agregar el parametro a la consulta donde estan el simbolo ? #1  
                    ps.setString(2, pUsuario.getNombre()); // agregar el parametro a la consulta donde estan el simbolo ? #2  
                    ps.setString(3, pUsuario.getApellido()); // agregar el parametro a la consulta donde estan el simbolo ? #3  
                    ps.setString(4, pUsuario.getLogin()); // agregar el parametro a la consulta donde estan el simbolo ? #4  
                    ps.setByte(5, pUsuario.getEstatus()); // agregar el parametro a la consulta donde estan el simbolo ? #5  
                    ps.setInt(6, pUsuario.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #6  
                    result = ps.executeUpdate(); // ejecutar la consulta UPDATE en la base de datos
                    ps.close(); // cerrar el PreparedStatement
                } catch (SQLException ex) {
                    throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda 
                }
                conn.close(); // cerrar la conexion a la base de datos
            } 
            catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al obtener la conexion en el caso que suceda 
            }
        } else {
            result = 0;
            throw new RuntimeException("Login ya existe"); // enviar una exception para notificar que el login existe
        }
        return result; // Retornar el numero de fila afectadas en el UPDATE en la base de datos 
    }
     
      public static int eliminar(Usuario pUsuario) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "DELETE FROM Usuario WHERE Id=?"; //definir la consulta DELETE a la tabla de Usuario utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {  // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pUsuario.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
                result = ps.executeUpdate(); // ejecutar la consulta DELETE en la base de datos
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex;  // enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda 
        }
        return result; // Retornar el numero de fila afectadas en el DELETE en la base de datos 
    }
      
      static int asignarDatosResultSet(Usuario pUsuario, ResultSet pResultSet, int pIndex) throws Exception {
        //  SELECT u.Id(indice 1), u.IdRol(indice 2), u.Nombre(indice 3), u.Apellido(indice 4), u.Login(indice 5), 
        // u.Estatus(indice 6), u.FechaRegistro(indice 7) * FROM Usuario
        pIndex++;
        pUsuario.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        pUsuario.setIdRol(pResultSet.getInt(pIndex)); // index 2
        pIndex++;
        pUsuario.setNombre(pResultSet.getString(pIndex)); // index 3
        pIndex++;
        pUsuario.setApellido(pResultSet.getString(pIndex)); // index 4
        pIndex++;
        pUsuario.setLogin(pResultSet.getString(pIndex)); // index 5
        pIndex++;
        pUsuario.setEstatus(pResultSet.getByte(pIndex)); // index 6
        pIndex++;
        pUsuario.setFechaRegistro(pResultSet.getDate(pIndex).toLocalDate()); // index 7
        return pIndex;
    }

    // Metodo para  ejecutar el ResultSet de la consulta SELECT a la tabla de Usuario
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Usuario> pUsuarios) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Usuario
                Usuario usuario = new Usuario();
                // Llenar las propiedaddes de la Entidad Usuario con los datos obtenidos de la fila en el ResultSet
                asignarDatosResultSet(usuario, resultSet, 0);
                pUsuarios.add(usuario); // agregar la entidad Usuario al ArrayList de Usuario
            }
            resultSet.close(); // cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex;// enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
 // Metodo para  ejecutar el ResultSet de la consulta SELECT a la tabla de Usuario y JOIN a la tabla de Rol
    private static void obtenerDatosIncluirRol(PreparedStatement pPS, ArrayList<Usuario> pUsuarios) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            HashMap<Integer, Rol> rolMap = new HashMap(); //crear un HashMap para automatizar la creacion de instancias de la clase Rol
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Usuario JOIN a la tabla de Rol
                Usuario usuario = new Usuario();
                 // Llenar las propiedaddes de la Entidad Usuario con los datos obtenidos de la fila en el ResultSet
                int index = asignarDatosResultSet(usuario, resultSet, 0);
                if (rolMap.containsKey(usuario.getIdRol()) == false) { // verificar que el HashMap aun no contenga el Rol actual
                    Rol rol = new Rol();
                    // en el caso que el Rol no este en el HashMap se asignara
                    RolDAL.asignarDatosResultSet(rol, resultSet, index);
                    rolMap.put(rol.getId(), rol); // agregar el Rol al  HashMap
                    usuario.setRol(rol); // agregar el Rol al Usuario
                } else {
                    // En el caso que el Rol existe en el HashMap se agregara automaticamente al Usuario
                    usuario.setRol(rolMap.get(usuario.getIdRol())); 
                }
                pUsuarios.add(usuario); // Agregar el Usuario de la fila actual al ArrayList de Usuario
            }
            resultSet.close(); // cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex; // enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }

    // Metodo para obtener por Id un registro de la tabla de Usuario 
    public static Usuario obtenerPorId(Usuario pUsuario) throws Exception {
        Usuario usuario = new Usuario();
        ArrayList<Usuario> usuarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pUsuario); // obtener la consulta SELECT de la tabla Usuario
             // Concatenar a la consulta SELECT de la tabla Usuario el WHERE  para comparar el campo Id
            sql += " WHERE u.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pUsuario.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (usuarios.size() > 0) { // verificar si el ArrayList de Usuario trae mas de un registro en tal caso solo debe de traer uno
            usuario = usuarios.get(0); // Si el ArrayList de Usuario trae un registro o mas obtenemos solo el primero
        }
        return usuario; // devolver el Usuario encontrado por Id 
    }

     // Metodo para obtener todos los registro de la tabla de Usuario
    public static ArrayList<Usuario> obtenerTodos() throws Exception {
        ArrayList<Usuario> usuarios;
        usuarios = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(new Usuario()); // obtener la consulta SELECT de la tabla Usuario
            sql += agregarOrderBy(new Usuario()); // concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id 
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return usuarios; // devolver el ArrayList de Usuario
    }

    // Metodo para asignar los filtros de la consulta SELECT de la tabla de Usuario de forma dinamica
    static void querySelect(Usuario pUsuario, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); // obtener el PreparedStatement al cual aplicar los parametros
        if (pUsuario.getId() > 0) { // verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Usuario
            pUtilQuery.AgregarWhereAnd(" u.Id=? "); // agregar el campo Id al filtro de la consulta SELECT y agregar el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Id a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pUsuario.getId());
            }
        }
        // verificar si se va incluir el campo IdRol en el filtro de la consulta SELECT de la tabla de Usuario
        if (pUsuario.getIdRol() > 0) {
            pUtilQuery.AgregarWhereAnd(" u.IdRol=? "); // agregar el campo IdRol al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo IdRol a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pUsuario.getIdRol());
            }
        }
        // verificar si se va incluir el campo Nombre en el filtro de la consulta SELECT de la tabla de Usuario
        if (pUsuario.getNombre() != null && pUsuario.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Nombre LIKE ? "); // agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Usuario
                statement.setString(pUtilQuery.getNumWhere(), "%" + pUsuario.getNombre() + "%");
            }
        }
        // Verificar si se va incluir el campo Apellido en el filtro de la consulta SELECT de la tabla de Usuario
        if (pUsuario.getApellido() != null && pUsuario.getApellido().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Apellido LIKE ? "); // agregar el campo Apellido al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Apellido a la consulta SELECT de la tabla de Usuario
                statement.setString(pUtilQuery.getNumWhere(), "%" + pUsuario.getApellido() + "%");
            }
        }
        // Verificar si se va incluir el campo Login en el filtro de la consulta SELECT de la tabla de Usuario
        if (pUsuario.getLogin() != null && pUsuario.getLogin().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" u.Login=? "); // agregar el campo Login al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Login a la consulta SELECT de la tabla de Usuario
                statement.setString(pUtilQuery.getNumWhere(), pUsuario.getLogin());
            }
        }
        // Verificar si se va incluir el campo Estatus en el filtro de la consulta SELECT de la tabla de Usuario
        if (pUsuario.getEstatus() > 0) {
            pUtilQuery.AgregarWhereAnd(" u.Estatus=? "); // agregar el campo Estatus al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                 // agregar el parametro del campo Estatus a la consulta SELECT de la tabla de Usuario
                statement.setInt(pUtilQuery.getNumWhere(), pUsuario.getEstatus());
            }
        }
    }

     // Metodo para obtener todos los registro de la tabla de Usuario que cumplan con los filtros agregados 
     // a la consulta SELECT de la tabla de Usuario 
    public static ArrayList<Usuario> buscar(Usuario pUsuario) throws Exception {
        ArrayList<Usuario> usuarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pUsuario); // obtener la consulta SELECT de la tabla Usuario
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pUsuario, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Usuario 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pUsuario); // Concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pUsuario, utilQuery); // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Usuario
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return usuarios; // Devolver el ArrayList de Usuario
    }
    
    // Metodo para verificar si el Usuario puede ser autorizado en el sistema
    // comparando el Login, Password, Estatus en la base de datos
    public static Usuario login(Usuario pUsuario) throws Exception {
        Usuario usuario = new Usuario();
        ArrayList<Usuario> usuarios = new ArrayList();
        String password = encriptarMD5(pUsuario.getPassword()); // Encriptar el password en MD5
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pUsuario); // Obtener la consulta SELECT de la tabla Usuario
             // Concatenar a la consulta SELECT de la tabla Usuario el WHERE  para comparar los campos de Login, Password, Estatus
            sql += " WHERE u.Login=? AND u.Password=? AND u.Estatus=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setString(1, pUsuario.getLogin()); // Agregar el parametro a la consulta donde estan el simbolo ? #1 
                ps.setString(2, password); // Agregar el parametro a la consulta donde estan el simbolo ? #2 
                ps.setByte(3, Usuario.EstatusUsuario.ACTIVO); // Agregar el parametro a la consulta donde estan el simbolo ? #3 
                obtenerDatos(ps, usuarios); // Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (usuarios.size() > 0) { // Verificar si el ArrayList de Usuario trae mas de un registro en tal caso solo debe de traer uno
            usuario = usuarios.get(0); // Si el ArrayList de Usuario trae un registro o mas obtenemos solo el primero
        }
        return usuario; // Devolver la instancia de Usuario 
    }

    // Metodo para cambiar el password de un Usuario el cual solo se puede cambiar si envia el password actual correctamente
    public static int cambiarPassword(Usuario pUsuario, String pPasswordAnt) throws Exception {
        int result;
        String sql;
        Usuario usuarioAnt = new Usuario();
        usuarioAnt.setLogin(pUsuario.getLogin());
        usuarioAnt.setPassword(pPasswordAnt);
        Usuario usuarioAut = login(usuarioAnt); // Obtenemos el Usuario autorizado validandolo en el metodo de login
        // Si el usuario que retorno el metodo de login tiene el Id mayor a cero y el Login es igual que el Login del Usuario que viene
        // en el parametro es un Usuario Autorizado
        if (usuarioAut.getId() > 0 && usuarioAut.getLogin().equals(pUsuario.getLogin())) {
            try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                sql = "UPDATE Usuario SET Password=? WHERE Id=?"; // Crear la consulta Update a la tabla de Usuario para poder modificar el Password
                try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                    // Agregar el parametro a la consulta donde estan el simbolo ? #1 pero antes encriptar el password para enviarlo encriptado
                    ps.setString(1, encriptarMD5(pUsuario.getPassword())); //
                    ps.setInt(2, pUsuario.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #2 
                    result = ps.executeUpdate(); // Ejecutar la consulta UPDATE en la base de datos
                    ps.close(); // Cerrar el PreparedStatement
                } catch (SQLException ex) {
                    throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
                }
                conn.close(); // Cerrar la conexion a la base de datos
            }
            catch (SQLException ex) {
                throw ex;// Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
            }
        } else {
            result = 0;
            // Enviar la excepcion en el caso que el usuario que intenta modificar el password ingresa un password incorrecto
            throw new RuntimeException("El password actual es incorrecto");
        }
        return result; // Retornar el numero de fila afectadas en el UPDATE en la base de datos 
    }

    // Metodo para obtener todos los registro de la tabla de Usuario que cumplan con los filtros agregados 
     // a la consulta SELECT de la tabla de Usuario 
    public static ArrayList<Usuario> buscarIncluirRol(Usuario pUsuario) throws Exception {
        ArrayList<Usuario> usuarios = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = "SELECT "; // Iniciar la variables para el String de la consulta SELECT
            if (pUsuario.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
                sql += "TOP " + pUsuario.getTop_aux() + " "; // Agregar el TOP en el caso que se este utilizando SQL SERVER
            }
            sql += obtenerCampos(); // Obtener los campos de la tabla de Usuario que iran en el SELECT
            sql += ",";
            sql += RolDAL.obtenerCampos(); // Obtener los campos de la tabla de Rol que iran en el SELECT
            sql += " FROM Usuario u";
            sql += " JOIN Rol r on (u.IdRol=r.Id)"; // agregar el join para unir la tabla de Usuario con Rol
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(pUsuario, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Usuario 
            sql = utilQuery.getSQL();
            sql += agregarOrderBy(pUsuario); // Concatenar a la consulta SELECT de la tabla Usuario el ORDER BY por Id
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(pUsuario, utilQuery); // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Usuario
                obtenerDatosIncluirRol(ps, usuarios);// Llenar el ArrayList de Usuario con las fila que devolvera la consulta SELECT a la tabla de Usuario
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                 throw ex;// Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } catch (SQLException ex) {
            throw ex;// Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return usuarios; // Devolver el ArrayList de Usuario
    }
}
    

