package cr.ac.ucenfotec.helpdesku.ui;

import cr.ac.ucenfotec.helpdesku.bl.entities.Usuario;
import cr.ac.ucenfotec.helpdesku.tl.ControladorHelpDesk;
import java.util.List;
import java.util.Scanner;

public class UILogin {
    private ControladorHelpDesk controlador;
    private Scanner scanner;

    public UILogin(ControladorHelpDesk controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenuInicio() {
        int opcion;
        do {
            System.out.println("\n" + "═".repeat(60));
            System.out.println("           SISTEMA HELP DESK UNIVERSITARIO");
            System.out.println("═".repeat(60));
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar Sesión");
            System.out.println("3. Salir");
            System.out.println("─".repeat(40));
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> registrarUsuario();
                case 2 -> iniciarSesion();
                case 3 -> System.out.println("\n Hasta pronto!");
                default -> System.out.println("Opción inválida. Por favor seleccione 1-3.");
            }
        } while (opcion != 3);
    }

    private void registrarUsuario() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("          REGISTRO DE NUEVO USUARIO");
        System.out.println("─".repeat(50));

        String nombre = solicitarNombre();
        String correo = solicitarCorreo();
        String telefono = solicitarTelefono();
        String rol = solicitarRol();
        String password = solicitarPassword();

        try {
            // Verificar si el correo ya existe
            if (correoExiste(correo)) {
                System.out.println("Error: El correo electrónico ya está registrado");
                return;
            }

            controlador.registrarUsuario(nombre, password, rol, correo, telefono);
            System.out.println("Usuario registrado exitosamente");
            System.out.println("\n Resumen del registro:");
            System.out.println("   ├─ Nombre: " + nombre);
            System.out.println("   ├─ Correo: " + correo);
            System.out.println("   ├─ Teléfono: " + telefono);
            System.out.println("   └─ Rol: " + rol);
        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
    }

    private void iniciarSesion() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("            INICIO DE SESIÓN");
        System.out.println("─".repeat(50));

        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        Usuario usuario = autenticarUsuario(correo, password);

        if (usuario != null) {
            System.out.println("Inicio de sesión exitoso!");
            System.out.println("Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");

            // Redirigir al menú principal
            UI interfaz = new UI(controlador);
            interfaz.mostrarMenuPrincipal();
        } else {
            System.out.println("Credenciales incorrectas. Por favor intente nuevamente.");
        }
    }

    private boolean correoExiste(String correo) {
        List<Usuario> usuarios = controlador.obtenerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    private Usuario autenticarUsuario(String correo, String password) {
        // Solo delegar al controlador, SIN lógica de negocio
        return controlador.autenticarUsuario(correo, password);
    }

    // Métodos de validación (copiados de UIUsuario)
    private String solicitarNombre() {
        String nombre;
        boolean valido;
        do {
            valido = true;
            System.out.print("Ingrese el nombre completo: ");
            nombre = scanner.nextLine().trim();

            if (nombre.isEmpty() || nombre.length() < 2 || nombre.length() > 50 || nombre.matches(".*\\d.*")) {
                System.out.println("Nombre inválido. Debe tener entre 2-50 caracteres y no contener números.");
                valido = false;
            }
        } while (!valido);
        return nombre;
    }

    private String solicitarCorreo() {
        String correo;
        boolean valido;
        do {
            valido = true;
            System.out.print("Ingrese el correo electrónico: ");
            correo = scanner.nextLine();

            if (correo.isEmpty() || correo.length() < 6 || correo.length() > 100 || !correo.contains("@") || !correo.contains(".")) {
                System.out.println("Correo inválido. Formato esperado: ejemplo@dominio.com");
                valido = false;
            }
        } while (!valido);
        return correo;
    }

    private String solicitarRol() {
        String rol;
        boolean valido;
        do {
            valido = true;
            System.out.print("Ingrese el rol (estudiante/funcionario/administrador): ");
            rol = scanner.nextLine().toLowerCase();

            if (!rol.equals("estudiante") && !rol.equals("funcionario") && !rol.equals("administrador")) {
                System.out.println("Rol inválido. Opciones válidas: estudiante, funcionario, administrador.");
                valido = false;
            }
        } while (!valido);
        return rol;
    }

    private String solicitarPassword() {
        String password;
        boolean valido;
        do {
            valido = true;
            System.out.print("Ingrese la contraseña: ");
            password = scanner.nextLine().trim();

            if (password.length() < 8 ||
                    !password.matches(".*[A-Z].*") ||
                    !password.matches(".*[a-z].*") ||
                    !password.matches(".*[0-9].*") ||
                    !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                System.out.println("La contraseña debe tener:");
                System.out.println("   - Mínimo 8 caracteres");
                System.out.println("   - Al menos una mayúscula");
                System.out.println("   - Al menos una minúscula");
                System.out.println("   - Al menos un número");
                System.out.println("   - Al menos un carácter especial");
                valido = false;
            }
        } while (!valido);
        return password;
    }

    private String solicitarTelefono() {
        String telefono;
        boolean valido;
        do {
            valido = true;
            System.out.print("Ingrese el número de teléfono (8 dígitos): ");
            telefono = scanner.nextLine().trim();

            String soloDigitos = telefono.replaceAll("[^\\d]", "");
            if (soloDigitos.length() != 8) {
                System.out.println("El teléfono debe tener exactamente 8 dígitos numéricos.");
                valido = false;
            }
        } while (!valido);
        return telefono;
    }
}