package dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import modelo.Alumno;
import modelo.Grupo;
import pool.DatabasePool;


public class AlumnosBD implements IAlumnosDao {

	private final static Scanner sc = new Scanner(System.in);

	@Override
	public boolean insertarAlumno(Connection conexionBD, Alumno alumno) throws SQLException {
		// Primero, obtener el numeroGrupo del grupo del alumno
		int numeroGrupo = obtenerNumeroGrupo(alumno.getGrupo().getNombreGrupo());

		if (numeroGrupo == -1) {
			System.out.println("Error: El grupo no existe en la base de datos.");
			return false;
		}

		String sql = "INSERT INTO alumnos (nombre, apellidos, genero, fechaNacimiento, ciclo, curso, numeroGrupo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
			sentencia.setString(1, alumno.getNombre());
			sentencia.setString(2, alumno.getApellidos());
			sentencia.setString(3, String.valueOf(alumno.getGenero())); // Aseguramos que esté en mayúsculas
			sentencia.setDate(4, new java.sql.Date(alumno.getFechaNacimiento().getTime()));
			sentencia.setString(5, alumno.getCiclo());
			sentencia.setString(6, alumno.getCurso());
			sentencia.setInt(7, numeroGrupo); // Usar el numeroGrupo obtenido

			int filasAfectadas = sentencia.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.out.println("Error al insertar el alumno: " + e.getMessage());
			return false;
		}

	}

	/**
	 * Solicita al usuario los datos necesarios para crear un objeto Alumno.
	 * 
	 * @return Un objeto Alumno con los datos ingresados por el usuario.
	 */
	public Alumno solicitarDatosAlumno() {
		String nombre, apellidos, ciclo, curso, nombreGrupo;
		char respuestaGenero;
		Date fechaNacimiento = null;

		System.out.println("Introduce el nombre del alumno:");
		nombre = sc.nextLine().toUpperCase().trim();

		System.out.println("Introduce los apellidos del alumno:");
		apellidos = sc.nextLine().toUpperCase().trim();

		// Validar género
		do {
			System.out.println("Introduce el género del alumno (M/F):");
			respuestaGenero = sc.nextLine().toUpperCase().charAt(0); // Convertir a mayúscula
			if (respuestaGenero != 'M' && respuestaGenero != 'F') {
				System.out.println("Respuesta no válida. Introduce 'M' o 'F'.");
			}
		} while (respuestaGenero != 'M' && respuestaGenero != 'F');

		// Validar fecha de nacimiento
		do {
			System.out.println("Introduce la fecha de nacimiento (dd-MM-aaaa):");
			String fechaInput = sc.nextLine();
			try {
				SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
				formatoFecha.setLenient(false); // Validación estricta
				fechaNacimiento = formatoFecha.parse(fechaInput);
			} catch (ParseException e) {
				System.out.println("Formato de fecha inválido. Intenta de nuevo.");
			}
		} while (fechaNacimiento == null);

		System.out.println("Introduce el ciclo del alumno:");
		ciclo = sc.nextLine().trim().toUpperCase();

		System.out.println("Introduce el curso del alumno:");
		curso = sc.nextLine().trim().toUpperCase();

		// Validar nombre del grupo
		do {
			System.out.println("Introduce el nombre del grupo del alumno:");
			nombreGrupo = sc.nextLine().toUpperCase(); // Convertir a mayúsculas
			if (!validarNombreGrupo(nombreGrupo)) {
				System.out.println("El nombre del grupo no es válido. Intenta de nuevo.");
			}
		} while (!validarNombreGrupo(nombreGrupo));

		// Crear el objeto Grupo
		Grupo grupo = new Grupo(nombreGrupo);

		// Crear y devolver el objeto Alumno
		return new Alumno(nombre, apellidos, respuestaGenero, fechaNacimiento, ciclo, curso, grupo);
	}

	/**
	 * Recupera el número del grupo a partir de su nombre.
	 * 
	 * @param nombreGrupo El nombre del grupo.
	 * @return El numeroGrupo correspondiente o -1 si no existe.
	 */
	private int obtenerNumeroGrupo(String nombreGrupo) {
		String sql = "SELECT numeroGrupo FROM grupos WHERE nombreGrupo = ?";
		try (Connection conexionBD = DatabasePool.getConnection();
				PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
			sentencia.setString(1, nombreGrupo);

			try (ResultSet resultado = sentencia.executeQuery()) {
				if (resultado.next()) {
					return resultado.getInt("numeroGrupo");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al obtener el numeroGrupo: " + e.getMessage());
		}
		return -1; // Si no se encuentra el grupo, devolver -1
	}

	/**
	 * Valida si un nombre de grupo existe en la base de datos.
	 * 
	 * @param nombreGrupo El nombre del grupo a validar.
	 * @return true si el grupo existe, false en caso contrario.
	 */
	public boolean validarNombreGrupo(String nombreGrupo) {
		String sql = "SELECT * FROM grupos WHERE nombreGrupo = ?";
		try (Connection conexionBD = DatabasePool.getConnection();
				PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
			sentencia.setString(1, nombreGrupo);
			try (ResultSet resultado = sentencia.executeQuery()) {
				return resultado.next();
			}
		} catch (SQLException e) {
			System.out.println("Error al validar el grupo: " + e.getMessage());
			return false;
		}
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
				+ "FROM alumnos a " + "JOIN grupos g ON a.numeroGrupo = g.numeroGrupo " + "ORDER BY a.nia";

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

	/**
	 * Guarda todos los alumnos en un fichero de texto. La información de los
	 * alumnos, incluyendo el grupo al que pertenecen, se obtiene desde la base de
	 * datos y luego se escribe en un archivo de texto.
	 * 
	 * @param conexionBD Conexión a la base de datos MySQL.
	 */
	
	@Override
	public void guardarAlumnosEnFicheroTexto(Connection conexionBD) throws SQLException {
		// Definir el nombre del fichero de texto donde se guardarán los alumnos
		File fichero = new File("alumnos.txt");

		// Comprobamos si el fichero ya existe. Si es así, preguntamos al usuario si
		// desea sobreescribirlo
		if (fichero.exists()) {
			System.out.print("El fichero ya existe. ¿Desea sobreescribirlo? (S/N): ");
			char respuesta = sc.nextLine().charAt(0);
			if (respuesta != 'S' && respuesta != 's') {
				System.out.println("Operación cancelada. El fichero no se sobrescribirá.");
				return;
			}
		}

		// Preparamos el flujo de salida para escribir en el fichero de texto
		// Utilizamos un BufferedWriter para escribir en el fichero de texto.
		// Lo creamos con un FileWriter que apunta al fichero alumnos.txt.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero))) {

			// Preparamos la consulta SQL para obtener los datos de los alumnos y sus
			// grupos:

			/*
			 * Explicación de la consulta SQL:
			 * 
			 * SELECT: Indica que queremos obtener datos de la base de datos.
			 * 
			 * a.nia, a.nombre, a.apellidos, a.genero, a.fechaNacimiento, a.ciclo, a.curso:
			 * Son los campos que seleccionamos de la tabla alumnos.
			 * 
			 * El prefijo a es un alias para la tabla alumnos. g.nombreGrupo: Este es el
			 * nombre del grupo, que proviene de la tabla grupos.
			 * 
			 * El prefijo g es un alias para la tabla grupos.
			 * 
			 * Esta parte indica que vamos a trabajar con la tabla alumnos, que hemos
			 * abreviado como a para simplificar la consulta.
			 * 
			 * JOIN grupos g ON a.numeroGrupo = g.numeroGrupo:
			 * 
			 * JOIN: Es la palabra clave que indica que queremos combinar datos de dos
			 * tablas.
			 * 
			 * En este caso, estamos combinando alumnos con grupos.
			 * 
			 * a.numeroGrupo = g.numeroGrupo: La condición de la unión.
			 * 
			 * Queremos que se combinen los registros de alumnos y grupos cuando el valor de
			 * numeroGrupo en la tabla alumnos coincida con el valor de numeroGrupo en la
			 * tabla grupos.
			 * 
			 * ¿Por qué usamos JOIN?
			 * 
			 * Usamos un JOIN para obtener información de dos tablas relacionadas. En este
			 * caso, un alumno pertenece a un grupo, y para obtener el nombre del grupo,
			 * necesitamos combinar las tablas alumnos y grupos.
			 */
			String sql = "SELECT a.nia, a.nombre, a.apellidos, a.genero, a.fechaNacimiento, a.ciclo, a.curso, g.nombreGrupo "
					+ "FROM alumnos a " + "JOIN grupos g ON a.numeroGrupo = g.numeroGrupo";

			// Ejecutamos la consulta SQL
			try (PreparedStatement sentencia = conexionBD.prepareStatement(sql);
					ResultSet resultado = sentencia.executeQuery()) {

				// Escribimos los encabezados de las columnas en el fichero
				writer.write("NIA,Nombre,Apellidos,Género,Fecha Nacimiento,Ciclo,Curso,Nombre del Grupo");
				writer.newLine();

				// Recorremos los resultados de la consulta SQL
				while (resultado.next()) {
					// Recuperamos los datos de cada columna del resultado de la consulta
					int nia = resultado.getInt("nia");
					String nombre = resultado.getString("nombre");
					String apellidos = resultado.getString("apellidos");
					String genero = resultado.getString("genero");
					String fechaNacimiento = resultado.getString("fechaNacimiento");
					String ciclo = resultado.getString("ciclo");
					String curso = resultado.getString("curso");
					String nombreGrupo = resultado.getString("nombreGrupo");

					// Escribimos los datos de cada alumno en el fichero de texto
					writer.write(nia + "," + nombre + "," + apellidos + "," + genero + "," + fechaNacimiento + ","
							+ ciclo + "," + curso + "," + nombreGrupo);
					writer.newLine();
				}

				System.out.println("Datos de los alumnos guardados correctamente en el fichero 'alumnos.txt'.");
			} catch (SQLException e) {
				System.out.println("Error al ejecutar la consulta SQL: " + e.getMessage());
			}

		} catch (IOException e) {
			System.out.println("Error al escribir en el fichero: " + e.getMessage());
		}

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
	
	/**
	 * Inserta un nuevo grupo en la base de datos.
	 *
	 * @param conexionBD La conexión a la base de datos.
	 * @param grupo      El objeto Grupo que se desea insertar.
	 * @return true si la inserción fue exitosa, false en caso contrario.
	 */

	@Override
	public boolean insertarGrupo(Connection conexionBD, Grupo grupo) throws SQLException {
		String sql = "INSERT INTO grupos (nombreGrupo) VALUES (?)";

		try (PreparedStatement sentencia = conexionBD.prepareStatement(sql)) {
			sentencia.setString(1, grupo.getNombreGrupo().toUpperCase()); // Convertir nombre a mayúsculas

			int filasAfectadas = sentencia.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.out.println("Error al insertar el grupo: " + e.getMessage());
			return false;
		}
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
