/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vitor
 */
public class Investidor {
    
    private String cpf, senha, nome, email;
    private double saldoReais;
    private double saldoBtc;
    private double saldoEth;
    private double saldoXrp;

    // Construtor vazio
    public Investidor() {
    }

    // Construtor com parâmetros
    public Investidor(String cpf, String senha, String nome, String email) {
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters básicos
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters e Setters dos Saldos
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