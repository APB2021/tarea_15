package dao;

import java.sql.Connection;
import java.sql.SQLException;

import modelo.Alumno;
import modelo.Grupo;

public class AlumnosFichero implements IAlumnosDao{

	@Override
	public boolean insertarAlumno(Connection conexionBD, Alumno alumno) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mostrarTodosLosAlumnos(Connection conexionBD) throws SQLException {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public Alumno solicitarDatosAlumno() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
