package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import modelo.Alumno;
import modelo.Grupo;

public class AlumnosDaoImpl implements AlumnosDao {

	@Override
	public boolean insertarAlumno(Connection conexionBD, Alumno alumno) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Muestra todos los alumnos con sus respectivos grupos.
	 * 
	 * @param conexionBD La conexión a la base de datos.
	 * @return true si se muestra la lista correctamente, false en caso contrario.
	 */

	@Override
	public boolean mostrarTodosLosAlumnos(Connection conexionBD) throws SQLException {
		String sql = "SELECT a.nia, a.nombre, a.apellidos, a.genero, a.fechaNacimiento, a.ciclo, a.curso, g.nombreGrupo "
				+ "FROM alumnos a " + "JOIN grupos g ON a.numeroGrupo = g.numeroGrupo" + "ORDER BY a.nia";

		try (PreparedStatement sentencia = conexionBD.prepareStatement(sql);
				ResultSet resultado = sentencia.executeQuery()) {

			if (!resultado.next()) {
				System.out.println("No hay alumnos registrados.");
				return false;
			}

			// Mostrar todos los resultados
			do {
				int nia = resultado.getInt("nia");
				String nombre = resultado.getString("nombre");
				String apellidos = resultado.getString("apellidos");
				String genero = resultado.getString("genero");
				Date fechaNacimiento = resultado.getDate("fechaNacimiento");
				String ciclo = resultado.getString("ciclo");
				String curso = resultado.getString("curso");
				String nombreGrupo = resultado.getString("nombreGrupo");

				// Mostrar los datos del alumno y del grupo
				System.out.println("NIA: " + nia);
				System.out.println("Nombre: " + nombre);
				System.out.println("Apellidos: " + apellidos);
				System.out.println("Género: " + genero);
				System.out.println("Fecha de nacimiento: " + fechaNacimiento);
				System.out.println("Ciclo: " + ciclo);
				System.out.println("Curso: " + curso);
				System.out.println("Grupo: " + nombreGrupo);
				System.out.println("-------------------------");

			} while (resultado.next());

			return true;
		} catch (SQLException e) {
			System.out.println("Error al recuperar los alumnos: " + e.getMessage());
			return false;
		}

	}

	@Override
	public void guardarAlumnosEnFicheroTexto(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean leerAlumnosDeFicheroTexto(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modificarNombreAlumnoPorNia(Connection conexion, int nia, String nuevoNombre) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eliminarAlumnoPorNIA(Connection conexionBD, int nia) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eliminarAlumnosPorApellidos(Connection conexionBD, String apellidos) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void guardarAlumnosEnFicheroJSON(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean leerAlumnosDeFicheroJSON(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertarGrupo(Connection conexionBD, Grupo grupo) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eliminarAlumnosPorGrupo(Connection conexionBD, String grupo) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void guardarGruposEnFicheroJSON(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean leerGruposDeFicheroJSON(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public static Alumno solicitarDatosAlumno() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean mostrarTodosLosGrupos(Connection conexionBD) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean guardarGruposEnXML(Connection conexionBD) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean leerYGuardarGruposXML(String rutaArchivo, Connection conexionBD) {
		// TODO Auto-generated method stub
		return false;
	}

}
