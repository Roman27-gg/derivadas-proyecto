package com.ut.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.eval.DoubleEvaluator;

import com.ut.exception.MultiplesVariablesException;
import com.ut.exception.VariableNoEncontradaException;
import com.ut.model.Funcion;

public class FuncionService {
    private Funcion funcion;
    private DoubleEvaluator eval;

    public FuncionService(Funcion funcion) {
        this.eval = new DoubleEvaluator();
        this.funcion = funcion;
        buscarVariable(funcion.getExpresion());
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
        buscarVariable(funcion.getExpresion());
    }

    public String buscarVariable(String expresion) {
        Pattern pattern = Pattern.compile("x||y||z");
        Matcher matcher = pattern.matcher(expresion);
        Set<String> variables = new HashSet<>();
        while (matcher.find()) {
            if (matcher.group().length() != 0) {
                variables.add(matcher.group());
            }
        }
        if (variables.size() == 0) {
            throw new VariableNoEncontradaException("No existe ninguna variable en la expresion");
        } else if (variables.size() == 1) {
            funcion.setVariable(variables.iterator().next());
            return variables.iterator().next();
        } else {
            throw new MultiplesVariablesException("La expresion posee mas de una variable");
        }
    }

    public String derivar(Funcion funcion) {
        ExprEvaluator evaluator = new ExprEvaluator();
        IExpr derivadaExpr = evaluator.eval(("D(" + funcion.getExpresion() + "," + funcion.getVariable() + ")"));
        String derivada = derivadaExpr.toString();
        funcion.setDerivada(derivada);
        return derivada;
    }

    public double[][] evaluarFuncion(Funcion funcion, double inicio, double fin) {
        List<Double> puntosx = new ArrayList<>();
        double valor;
        puntosx.add(inicio);
        for (double x = -20; x <= 20; x+=0.5)
         {
            try {
                if (contieneTrigonometrica(funcion.getDerivada())) {
                    valor = evaluarRazonesTrigonometricas(funcion.getDerivada(), x);
                } else {
                    valor = eval.evaluate(funcion.getDerivada().replace("x", String.valueOf(x)));
                }
                if (Math.abs(valor) < 0.001 && inicio < x && x < fin) {
                    puntosx.add(x);
                }
            } catch (Exception e) {
                if (inicio < x && x < fin) {
                    puntosx.add(x);
                }
            }
        }
        puntosx.add(fin);
        double[][] puntoscriticosev = new double[puntosx.size()][2];
        double fx;
        for (int i = 0; i < puntosx.size(); i++) {
            double x = puntosx.get(i);
            if (contieneTrigonometrica(funcion.getExpresion())) {
                fx = evaluarRazonesTrigonometricas(funcion.getExpresion(), x);
            } else {
                fx = eval.evaluate(funcion.getExpresion().replace("x", String.valueOf(x)));
            }
            puntoscriticosev[i][0] = x;
            puntoscriticosev[i][1] = fx;
        }
        funcion.setPuntoscriticos(puntoscriticosev);
        maxMin(funcion);
        intervalos(funcion);
        return puntoscriticosev;
    }

    private double[][] maxMin(Funcion funcion) {
        double[][] puntoscriticos = funcion.getPuntoscriticos();
        if (puntoscriticos.length == 0)
            return new double[0][0];
        double xMax = puntoscriticos[0][0];
        double yMax = puntoscriticos[0][1];
        double xMin = puntoscriticos[0][0];
        double yMin = puntoscriticos[0][1];
        for (int i = 1; i < puntoscriticos.length; i++) {
            if (puntoscriticos[i][1] > yMax) {
                yMax = puntoscriticos[i][1];
                xMax = puntoscriticos[i][0];
            }
            if (puntoscriticos[i][1] < yMin) {
                yMin = puntoscriticos[i][1];
                xMin = puntoscriticos[i][0];
            }
        }
        double[][] maxmin = {
                { xMax, yMax },
                { xMin, yMin }
        };
        funcion.setMaxMin(maxmin);
        return maxmin;
    }

    private List<String> intervalos(Funcion funcion) {
        List<String> intervalos = new ArrayList<>();
        double[] puntoscriticos = new double[funcion.getPuntoscriticos().length];
        for (int i = 0; i < puntoscriticos.length; i++) {
            puntoscriticos[i] = funcion.getPuntoscriticos()[i][0];
        }
        List<Double> limites = new ArrayList<>();
        limites.add(Double.NEGATIVE_INFINITY);
        for (double p : puntoscriticos) {
            limites.add(p);
        }
        limites.add(Double.POSITIVE_INFINITY);
        double a;
        double b;
        double puntoPrueba;
        double valorb;
        for (int i = 0; i < limites.size() - 1; i++) {
            a = limites.get(i);
            b = limites.get(i + 1);
            if (Double.isInfinite(a)) {
                puntoPrueba = b - 5;
                if (contieneTrigonometrica(funcion.getExpresion())) {
                    valorb = evaluarRazonesTrigonometricas(funcion.getExpresion(), b);
                } else {
                    valorb = eval.evaluate(funcion.getExpresion().replace("x", String.valueOf(b)));
                }
            } else if (Double.isInfinite(b)) {
                puntoPrueba = a;
                if (contieneTrigonometrica(funcion.getExpresion())) {
                    valorb = evaluarRazonesTrigonometricas(funcion.getExpresion(), a + 5);
                } else {
                    valorb = eval.evaluate(funcion.getExpresion().replace("x", String.valueOf(a + 5)));
                }
            } else {
                puntoPrueba = a;
                if (contieneTrigonometrica(funcion.getExpresion())) {
                    valorb = evaluarRazonesTrigonometricas(funcion.getExpresion(), b);
                } else {
                    valorb = eval.evaluate(funcion.getExpresion().replace("x", String.valueOf(b)));
                }
            }
            double valor;
            if (contieneTrigonometrica(funcion.getExpresion())) {
                valor = evaluarRazonesTrigonometricas(funcion.getExpresion(), puntoPrueba);
            } else {
                valor = eval.evaluate(funcion.getExpresion().replace("x", String.valueOf(puntoPrueba)));
            }
            String tipo = valor < valorb ? "Creciente" : valorb < valor ? "Decreciente" : "Crítico";
            String lima = Double.isInfinite(a) ? "−∞" : String.format("%.2f", a);
            String limb = Double.isInfinite(b) ? "∞" : String.format("%.2f", b);
            String intervalotexto = String.format("(%s, %s) → %s", lima, limb, tipo);
            intervalos.add(intervalotexto);
        }
        funcion.setIntervalos(intervalos);
        return intervalos;
    }

    private double evaluarRazonesTrigonometricas(String expr, double x) {
        ExprEvaluator evaluator = new ExprEvaluator();
        String value = evaluator.eval(reemplazarXPorValor(expr, x)).toString();
        return Double.parseDouble(value);
    }

    private boolean contieneTrigonometrica(String expresion) {
        Pattern pattern = Pattern.compile("(?i)\\b(sin|cos|tan|sec|csc|cot|asin|acos|atan)\\s*\\(");
        Matcher matcher = pattern.matcher(expresion);
        return matcher.find();
    }

    private String reemplazarXPorValor(String expresion, double valorX) {
        String valorStr = String.valueOf(valorX);
        return expresion.replaceAll("\\bx\\b", valorStr)
                .replaceAll("sin\\(", "Sin(")
                .replaceAll("cos\\(", "Cos(")
                .replaceAll("tan\\(", "Tan(")
                .replaceAll("sec\\(", "Sec(")
                .replaceAll("csc\\(", "Csc(")
                .replaceAll("cot\\(", "Cot(")
                .replaceAll("asin\\(", "ArcSin(")
                .replaceAll("acos\\(", "ArcCos(")
                .replaceAll("atan\\(", "ArcTan(");
    }

    

}