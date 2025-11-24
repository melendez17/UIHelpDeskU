package cr.ac.ucenfotec.helpdesku.ui;

import cr.ac.ucenfotec.helpdesku.bl.entities.*;
import cr.ac.ucenfotec.helpdesku.tl.ControladorHelpDesk;
import java.util.List;
import java.util.Scanner;

public class UI {
    private Scanner scanner;
    private ControladorHelpDesk controlador;

    public UI(ControladorHelpDesk controlador) {
        this.scanner = new Scanner(System.in);
        this.controlador = controlador;
    }

    // Métodos de utilidad para formato
    private static void printHeader(String title) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("   " + title);
        System.out.println("═".repeat(60));
    }

    private static void printSubHeader(String title) {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("  " + title);
        System.out.println("─".repeat(50));
    }

    private static void printSuccess(String message) {
        System.out.println("Exito: " + message);
    }

    private static void printError(String message) {
        System.out.println("Error: " + message);
    }

    private static void printInfo(String message) {
        System.out.println("Atención: " + message);
    }

    public void mostrarMenuPrincipal() {
        int opcion;
        do {
            printHeader("SISTEMA HELP DESK U");
            System.out.println("1. Gestión de Usuarios");
            System.out.println("2. Gestión de Departamentos");
            System.out.println("3. Gestión de Tickets");
            System.out.println("4. Gestión de Diccionarios");
            System.out.println("5. Salir");
            System.out.println("─".repeat(40));
            System.out.print("Seleccione una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> new UIUsuario(controlador).mostrarMenu();
                case 2 -> new UIDepartamento(controlador).mostrarMenu();
                case 3 -> new UITicket(controlador).mostrarMenu();
                case 4 -> new UIDiccionario(controlador).mostrarMenu();
                case 5 -> System.out.println("\n ¡Hasta pronto!");
                default -> printError("Opción inválida. Por favor seleccione 1-5.");
            }
        } while (opcion != 5);
    }

    // ===================== CLASE USUARIO =====================
    public static class UIUsuario {
        private ControladorHelpDesk controlador;
        private Scanner scanner;

        public UIUsuario(ControladorHelpDesk controlador) {
            this.controlador = controlador;
            this.scanner = new Scanner(System.in);
        }

        public void mostrarMenu() {
            int opcion;
            do {
                printHeader("GESTIÓN DE USUARIOS");
                System.out.println("1. Registrar usuario");
                System.out.println("2. Listar usuarios");
                System.out.println("3. Volver al menú principal");
                System.out.println("─".repeat(40));
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> registrarUsuario();
                    case 2 -> listarUsuarios();
                    case 3 -> printInfo("Volviendo al menú principal...");
                    default -> printError("Opción inválida");
                }
            } while (opcion != 3);
        }

        private void registrarUsuario() {
            printSubHeader("REGISTRAR NUEVO USUARIO");
            String nombre = solicitarNombre();
            String correo = solicitarCorreo();
            String telefono = solicitarTelefono();
            String rol = solicitarRol();
            String password = solicitarPassword();

            try {
                controlador.registrarUsuario(nombre, password, rol, correo, telefono);
                printSuccess("Usuario registrado exitosamente");
                System.out.println("\n Resumen del registro:");
                System.out.println("   ├─ Nombre: " + nombre);
                System.out.println("   ├─ Correo: " + correo);
                System.out.println("   ├─ Teléfono: " + telefono);
                System.out.println("   └─ Rol: " + rol);
            } catch (Exception e) {
                printError("Error al registrar usuario: " + e.getMessage());
            }
        }

        private void listarUsuarios() {
            printSubHeader("LISTA DE USUARIOS REGISTRADOS");
            List<Usuario> usuarios = controlador.obtenerUsuarios();

            if (usuarios.isEmpty()) {
                printInfo("No hay usuarios registrados en el sistema");
            } else {
                System.out.println("┌─────┬──────────────────────┬──────────────────────────┬────────────────┐");
                System.out.println("│ No. │ Nombre               │ Correo                   │ Rol            │");
                System.out.println("├─────┼──────────────────────┼──────────────────────────┼────────────────┤");

                for (int i = 0; i < usuarios.size(); i++) {
                    Usuario usuario = usuarios.get(i);
                    System.out.printf("│ %-3d │ %-20s │ %-24s │ %-14s │%n",
                            (i + 1),
                            truncarTexto(usuario.getNombre(), 20),
                            truncarTexto(usuario.getCorreo(), 24),
                            usuario.getRol());
                }
                System.out.println("└─────┴──────────────────────┴──────────────────────────┴────────────────┘");
                System.out.printf("Total de usuarios: %d%n", usuarios.size());
            }
        }

        private String truncarTexto(String texto, int longitud) {
            if (texto.length() > longitud) {
                return texto.substring(0, longitud - 3) + "...";
            }
            return texto;
        }

        private String solicitarNombre() {
            String nombre;
            boolean valido;
            do {
                valido = true;
                System.out.print("\n Ingrese el nombre completo: ");
                nombre = scanner.nextLine().trim();

                if (nombre.isEmpty() || nombre.length() < 2 || nombre.length() > 50 || nombre.matches(".*\\d.*")) {
                    printError("Nombre inválido. Debe tener entre 2-50 caracteres y no contener números.");
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
                    printError("Correo inválido. Formato esperado: ejemplo@dominio.com");
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
                    printError("Rol inválido. Opciones válidas: estudiante, funcionario, administrador.");
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
                    printError("La contraseña debe tener:\n   - Mínimo 8 caracteres\n   - Al menos una mayúscula\n   - Al menos una minúscula\n   - Al menos un número\n   - Al menos un carácter especial");
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
                    printError("El teléfono debe tener exactamente 8 dígitos numéricos.");
                    valido = false;
                }
            } while (!valido);
            return telefono;
        }
    }

    // ===================== CLASE DEPARTAMENTO =====================
    public static class UIDepartamento {
        private ControladorHelpDesk controlador;
        private Scanner scanner;

        public UIDepartamento(ControladorHelpDesk controlador) {
            this.controlador = controlador;
            this.scanner = new Scanner(System.in);
        }

        public void mostrarMenu() {
            int opcion;
            do {
                printHeader("GESTIÓN DE DEPARTAMENTOS");
                System.out.println("1. Registrar departamento");
                System.out.println("2. Listar departamentos");
                System.out.println("3. Volver al menú principal");
                System.out.println("─".repeat(40));
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> registrarDepartamento();
                    case 2 -> listarDepartamentos();
                    case 3 -> printInfo("Volviendo al menú principal...");
                    default -> printError("Opción inválida");
                }
            } while (opcion != 3);
        }

        private void registrarDepartamento() {
            printSubHeader("REGISTRAR NUEVO DEPARTAMENTO");
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();
            System.out.print("Correo (opcional): ");
            String correo = scanner.nextLine();
            System.out.print("Extensión telefónica (opcional): ");
            String extension = scanner.nextLine();

            try {
                controlador.registrarDepartamento(nombre, descripcion, correo, extension);
                printSuccess("Departamento registrado exitosamente");
            } catch (Exception e) {
                printError("Error al registrar departamento: " + e.getMessage());
            }
        }

        private void listarDepartamentos() {
            printSubHeader("LISTA DE DEPARTAMENTOS");
            List<Departamento> lista = controlador.obtenerDepartamentos();
            if (lista.isEmpty()) {
                printInfo("No hay departamentos registrados.");
            } else {
                System.out.println("┌─────┬──────────────────────┬────────────────────────────┬──────────────────────┬────────────┐");
                System.out.println("│ No. │ Nombre               │ Descripción                │ Correo               │ Extensión  │");
                System.out.println("├─────┼──────────────────────┼────────────────────────────┼──────────────────────┼────────────┤");

                for (int i = 0; i < lista.size(); i++) {
                    Departamento d = lista.get(i);
                    System.out.printf("│ %-3d │ %-20s │ %-26s │ %-20s │ %-10s │%n",
                            (i + 1),
                            truncarTexto(d.getNombre(), 20),
                            truncarTexto(d.getDescripcion(), 26),
                            truncarTexto(d.getCorreo() != null ? d.getCorreo() : "N/A", 20),
                            d.getExtensionTelefono() != null ? d.getExtensionTelefono() : "N/A");
                }
                System.out.println("└─────┴──────────────────────┴────────────────────────────┴──────────────────────┴────────────┘");
                System.out.printf("Total de departamentos: %d%n", lista.size());
            }
        }

        private String truncarTexto(String texto, int longitud) {
            if (texto == null) return "N/A";
            if (texto.length() > longitud) {
                return texto.substring(0, longitud - 3) + "...";
            }
            return texto;
        }
    }

    // ===================== CLASE DICCIONARIO =====================
    public static class UIDiccionario {
        private ControladorHelpDesk controlador;
        private Scanner scanner;

        public UIDiccionario(ControladorHelpDesk controlador) {
            this.controlador = controlador;
            this.scanner = new Scanner(System.in);
        }

        public void mostrarMenu() {
            int opcion;
            do {
                printHeader("GESTIÓN DE DICCIONARIOS");
                System.out.println("1. Diccionario Emocional");
                System.out.println("2. Diccionario Técnico");
                System.out.println("3. Volver al menú principal");
                System.out.println("─".repeat(40));
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> gestionarDiccionarioEmocional();
                    case 2 -> gestionarDiccionarioTecnico();
                    case 3 -> printInfo("Volviendo al menú principal...");
                    default -> printError("Opción inválida");
                }
            } while (opcion != 3);
        }

        private void gestionarDiccionarioEmocional() {
            int opcion;
            do {
                printSubHeader("DICCIONARIO EMOCIONAL");
                System.out.println("1. Agregar palabra emocional");
                System.out.println("2. Listar palabras emocionales");
                System.out.println("3. Volver al menú de diccionarios");
                System.out.println("─".repeat(40));
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> agregarPalabraEmocional();
                    case 2 -> listarDiccionarioEmocional();
                    case 3 -> printInfo("Volviendo...");
                    default -> printError("Opción inválida");
                }
            } while (opcion != 3);
        }

        private void gestionarDiccionarioTecnico() {
            int opcion;
            do {
                printSubHeader("DICCIONARIO TÉCNICO");
                System.out.println("1. Agregar palabra técnica");
                System.out.println("2. Listar palabras técnicas");
                System.out.println("3. Volver al menú de diccionarios");
                System.out.println("─".repeat(40));
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> agregarPalabraTecnica();
                    case 2 -> listarDiccionarioTecnico();
                    case 3 -> printInfo("Volviendo...");
                    default -> printError("Opción inválida");
                }
            } while (opcion != 3);
        }

        private void agregarPalabraEmocional() {
            printSubHeader("AGREGAR PALABRA EMOCIONAL");
            System.out.print("Palabra: ");
            String palabra = scanner.nextLine();
            System.out.print("Emoción (Frustración/Urgencia/Neutralidad/Positivo): ");
            String emocion = scanner.nextLine();

            try {
                controlador.agregarPalabraEmocion(palabra, emocion);
                printSuccess("Palabra emocional agregada correctamente");
            } catch (Exception e) {
                printError("Error al agregar palabra: " + e.getMessage());
            }
        }

        private void listarDiccionarioEmocional() {
            printSubHeader("PALABRAS EMOCIONALES");
            List<DiccionarioEmocion> palabras = controlador.obtenerDiccionarioEmociones();
            if (palabras.isEmpty()) {
                printInfo("No hay palabras registradas en el diccionario emocional.");
            } else {
                System.out.println("┌─────┬──────────────────────┬──────────────────┐");
                System.out.println("│ No. │ Palabra              │ Emoción          │");
                System.out.println("├─────┼──────────────────────┼──────────────────┤");

                for (int i = 0; i < palabras.size(); i++) {
                    DiccionarioEmocion p = palabras.get(i);
                    System.out.printf("│ %-3d │ %-20s │ %-16s │%n",
                            (i + 1),
                            truncarTexto(p.getPalabra(), 20),
                            p.getClasificacion());
                }
                System.out.println("└─────┴──────────────────────┴──────────────────┘");
                System.out.printf("Total de palabras: %d%n", palabras.size());
            }
        }

        private void agregarPalabraTecnica() {
            printSubHeader("AGREGAR PALABRA TÉCNICA");
            System.out.print("Palabra: ");
            String palabra = scanner.nextLine();
            System.out.print("Categoría (Redes/Impresoras/Cuentas/Hardware): ");
            String categoria = scanner.nextLine();

            try {
                controlador.agregarPalabraTecnica(palabra, categoria);
                printSuccess("Palabra técnica agregada correctamente");
            } catch (Exception e) {
                printError("Error al agregar palabra: " + e.getMessage());
            }
        }

        private void listarDiccionarioTecnico() {
            printSubHeader("PALABRAS TÉCNICAS");
            List<DiccionarioTecnico> palabras = controlador.obtenerDiccionarioTecnico();
            if (palabras.isEmpty()) {
                printInfo("No hay palabras registradas en el diccionario técnico.");
            } else {
                System.out.println("┌─────┬──────────────────────┬──────────────────┐");
                System.out.println("│ No. │ Palabra              │ Categoría        │");
                System.out.println("├─────┼──────────────────────┼──────────────────┤");

                for (int i = 0; i < palabras.size(); i++) {
                    DiccionarioTecnico p = palabras.get(i);
                    System.out.printf("│ %-3d │ %-20s │ %-16s │%n",
                            (i + 1),
                            truncarTexto(p.getPalabra(), 20),
                            p.getClasificacion());
                }
                System.out.println("└─────┴──────────────────────┴──────────────────┘");
                System.out.printf("Total de palabras: %d%n", palabras.size());
            }
        }

        private String truncarTexto(String texto, int longitud) {
            if (texto == null) return "";
            if (texto.length() > longitud) {
                return texto.substring(0, longitud - 3) + "...";
            }
            return texto;
        }
    }

    // ===================== CLASE TICKET =====================
    public static class UITicket {
        private ControladorHelpDesk controlador;
        private Scanner scanner;

        public UITicket(ControladorHelpDesk controlador) {
            this.controlador = controlador;
            this.scanner = new Scanner(System.in);
        }

        public void mostrarMenu() {
            int opcion;
            do {
                printHeader("GESTIÓN DE TICKETS");
                System.out.println("1. Registrar ticket");
                System.out.println("2. Listar tickets");
                System.out.println("3. Volver al menú principal");
                System.out.println("─".repeat(40));
                System.out.print("Seleccione una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> registrarTicket();
                    case 2 -> listarTickets();
                    case 3 -> printInfo("Volviendo al menú principal...");
                    default -> printError("Opción inválida");
                }
            } while (opcion != 3);
        }

        private void registrarTicket() {
            printSubHeader("REGISTRAR NUEVO TICKET");
            List<Usuario> usuarios = controlador.obtenerUsuarios();
            if (usuarios.isEmpty()) {
                printError("No hay usuarios registrados. Debe registrar al menos un usuario primero.");
                return;
            }

            System.out.println("\nSeleccione un usuario:");
            System.out.println("┌─────┬──────────────────────┬──────────────────────────┐");
            System.out.println("│ No. │ Nombre               │ Correo                   │");
            System.out.println("├─────┼──────────────────────┼──────────────────────────┤");
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario usuario = usuarios.get(i);
                System.out.printf("│ %-3d │ %-20s │ %-24s │%n",
                        (i + 1),
                        truncarTexto(usuario.getNombre(), 20),
                        truncarTexto(usuario.getCorreo(), 24));
            }
            System.out.println("└─────┴──────────────────────┴──────────────────────────┘");
            System.out.print("Número de usuario: ");
            int idxUsuario = scanner.nextInt() - 1;
            scanner.nextLine();

            if (idxUsuario < 0 || idxUsuario >= usuarios.size()) {
                printError("Selección inválida.");
                return;
            }
            Usuario usuario = usuarios.get(idxUsuario);

            List<Departamento> departamentos = controlador.obtenerDepartamentos();
            if (departamentos.isEmpty()) {
                printError("No hay departamentos registrados. Debe registrar al menos un departamento primero.");
                return;
            }

            System.out.println("\nSeleccione un departamento:");
            System.out.println("┌─────┬──────────────────────┬────────────────────────────┐");
            System.out.println("│ No. │ Nombre               │ Descripción                │");
            System.out.println("├─────┼──────────────────────┼────────────────────────────┤");
            for (int i = 0; i < departamentos.size(); i++) {
                Departamento depto = departamentos.get(i);
                System.out.printf("│ %-3d │ %-20s │ %-26s │%n",
                        (i + 1),
                        truncarTexto(depto.getNombre(), 20),
                        truncarTexto(depto.getDescripcion(), 26));
            }
            System.out.println("└─────┴──────────────────────┴────────────────────────────┘");
            System.out.print("Número de departamento: ");
            int idxDepto = scanner.nextInt() - 1;
            scanner.nextLine();

            if (idxDepto < 0 || idxDepto >= departamentos.size()) {
                printError("Selección inválida.");
                return;
            }
            Departamento depto = departamentos.get(idxDepto);

            System.out.print("Asunto: ");
            String asunto = scanner.nextLine();
            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();
            String estado = "Pendiente";

            try {
                controlador.registrarTicket(asunto, descripcion, usuario, estado, depto);
                printSuccess("Ticket registrado correctamente");
                System.out.println("\n Resumen del ticket:");
                System.out.println("   ├─ Asunto: " + asunto);
                System.out.println("   ├─ Usuario: " + usuario.getNombre());
                System.out.println("   ├─ Departamento: " + depto.getNombre());
                System.out.println("   └─ Estado: " + estado);
            } catch (Exception e) {
                printError("Error al registrar ticket: " + e.getMessage());
            }
        }

        private void listarTickets() {
            printSubHeader("LISTA DE TICKETS");
            List<Ticket> tickets = controlador.obtenerTickets();
            if (tickets.isEmpty()) {
                printInfo("No hay tickets registrados.");
            } else {
                System.out.println("┌─────┬──────────────────────┬──────────────────────┬──────────────┬──────────────────────┐");
                System.out.println("│ No. │ Asunto               │ Usuario              │ Estado       │ Departamento         │");
                System.out.println("├─────┼──────────────────────┼──────────────────────┼──────────────┼──────────────────────┤");

                for (int i = 0; i < tickets.size(); i++) {
                    Ticket t = tickets.get(i);
                    //String estadoIcon = getEstadoIcon(t.getEstado());
                    System.out.printf("│ %-3d │ %-20s │ %-20s │ %-12s │ %-20s │%n",
                            (i + 1),
                            truncarTexto(t.getAsunto(), 20),
                            truncarTexto(t.getUsuario().getNombre(), 20),
                            t.getEstado(),
                            truncarTexto(t.getDepartamento().getNombre(), 20));
                }
                System.out.println("└─────┴──────────────────────┴──────────────────────┴──────────────┴──────────────────────┘");
                System.out.printf("Total de tickets: %d%n", tickets.size());
            }
        }

        private String truncarTexto(String texto, int longitud) {
            if (texto == null) return "";
            if (texto.length() > longitud) {
                return texto.substring(0, longitud - 3) + "...";
            }
            return texto;
        }
    }
}