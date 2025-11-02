package com.ut.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.matheclipse.parser.client.eval.DoubleEvaluator;

import com.ut.exception.MultiplesVariablesException;
import com.ut.exception.VariableNoEncontradaException;
import com.ut.model.Funcion;

public class FuncionService {
    private Funcion funcion;
    DoubleEvaluator eval;

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
        Pattern pattern = Pattern.compile("\\b(?!sin|cos|tan|log|exp|ln|sqrt)[a-zA-Z]\\b");
        Matcher matcher = pattern.matcher(expresion);
        Set<String> variables = new HashSet<>();
        while (matcher.find()) {
            variables.add(matcher.group());
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
        String derivada = String
                .valueOf(eval.evaluate("D(" + funcion.getExpresion() + "," + funcion.getVariable() + ")"));
        funcion.setDerivada(derivada);
        return derivada;
    }

    public double[][] buscarPuntosCriticos(Funcion funcion, double inicio, double fin) {
        List<Double> puntosx = new ArrayList<>();
        puntosx.add(inicio);
        for (double x = -20; x <= 20; x++) {
            try {
                double valor = eval.evaluate(funcion.getDerivada().replace("x", String.valueOf(x)));
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
        for (int i = 0; i < puntosx.size(); i++) {
            double x = puntosx.get(i);
            double fx = eval.evaluate(funcion.getExpresion().replace("x", String.valueOf(x)));
            puntoscriticosev[i][0] = x;
            puntoscriticosev[i][1] = fx;
        }
        maxMin(funcion);
        funcion.setPuntoscriticos(puntoscriticosev);
        return puntoscriticosev;
    }

    private double[][] maxMin(Funcion funcion) {
        double[][] puntoscriticos = funcion.getPuntoscriticos();
        if (puntoscriticos.length == 0) return new double[0][0];
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

    public double[][] intervalos(Funcion funcion) {
        return null;

    }

}
