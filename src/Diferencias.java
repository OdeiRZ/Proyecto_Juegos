import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clase Diferencias que extiende de JFrame. 
 * Se encarga de generar la Interfaz Gráfica que usaremos para implementar 
 * los métodos de control y gestión para cargar el juego Diferencias.
 *
 * @author Odei
 * @version 30.06.2014
 */
public class Diferencias extends JFrame {
    /**
     * Variable usada para almacenar el JFrame de la Interfaz del juego.
     */
    protected JFrame frame;
    
    /**
     * Variables botón usadas para que el usuario interactúe durante el juego.
     */
    protected JButton bt_pista, bt_comenzar;
    
    /**
     * Variables JLabel usadas para mostrar los errores durante el juego.
     */
    protected JLabel error1, error2, error3;
    
    /**
     * Variables JLabel usadas para mostrar los aciertos durante el juego.
     */
    protected JLabel ok1, ok2, ok3, ok4, ok5, ok6, ok7;
    
    /**
     * Variables JLabel usadas para mostrar las imágenes durante el juego.
     */
    protected JLabel imgDif, imgOri;
    
    /**
     * Variable Label usada para almacenar la duración del juego.
     */
    protected static JLabel tiempo;
    
    /**
     * Variable JMenuBar usada para almacenar el menú mostrado durante el juego
     */
    protected JMenuBar mbar;
    
    /**
     * Variables booleanas usadas para controlar el inicio y fin del juego.
     */
    protected boolean inicio, fin;
       
    /**
     * Variable booleana usada para mostrar el contador de tiempo de juego.
     */
    protected static boolean reloj_encendido;
    
    /**
     * Variables enteras usadas para controlar los errores y aciertos del juego.
     */
    protected int errores, aciertos;
        
    /**
     * Variable array de cadenas usada para almacenar el listado de imágenes 
     * disponibles a la hora de generar una nueva partida del juego Diferencias.
     */
    protected final String imagenes[] = {
        "L1", "L2", "L3",
        "Luffy1", "Luffy2", "Luffy3",
        "Lucy1", "Lucy2", "Lucy3",
        "Goku1", "Goku2", "Goku3",
        "Misaki1", "Misaki2", "Misaki3",                                                   
        "Eduard1", "Eduard2", "Eduard3",
        "Sakura1", "Sakura2", "Sakura3",
        "Spike1", "Spike2", "Spike3",
        "Kenshin1", "Kenshin2", "Kenshin3" };
    
    /**
     * Variable vector de Puntos que contendrá las coordenadas de la imagen
     * seleccionada aleatoriamente al comenzar cada nueva partida.
     */
    protected Point coordenadas_imagen[] = { 
        new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0), 
        new Point(0, 0), new Point(0, 0), new Point(0, 0) };
    
    /**
     * Variable vector de Puntos que contiene todas las coordenadas de las
     * imágenes cargadas por defecto para el juego.
     */
    protected final Point vector[][] = { 
        new Point[]{ new Point(289, 302), new Point(335, 286), new Point(183, 65), new Point(102, 450), new Point(311, 478), new Point(212, 368), new Point(295, 165)},            //L1
        new Point[]{ new Point(306, 272), new Point(361, 118), new Point(124, 142), new Point(117, 410), new Point(254, 389), new Point(260, 449), new Point(126, 336)},           //L2
        new Point[]{ new Point(121, 365), new Point(260, 241), new Point(327, 247), new Point(117, 254), new Point(210, 465), new Point(203, 283), new Point(299, 424)},           //L3
        new Point[]{ new Point(194, 453), new Point(286, 59), new Point(163, 243), new Point(166, 289), new Point(88, 232), new Point(152, 354), new Point(240, 262)},             //Luffy1
        new Point[]{ new Point(162, 399), new Point(81, 468), new Point(231, 369), new Point(171, 221), new Point(262, 269), new Point(271, 133), new Point(257, 465)},            //Luffy2
        new Point[]{ new Point(227, 59), new Point(144, 283), new Point(123, 187), new Point(233, 399), new Point(84, 355), new Point(275, 237), new Point(319, 456)},             //Luffy3
        new Point[]{ new Point(92, 151), new Point(92, 481), new Point(146, 471), new Point(128, 337), new Point(186, 206), new Point(300, 422), new Point(235, 421)},             //Lucy1
        new Point[]{ new Point(219, 140), new Point(118, 212), new Point(169, 401), new Point(64, 322), new Point(199, 314), new Point(279, 281), new Point(172, 72)},             //Lucy2
        new Point[]{ new Point(182, 182), new Point(268, 115), new Point(198, 336), new Point(282, 466), new Point(136, 299), new Point(83, 468), new Point(182, 248)},            //Lucy3
        new Point[]{ new Point(268, 303), new Point(194, 150), new Point(121, 157), new Point(117, 410), new Point(199, 53), new Point(84, 139), new Point(197, 251)},             //Goku1
        new Point[]{ new Point(11, 202), new Point(391, 143), new Point(144, 70), new Point(175, 141), new Point(94, 375), new Point(286, 414), new Point(97, 241)},               //Goku2
        new Point[]{ new Point(243, 216), new Point(92, 336), new Point(196, 112), new Point(182, 309), new Point(46, 211), new Point(111, 167), new Point(217, 154)},             //Goku3
        new Point[]{ new Point(199, 180), new Point(251, 219), new Point(151, 484), new Point(195, 331), new Point(260, 173), new Point(206, 78), new Point(201, 223)},            //Misaki1
        new Point[]{ new Point(157, 300), new Point(214, 270), new Point(237, 407), new Point(157, 491), new Point(211, 170), new Point(143, 126), new Point(272, 458)},           //Misaki2
        new Point[]{ new Point(135, 200), new Point(215, 151), new Point(263, 262), new Point(244, 488), new Point(268, 431), new Point(192, 278), new Point(188, 319)},           //Misaki3
        new Point[]{ new Point(201, 127), new Point(392, 327), new Point(23, 315), new Point(205, 472), new Point(210, 27), new Point(108, 421), new Point(233, 386)},             //Eduard1
        new Point[]{ new Point(291, 178), new Point(202, 209), new Point(179, 440), new Point(145, 56), new Point(180, 104), new Point(177, 356), new Point(231, 132)},            //Eduard2
        new Point[]{ new Point(199, 322), new Point(135, 476), new Point(93, 284), new Point(188, 93), new Point(149, 220), new Point(256, 425), new Point(226, 154)},             //Eduard3
        new Point[]{ new Point(349, 327), new Point(171, 486), new Point(76, 320), new Point(158, 213), new Point(265, 204), new Point(83, 39), new Point(193, 341)},              //Sakura1
        new Point[]{ new Point(147, 269), new Point(120, 388), new Point(251, 245), new Point(78, 206), new Point(365, 168), new Point(328, 485), new Point(260, 229)},            //Sakura2
        new Point[]{ new Point(166, 7), new Point(133, 166), new Point(241, 267), new Point(68, 489), new Point(112, 311), new Point(167, 100), new Point(115, 248)},              //Sakura3
        new Point[]{ new Point(206, 90), new Point(262, 480), new Point(248, 215), new Point(361, 275), new Point(297, 198), new Point(160, 334), new Point(261, 264)},            //Spike1
        new Point[]{ new Point(230, 376), new Point(33, 481), new Point(265, 170), new Point(156, 171), new Point(329, 491), new Point(219, 219), new Point(364, 290)},            //Spike2
        new Point[]{ new Point(113, 462), new Point(236, 421), new Point(273, 332), new Point(228, 195), new Point(346, 180), new Point(271, 148), new Point(259, 215)},           //Spike3
        new Point[]{ new Point(63, 348), new Point(158, 392), new Point(193, 38), new Point(254, 131), new Point(203, 148), new Point(191, 477), new Point(306, 394)},             //Kenshin1
        new Point[]{ new Point(281, 36), new Point(217, 160), new Point(254, 151), new Point(218, 417), new Point(126, 313), new Point(317, 172), new Point(270, 244)},            //Kenshin2
        new Point[]{ new Point(242, 123), new Point(92, 363), new Point(308, 276), new Point(163, 434), new Point(245, 478), new Point(252, 184), new Point(183, 53)}};            //Kenshin3

    /**
     * Constructor de la Interfaz Gráfica Diferencias.
     * Genera e inicializa la Interfaz y los elementos utilizados
     * para visualizar de forma interactiva la ejecución del juego Diferencias.
     */
    public Diferencias() {
        JPanel panel = new JPanel(null);                                        // Creamos un panel para dibujar la interfaz gráfica
        JLabel texto = new JLabel("Imagen Original");
        JLabel texto2 = new JLabel("Busca las Diferencias Aquí");
        mbar = new JMenuBar();                                                  // Creamos un menú para operar con distintas opciones que habilitaremos
        JMenu menu = new JMenu("Opciones");                                     // Le ponemos nombre al menú
        tiempo = new JLabel("0");
        bt_pista = new JButton("Pista");
        bt_comenzar = new JButton("Comenzar");
        error1 = new JLabel("Error 1");
        error2 = new JLabel("Error 2");
        error3 = new JLabel("Error 3");
        ok1 = new JLabel("Acierto 1");                                          // Agregamos etiquetas, botones y demás elementos a la Interfaz
        ok2 = new JLabel("Acierto 2");
        ok3 = new JLabel("Acierto 3");
        ok4 = new JLabel("Acierto 4");
        ok5 = new JLabel("Acierto 5");
        ok6 = new JLabel("Acierto 6");
        ok7 = new JLabel("Acierto 7");
        imgDif = new JLabel(new ImageIcon(Inicio.ruta + "7 Diferencias/2.png"));
        imgOri = new JLabel(new ImageIcon(Inicio.ruta + "7 Diferencias/1.png"));// Cargamos imágenes con las instrucciones de juego
        
        panel.add(texto).setBounds(160, 10, 300, 20);
        panel.add(texto2).setBounds(620, 10, 300, 20);
        panel.add(tiempo).setBounds(465, 510, 95, 20);
        panel.add(imgOri).setBounds(20, 40, 400, 500);
        panel.add(imgDif).setBounds(525, 40, 400, 500);
        panel.add(bt_comenzar).setBounds(425, 50, 95, 50);                      // Posicionamos elementos en pantalla
        panel.add(bt_pista).setBounds(425, 120, 95, 50);
        panel.add(error1).setBounds(450, 220, 95, 20);
        panel.add(error2).setBounds(450, 240, 95, 20);
        panel.add(error3).setBounds(450, 260, 95, 20);
        panel.add(ok1).setBounds(445, 340, 95, 20);
        panel.add(ok2).setBounds(445, 360, 95, 20);
        panel.add(ok3).setBounds(445, 380, 95, 20);
        panel.add(ok4).setBounds(445, 400, 95, 20);
        panel.add(ok5).setBounds(445, 420, 95, 20);
        panel.add(ok6).setBounds(445, 440, 95, 20);
        panel.add(ok7).setBounds(445, 460, 95, 20);
        
        texto.setForeground(Color.gray);
        texto2.setForeground(Color.gray);
        error1.setForeground(Color.gray);
        error2.setForeground(Color.gray);
        error3.setForeground(Color.gray);
        ok1.setForeground(Color.gray);
        ok2.setForeground(Color.gray);
        ok3.setForeground(Color.gray);
        ok4.setForeground(Color.gray);
        ok5.setForeground(Color.gray);
        ok6.setForeground(Color.gray);
        ok7.setForeground(Color.gray);
        texto.setFont(new Font("Algerian", Font.ITALIC, 14));                   // Asignamos tipografía y tamaño de fuente
        texto2.setFont(new Font("Algerian", Font.ITALIC, 14));
        imgDif.setBorder(new CompoundBorder(new EtchedBorder(), 
                                                new LineBorder(Color.white)));  // Asignamos bordes a las imágenes
        imgOri.setBorder(new CompoundBorder(new EtchedBorder(),
                                                new LineBorder(Color.white)));
        bt_pista.setEnabled(false);
        String nmi[] = {"Nuevo Juego", "Cambiar Canción","Ver Record", "Salir"};
        for (String mi : nmi) {
            JMenuItem m = new JMenuItem(mi);                                    // Agregamos los elementos del menú
            menu.add(m).setAccelerator(KeyStroke.getKeyStroke(mi.charAt(0), 
                                                    KeyEvent.CTRL_DOWN_MASK));  // y una tecla asociada a cada uno de ellos
            m.addActionListener(al);
        }		
        mbar.add(menu);
        
        bt_comenzar.addActionListener(al);
        bt_pista.addActionListener(al);
        imgDif.addMouseListener(new EscuchadorRaton());                         // Agregamos escuchadores de eventos sobre diferentes objetos

        frame = new JFrame("Las 7 Diferencias");                                // Creamos JFrame y le ponemos título
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage(
                                                    Inicio.ruta + "logo.png")); // Le ponemos un icono a la ventana
        frame.add(panel);
        frame.setSize(950, 610);                                                // y le asignamos tamaño y demás parámetros
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(mbar);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
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
     * utilizadas durante la ejecución del juego Diferencias.
     */
    ActionListener al = new ActionListener() {
        /**
         * Método usado para capturar el evento lanzado por el usuario.
         * @param evt ActionEvent: evento lanzado por el usuario
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("Comenzar")) {                    // Si seleccionamos Comenzar
                Reloj reloj = new Reloj();                                      // iniciamos el contador del reloj
                inicio = true;
                fin = false;                                                    // y des/habilitamos botones y variables iniciales
                bt_pista.setEnabled(true);
                bt_comenzar.setEnabled(false);

                JMenu menu2 = new JMenu();
                JMenuItem m = new JMenuItem("Truco");
                menu2.add(m).setAccelerator(KeyStroke.getKeyStroke('T', 
                                                    KeyEvent.CTRL_DOWN_MASK));  // Asignamos un menú oculto para permitir un truco
                m.addActionListener(al);
                mbar.add(menu2);

                int indice = (int)(Math.random() * imagenes.length);
                imgDif.setIcon(new ImageIcon(Inicio.ruta + "7 Diferencias/" + 
                                    imagenes[indice] + ".png"));
                imgOri.setIcon(new ImageIcon(Inicio.ruta + "7 Diferencias/" + 
                                    imagenes[indice].substring(0,
                                    imagenes[indice].length() - 1) + ".png"));  // Asignamos imágenes seleccionadas
                for (int i = 0; i < 7; i++) {
                    coordenadas_imagen[i].x = vector[indice][i].x;
                    coordenadas_imagen[i].y = vector[indice][i].y;              // y cargamos sus coordenadas en variable
                }
            } else if (evt.getActionCommand().equals("Pista")) {                // Si pulsamos el botón Pista
                bt_pista.setEnabled(false);
                for (int i = 0; i < 7; i++) {
                    if (coordenadas_imagen[i].x != -50) {                       // Buscamos la primera de las coordenadas de la imagen seleccionada
                        imgDif.add(new JLabel(new ImageIcon(
                            Inicio.ruta + "7 Diferencias/circulo.png")))
                            .setBounds(coordenadas_imagen[i].x - 50,  
                                        coordenadas_imagen[i].y - 50, 100, 100);// y la marcamos a modo de ayuda
                        break;
                    }
                }
                try {
                    Clip pista = AudioSystem.getClip();
                    pista.open(AudioSystem.getAudioInputStream(new File(
                                    Inicio.ruta + "7 Diferencias/Bonus.wav"))); // Reproduciendo un sonido de ayuda
                    pista.loop(0); 
                } catch (Exception e) {
                    System.out.println("Error de audio: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Nuevo Juego")) {          // Si pulsamos Nuevo Juego
                frame.dispose();
                Reloj.segundos = -1;
                tiempo.setText("0");                                            // Reinicializamos algunas variables
                reloj_encendido = false;
                try {
                    Diferencias diferencias = new Diferencias();                // y lanzamos una nueva instancia del juego
                } catch(Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Cambiar Canción")) {      // Si pulsamos Cambiar Canción 
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
            } else if (evt.getActionCommand().equals("Ver Record")) {           // Si pulsamos Ver Record 
                try {
                    verRecord();                                                // Lanzamos una ventana de diálogo con el record actual
                } catch(Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (evt.getActionCommand().equals("Salir")) {                // Si seleccionamos Salir
                frame.dispose();                                                // Cerramos frame actual
                Reloj.segundos = -1;
                reloj_encendido = false;
                Inicio.frame.setVisible(true);                                  // y mostramos el inicial
            } else if (evt.getActionCommand().equals("Truco")) {                // Si lanzamos el Truco
                for (int i = 0; i < 7; i++)
                    if (coordenadas_imagen[i].x != -50)
                        imgDif.add(new JLabel(new ImageIcon(
                            Inicio.ruta + "7 Diferencias/circulo.png")))
                            .setBounds(coordenadas_imagen[i].x - 25,
                                       coordenadas_imagen[i].y - 25, 50, 50);   // Mostramos por pantalla todas las coordenadas de la imagen actual
                try {
                    Clip truco = AudioSystem.getClip();
                    truco.open(AudioSystem.getAudioInputStream(new File(
                                    Inicio.ruta + "7 Diferencias/Bonus.wav"))); // y reproducimos un sonido a modo de aviso
                    truco.loop(0);
                } catch (Exception e) {
                    System.out.println("Error de audio: " + e.getMessage());
                }
            }
        }
    };

    /**
     * Creamos un escuchador de eventos para capturar las opciones
     * seleccionadas con el ratón durante la ejecución del juego.
     */
    public class EscuchadorRaton extends MouseAdapter {
        /**
         * Método usado para capturar el evento lanzado por el usuario.
         * @param e MouseEvent: evento lanzado por el usuario
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (inicio) {                                                       // Si el juego ha sido iniciado
                boolean sw = false;
                for (int i = 0; i < 7; i++) {	
                    if (e.getX() >= coordenadas_imagen[i].x - 30 && 
                        e.getX() <= coordenadas_imagen[i].x + 30 && 
                        e.getY() >= coordenadas_imagen[i].y - 30 && 
                        e.getY() <= coordenadas_imagen[i].y + 30) {             // y se ha pinchado cerca de alguna de las coordenadas de la imagen
                        imgDif.add(new JLabel(new ImageIcon(
                            Inicio.ruta + "7 Diferencias/cruz-verde.png")))
                            .setBounds(e.getX() - 25, e.getY() - 25, 50, 50);   // mostramos dicha seleccion
                        switch (++aciertos) {
                            case 1: ok1.setForeground(Color.green);
                            break;
                            case 2: ok2.setForeground(Color.green);             // y marcamos el acierto en pantalla
                            break;
                            case 3: ok3.setForeground(Color.green);
                            break;
                            case 4: ok4.setForeground(Color.green);
                            break;
                            case 5: ok5.setForeground(Color.green);
                            break;
                            case 6: ok6.setForeground(Color.green);
                            break;
                            case 7: ok7.setForeground(Color.green);
                                    fin = true;
                        }
                        coordenadas_imagen[i].x = -50;                          // Asignamos nuevas coordenadas para no poder seleccionar la misma diferencia
                        coordenadas_imagen[i].y = -50;
                        sw = true;                                              // permitiendo aciertos múltiples
                        
                        try {
                            Clip golpe = AudioSystem.getClip();
                            golpe.open(AudioSystem.getAudioInputStream(new File(
                                    Inicio.ruta + "7 Diferencias/Golpe2.wav")));// y reproduciendo un sonido de aviso
                            golpe.loop(0);
                        } catch (Exception e1) {
                            System.out.println("Error: " + e1.getMessage());
                        }
                    }
                }
                if (!sw) {                                                      // Si no hemos seleccionado un error válido
                    imgDif.add(new JLabel(new ImageIcon(
                            Inicio.ruta + "7 Diferencias/cruz-roja.png")))
                            .setBounds(e.getX() - 25, e.getY() - 25, 50, 50);   // marcamos el error en pantalla
                    switch (++errores) {
                        case 1: error1.setForeground(Color.red);
                        break;
                        case 2: error2.setForeground(Color.red);
                        break;
                        case 3: error3.setForeground(Color.red);                // y si llegamos a 3 finalizamos el juego
                                fin = true;
                    }
                    try {
                        Clip golpe = AudioSystem.getClip();
                        golpe.open(AudioSystem.getAudioInputStream(new File(
                                    Inicio.ruta + "7 Diferencias/Golpe.wav"))); // reproduciendo un sonido de aviso
                        golpe.loop(0);
                    } catch (Exception e2) {
                        System.out.println("Error de audio: " + e2.getMessage());
                    }
                }
                if (fin) {                                                      // Si se ha finalizado el juego
                    inicio = false;
                    reloj_encendido = false;
                    bt_pista.setEnabled(false);
                    imgDif.setEnabled(false);
                    String resul = (aciertos == 7) ? "ganar" : "perder";
                    imgDif.add(new JLabel(new ImageIcon(
                            Inicio.ruta + "7 Diferencias/" + resul + ".png")))
                                                .setBounds(50, 100, 300, 300);  // Comprobamos si hemos ganado o perdido
                    try {
                        Clip fanfarria = AudioSystem.getClip();
                        fanfarria.open(AudioSystem.getAudioInputStream(new File(
                            Inicio.ruta + "7 Diferencias/" + resul + ".wav"))); // reproduciendo un sonido en consecuencia
                        fanfarria.loop(0);
                        ventanaDialogo(resul);                                  // y lanzamos una ventana con el resultado
                    } catch (Exception e2) {
                        System.out.println("Error: " + e2.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * Método usado para comprobar si existe un nuevo record para el usuario.
     * @param duracion long: segundos de duración de la partida
     */
    private void comprobarRecord(long duracion) throws IOException {
        String aux = new BufferedReader(new FileReader(
                    Inicio.ruta + "7 Diferencias/diferencias.txt")).readLine();	// Obtenemos record actual
        if (duracion < Long.parseLong(aux.substring(aux.indexOf('#') + 1, 
                                                            aux.length()))) {   // y lo comparamos con el nuevo
            PrintWriter out = new PrintWriter(new FileWriter(
                    Inicio.ruta + "7 Diferencias/diferencias.txt"));            // Si se bate el record lo guardamos
            out.print(JOptionPane.showInputDialog(this, 
                    "Introduce tu nombre para registrarlo", "Record batido",
                    JOptionPane.INFORMATION_MESSAGE) + "#" + duracion);
            out.flush();
            out.close();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "¡¡ No has batido el Record, pero sigue intentandolo !!",
                    "Record no batido", JOptionPane.INFORMATION_MESSAGE);       // y en cualquier caso mostramos una ventana al usuario
        }
    }
    
    /**
     * Método usado para lanzar una ventana donde veremos el record actual.
     */
    private void verRecord() throws IOException {
        String aux = new BufferedReader(new FileReader(
                    Inicio.ruta + "7 Diferencias/diferencias.txt")).readLine(); // Obtenemos el record actual
        JOptionPane.showMessageDialog(this, "¡¡ El Record lo tiene " + 
            aux.substring(0, aux.indexOf('#')) + " con " + 
            aux.substring(aux.indexOf('#') + 1, aux.length()) +
            " Segundos !!", "Mejor Tiempo", JOptionPane.INFORMATION_MESSAGE);   // y lo mostramos en una ventana de diálogo
    }

    /**
     * Método usado para lanzar una ventana donde veremos el resultado tras
     * la finalización del juego comprobando el record si hemos ganado.
     */
    private void ventanaDialogo(String aux) throws IOException {
        if (aux.equals("perder")) {
            JOptionPane.showMessageDialog(this,
                "Has Perdido el Juego :(", "Pierdes la Partida",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "¡¡ Has Ganado el Juego con " + errores + " fallos en " + 
                (1 + Reloj.segundos) + " Segundos :) !!", "Ganas la Partida",
                JOptionPane.INFORMATION_MESSAGE);
            comprobarRecord(Reloj.segundos);
        }
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
