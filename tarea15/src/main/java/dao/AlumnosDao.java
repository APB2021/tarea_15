package dao;

import java.sql.Connection;
import java.sql.SQLException;

import model.Alumno;
import model.Grupo;

public interface AlumnosDao {

	public boolean insertarAlumno(Connection conexionBD, Alumno alumno) throws SQLException;

	public boolean mostrarTodosLosAlumnos(Connection conexionBD) throws SQLException;

	public void guardarAlumnosEnFicheroTexto(Connection conexionBD) throws SQLException;

	public boolean leerAlumnosDeFicheroTexto(Connection conexionBD) throws SQLException;

	public boolean modificarNombreAlumnoPorNia(Connection conexion, int nia, String nuevoNombre) throws SQLException;

	public boolean eliminarAlumnoPorNIA(Connection conexionBD, int nia) throws SQLException;

	public boolean eliminarAlumnosPorApellidos(Connection conexionBD, String apellidos) throws SQLException;

	public void guardarAlumnosEnFicheroJSON(Connection conexionBD) throws SQLException;

	public boolean leerAlumnosDeFicheroJSON(Connection conexionBD) throws SQLException;

	// GRUPOS:

	public boolean insertarGrupo(Connection conexionBD, Grupo grupo) throws SQLException;

	public boolean eliminarAlumnosPorGrupo(Connection conexionBD, String grupo) throws SQLException;

	public void guardarGruposEnFicheroJSON(Connection conexionBD) throws SQLException;

	public boolean leerGruposDeFicheroJSON(Connection conexionBD) throws SQLException;

}
