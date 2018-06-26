/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package procesador;

import helpClases.filtros.FileFiltExt;
import helpClases.utils.ImageWork;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Fabiel
 */
public class CalendarioVisual extends javax.swing.JFrame {

    String absolutePath;

    BackgroundWorker backgroundWorker;

    /**
     * Creates new form CalendarioVisual
     */
    public CalendarioVisual() {
        initComponents();
        setLocationRelativeTo(null);
        textFieldChooser1.setChooserFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private final class BackgroundWorker extends SwingWorker<String, String> {

        final Color colorAnno = jColorAnno.getBackground();
        final Color colorMes = jColorMes.getBackground();
        final Color colorDomingo = jColorDomingo.getBackground();
        Color fondo = jColorFondo.getBackground();
        Color letra = jColorLetra.getBackground();
        final int alturaRenglon = (Integer) jSAlturaRenglon.getValue();
        final int anchoEspacio = (Integer) jSAnchoEspacio.getValue();
        int espacioEntreMesesVertical = (Integer) jSEspacioMesVert.getValue();
        int espacioEntreMesesHorizontal = (Integer) jSEspacioMesHrt.getValue();
        final int espacioEntreDiasHorizontal = (Integer) jSEspacioDiasHr.getValue();
        final int espacioEntreDiasVertical = (Integer) jSEspacioDiasVrt.getValue();
        int tamanoAnno = alturaRenglon;

        final int anchoMes = 7 * anchoEspacio + espacioEntreDiasVertical * 6;
        //el alto de la imagen de un  mes es la cantidad des semanas que el maximo es 8
        //por el alto de un renglon + el alto de los dias(L M Mi J V.. )+ el alto de el nombre  del mes
        int altoMes = 8 * alturaRenglon + espacioEntreDiasHorizontal * 7;

        HashMap<Integer, BufferedImage> dias = new HashMap();
        HashMap<Integer, BufferedImage> diasRojos = new HashMap();
        HashMap<Integer, BufferedImage> meses = new HashMap();
        BufferedImage nombresSemana = new BufferedImage(anchoMes, alturaRenglon, BufferedImage.TYPE_INT_ARGB);

        private void llenarNumeroDias() {
            for (int i = 1; i < 32; i++) {
                BufferedImage numero = new BufferedImage(anchoEspacio, alturaRenglon, BufferedImage.TYPE_INT_ARGB);
                Graphics2D numeroG = numero.createGraphics();
                numeroG.setColor(fondo);
                numeroG.fill(new Rectangle2D.Double(0, 0, anchoEspacio, alturaRenglon));
                numeroG.setColor(letra);
                numeroG.setFont(numeroG.getFont().deriveFont(Font.BOLD, alturaRenglon));
                FontMetrics fontMetrics = numeroG.getFontMetrics();
                int anchodia = fontMetrics.stringWidth(i + "");
                numeroG.drawString(i + "", anchoEspacio / 2 - anchodia / 2, alturaRenglon - fontMetrics.getDescent() / 2);
//            numeroG.drawString(i + "", 0, alturaRenglon);
                dias.put(i, numero);
            }
        }

        private void llenarNumeroDiasRojos() {
            for (int i = 1; i < 32; i++) {
                BufferedImage numero = new BufferedImage(anchoEspacio, alturaRenglon, BufferedImage.TYPE_INT_ARGB);
                Graphics2D numeroG = numero.createGraphics();
                numeroG.setColor(fondo);
                numeroG.fill(new Rectangle2D.Double(0, 0, anchoEspacio, alturaRenglon));
                numeroG.setColor(colorDomingo);
                numeroG.setFont(numeroG.getFont().deriveFont(Font.BOLD, alturaRenglon));
                FontMetrics fontMetrics = numeroG.getFontMetrics();
                int anchodia = fontMetrics.stringWidth(i + "");
                numeroG.drawString(i + "", anchoEspacio / 2 - anchodia / 2, alturaRenglon - fontMetrics.getDescent() / 2);
//            numeroG.drawString(i + "", 0, alturaRenglon);
                diasRojos.put(i, numero);
            }
        }
//<editor-fold defaultstate="collapsed" desc="diassemana">

        private void pintarDiasSemana() {
            Graphics2D createGraphics = nombresSemana.createGraphics();
            createGraphics.setFont(createGraphics.getFont().deriveFont(Font.BOLD, alturaRenglon - espacioEntreDiasHorizontal));
            String[] semana = new String[]{"L", "M", "Mi", "J", "V", "S", "D"};
            for (int i = 0; i < 7; i++) {
                createGraphics.setColor(fondo);
                createGraphics.fill(new Rectangle2D.Double(0, 0, anchoEspacio, alturaRenglon));
                if (i == 6) {
                    createGraphics.setColor(colorDomingo);
                } else {
                    createGraphics.setColor(letra);
                }
                FontMetrics fontMetrics = createGraphics.getFontMetrics();
                int anchodia = fontMetrics.stringWidth(semana[i]);
                createGraphics.drawString(semana[i], anchoEspacio / 2 - anchodia / 2, alturaRenglon - fontMetrics.getDescent() / 2);
//            createGraphics.drawString(semana[i], 0, alturaRenglon - espacioEntreDiasHorizontal);
                createGraphics.translate(anchoEspacio + espacioEntreDiasVertical, 0);
            }
        }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="pintarmeses">
        int anno = (Integer) jSAnno.getValue();

        private void pintarMeses() {

            Calendar calendar = Calendar.getInstance();
            calendar.set(anno, 0, 1, 0, 0, 0);
            String[] nombreMeses = new String[]{"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
//        String[] nombreMeses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            for (int i = 0; i < 12; i++) {
                //   int cntdSemanas = calendar.getMaximum(Calendar.WEEK_OF_MONTH);
                //int altoMes = (cntdSemanas * alturaRenglon) + alturaRenglon * 2;
                BufferedImage calendarioDelMes = new BufferedImage(anchoMes, altoMes, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graficosDelMes = calendarioDelMes.createGraphics();
                graficosDelMes.setFont(graficosDelMes.getFont().deriveFont(Font.BOLD, alturaRenglon));
                graficosDelMes.setColor(fondo);
                graficosDelMes.fill(new Rectangle2D.Double(0, 0, anchoMes, alturaRenglon));
                graficosDelMes.setColor(colorMes);
                FontMetrics fontMetrics = graficosDelMes.getFontMetrics();
                int anchoNombreMes = fontMetrics.stringWidth(nombreMeses[i]);
                graficosDelMes.drawString(nombreMeses[i], anchoMes / 2 - anchoNombreMes / 2, alturaRenglon - fontMetrics.getDescent() / 2);
                // graficosDelMes.translate(0, espacioEntreDiasHorizontal);
                //dibujar L M Mi J V S D
                graficosDelMes.drawImage(nombresSemana, 0, alturaRenglon + espacioEntreDiasHorizontal, null);
                //trasladar los graficos
                graficosDelMes.translate(0, (alturaRenglon + espacioEntreDiasHorizontal) * 2);
                int cntdDias = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int j = 0; j < cntdDias; j++) {//dibujar los numeros de los dias debajo del Dia(L M Mi J V S D) que le corresponda
                    int get = calendar.get(Calendar.DAY_OF_WEEK);
                    BufferedImage numero;
                    if (get != 1) {
                        numero = dias.get(calendar.get(Calendar.DAY_OF_MONTH));
                    } else {
                        numero = diasRojos.get(calendar.get(Calendar.DAY_OF_MONTH));
                    }
                    switch (get) {
                        case 2://lunes
                            graficosDelMes.drawImage(numero, 0, 0, null);
                            break;
                        case 3://martes
                            graficosDelMes.drawImage(numero, anchoEspacio + espacioEntreDiasVertical, 0, null);
                            break;
                        case 4://miercoles
                            graficosDelMes.drawImage(numero, (anchoEspacio + espacioEntreDiasVertical) * 2, 0, null);
                            break;
                        case 5://jueves
                            graficosDelMes.drawImage(numero, (anchoEspacio + espacioEntreDiasVertical) * 3, 0, null);
                            break;
                        case 6: //viernes
                            graficosDelMes.drawImage(numero, (anchoEspacio + espacioEntreDiasVertical) * 4, 0, null);
                            break;
                        case 7://sabado
                            graficosDelMes.drawImage(numero, (anchoEspacio + espacioEntreDiasVertical) * 5, 0, null);
                            break;
                        case 1://domingo
                            graficosDelMes.drawImage(numero, (anchoEspacio + espacioEntreDiasVertical) * 6, 0, null);
                            //bajar el alto del renglon para escribir los demas dias
                            graficosDelMes.translate(0, alturaRenglon + espacioEntreDiasHorizontal);
                            break;
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                meses.put(i, calendarioDelMes);
            }
        }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Crear los Calendarios de los meses">
        public BufferedImage creandoCalendarioMesesVertical() {
            int anchoCalendario = anchoMes * 3 + espacioEntreMesesVertical * 2;
            int altoCalendario = altoMes * 4 + espacioEntreMesesHorizontal * 3 + tamanoAnno + espacioEntreDiasHorizontal;
//pintar los numero de los dias para pintarlos una sola vez y luego rehuzarlos
            //pintar los nombres de la semana
            BufferedImage calendarioAño = new BufferedImage(anchoCalendario, altoCalendario, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2DCalendario = calendarioAño.createGraphics();
            graphics2DCalendario.setColor(fondo);
            graphics2DCalendario.fill(new Rectangle2D.Double(0, 0, anchoCalendario, tamanoAnno));
            graphics2DCalendario.setColor(colorAnno);
            graphics2DCalendario.setFont(graphics2DCalendario.getFont().deriveFont(Font.BOLD, (float) tamanoAnno));

            FontMetrics fontMetrics = graphics2DCalendario.getFontMetrics();
            String mensaje = "FELIZ " + anno + " FIGURA!!!";
            int anchoAnno = fontMetrics.stringWidth(mensaje);
            graphics2DCalendario.drawString(mensaje, anchoCalendario / 2 - anchoAnno / 2, tamanoAnno - fontMetrics.getDescent() / 2);
//        graphics2DCalendario.drawString(anno + "", 0, tamanoAnno);
            graphics2DCalendario.translate(0, tamanoAnno + espacioEntreDiasHorizontal);
            for (int i = 0; i < 12; i++) {
                switch (i) {//dibujar el mes en el calendario
                    case 0:
                        graphics2DCalendario.drawImage(meses.get(i), 0, 0, null);
                        break;
                    case 1:
                        graphics2DCalendario.drawImage(meses.get(i), anchoMes + espacioEntreMesesVertical, 0, null);
                        break;
                    case 2:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, 0, null);
                        break;
                    case 3:
                        graphics2DCalendario.drawImage(meses.get(i), 0, altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 4:
                        graphics2DCalendario.drawImage(meses.get(i), anchoMes + espacioEntreMesesVertical, altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 5:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 6:
                        graphics2DCalendario.drawImage(meses.get(i), 0, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                    case 7:
                        graphics2DCalendario.drawImage(meses.get(i), anchoMes + espacioEntreMesesVertical, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                    case 8:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                    case 9:
                        graphics2DCalendario.drawImage(meses.get(i), 0, (altoMes + espacioEntreMesesHorizontal) * 3, null);
                        break;
                    case 10:
                        graphics2DCalendario.drawImage(meses.get(i), anchoMes + espacioEntreMesesVertical, (altoMes + espacioEntreMesesHorizontal) * 3, null);
                        break;
                    case 11:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, (altoMes + espacioEntreMesesHorizontal) * 3, null);
                        break;
                }

            }
            return calendarioAño;
        }
        //  ImageIO.write(calendarioAño, "PNG", new File("calendario " + anno + ".png"));

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="creandoCalendarioMesesHorizontal">
        private BufferedImage creandoCalendarioMesesHorizontal() {
            int anchoCalendario = anchoMes * 4 + espacioEntreMesesVertical * 3;
            int altoCalendario = altoMes * 3 + espacioEntreMesesHorizontal * 2 + tamanoAnno + espacioEntreDiasHorizontal;
//pintar los numero de los dias para pintarlos una sola vez y luego rehuzarlos
            //pintar los nombres de la semana
            BufferedImage calendarioAño = new BufferedImage(anchoCalendario, altoCalendario, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2DCalendario = calendarioAño.createGraphics();
            graphics2DCalendario.setColor(fondo);
            graphics2DCalendario.fill(new Rectangle2D.Double(0, 0, anchoCalendario, tamanoAnno));
            graphics2DCalendario.setColor(colorAnno);
            graphics2DCalendario.setFont(graphics2DCalendario.getFont().deriveFont(Font.BOLD, (float) tamanoAnno));

            FontMetrics fontMetrics = graphics2DCalendario.getFontMetrics();
            String mensaje = "FELIZ " + anno + " FIGURA!!!";
            int anchoAnno = fontMetrics.stringWidth(mensaje);
            graphics2DCalendario.drawString(mensaje, anchoCalendario / 2 - anchoAnno / 2, tamanoAnno - fontMetrics.getDescent() / 2);

//        graphics2DCalendario.drawString(anno + "", 0, tamanoAnno);
            graphics2DCalendario.translate(0, tamanoAnno + espacioEntreDiasHorizontal);

            for (int i = 0; i < 12; i++) {
                switch (i) {//dibujar el mes en el calendario
                    case 0:
                        graphics2DCalendario.drawImage(meses.get(i), 0, 0, null);
                        break;
                    case 1:
                        graphics2DCalendario.drawImage(meses.get(i), anchoMes + espacioEntreMesesVertical, 0, null);
                        break;
                    case 2:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, 0, null);
                        break;
                    case 3:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 3, 0, null);
                        break;
                    case 4:
                        graphics2DCalendario.drawImage(meses.get(i), 0, altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 5:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical), altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 6:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 7:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 3, altoMes + espacioEntreMesesHorizontal, null);
                        break;
                    case 8:
                        graphics2DCalendario.drawImage(meses.get(i), 0, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                    case 9:
                        graphics2DCalendario.drawImage(meses.get(i), anchoMes + espacioEntreMesesVertical, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                    case 10:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 2, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                    case 11:
                        graphics2DCalendario.drawImage(meses.get(i), (anchoMes + espacioEntreMesesVertical) * 3, (altoMes + espacioEntreMesesHorizontal) * 2, null);
                        break;
                }
            }
            //  ImageIO.write(calendarioAño, "PNG", new File("calendario " + anno + ".png"));
            return calendarioAño;
        }
//</editor-fold>

        private void inicializar() {
            llenarNumeroDias();
            llenarNumeroDiasRojos();
            pintarDiasSemana();
            pintarMeses();
        }

        @Override
        protected String doInBackground() throws Exception {
            int anchoValue = 1050, alturaValue = 1500, espacioH = 10, espacioV = 10;
            inicializar();
            BufferedImage calendVertical = ImageWork.getScaledInstace(anchoValue - espacioV * 2, alturaValue - espacioH * 2, Image.SCALE_SMOOTH, creandoCalendarioMesesVertical());
            BufferedImage calendHorizontal = ImageWork.getScaledInstace(alturaValue - espacioV * 2, anchoValue - espacioH * 2, Image.SCALE_SMOOTH, creandoCalendarioMesesHorizontal());

            File chooserSelectedFile = textFieldChooser1.getChooserSelectedFile();
            File dir = new File(chooserSelectedFile, anno + "");
            dir.mkdirs();
            File[] listFiles = chooserSelectedFile.listFiles(FileFiltExt.filtroDeImagenes());
            jProgressBar1.setMaximum(listFiles.length);
            for (int i = 0; i < listFiles.length && !isCancelled(); i++) {
                BufferedImage read = ImageIO.read(listFiles[i]);
                if (read.getHeight() >= read.getWidth()) {
                    read = ImageWork.getScaledInstace(anchoValue, alturaValue, Image.SCALE_SMOOTH, read);
                    Graphics graphics = read.getGraphics();
                    graphics.drawImage(calendVertical, espacioV, espacioH, null);
                } else {
                    read = ImageWork.getScaledInstace(alturaValue, anchoValue, Image.SCALE_SMOOTH, read);
                    Graphics graphics = read.getGraphics();
                    graphics.drawImage(calendHorizontal, espacioV, espacioH, null);
                }
                ImageIO.write(read, "PNG", new File(dir, listFiles[i].getName()));
                publish(listFiles[i].getName());
            }
            publish();
            return "Termine";
        }

        @Override
        protected void process(java.util.List<String> lista) {
            if (!isCancelled()) {
                String get = lista.get(lista.size() - 1);
                jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                jLabel1.setText("Procesando: " + get);
                //    buttonPanelWorker1.setLabelText("Procesando: " + get);
            }
            if (isDone()) {
                hacerCalendario.setActionCommand("Hacer Calendarios");
                hacerCalendario.setText("Hacer Calendarios");
                backgroundWorker = null;
            }
            super.process(lista); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected void done() {
            hacerCalendario.setActionCommand("Hacer Calendarios");
            hacerCalendario.setText("Hacer Calendarios");
            super.done();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textFieldChooser1 = new swingClases.swingderivados.TextFieldChooser();
        jSAnno = new javax.swing.JSpinner();
        hacerCalendario = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jColorAnno = new swingClases.swingderivados.JButtonColorChooser();
        jColorMes = new swingClases.swingderivados.JButtonColorChooser();
        jColorDomingo = new swingClases.swingderivados.JButtonColorChooser();
        jColorFondo = new swingClases.swingderivados.JButtonColorChooser();
        jColorLetra = new swingClases.swingderivados.JButtonColorChooser();
        jSAlturaRenglon = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSAnchoEspacio = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSEspacioMesVert = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jSEspacioMesHrt = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSEspacioDiasHr = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSEspacioDiasVrt = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jSAlturaAnno = new javax.swing.JSpinner();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calendario Maker");
        setResizable(false);

        textFieldChooser1.setChooserCurrentDirectory(new java.io.File("G:\\Fabiel\\Mis Documentos\\Imagenes\\Postales\\Para Calendarios"));

        jSAnno.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2014), null, null, Integer.valueOf(1)));

        hacerCalendario.setText("Hacer Calendarios");
        hacerCalendario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hacerCalendarioActionPerformed(evt);
            }
        });

        jLabel3.setText("Año");

        jColorAnno.setBackground(new java.awt.Color(0, 0, 255));
        jColorAnno.setText("Año");

        jColorMes.setBackground(new java.awt.Color(0, 0, 255));
        jColorMes.setText("Mes");

        jColorDomingo.setBackground(new java.awt.Color(255, 0, 0));
        jColorDomingo.setText("Domingo");

        jColorFondo.setBackground(new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 100)
        );
        jColorFondo.setText("Fondo");

        jColorLetra.setBackground(new java.awt.Color(0, 0, 5));
        jColorLetra.setForeground(new java.awt.Color(255, 255, 255));
        jColorLetra.setText("Letra");

        jSAlturaRenglon.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), null, null, Integer.valueOf(1)));

        jLabel4.setText("AlturaRenglon");

        jLabel5.setText("Ancho Espacio");

        jSAnchoEspacio.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(115), null, null, Integer.valueOf(1)));

        jLabel6.setText("Espacio Mes Vert");

        jSEspacioMesVert.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), null, null, Integer.valueOf(1)));

        jLabel7.setText("Espacio Mes Hrt");

        jSEspacioMesHrt.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), null, null, Integer.valueOf(1)));

        jLabel8.setText("Espacio Dias Hrt");

        jSEspacioDiasHr.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(5), null, null, Integer.valueOf(1)));

        jLabel9.setText("Espacio Dias Vert");

        jSEspacioDiasVrt.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(5), null, null, Integer.valueOf(1)));

        jLabel10.setText("Altura Año");

        jSAlturaAnno.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), null, null, Integer.valueOf(1)));

        jLabel1.setText("Seleccione el directorio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSAnchoEspacio)
                            .addComponent(jSEspacioMesVert)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSEspacioDiasHr)
                            .addComponent(jSEspacioMesHrt)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSAlturaAnno)
                            .addComponent(jSEspacioDiasVrt)))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hacerCalendario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jColorFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jColorAnno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jColorLetra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jColorMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jColorDomingo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSAnno, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                    .addComponent(jSAlturaRenglon)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textFieldChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSAnno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSAlturaRenglon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSAnchoEspacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSEspacioMesVert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSEspacioMesHrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSEspacioDiasHr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSEspacioDiasVrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSAlturaAnno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jColorAnno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jColorMes, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jColorDomingo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jColorFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jColorLetra, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hacerCalendario)
                .addGap(4, 4, 4)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hacerCalendarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hacerCalendarioActionPerformed
        String actionCommand = evt.getActionCommand();
        if ("Hacer Calendarios".equals(actionCommand)) {
            if (!textFieldChooser1.isChoosed()) {
                JOptionPane.showMessageDialog(this, "Seleccione el directorio de las imagenes");
            } else {
                hacerCalendario.setActionCommand("Cancelar");
                hacerCalendario.setText("Cancelar");
                backgroundWorker = new BackgroundWorker();
                backgroundWorker.execute();
            }
        } else {
            hacerCalendario.setActionCommand("Hacer Calendarios");
            hacerCalendario.setText("Hacer Calendarios");
            backgroundWorker.cancel(true);
            backgroundWorker = null;
        }
    }//GEN-LAST:event_hacerCalendarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CalendarioVisual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CalendarioVisual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CalendarioVisual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CalendarioVisual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CalendarioVisual().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hacerCalendario;
    private swingClases.swingderivados.JButtonColorChooser jColorAnno;
    private swingClases.swingderivados.JButtonColorChooser jColorDomingo;
    private swingClases.swingderivados.JButtonColorChooser jColorFondo;
    private swingClases.swingderivados.JButtonColorChooser jColorLetra;
    private swingClases.swingderivados.JButtonColorChooser jColorMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JSpinner jSAlturaAnno;
    private javax.swing.JSpinner jSAlturaRenglon;
    private javax.swing.JSpinner jSAnchoEspacio;
    private javax.swing.JSpinner jSAnno;
    private javax.swing.JSpinner jSEspacioDiasHr;
    private javax.swing.JSpinner jSEspacioDiasVrt;
    private javax.swing.JSpinner jSEspacioMesHrt;
    private javax.swing.JSpinner jSEspacioMesVert;
    private swingClases.swingderivados.TextFieldChooser textFieldChooser1;
    // End of variables declaration//GEN-END:variables
}
