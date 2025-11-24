package cr.ac.ucenfotec.helpdesku.tl;

import cr.ac.ucenfotec.helpdesku.bl.entities.*;
import cr.ac.ucenfotec.helpdesku.bl.logic.GestorHelpDesk;
import cr.ac.ucenfotec.helpdesku.dl.Repomemory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Controlador de la capa de presentación para el sistema HelpDesk U.
 * Permite la interacción con la lógica de negocio (GestorHelpDesk)
 * y proporciona métodos para registrar usuarios, departamentos, tickets
 * y gestionar diccionarios de emociones y palabras técnicas.
 */
public class ControladorHelpDesk {

    /**
     * Gestor de la capa de negocio que administra usuarios, tickets, departamentos y diccionarios.
     */
    private GestorHelpDesk gestor = new GestorHelpDesk();

    /**
     * Repositorio en memoria para la validación de datos únicos, como correos.
     */
    private Repomemory repositorio;

    /**
     * Constructor que inicializa el gestor de negocio.
     */
    public ControladorHelpDesk() {
        this.gestor = new GestorHelpDesk();
    }

    // ================== MÉTODOS PARA USUARIOS ==================

    /**
     * Registra un usuario en el sistema, aplicando hash a la contraseña.
     *
     * @param nombre Nombre del usuario.
     * @param password Contraseña del usuario.
     * @param rol Rol del usuario.
     * @param correo Correo electrónico.
     * @param telefono Número de teléfono.
     */
    public void registrarUsuario(String nombre, String password, String rol, String correo, String telefono) {
        String hashedPassword = this.hashPassword(password);
        Usuario usuario = new Usuario(nombre, hashedPassword, rol, correo, telefono);
        this.gestor.registrarUsuario(usuario);
    }

    /**
     * Autentica a un usuario usando correo y contraseña.
     *
     * @param correo Correo del usuario.
     * @param password Contraseña del usuario.
     * @return Usuario autenticado si las credenciales son correctas; null en caso contrario.
     */
    public Usuario autenticarUsuario(String correo, String password) {
        return gestor.autenticarUsuario(correo, password);
    }

    /**
     * Valida el nombre de un usuario asegurando longitud válida y no vacío.
     *
     * @param nombre Nombre a validar.
     */
    public void validadNombre(String nombre) {
        if (!nombre.trim().isEmpty() && nombre != null) {
            String nombreLimpio = nombre.trim();
            if (nombreLimpio.length() < 2 || nombreLimpio.length() > 50) {
                throw new IllegalArgumentException("El nombre debe tener entre 2 y 50 caracteres");
            }
        } else {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
    }

    /**
     * Valida el formato y longitud del correo electrónico.
     *
     * @param correo Correo a validar.
     */
    public void validarCorreo(String correo) {
        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Formato de correo inválido. Ejemplo: usuario@dominio.com");
        } else {
            this.validarCorreoUnico(correo);
            if (correo.length() > 100) {
                throw new IllegalArgumentException("El correo es demasiado largo");
            }
        }
    }

    /**
     * Valida que el correo sea único en el sistema.
     *
     * @param correo Correo a validar.
     */
    public void validarCorreoUnico(String correo) {
        if (this.repositorio.existeCorreo(correo.toLowerCase())) {
            throw new IllegalArgumentException("El correo ya existe en el sistema");
        }
    }

    /**
     * Aplica hash SHA-256 a una contraseña.
     *
     * @param password Contraseña en texto plano.
     * @return Contraseña hasheada en hexadecimal.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    /**
     * Devuelve todos los usuarios registrados.
     *
     * @return Lista de usuarios.
     */
    public List<Usuario> obtenerUsuarios() {
        return this.gestor.listarUsuarios();
    }

    // ================== MÉTODOS PARA DEPARTAMENTOS ==================

    /**
     * Registra un departamento en el sistema.
     *
     * @param nombre Nombre del departamento.
     * @param descripcion Descripción del departamento.
     * @param correo Correo electrónico del departamento.
     * @param extensionTelefono Teléfono del departamento.
     */
    public void registrarDepartamento(String nombre, String descripcion, String correo, String extensionTelefono) {
        Departamento departamento = new Departamento(nombre, descripcion, correo, extensionTelefono);
        this.gestor.registrarDepartamento(departamento);
    }

    /**
     * Devuelve todos los departamentos registrados.
     *
     * @return Lista de departamentos.
     */
    public List<Departamento> obtenerDepartamentos() {
        return this.gestor.listarDepartamentos();
    }

    // ================== MÉTODOS PARA TICKETS ==================

    /**
     * Registra un ticket en el sistema.
     *
     * @param asunto Asunto del ticket.
     * @param descripcion Descripción del ticket.
     * @param usuario Usuario que crea el ticket.
     * @param estado Estado inicial del ticket.
     * @param departamento Departamento asignado al ticket.
     */
    public void registrarTicket(String asunto, String descripcion, Usuario usuario, String estado, Departamento departamento) {
        if (usuario != null && departamento != null) {
            Ticket ticket = new Ticket(asunto, descripcion, usuario, estado, departamento);
            this.gestor.registrarTicket(ticket);
        }
    }

    /**
     * Devuelve todos los tickets registrados.
     *
     * @return Lista de tickets.
     */
    public List<Ticket> obtenerTickets() {
        return this.gestor.listarTickets();
    }

    // ================== MÉTODOS PARA DICCIONARIOS ==================

    /**
     * Agrega una palabra emocional al diccionario.
     *
     * @param emocion Tipo de emoción asociada.
     * @param palabra Palabra a agregar.
     */
    public void agregarPalabraEmocion(String emocion, String palabra) {
        DiccionarioEmocion entrada = new DiccionarioEmocion(palabra, emocion);
        this.gestor.agregarPalabraEmocion(entrada);
    }

    /**
     * Devuelve todas las palabras emocionales registradas.
     *
     * @return Lista de palabras emocionales.
     */
    public List<DiccionarioEmocion> obtenerDiccionarioEmociones() {
        return this.gestor.listarDiccionarioEmociones();
    }

    /**
     * Agrega una palabra técnica al diccionario.
     *
     * @param categoria Categoría técnica asociada.
     * @param palabra Palabra a agregar.
     */
    public void agregarPalabraTecnica(String categoria, String palabra) {
        DiccionarioTecnico entrada = new DiccionarioTecnico(palabra, categoria);
        this.gestor.agregarPalabraTecnica(entrada);
    }

    /**
     * Devuelve todas las palabras técnicas registradas.
     *
     * @return Lista de palabras técnicas.
     */
    public List<DiccionarioTecnico> obtenerDiccionarioTecnico() {
        return this.gestor.listarDiccionarioTecnico();
    }

    // ================== MÉTODOS DE BÚSQUEDA ==================

    /**
     * Devuelve un usuario por índice.
     *
     * @param index Índice del usuario.
     * @return Usuario correspondiente o null si no existe.
     */
    public Usuario getUsuario(int index) {
        return this.gestor.getUsuario(index);
    }

    /**
     * Devuelve un departamento por índice.
     *
     * @param index Índice del departamento.
     * @return Departamento correspondiente o null si no existe.
     */
    public Departamento getDepartamento(int index) {
        return this.gestor.getDepartamento(index);
    }
}
