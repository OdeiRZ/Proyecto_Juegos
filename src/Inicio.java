import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Clase Inicio que extiende de JFrame. 
 * Se encarga de generar la Interfaz Gráfica Inicial que usaremos para 
 * implementar los métodos de control para cargar los diferentes juegos y demás.
 *
 * @author Odei
 * @version 30.06.2014
 */
public class Inicio extends JFrame {
    /**
     * Variable usada para almacenar el Frame de la Interfaz de Inicio.
     */
    protected static JFrame frame;
    
    /**
     * Variable botón usada para lanzar el juego seleccionado por el Usuario.
     */
    protected JButton btn;
    
    /**
     * Variable JLabel usada para mostrar el juego seleccionado por el Usuario.
     */
    protected JLabel img;
    
    /**
     * Variable JComboBox usada para almacenar el listado de juegos disponibles.
     */
    protected final JComboBox juegos;
    
    /**
     * Variable Clip usada para almacenar la canción que sonara de fondo 
     * mientras navegamos por las diferentes interfaces y juegos.
     */
    protected static Clip audio;
    
    /**
     * Variable entera usada para almacenar el número de canción reproducida.
     */
    protected static int num_cancion;
    
    /**
     * Variable de tipo cadena usada para almacenar la Ruta del equipo 
     * donde estarán almacenados los ficheros utilizados por el juego.
     */
    protected static final String ruta = "src/recursos/";
    
    /**
     * Variable array de cadenas usada para almacenar el listado de canciones 
     * disponibles almacenadas en /recursos/audio.
     */
    protected final static String audios[] = {
        "Whale Trail", "Star Light", "Water Melody", "Intermission", "Mango" };
    
    /**
     * Variable array de cadenas usada para almacenar el listado de juegos 
     * disponibles pertenecientes a cada una de las carpetas en /recursos.
     */
    protected final String nombres[] = {
        "                 >> Elije un Juego <<", "7 Diferencias", 
                                        "Ahorcado", "Oráculo" };
    
    /**
     * Constructor de la Interfaz Gráfica inicial.
     * Genera e inicializa la Interfaz y los elementos utilizados
     * para visualizar de forma interactiva la ejecución de la Aplicación.
     */
    public Inicio() {
        JPanel panel = new JPanel(null);                                        // Creamos un panel para dibujar la interfaz gráfica
        JMenuBar mbar = new JMenuBar();                                         // a su vez creamos un menú para operar con distintas opciones que habilitaremos
        JMenu menu = new JMenu("Opciones");                                     // Le ponemos nombre al menú
        btn = new JButton("", new ImageIcon(ruta + "Botones.png"));
        img = new JLabel(new ImageIcon(ruta + "Consola.png"));                  // Agregamos etiquetas, botones y demás elementos a la Interfaz
        juegos = new JComboBox(nombres);
        
        panel.add(img).setBounds(5, 5, 300, 450);
        panel.add(btn).setBounds(95, 365, 100, 50);                             // Posicionamos elementos en pantalla
        panel.add(juegos).setBounds(35, 465, 240, 40);

        juegos.addActionListener(al);
        btn.addActionListener(al);
        btn.setEnabled(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        img.setBorder(new LineBorder(Color.black, 1, true));

        String nombre_menuItem[] = {"Cambiar Canción", "Salir"};
        for (String mi : nombre_menuItem) {
            JMenuItem m = new JMenuItem(mi);                                    // Agregamos los elementos del menú
            menu.add(m).setAccelerator(KeyStroke.getKeyStroke(mi.charAt(0), 
                                                    KeyEvent.CTRL_DOWN_MASK));  // y una tecla asociada a cada uno de ellos
            m.addActionListener(al);
        }
        mbar.add(menu);

        try {
            audio = AudioSystem.getClip();
            audio.open(AudioSystem.getAudioInputStream(new File(
                            ruta + "audio/" + audios[num_cancion] + ".wav")));  // Cargamos canción inicial
            audio.loop(Clip.LOOP_CONTINUOUSLY);                                 // y la reproducimos en bucle
        } catch (Exception e) {
            System.out.println("Error de audio: " + e.getMessage());
        }

        frame = new JFrame("Máquina del Tiempo");                               // Creamos JFrame y le ponemos título
        frame.add(panel);                                                       // agregando el panel previamente creado
        frame.setJMenuBar(mbar);                                                // y la barra del menú
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage(
                                                            ruta + "logo.png"));// Le ponemos un icono a la ventana
        frame.setResizable(false);
        frame.setSize(320, 570);                                                // y le asignamos tamaño y demás parámetros
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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
            if (evt.getSource()instanceof JComboBox) {                          // Si el evento es producido por el JComboBox
                if (!juegos.getSelectedItem().toString()
                                                .contains("Elije un Juego")) {  // Si no seleccionamos la primera opción Elije un Juego
                    try {
                        Clip seleccion = AudioSystem.getClip();
                        seleccion.open(AudioSystem.getAudioInputStream(
                                    new File(ruta + "audio/seleccion.wav")));   // Reproducimos sonido de selección
                        seleccion.loop(0);
                    } catch (Exception e) {
                        System.out.println("Error de audio: " + e.getMessage());
                    }
                    img.setIcon(new ImageIcon(ruta + 
                                juegos.getSelectedItem().toString() + "/" + 
                                juegos.getSelectedItem().toString() + ".png")); // Mostramos imagen de juego seleccionado
                    btn.setEnabled(true);                                       // y habilitamos botón de inicio
                } else {                                                        // En caso contrario
                    img.setIcon(new ImageIcon(ruta + "Consola.png"));           // ponemos imagen por defecto
                    btn.setEnabled(false);                                      // y deshabilitamos botón de inicio
                }
            } else {
                if (evt.getActionCommand().equals("Cambiar Canción")) {         // Si seleccionamos Cambiar Canción
                    try {
                        audio.close();
                        audio.open(AudioSystem.getAudioInputStream(new File(
                                        ruta + "audio/" + audios[num_cancion = 
                                        (num_cancion == audios.length-1) 
                                        ? 0 : ++num_cancion] + ".wav")));       // Reproducimos siguiente canción
                        audio.loop(Clip.LOOP_CONTINUOUSLY);                     // en forma de bucle
                    } catch (Exception e) {
                        System.out.println("Error de audio: " + e.getMessage());
                    }
                } else if (evt.getActionCommand().equals("Salir")) {            // Si seleccionamos Salir
                    System.exit(0);                                             // salimos de la aplicación
                } else {                                                        // En cualquier otro caso cargamos el juego seleccionado por el usuario
                    frame.setVisible(false);                                    // haciendo invisible el frame actual y lanzando el oportuno
                    if (juegos.getSelectedItem().toString()
                                                    .equals("7 Diferencias")) {
                        try {
                            Diferencias diferencias = new Diferencias();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else if (juegos.getSelectedItem().toString()
                                                        .equals("Ahorcado")) {
                        try {
                            Ahorcado ahorcado = new Ahorcado();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        audio.close();                                          // Cerramos audio actual ya que el juego Oraculo reproduce su propio audio
                        try {
                            Oraculo oraculo = new Oraculo();
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
            }
        }
    };
    
    /**
     * Método Principal de la Clase Inicio.
     * Lanza una Instancia del Programa llamando al Constructor de Inicio.
     * @param args String[]: argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        Inicio interfaz = new Inicio();                                         // Lanzamos una Instancia del Programa
    }
}
