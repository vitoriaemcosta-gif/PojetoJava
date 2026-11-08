/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vitor
 */
public class Carteira {
    private double saldoReais;
    private double saldoBtc;
    private double saldoEth;
    private double saldoXrp;

    public Carteira() {
        this.saldoReais = 0.0;
        this.saldoBtc = 0.0;
        this.saldoEth = 0.0;
        this.saldoXrp = 0.0;
    }

    public Carteira(double saldoReais, double saldoBtc, double saldoEth, 
            double saldoXrp) {
        this.saldoReais = saldoReais;
        this.saldoBtc = saldoBtc;
        this.saldoEth = saldoEth;
        this.saldoXrp = saldoXrp;
    }

    public double getSaldoReais() {
        return saldoReais;
    }

    public void setSaldoReais(double saldoReais) {
        this.saldoReais = saldoReais;
    }

    public double getSaldoBtc() {
        return saldoBtc;
    }

    public void setSaldoBtc(double saldoBtc) {
        this.saldoBtc = saldoBtc;
    }

    public double getSaldoEth() {
        return saldoEth;
    }

    public void setSaldoEth(double saldoEth) {
        this.saldoEth = saldoEth;
    }

    public double getSaldoXrp() {
        return saldoXrp;
    }

    public void setSaldoXrp(double saldoXrp) {
        this.saldoXrp = saldoXrp;
    }
}