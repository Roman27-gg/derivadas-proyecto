package com.ut.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jfree.chart.ChartPanel;


public class AppUi {
    private JFrame frame;
    private JTextField funcionInput;
    private JTextField inicioInput;
    private JTextField finInput;
    private JButton procesarBtn;
    private JTextArea resultadosArea;
    private JPanel panelGrafica;

    public AppUi() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        frame = new JFrame("Análisis de Función");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Función f(x):"));
        funcionInput = new JTextField(20);
        topPanel.add(funcionInput);

        topPanel.add(new JLabel("Inicio:"));
        inicioInput = new JTextField("-2", 5);
        topPanel.add(inicioInput);

        topPanel.add(new JLabel("Fin:"));
        finInput = new JTextField("2", 5);
        topPanel.add(finInput);

        procesarBtn = new JButton("Procesar");
        topPanel.add(procesarBtn);

        resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultadosArea);
        scroll.setPreferredSize(new Dimension(350, 500));

        panelGrafica = new JPanel(new BorderLayout());
        panelGrafica.setPreferredSize(new Dimension(550, 500));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scroll, BorderLayout.WEST);
        mainPanel.add(panelGrafica, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public String getFuncionIngresada() {
        return funcionInput.getText().trim();
    }

    public double getInicioIntervalo() {
        try {
            return Double.parseDouble(inicioInput.getText().trim());
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    public double getFinIntervalo() {
        try {
            return Double.parseDouble(finInput.getText().trim());
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    public void setProcesarAction(ActionListener listener) {
        procesarBtn.addActionListener(listener);
    }

    public void mostrarResultado(String texto) {
        resultadosArea.setText(texto);
    }

    public void mostrarGrafica(ChartPanel panel) {
        panelGrafica.removeAll();
        panelGrafica.add(panel, BorderLayout.CENTER);
        panelGrafica.revalidate();
        panelGrafica.repaint();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
