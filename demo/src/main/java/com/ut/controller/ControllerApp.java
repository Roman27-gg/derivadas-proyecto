package com.ut.controller;

import org.jfree.chart.ChartPanel;

import com.ut.model.Funcion;
import com.ut.service.FuncionService;
import com.ut.service.GraficaService;
import com.ut.ui.AppUi;

public class ControllerApp {

    private final AppUi ui;
    private FuncionService funcionService;
    private final GraficaService graficaService;

    public ControllerApp(AppUi ui, GraficaService graficaService) {
        this.ui = ui;
        this.graficaService = graficaService;
        inicializarEventos();
    }

    private void inicializarEventos() {
        ui.setProcesarAction(e -> procesarFuncion());
    }

    private void procesarFuncion() {
        try {
            String expresion = ui.getFuncionIngresada();
            double inicio = ui.getInicioIntervalo();
            double fin = ui.getFinIntervalo();
            if (expresion.isEmpty()) {
                ui.mostrarError("Debe ingresar una función.");
                return;
            }
            Funcion funcion = new Funcion(expresion);
            funcionService = new FuncionService(funcion);
            funcionService.derivar(funcion);
            funcionService.evaluarFuncion(funcion, inicio, fin);
            ChartPanel panel = graficaService.graficarPanel(funcion);
            StringBuilder sb = new StringBuilder();
            sb.append("Función: ").append(funcion.getExpresion()).append("\n");
            sb.append("Derivada: ").append(funcion.getDerivada()).append("\n\n");
            sb.append("Puntos Críticos:\n");
            double[][] puntos = funcion.getPuntoscriticos();
            if (puntos != null) {
                for (double[] p : puntos) {
                    sb.append(String.format("x = %.3f, f(x) = %.3f%n", p[0], p[1]));
                }
            }
            sb.append("\nMáximo y Mínimo:\n");
            double[][] maxmin = funcion.getMaxMin();
            if (maxmin != null && maxmin.length == 2) {
                sb.append(String.format("Máximo → x = %.3f, f(x) = %.3f%n", maxmin[0][0], maxmin[0][1]));
                sb.append(String.format("Mínimo → x = %.3f, f(x) = %.3f%n", maxmin[1][0], maxmin[1][1]));
            }
            sb.append("\nIntervalos:\n");
            if (funcion.getIntervalos() != null) {
                for (String intervalo : funcion.getIntervalos()) {
                    sb.append(intervalo).append("\n");
                }
            }
            ui.mostrarResultado(sb.toString());
            ui.mostrarGrafica(panel);
        } catch (Exception ex) {
            ui.mostrarError("Error al procesar la función: " + ex.getMessage());
        }
    }

    public void iniciarApp() {
        ui.mostrar();
    }
}