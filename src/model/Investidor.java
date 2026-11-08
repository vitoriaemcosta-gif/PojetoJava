package model;

public class Investidor extends Pessoa {
    
    private Carteira carteira;

    // Construtor vazio
    public Investidor() {
        super();
        this.carteira = new Carteira();
    }

    // Construtor com parâmetros utilizando o super para os dados da Pessoa
    public Investidor(String cpf, String senha, String nome, String email) {
        super(cpf, senha, nome, email);
        this.carteira = new Carteira();
    }

    public Carteira getCarteira() {
        return carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    // Atalhos úteis para manter compatibilidade direta com os controladores atuais
    public double getSaldoReais() {
        return carteira.getSaldoReais();
    }

    public void setSaldoReais(double saldoReais) {
        carteira.setSaldoReais(saldoReais);
    }

    public double getSaldoBtc() {
        return carteira.getSaldoBtc();
    }

    public void setSaldoBtc(double saldoBtc) {
        carteira.setSaldoBtc(saldoBtc);
    }

    public double getSaldoEth() {
        return carteira.getSaldoEth();
    }

    public void setSaldoEth(double saldoEth) {
        carteira.setSaldoEth(saldoEth);
    }

    public double getSaldoXrp() {
        return carteira.getSaldoXrp();
    }

    public void setSaldoXrp(double saldoXrp) {
        carteira.setSaldoXrp(saldoXrp);
    }
}