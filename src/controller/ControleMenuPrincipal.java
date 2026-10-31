package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Investidor;
import view.MenuPrincipal;
import view.Login;
import dao.Conexao;

public class ControleMenuPrincipal {
    
    private MenuPrincipal tela;
    private Investidor investidorLogado;

    // Cotações atuais simuladas
    private double cotacaoBtc = 310500.00;
    private double cotacaoEth = 16108.00;
    private double cotacaoXrp = 3.50;

    public ControleMenuPrincipal(MenuPrincipal tela, Investidor investidorLogado) {
        this.tela = tela;
        this.investidorLogado = investidorLogado;
        
        // Exibe o nome e o saldo inicial na interface
        this.tela.getLbl_nome().setText("Olá, " + investidorLogado.getNome());
        carregarSaldos();
    }

    public void carregarSaldos() {
        tela.getLbl_saldo().setText(String.format("R$ %.2f", investidorLogado.getSaldoReais()));
    }

    public void consultarSaldoDetalhado() {
        String senha = JOptionPane.showInputDialog(tela, "Digite sua senha de 6 dígitos:", "Confirmação de Segurança", JOptionPane.QUESTION_MESSAGE);
        
        if (senha != null) {
            if (senha.equals(investidorLogado.getSenha())) {
                String mensagem = String.format(
                    "Nome: %s\nCPF: %s\nReais: R$ %.2f\nBitcoin: %.4f BTC\nEthereum: %.4f ETH\nRipple: %.4f XRP",
                    investidorLogado.getNome(),
                    investidorLogado.getCpf(),
                    investidorLogado.getSaldoReais(),
                    investidorLogado.getSaldoBtc(),
                    investidorLogado.getSaldoEth(),
                    investidorLogado.getSaldoXrp()
                );
                JOptionPane.showMessageDialog(tela, mensagem, "Saldo Detalhado da Carteira", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tela, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void depositar() {
        String valorStr = JOptionPane.showInputDialog(tela, "Digite o valor que deseja depositar (R$):");
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor > 0) {
                    investidorLogado.setSaldoReais(investidorLogado.getSaldoReais() + valor);
                    atualizarSaldosNoBanco();
                    
                    JOptionPane.showMessageDialog(tela, "Depósito de R$ " + valor + " realizado com sucesso!");
                    carregarSaldos();
                } else {
                    JOptionPane.showMessageDialog(tela, "O valor deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(tela, "Por favor, digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void sacar() {
        String valorStr = JOptionPane.showInputDialog(tela, "Digite o valor que deseja sacar (R$):");
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                if (valor > 0) {
                    if (investidorLogado.getSaldoReais() >= valor) {
                        investidorLogado.setSaldoReais(investidorLogado.getSaldoReais() - valor);
                        atualizarSaldosNoBanco();
                        
                        JOptionPane.showMessageDialog(tela, "Saque de R$ " + valor + " realizado com sucesso!");
                        carregarSaldos();
                    } else {
                        JOptionPane.showMessageDialog(tela, "Saldo insuficiente para saque! O saldo não pode ficar negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(tela, "O valor deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(tela, "Por favor, digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void comprar() {
        String senha = JOptionPane.showInputDialog(tela, "Digite sua senha de 6 dígitos para comprar:", "Segurança", JOptionPane.QUESTION_MESSAGE);
        if (senha != null) {
            if (!senha.equals(investidorLogado.getSenha())) {
                JOptionPane.showMessageDialog(tela, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] opcoesMoedas = {"Bitcoin (BTC)", "Ethereum (ETH)", "Ripple (XRP)"};
            int escolha = JOptionPane.showOptionDialog(
                tela, "Selecione a criptomoeda que deseja comprar:", "Compra de Criptoativo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoesMoedas, opcoesMoedas[0]
            );

            if (escolha == JOptionPane.CLOSED_OPTION) return;

            String valorStr = JOptionPane.showInputDialog(tela, "Digite o valor em Reais (R$) que deseja investir:");
            if (valorStr != null && !valorStr.isEmpty()) {
                try {
                    double valorReais = Double.parseDouble(valorStr);
                    if (valorReais <= 0) {
                        JOptionPane.showMessageDialog(tela, "O valor deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    double taxa = 0;
                    double cotacao = 0;

                    if (escolha == 0) { taxa = 0.02; cotacao = cotacaoBtc; } // BTC: 2%
                    else if (escolha == 1) { taxa = 0.01; cotacao = cotacaoEth; } // ETH: 1%
                    else if (escolha == 2) { taxa = 0.01; cotacao = cotacaoXrp; } // XRP: 1%

                    double valorTaxa = valorReais * taxa;
                    double valorLiquido = valorReais - valorTaxa;

                    if (investidorLogado.getSaldoReais() >= valorReais) {
                        investidorLogado.setSaldoReais(investidorLogado.getSaldoReais() - valorReais);
                        double qtdCrypto = valorLiquido / cotacao;

                        if (escolha == 0) investidorLogado.setSaldoBtc(investidorLogado.getSaldoBtc() + qtdCrypto);
                        else if (escolha == 1) investidorLogado.setSaldoEth(investidorLogado.getSaldoEth() + qtdCrypto);
                        else if (escolha == 2) investidorLogado.setSaldoXrp(investidorLogado.getSaldoXrp() + qtdCrypto);

                        atualizarSaldosNoBanco();
                        carregarSaldos();
                        JOptionPane.showMessageDialog(tela, String.format("Compra realizada com sucesso!\nTaxa cobrada: R$ %.2f\nQuantidade adquirida: %.4f", valorTaxa, qtdCrypto));
                    } else {
                        JOptionPane.showMessageDialog(tela, "Saldo em Reais insuficiente para esta compra!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(tela, "Por favor, digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void vender() {
        String senha = JOptionPane.showInputDialog(tela, "Digite sua senha de 6 dígitos para vender:", "Segurança", JOptionPane.QUESTION_MESSAGE);
        if (senha != null) {
            if (!senha.equals(investidorLogado.getSenha())) {
                JOptionPane.showMessageDialog(tela, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] opcoesMoedas = {"Bitcoin (BTC)", "Ethereum (ETH)", "Ripple (XRP)"};
            int escolha = JOptionPane.showOptionDialog(
                tela, "Selecione a criptomoeda que deseja vender:", "Venda de Criptoativo",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoesMoedas, opcoesMoedas[0]
            );

            if (escolha == JOptionPane.CLOSED_OPTION) return;

            double saldoDisponivel = (escolha == 0) ? investidorLogado.getSaldoBtc() : (escolha == 1) ? investidorLogado.getSaldoEth() : investidorLogado.getSaldoXrp();
            String qtdStr = JOptionPane.showInputDialog(tela, String.format("Saldo disponível: %.4f\nQuanto deseja vender?", saldoDisponivel));

            if (qtdStr != null && !qtdStr.isEmpty()) {
                try {
                    double qtdVenda = Double.parseDouble(qtdStr);
                    if (qtdVenda <= 0 || qtdVenda > saldoDisponivel) {
                        JOptionPane.showMessageDialog(tela, "Quantidade inválida ou superior ao saldo disponível!", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double taxa = 0;
                    double cotacao = 0;

                    if (escolha == 0) { taxa = 0.03; cotacao = cotacaoBtc; } // BTC: 3%
                    else if (escolha == 1) { taxa = 0.02; cotacao = cotacaoEth; } // ETH: 2%
                    else if (escolha == 2) { taxa = 0.01; cotacao = cotacaoXrp; } // XRP: 1%

                    double valorBruto = qtdVenda * cotacao;
                    double valorTaxa = valorBruto * taxa;
                    double valorLiquido = valorBruto - valorTaxa;

                    if (escolha == 0) investidorLogado.setSaldoBtc(investidorLogado.getSaldoBtc() - qtdVenda);
                    else if (escolha == 1) investidorLogado.setSaldoEth(investidorLogado.getSaldoEth() - qtdVenda);
                    else if (escolha == 2) investidorLogado.setSaldoXrp(investidorLogado.getSaldoXrp() - qtdVenda);

                    investidorLogado.setSaldoReais(investidorLogado.getSaldoReais() + valorLiquido);

                    atualizarSaldosNoBanco();
                    carregarSaldos();
                    JOptionPane.showMessageDialog(tela, String.format("Venda realizada com sucesso!\nValor líquido em Reais: R$ %.2f\nTaxa cobrada: R$ %.2f", valorLiquido, valorTaxa));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(tela, "Por favor, digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void extrato() {
        String senha = JOptionPane.showInputDialog(tela, "Digite sua senha de 6 dígitos para ver o extrato:", "Segurança", JOptionPane.QUESTION_MESSAGE);
        if (senha != null && senha.equals(investidorLogado.getSenha())) {
            JOptionPane.showMessageDialog(tela, "Exibindo extrato de operações...");
        } else if (senha != null) {
            JOptionPane.showMessageDialog(tela, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarCotacao() {
        cotacaoBtc *= (1 + (Math.random() * 0.10 - 0.05));
        cotacaoEth *= (1 + (Math.random() * 0.10 - 0.05));
        cotacaoXrp *= (1 + (Math.random() * 0.10 - 0.05));

        JOptionPane.showMessageDialog(tela, String.format(
            "Cotações atualizadas com sucesso!\n\nBitcoin: R$ %.2f\nEthereum: R$ %.2f\nRipple: R$ %.2f",
            cotacaoBtc, cotacaoEth, cotacaoXrp
        ), "Atualização de Mercado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void sair() {
        tela.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    private void atualizarSaldosNoBanco() {
        Conexao conexao = new Conexao();
        try {
            Connection conn = conexao.getConnection();
            String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, investidorLogado.getSaldoReais());
            stmt.setDouble(2, investidorLogado.getSaldoBtc());
            stmt.setDouble(3, investidorLogado.getSaldoEth());
            stmt.setDouble(4, investidorLogado.getSaldoXrp());
            stmt.setString(5, investidorLogado.getCpf());
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela, "Erro ao atualizar saldos na base de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}