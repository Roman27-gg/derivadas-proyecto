package com.ut;

import com.ut.controller.ControllerApp;
import com.ut.service.GraficaService;
import com.ut.ui.AppUi;

public class Main {
    public static void main(String[] args) {
        GraficaService grafica = new GraficaService();
        AppUi Ui= new AppUi();
        ControllerApp controller = new ControllerApp(Ui, grafica);
        controller.iniciarApp();
    }
}