
package cr.ac.ucenfotec.helpdesku.ui;

import cr.ac.ucenfotec.helpdesku.tl.ControladorHelpDesk;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Mesa de Ayuda Universitaria....");

        //UI interfaz = new UI();
        //interfaz.mostrarMenuPrincipal();

        ControladorHelpDesk controlador = new ControladorHelpDesk();

        UILogin uiLogin = new UILogin(controlador);
        uiLogin.mostrarMenuInicio();

        System.out.println("Sistema finalizado");
    }
}
