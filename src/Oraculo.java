import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Clase Oraculo que extiende de JFrame. 
 * Se encarga de generar la Interfaz Gráfica que usaremos para implementar 
 * los métodos de control y gestión para cargar el juego Oraculo.
 *
 * @author Odei
 * @version 30.06.2014
 */
public class Oraculo extends JFrame {
    /**
     * Variable usada para almacenar el Frame de la Interfaz del juego.
     */
    protected JFrame frame;
    
    /**
     * Variables usadas para almacenar los textos mostrados durante el juego.
     */
    protected JLabel texto, texto2, texto3;
    
    /**
     * Variables botón usadas para ejecutar las diferentes opciones 
     * habilitadas durante la ejecución del juego Oraculo.
     */
    protected JButton menor, comenzar, mayor;
    
    /**
     * Variable JMenuItem usada para habilitar o deshabilitar opciones 
     * del menú durante la ejecución del juego.
     */
    protected JMenuItem m_limite;
    
    /**
     * Variable Clip usada para almacenar la canción que sonara de fondo.
     */
    protected Clip audio;
    
    /**
     * Variable booleana usada para controlar la finalización del juego.
     */
    protected boolean fin;
    
    /**
     * Variables long usadas para controlar los límites del juego.
     */
    protected long t, der, izq, cen, con, max;

    /**
     * Constructor de la Interfaz Gráfica Oraculo.
     * Genera e inicializa la Interfaz y los elementos utilizados
     * para visualizar de forma interactiva la ejecución del juego Oraculo.
     */
    public Oraculo() {
        frame = new JFrame("Oráculo de Delfos");                                // Creamos JFrame y le ponemos título
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage(
                                                    Inicio.ruta + "logo.png")); // Le ponemos un icono a la ventana
        frame.setSize(470, 200);                                                // y le asignamos tamaño y demás parámetros
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        fin = false;
        t = 2000;                                                               // Asignamos límite inicial en 2000
        der = t;                                                                // e inicializamos demás parámetros
        izq = 1;
        max = 1;
        
        calcularMaximo();                                                       // Calculamos el número máximo de intentos
        JPanel panel = new JPanel(null);                                        // Creamos un panel para dibujar la interfaz gráfica
        JMenuBar mbar = new JMenuBar();                                         // a su vez creamos un menú para operar con distintas opciones que habilitaremos
        JMenu menu = new JMenu("Opciones");                                     // Le ponemos nombre al menú
        texto = new JLabel("... Piensa un numero entre 1 y " + t + " ...");
        texto2 = new JLabel("...(Máximo número de intentos " + max + ")...");
        texto3 = new JLabel();
        menor = new JButton("<");
        comenzar = new JButton("Comenzar");                                     // Agregamos etiquetas, botones y demás elementos a la Interfaz
        mayor = new JButton(">");
        JLabel img = new JLabel(new ImageIcon(Inicio.ruta + 
                                                        "Oráculo/oraculo.gif"));
        texto.setFont(new Font("Algerian", Font.ITALIC, 14));
        texto.setForeground(Color.white);
        texto2.setFont(new Font("Algerian", Font.ITALIC, 14));
        texto2.setForeground(Color.white);
        texto3.setFont(new Font("Algerian", Font.ITALIC, 14));
        texto3.setForeground(Color.red);
        
        panel.add(texto).setBounds(20, 15, 300, 20);
        panel.add(texto2).setBounds(25, 35, 300, 20);
        panel.add(texto3).setBounds(120, 70, 300, 20);                          // Posicionamos elementos en pantalla
        panel.add(img).setBounds(310, 10, 140, 140);
        panel.add(menor).setBounds(30, 110, 70, 20);
        panel.add(comenzar).setBounds(105, 110, 100, 20);
        panel.add(mayor).setBounds(210, 110, 70, 20);
        panel.setBackground(new Color(28, 28, 28));
        frame.add(panel);
        frame.setJMenuBar(mbar);

        menor.addActionListener(al);                                            // Agregamos a los botones escuchadores de eventos
        comenzar.addActionListener(al);
        mayor.addActionListener(al);

        menor.setEnabled(false);                                                // Habilitamos y deshabilitamos botones inicialmente
        mayor.setEnabled(false);
        frame.setVisible(true);
        try {
            audio = AudioSystem.getClip();
            audio.open(AudioSystem.getAudioInputStream(
                                new File(Inicio.ruta + "Oráculo/oraculo.wav")));// Reproducimos sonido de fondo en bucle
            audio.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Error de audio: " + e.getMessage());
        }
        
        String mi[] = {"Nuevo Oráculo", "Cambiar Límite", "Salir"};
        for (int i = 0; i < mi.length; i++) {
            if (i != 1) {
                JMenuItem m = new JMenuItem(mi[i]);                             // Agregamos los elementos del menú
                menu.add(m).setAccelerator(KeyStroke.getKeyStroke(
                                    mi[i].charAt(0), KeyEvent.CTRL_DOWN_MASK)); // y una tecla asociada a cada uno de ellos
                m.addActionListener(al);
            } else {                                                            // Agregamos menuitem global para poder controlar su visibilidad
                m_limite = new JMenuItem(mi[i]);
                menu.add(m_limite).setAccelerator(KeyStroke.getKeyStroke(
                                    mi[i].charAt(0), KeyEvent.CTRL_DOWN_MASK));
                m_limite.addActionListener(al);
            }
        }
        mbar.add(menu);
        
        frame.addWindowListener(new WindowAdapter() {                           // Agregamos escuchador de eventos sobre el frame del juego
           /**
            * Método usado para capturar el cierre de la ventana actual.
            */
            @Override
            public void windowClosing(WindowEvent me) {
                try {
                    audio.close();                                              // Cerramos el audio actual
                    Inicio.audio.open(AudioSystem.getAudioInputStream(
                            new File(Inicio.ruta + "audio/" + 
                            Inicio.audios[Inicio.num_cancion] + ".wav")));      // y lanzamos el reproducido previamente
                    Inicio.audio.loop(Clip.LOOP_CONTINUOUSLY);                  // en forma de bucle
                } catch (Exception e) {
                    System.out.println("Error de audio: " + e.getMessage());
                }
                frame.dispose();                                                // Cerramos el frame actual
                Inicio.frame.setVisible(true);                                  // y habilitamos la visibilidad del frame inicial
            } 
        });
    }
    
    /**
     * Creamos un escuchador de eventos para capturar las opciones
     * utilizadas durante la ejecución del juego Oraculo.
     */
    ActionListener al = new ActionListener() {
        /**
         * Método usado para capturar el evento lanzado por el usuario.
         * @param evt ActionEvent: evento lanzado por el usuario
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("Nuevo Oráculo")) {               // Si seleccionamos Nuevo Oráculo
                audio.close();
                frame.dispose();
                m_limite.setVisible(true);
                try {
                    Oraculo oraculo = new Oraculo();                            // Lanzamos una nueva instancia del juego Oraculo
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Salir")) {                // Si seleccionamos Salir
                frame.dispose();
                Inicio.frame.setVisible(true);                                  // Cerramos frame actual y habilitamos el inicial
                try {
                    audio.close();
                    Inicio.audio.open(AudioSystem.getAudioInputStream(
                            new File(Inicio.ruta + "audio/" + 
                            Inicio.audios[Inicio.num_cancion] + ".wav")));      // Volviendo a lanzar el audio previamente reproducido
                    Inicio.audio.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (Exception e) {
                    System.out.println("Error de audio: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Comenzar")) {             // Si seleccionamos Comenzar
                cen = (izq + der) / 2;
                menor.setEnabled(true);                                         // habilitamos botones e iniciamos demás variables
                mayor.setEnabled(true);
                comenzar.setText("" + cen);
                texto3.setText("Intento: " + (++con));
                m_limite.setVisible(false);
            } else if (evt.getActionCommand().equals("Cambiar Límite")) {       // Si seleccionamos Cambiar Límite
                try {
                    String aux = (JOptionPane.showInputDialog(new Frame(), 
                            "Introduce nuevo Límite Máximo", "Nuevo Límite", 
                            JOptionPane.INFORMATION_MESSAGE));                  // Lanzamos una ventana para obtener el nuevo límite
                    try {
                        t = Long.parseLong(aux);                                // Convertimos valor capturado si es posible
                    } catch (NumberFormatException e) { }
                    calcularMaximo();                                           // Recalculamos máximo
                    texto.setText("... Piensa un número entre 1 y "+ t +" ...");// y mostramos por pantalla los mensajes con límites 
                    texto2.setText("...(Máximo número de intentos "+max+")...");
                    der = t;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                if (evt.getActionCommand().equals("<") || 
                    evt.getActionCommand().equals(">")) {                       // Si pulsamos los botones < o >
                    cen = (izq + der) / 2;
                    if (con == max) {                                           // Si es el último intento
                        if (evt.getActionCommand().equals("<")) {
                            cen -= 1;                                           // Recalculamos los valores y actualizamos el valor
                        } else if (evt.getActionCommand().equals(">")) {
                            cen += 1;
                        }
                        fin = true;
                    } else {                                                    // En caso contrario
                        if (evt.getActionCommand().equals("<")) {
                            der = cen - 1;                                      // Recalculamos los valores y actualizamos el valor
                        } else if (evt.getActionCommand().equals(">")) {
                            izq = cen + 1;
                        }
                        cen = (izq + der) / 2;
                        comenzar.setText("" + cen);
                        texto3.setText("Intento: " + (++con));
                    }
                } else {
                    fin = true; 
                }
            }
            if (fin) {                                                          // Si se ha finalizado el juego
                menor.setEnabled(false);
                comenzar.setEnabled(false);                                     // Deshabilitamos los botones
                mayor.setEnabled(false);

                String espacios;
                if (cen > 1000) {
                    espacios = " ";
                } else if (cen > 100) { 
                    espacios = "  ";
                } else if (cen > 10) {
                    espacios = "   ";
                } else {
                    espacios = "     ";
                }
                texto3.setText(espacios + "!! " + cen + " !!");                 // y mostramos resultado final
            }
        }
    };
    
    /**
     * Método usado para calcular el número máximo de intentos para adivinar
     * el número inicial asignado o el introducido por el usuario.
     */
    private void calcularMaximo() {
        con = 0;
        max = 1;
        while (max < t + 1) {
            max *= 2;
            con++;
        }
        max = con - 1;
        con = 0;
    }
}
