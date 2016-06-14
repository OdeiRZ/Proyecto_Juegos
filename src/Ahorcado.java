import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Clase Ahorcado que extiende de Frame e implementa ActionListener. 
 * Se encarga de generar la Interfaz Gráfica que usaremos para implementar 
 * los métodos de control y gestión para cargar el juego Ahorcado.
 *
 * @author Odei
 * @version 30.06.2014
 */
public class Ahorcado extends Frame {
    /**
     * Variable TextField usada para capturar texto durante el juego.
     */
    protected TextField letra;
    
    /**
     * Variables usadas para almacenar los textos mostrados durante el juego.
     */
    protected Label letras_usadas, txt_fin, truco, record, termino;
    
    /**
     * Variable Label usada para almacenar la duración del juego.
     */
    protected static Label tiempo;
    
    /**
     * Variable botón usada para interactuar con el usuario durante el juego.
     */
    protected Button introducir;
    
    /**
     * Variable Graphics usada para mostrar los errores del usuario en el juego.
     */
    protected Graphics g;
    
    /**
     * Variable booleana usada para controlar la finalización del juego.
     */
    protected boolean fin;
    
    /**
     * Variable booleana usada para mostrar el contador de tiempo de juego.
     */
    protected static boolean reloj_encendido;
    
    /**
     * Variable entera usada para almacenar los intentos durante el juego.
     */
    protected int intentos;
    
    /**
     * Variable usada para almacenar los caracteres a comparar durante el juego.
     */
    protected int comparar[];
    
    /**
     * Variable long usada para almacenar la hora de inicio del juego.
     */
    protected long ti;
    
    /**
     * Variables cadena usadas para almacenar el término seleccionado de juego.
     */
    protected String termino_aux, eleccion;
    
    /**
     * Variable array de cadenas usada para almacenar el listado de palabras 
     * disponibles a la hora de generar una nueva partida del juego Ahorcado.
     */
    protected final String palabras[] = {
        "Mozart", "Cervantes", "Nietzsche", "Kepler", "Darwin", "Aristoteles",
        "Chaplin", "Fawkes", "Beethoven", "Einstein", "Kennedy", "Tesla"};

    /**
     * Constructor de la Interfaz Gráfica Ahorcado.
     * Genera e inicializa la Interfaz y los elementos utilizados
     * para visualizar de forma interactiva la ejecución del juego Ahorcado.
     */
    public Ahorcado() { 
        Canvas dibujo = new Canvas();                                           // Creamos lienzo para dibujar las líneas del ahorcado
        dibujo.setSize(200, 150);
        setVisible(true);
        setSize(500, 300);                                                      // y le asignamos tamaño y demás parámetros a la ventana
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("¡¡Ahorcado con Personajes!!");                                // Le ponemos título
        setIconImage(Toolkit.getDefaultToolkit().createImage(
                                                    Inicio.ruta + "logo.png")); // y le asignamos un icono a la ventana
        Panel p = new Panel(new GridLayout(7, 1));                              // Creamos un panel para dibujar la interfaz gráfica
        Panel p1 = new Panel();
        Panel p2 = new Panel();
        Panel p3 = new Panel();
        letra = new TextField();                                                // Agregamos etiquetas, botones y demás elementos a la Interfaz
        tiempo = new Label("0");
        introducir = new Button("Comenzar");
        termino = new Label();
        letras_usadas = new Label();
        txt_fin = new Label();
        truco = new Label();
        record = new Label();
        
        p.add(truco);
        p.setBackground(Color.orange);
        truco.setForeground(Color.gray);
        p.add(record);
        record.setForeground(Color.red);
        p1.add(tiempo);
        p1.add(new Label("Introduce Letra: "));
        p1.add(letra);
        p1.add(introducir);
        p2.add(new Label("Personaje: "));
        p2.add(termino);
        p3.add(new Label("Letras Usadas"));
        p.add(p1);
        p.add(p2);
        p.add(p3);
        p.add(letras_usadas);
        letras_usadas.setForeground(Color.red);
        p.add(txt_fin);
        txt_fin.setForeground(Color.blue);
        add("East", dibujo);
        add("Center", p);
        g = dibujo.getGraphics();

        fin = false;
        intentos = 0;
        termino_aux = "";
        Reloj.segundos = 0;                                                     // Inicializamos demás variables
        reloj_encendido = false;
        eleccion = palabras[(int)(Math.random() * palabras.length)];            // Seleccionamos una palabra aleatoria
        for (int i = 0; i < eleccion.length(); i++) {
            termino_aux += "_ ";                                                // y rellenamos con espacios la palabra auxiliar que usaremos para comparar
        }
        termino.setText(termino_aux);
        comparar = new int[eleccion.length()];
        letra.setEnabled(false);
        introducir.addActionListener(al);
        
        MenuBar menubar = new MenuBar();                                        // Creamos un menú para operar con distintas opciones que habilitaremos
        Menu menu = new Menu("Opciones");                                       // Le ponemos nombre al menú
        String mi[] = {"Nuevo Juego", "Cambiar Canción", "Ver Record", "Salir"};
        for (String mi1 : mi) {
            menu.add(new MenuItem(mi1));                                        // Agregamos los elementos del menú
        }
        menubar.add(menu);
        menu.addActionListener(al);
        Menu menu1 = new Menu();
        menu1.add(new MenuItem(eleccion, new MenuShortcut(KeyEvent.VK_T)));     // Agregamos una opción oculta al menú para ver la palabra seleccionada
        menubar.add(menu1);
        menu1.addActionListener(al);
        setMenuBar(menubar);
        
        addWindowListener(new WindowAdapter() {
           /**
            * Método usado para capturar el cierre de la ventana actual.
            */
            @Override
            public void windowClosing(WindowEvent me) {
                dispose();                                                      // Cerramos el frame actual
                Inicio.frame.setVisible(true);                                  // y habilitamos la visibilidad del frame inicial
            } 
        });
    }

    /**
     * Creamos un escuchador de eventos para capturar las opciones
     * utilizadas durante la ejecución de la aplicación.
     */
    ActionListener al = new ActionListener()  {
        /**
         * Método usado para capturar el evento lanzado por el usuario.
         * @param evt ActionEvent: evento lanzado por el usuario
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("Nuevo Juego")) {                 // Si seleccionamos Nuevo Juego
                dispose();
                try { 
                    Ahorcado ahorcado = new Ahorcado();                         // Lanzamos una nueva instancia del juego Ahorcado
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Cambiar Canción")) {      // Si seleccionamos Cambiar Canción
                try {
                    Inicio.audio.close();
                    Inicio.audio.open(AudioSystem.getAudioInputStream(new File(
                                Inicio.ruta + "audio/" + Inicio.audios[(
                                Inicio.num_cancion == Inicio.audios.length - 1) 
                                ? 0 : ++Inicio.num_cancion] + ".wav")));        // Reproducimos siguiente canción
                    Inicio.audio.loop(Clip.LOOP_CONTINUOUSLY);                  // en forma de bucle
                } catch (Exception e) {
                    System.out.println("Error de audio: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Ver Record")) {           // Si seleccionamos Ver Record
                try {
                    verRecord();                                                // Obtenemos el record actual almacenado un fichero y lo mostramos
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Salir")) {                // Si seleccionamos Salir
                dispose();
                Inicio.frame.setVisible(true);                                  // Cerramos frame actual y habilitamos el inicial
            } else if (evt.getActionCommand().equals("Comenzar")) {             // Si seleccionamos Salir
                Reloj reloj = new Reloj();                                      // iniciamos el contador del reloj
                introducir.setLabel("Introducir");
                letra.setEnabled(true);                                         // habilitamos botón de envío
                ti = new Date().getTime();                                      // y obtenemos fecha actual 
            } else {
                if (evt.getActionCommand().equals(eleccion)) {                  // Si pulsamos la combinación de teclas para disparar el truco
                    truco.setText("                            Solución  ->  " + 
                                                                    eleccion);  // mostramos por pantalla la palabra seleccionada
                } else {
                    truco.setText("");
                }
                String caracter = null;
                if (Pattern.compile("^[A-Za-z]{1}$")
                                            .matcher(letra.getText()).find()) { // Si la letra intrucida es un único caracter entre la a y la z
                    caracter = letra.getText();/*.substring(0, 1);*/            // capturamos dicho caracter
                    termino_aux = letras_usadas.getText();
                    if (!termino_aux.contains(caracter)) {                      // y lo incluimos entre los usados si no se ha usado antes
                        termino_aux += caracter + ", ";
                    }
                    letras_usadas.setText(termino_aux);
                }
                if (caracter != null && 
                    eleccion.toLowerCase().contains(caracter.toLowerCase())) {  // Si el caracter no es nulo y esta entre las letras de la palabra
                    termino_aux = "";
                    fin = true;
                    for (int i = 0; i < comparar.length; i++) {
                        if (caracter.equalsIgnoreCase("" + eleccion.charAt(i))){
                            comparar[i] = 1;
                        }
                        if (comparar[i] == 1) {
                            termino_aux += eleccion.charAt(i) + " ";            // Actualizamos término a mostrar
                        } else {
                            termino_aux += "_ ";
                        }
                    }
                    termino.setText(termino_aux);
                    for (int j = 0; j <comparar.length; j++) {
                        if (comparar[j] != 1) {                                 // y comprobamos si se ha rellenado completamente
                            fin = false;
                        }
                    }
                    if (fin) {							// En caso afirmativo ganamos el juego
                        long duracion = (new Date().getTime() - ti) / 1000;
                        txt_fin.setText("     ¡¡Has Ganado con " + intentos +
                            " Errores en " + (duracion + 1) + " Segundos!!");// obteniendo el tiempo utilizado
                        try {
                            comprobarRecord(duracion);				// y comprobando si se ha batido el anterior record
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        introducir.setEnabled(false);
                        g.drawImage(new ImageIcon(Inicio.ruta + "Ahorcado/" + 
                                    eleccion + ".jpg").getImage(), 0, 0, null); // Mostramos imagen de personaje 
                        reloj_encendido = false;
                    }
                } else if (Pattern.compile("^[A-Za-z]{1}$")
                                            .matcher(letra.getText()).find()) { // Si la letra intrucida es un único caracter entre la a y la z
                    switch (++intentos) {                                       // Dibujamos el número de error en pantalla
                        case 1:	g.fillRect(100, 230, 25, 5);                    // Palos
                        break;	
                        case 2:	g.fillRect(110, 40, 5, 190);                    // Palos	
                        break;
                        case 3:	g.fillRect(60, 40, 50, 5);                      // Palos		
                        break;
                        case 4:	g.fillRect(58, 40, 5, 20);                      // Palos		
                        break;
                        case 5:	g.drawOval(40, 60, 40, 40);                     // Palos	
                        break;
                        case 6:	g.drawLine(62, 100, 62, 170);                   // Palos	
                        break;
                        case 7:	g.drawLine(61, 110, 40, 130);                   // Palos	
                        break;
                        case 8:	g.drawLine(63, 110, 80, 130);                   // Palos	
                        break;
                        case 9:	g.drawLine(61, 171, 40, 210);                   // Palos	
                        break;
                        case 10:    g.drawLine(63, 171, 80, 210);               // Palos
                                    termino_aux = "";
                                    for (int i = 0; i < eleccion.length(); i++) {
                                        termino_aux += eleccion.charAt(i) + " ";
                                    }
                                    termino.setText(termino_aux);
                                    txt_fin.setText("          ¡¡Has Perdido con " + 
                                            intentos + " Intentos erroneos!!"); // Si llegamos a 10 intentos perdemos el juego
                                    g.drawImage(new ImageIcon(Inicio.ruta + 
                                            "Ahorcado/" + eleccion + ".jpg")
                                            .getImage(), 0, 0, null);           // y mostramos la imagen or pantalla del personaje
                                    introducir.setEnabled(false);               //letra.setEditable(false);
                                    reloj_encendido = false;
                    }
                }
                letra.setText("");						// Borramos caracter introducido en Textfield
                letra.requestFocusInWindow();					// y volvemos a posicionarnos en Textfield
            }
        }
    };
    
    /**
     * Método usado para comprobar si existe un nuevo record para el usuario.
     * @param duracion long: segundos de duración de la partida
     */
    private void comprobarRecord(long duracion) throws IOException {
        String segundos = new BufferedReader(new FileReader(
                            Inicio.ruta + "Ahorcado/ahorcado.txt")).readLine();	// Obtenemos record actual
        if (duracion < Long.parseLong(segundos)) {                              // y lo comparamos con el nuevo
            record.setText("         ¡¡Record batido, tienes la Mejor marca!!");
            PrintWriter out = new PrintWriter(new FileWriter(
                                        Inicio.ruta + "Ahorcado/ahorcado.txt")); // Si se bate el record lo guardamos
            out.print(duracion);
            out.flush();
            out.close();
        } else {
            record.setText("         ¡¡Record no batido, sigue intentandolo!!");// En caso contrario mostramos un mensaje
        }
    }
    
    /**
     * Método usado para lanzar una ventana donde veremos el record actual.
     */
    private void verRecord() throws IOException {
        String segundos = new BufferedReader(new FileReader(
                Inicio.ruta + "Ahorcado/ahorcado.txt")).readLine();             // Obtenemos el record actual
        JOptionPane.showMessageDialog(this, "¡¡ El Record esta en " + segundos +
                " Segundos !!","Mejor Tiempo", JOptionPane.INFORMATION_MESSAGE);// y lo mostramos en una ventana de diálogo
    }
    
    /**
     * Método que hará las veces de contador de tiempo durante el juego.
     */
    public static class Reloj implements Runnable {
        /**
         * Variable entera usada para almacenar los segundos de juego.
         */
        protected static int segundos;
        
        /**
         * Método que activa el reloj e inicia el contador de tiempo de juego.
         */
        public Reloj() {
            reloj_encendido = true;
            new Thread(this).start();
        }
        
        /**
         * Método que actualiza el contador de tiempo de juego en pantalla.
         */
        @Override
        public void run() {
            while (reloj_encendido) {
                try {
                    Thread.sleep(1000);                                         // Dormimos 1 segundo el hilo
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                tiempo.setText("" + (++segundos));                              // y actualizamos el valor por pantalla
            }
        }
    }
}
