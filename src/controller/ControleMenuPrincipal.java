package controller;

import java.util.Locale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Investidor;
import view.*;
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
    
    public void consultarSaldoReais() {
         // Instancia a sua view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão "Confirmar Senha" da sua view
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                // Abre a view SaldoReais passando o investidor logado
                SaldoReais telaReais = new SaldoReais(investidorLogado);
                telaReais.setLocationRelativeTo(tela);
                telaReais.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    public void consultarSaldoBtc() {
       // Instancia a sua view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão "Confirmar Senha" da sua view
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                // Abre a view SaldoBitcoin passando o investidor logado e a 
                //cotação atual
                SaldoBitcoin telaBtc = new SaldoBitcoin(investidorLogado, 
                        cotacaoBtc);
                telaBtc.setLocationRelativeTo(tela); // Centraliza em relação à 
                //janela principal
                telaBtc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }

    public void consultarSaldoXrp() {
        // Instancia a sua view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão "Confirmar Senha" da sua view
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                // Abre a view SaldoRipple passando o investidor logado e a 
                //cotação atual
                SaldoRipple telaRipple = new SaldoRipple(investidorLogado, 
                        cotacaoXrp);
                telaRipple.setLocationRelativeTo(tela); // Centraliza em relação à 
                //janela principal
                telaRipple.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    public void consultarSaldoEth() {
         // Instancia a sua view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão "Confirmar Senha" da sua view
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                // Abre a view SaldoEthereum passando o investidor logado e a 
                //cotação atual
                SaldoEthereum telaEth = new SaldoEthereum(investidorLogado, 
                        cotacaoEth);
                telaEth.setLocationRelativeTo(tela); // Centraliza em relação à 
                //janela principal
                telaEth.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    public void depositar() {
            Deposito telaDeposito = new Deposito();
            telaDeposito.setLocationRelativeTo(tela);

            // Ação ao clicar no botão de confirmar depósito
            telaDeposito.getBt_deposito().addActionListener(e -> {
                String valorStr = telaDeposito.getValorDigitado();

                if (valorStr != null && !valorStr.isEmpty() && !valorStr.equals("R$ 0,00")) {
                    try {
                        // Limpa o formato monetário ("R$", pontos de milhar e troca vírgula por ponto)
                        String valorLimpo = valorStr.replace("R$", "")
                                                    .replace(" ", "")
                                                    .replace(".", "")
                                                    .replace(",", ".");

                        double valor = Double.parseDouble(valorLimpo);

                        ControleDeposito controleDep = new ControleDeposito(investidorLogado);

                        if (controleDep.realizarDeposito(valor)) {
                            JOptionPane.showMessageDialog(telaDeposito, "Depósito de R$ " + String.format("%.2f", valor) + " realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            carregarSaldos(); // Atualiza os saldos no menu principal
                            telaDeposito.dispose(); // Fecha a tela de depósito
                        } else {
                            JOptionPane.showMessageDialog(telaDeposito, "O valor do depósito deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(telaDeposito, "Por favor, digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(telaDeposito, "Por favor, insira um valor para depositar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            // Ação do botão voltar com o ícone
            telaDeposito.getBt_voltar().addActionListener(e -> telaDeposito.dispose());

            telaDeposito.setVisible(true);
        }

    public void sacar() {
            Saque telaSaque = new Saque();
            telaSaque.setLocationRelativeTo(tela);

            // Ação ao clicar no botão de confirmar saque
            telaSaque.getBt_saque().addActionListener(e -> {
                String valorStr = telaSaque.getValorDigitado();

                if (valorStr != null && !valorStr.isEmpty() && !valorStr.equals("R$ 0,00")) {
                    try {
                        // Limpa o formato monetário
                        String valorLimpo = valorStr.replace("R$", "")
                                                    .replace(" ", "")
                                                    .replace(".", "")
                                                    .replace(",", ".");

                        double valor = Double.parseDouble(valorLimpo);

                        ControleSaque controleSaq = new ControleSaque(investidorLogado);

                        if (controleSaq.realizarSaque(valor)) {
                            JOptionPane.showMessageDialog(telaSaque, "Saque de R$ " + String.format("%.2f", valor) + " realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            carregarSaldos(); // Atualiza os saldos no menu principal
                            telaSaque.dispose(); // Fecha a tela de saque
                        } else {
                            JOptionPane.showMessageDialog(telaSaque, "Saldo insuficiente ou valor inválido para saque!", "Aviso", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(telaSaque, "Por favor, digite um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(telaSaque, "Por favor, insira um valor para sacar!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            // Ação do botão voltar
            telaSaque.getBt_voltar().addActionListener(e -> telaSaque.dispose());

            telaSaque.setVisible(true);
        }

    public void comprar() {
            Senha telaSenha = new Senha();
            telaSenha.setLocationRelativeTo(tela);

            telaSenha.getBt_senha().addActionListener(e -> {
                String senhaDigitada = telaSenha.getSenhaDigitada();

                if (senhaDigitada.equals(investidorLogado.getSenha())) {
                    telaSenha.dispose(); // Fecha a tela de senha

                    // Abre a tela de Compra passando o investidor logado
                    Compra telaCompra = new Compra(investidorLogado);
                    telaCompra.setLocationRelativeTo(tela);
                    telaCompra.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
            telaSenha.setVisible(true);
    }
    
    public void vender() {
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose();
                
                Vender telaVenda = new Vender(investidorLogado);
                telaVenda.setLocationRelativeTo(tela);
                telaVenda.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }

    public void extrato() {
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose();
                
                Extrato telaExtrato = new Extrato(investidorLogado);
                telaExtrato.setLocationRelativeTo(tela);
                telaExtrato.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
public void atualizarCotacao() {
    // Atualiza as cotações aleatoriamente
    cotacaoBtc *= (1 + (Math.random() * 0.10 - 0.05));
    cotacaoEth *= (1 + (Math.random() * 0.10 - 0.05));
    cotacaoXrp *= (1 + (Math.random() * 0.10 - 0.05));

    // Instancia e abre a tua nova View personalizada
    Cotacao telaCotacao = new Cotacao();
    telaCotacao.setLocationRelativeTo(tela);
    
    // Preenche os valores formatados nas labels da view
    telaCotacao.setValoresCotacao(cotacaoBtc, cotacaoEth, cotacaoXrp); 
    
    // Configura a ação do botão voltar da view (se já não estiver configurado dentro dela)
    telaCotacao.getBt_voltar().addActionListener(e -> telaCotacao.dispose());
    
    telaCotacao.setVisible(true);
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