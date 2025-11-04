package com.ut.service;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

import org.matheclipse.parser.client.eval.DoubleEvaluator;

import com.ut.model.Funcion;

public class GraficaService {
    private final DoubleEvaluator eval = new DoubleEvaluator();

    public ChartPanel graficarPanel(Funcion funcion) {
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

    private void personalizarEstilo(XYPlot plot) {
        plot.setBackgroundPaint(new Color(30, 30, 30));
        plot.setDomainGridlinePaint(new Color(80, 80, 80));
        plot.setRangeGridlinePaint(new Color(80, 80, 80));
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(100, 180, 255));
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShapesVisible(0, false);
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
            renderer.setSeriesPaint(index, new Color(255, 70, 70));
            renderer.setSeriesShape(index, new Ellipse2D.Double(-4, -4, 8, 8));
            renderer.setSeriesLinesVisible(index, false);
            renderer.setSeriesShapesVisible(index, true);
        }
    }

    private String reemplazarX(String expresion, double x) {
        String expr = expresion;
        expr = expr.replaceAll("(\\d)(\\()", "$1*("); 
        expr = expr.replaceAll("(\\))(\\d)", ")*$2"); 
        expr = expr.replaceAll("(\\))(\\()", ")*("); 
        expr = expr.replaceAll("x", "(" + x + ")");
        return expr;
    }
}