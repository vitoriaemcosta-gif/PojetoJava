/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vitor
 */
public class Ripple extends Moeda implements Tarifacao {
    public Ripple(double cotacao) {
        super(cotacao);
    }

    @Override
    public double calculaTaxaCompra(double valor) {
        return valor * 0.01;
    }

    @Override
    public double calculaTaxaVenda(double valor) {
        return valor * 0.01;
    }
}