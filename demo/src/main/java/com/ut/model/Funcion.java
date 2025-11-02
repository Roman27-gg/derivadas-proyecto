package com.ut.model;

import java.util.List;

public class Funcion {
    private String expresion;
    private String variable;
    private String derivada;
    private double[][] puntoscriticos;
    private double[][] maxMin;
    private List<String> intervalos;

    public Funcion(String expresion) {
        this.expresion = expresion;
    }

    public String getExpresion() {
        return expresion;
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getDerivada() {
        return derivada;
    }

    public void setDerivada(String derivada) {
        this.derivada = derivada;
    }

    public double[][] getPuntoscriticos() {
        return puntoscriticos;
    }

    public void setPuntoscriticos(double[][] puntoscriticos) {
        this.puntoscriticos = puntoscriticos;
    }

    public double[][] getMaxMin() {
        return maxMin;
    }

    public void setMaxMin(double[][] maxMin) {
        this.maxMin = maxMin;
    }

    public List<String> getIntervalos() {
        return intervalos;
    }

    public void setIntervalos(List<String> intervalos) {
        this.intervalos = intervalos;
    }

    
}