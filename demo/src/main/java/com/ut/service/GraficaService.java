package com.ut.service;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.math.BigDecimal;

import org.matheclipse.parser.client.eval.DoubleEvaluator;

import com.ut.model.Funcion;

public class GraficaService {
    private final DoubleEvaluator eval = new DoubleEvaluator();

    public ChartPanel graficar(Funcion funcion) {
        if (tieneAsintota(funcion)) {
            return graficaConInfinito(funcion);
        } else {
            return graficarPanel(funcion);
        }
    }

    private boolean tieneAsintota(Funcion funcion) {
        String expr = funcion.getExpresion();
        if (!expr.contains("/")) {
            return false;
        }
        try {
            for (double x = -20; x <= 20; x += 0.1) {
                String exprReemplazada = reemplazarX(expr, x);
                double y = eval.evaluate(exprReemplazada);
                if (x == 0) {
                    System.out.println(y);
                }
                if (Double.isInfinite(y) || Math.abs(y) > 1e6) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private ChartPanel graficarPanel(Funcion funcion) {
        XYSeries serieFuncion = new XYSeries("f(x)");
        for (double x = -20; x <= 20; x += 0.5) {
            try {
                String exprreemplazada = reemplazarX(funcion.getExpresion(), x);
                double y = eval.evaluate(exprreemplazada);
                if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                    serieFuncion.add(x, y);
                }
            } catch (Exception e) {
                continue;
            }
        }
        serieFuncion.add(0, eval.evaluate(funcion.getExpresion().replace("x", "(0)")));
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieFuncion);
        double[][] puntosCriticos = funcion.getPuntoscriticos();
        if (puntosCriticos != null && puntosCriticos.length > 0) {
            XYSeries seriePuntos = new XYSeries("Puntos críticos");
            for (double[] pc : puntosCriticos) {
                if (pc != null && pc.length == 2 && !Double.isNaN(pc[0]) && !Double.isNaN(pc[1])) {
                    seriePuntos.add(pc[0], pc[1]);
                }
            }
            dataset.addSeries(seriePuntos);
        }
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Gráfica de f(x)",
                "x",
                "f(x)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        XYPlot plot = chart.getXYPlot();
        personalizarEstilo(plot);
        personalizarPuntosCriticos(plot);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(750, 600));
        return panel;
    }

    private ChartPanel graficaConInfinito(Funcion funcion) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int contadorSeries = 1;
        XYSeries serieFuncion = new XYSeries("f(x) - parte " + contadorSeries);
        double limite = 1e6;

        for (double x = -20; x <= 20; x += 0.05) {
            try {
                String expr = reemplazarX(funcion.getExpresion(), x);
                double y = eval.evaluate(expr);

                if (Double.isFinite(y) && Math.abs(y) < limite) {
                    serieFuncion.add(x, y);
                } else {
                    if (serieFuncion.getItemCount() > 0) {
                        dataset.addSeries(serieFuncion);
                        contadorSeries++;
                        serieFuncion = new XYSeries("f(x) - parte " + contadorSeries);
                    }
                }
            } catch (Exception e) {
                if (serieFuncion.getItemCount() > 0) {
                    dataset.addSeries(serieFuncion);
                    contadorSeries++;
                    serieFuncion = new XYSeries("f(x) - parte " + contadorSeries);
                }
            }
        }

        if (serieFuncion.getItemCount() > 0)
            dataset.addSeries(serieFuncion);

        double[][] puntosCriticos = funcion.getPuntoscriticos();
        if (puntosCriticos != null && puntosCriticos.length > 0) {
            XYSeries seriePuntos = new XYSeries("Puntos críticos");
            for (double[] pc : puntosCriticos)
                if (pc != null && pc.length == 2 && Double.isFinite(pc[0]) && Double.isFinite(pc[1]))
                    seriePuntos.add(pc[0], pc[1]);
            if (seriePuntos.getItemCount() > 0)
                dataset.addSeries(seriePuntos);
        }

        if (dataset.getSeriesCount() == 0) {
            XYSeries dummy = new XYSeries("dummy");
            dummy.add(0, 0);
            dataset.addSeries(dummy);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Gráfica de f(x)", "x", "f(x)",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        XYPlot plot = chart.getXYPlot();
        personalizarEstilo(plot);
        personalizarPuntosCriticos(plot);
        plot.getDomainAxis().setRange(-20, 20);
        plot.getRangeAxis().setAutoRange(true);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(750, 600));
        return panel;
    }

    private void personalizarEstilo(XYPlot plot) {
        plot.setBackgroundPaint(new Color(30, 30, 30));
        plot.setDomainGridlinePaint(new Color(80, 80, 80));
        plot.setRangeGridlinePaint(new Color(80, 80, 80));
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(100, 180, 255));
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);
        plot.setRenderer(renderer);
        plot.getDomainAxis().setLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);
        plot.getDomainAxis().setTickLabelPaint(Color.LIGHT_GRAY);
        plot.getRangeAxis().setTickLabelPaint(Color.LIGHT_GRAY);
        plot.getChart().setBackgroundPaint(new Color(20, 20, 20));
    }

    private void personalizarPuntosCriticos(XYPlot plot) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        int index = plot.getDataset().getSeriesCount() - 1;
        if (index >= 1) {
            renderer.setSeriesPaint(index, new Color(0, 200, 180));
            renderer.setSeriesShape(index, new Ellipse2D.Double(-4, -4, 8, 8));
            renderer.setSeriesLinesVisible(index, false);
            renderer.setSeriesShapesVisible(index, true);
        }
    }

    private String reemplazarX(String expresion, double x) {
        BigDecimal bd = BigDecimal.valueOf(x).stripTrailingZeros();
        String valor = "(" + bd.toPlainString() + ")";

        String expr = expresion;
        expr = expr.replaceAll("(\\d)(\\()", "$1*(");
        expr = expr.replaceAll("(\\))(\\d)", ")*$2");
        expr = expr.replaceAll("(\\))(\\()", ")*(");
        expr = expr.replaceAll("\\bx\\b", valor);
        return expr;
    }

}