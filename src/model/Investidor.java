package model;

public class Investidor extends Pessoa {
    
    private double saldoReais;
    private double saldoBtc;
    private double saldoEth;
    private double saldoXrp;

    // Construtor vazio
    public Investidor() {
        super();
    }

    // Construtor com parâmetros utilizando o super para os dados da Pessoa
    public Investidor(String cpf, String senha, String nome, String email) {
        super(cpf, senha, nome, email);
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