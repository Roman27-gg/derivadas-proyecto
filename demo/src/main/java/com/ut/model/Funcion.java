package com.ut.model;


public class Funcion {
    private String expresion;
    private String variable;
    private String derivada;
    private double[][] puntoscriticos;
    private double[][] maxMin;
    private double[][] intervalos;

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

    public double[][] getIntervalos() {
        return intervalos;
    }

    public void setIntervalos(double[][] intervalos) {
        this.intervalos = intervalos;
    }

    public double[][] getMaxMin() {
        return maxMin;
    }

    public void setMaxMin(double[][] maxMin) {
        this.maxMin = maxMin;
    }
}