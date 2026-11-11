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
    
    // Referências para a interface gráfica do Menu Principal e para o 
    //Investidor autenticado
    private MenuPrincipal tela;
    private Investidor investidorLogado;

    // Cotações iniciais simuladas para as criptomoedas suportadas
    private double cotacaoBtc = 310500.00;
    private double cotacaoEth = 16108.00;
    private double cotacaoXrp = 3.50;

    // Construtor que inicializa o controlador do menu principal, associando a 
    //tela e o investidor logado
    public ControleMenuPrincipal(MenuPrincipal tela, Investidor investidorLogado) {
        this.tela = tela;
        this.investidorLogado = investidorLogado;
        
        // Exibe uma saudação personalizada com o nome do investidor na interface 
        //gráfica
        this.tela.getLbl_nome().setText("Olá, " + investidorLogado.getNome());
    }
    
    // Método para consultar o saldo em Reais mediante confirmação de senha
    public void consultarSaldoReais() {
         // Instancia a view personalizada de introdução de senha por motivos de 
         //segurança
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação executada ao clicar no botão de confirmação na tela de 
        //senha
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            // Valida se a senha introduzida corresponde à senha do investidor 
            //logado
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de introdução de senha
                
                // Abre a view de Saldo em Reais passando o objeto do investidor 
                //logado
                SaldoReais telaReais = new SaldoReais(investidorLogado);
                telaReais.setLocationRelativeTo(tela);
                telaReais.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    // Método para consultar o saldo de Bitcoin mediante confirmação de senha
    public void consultarSaldoBtc() {
       // Instancia a view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão de confirmar senha
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                
                // Abre a view de Saldo Bitcoin passando o investidor logado e a 
                //cotação atual do ativo
                SaldoBitcoin telaBtc = new SaldoBitcoin(investidorLogado, 
                        cotacaoBtc);
                telaBtc.setLocationRelativeTo(tela); 
                telaBtc.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }

    // Método para consultar o saldo de Ripple mediante confirmação de senha
    public void consultarSaldoXrp() {
        // Instancia a view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão de confirmar senha
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                
                // Abre a view de Saldo Ripple passando o investidor logado e a 
                //cotação atual
                SaldoRipple telaRipple = new SaldoRipple(investidorLogado, 
                        cotacaoXrp);
                telaRipple.setLocationRelativeTo(tela); 
                telaRipple.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    // Método para consultar o saldo de Ethereum mediante confirmação de senha
    public void consultarSaldoEth() {
         // Instancia a view personalizada de senha
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        // Define a ação ao clicar no botão de confirmar senha
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose(); // Fecha a tela de senha
                
                // Abre a view de Saldo Ethereum passando o investidor logado 
                //e a cotação atual
                SaldoEthereum telaEth = new SaldoEthereum(investidorLogado, 
                        cotacaoEth);
                telaEth.setLocationRelativeTo(tela); 
                telaEth.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    // Método para gerir o fluxo de depósito de Reais na conta do investidor
    public void depositar() {
            Deposito telaDeposito = new Deposito();
            telaDeposito.setLocationRelativeTo(tela);

            // Ação acionada ao clicar no botão de confirmação do depósito
            telaDeposito.getBt_deposito().addActionListener(e -> {
                String valorStr = telaDeposito.getValorDigitado();

                if (valorStr != null && !valorStr.isEmpty() && !valorStr.equals
                ("R$ 0,00")) {
                    try {
                        // Limpa a formatação monetária ("R$", espaços, pontos 
                        //de milhar e substitui vírgula por ponto)
                        String valorLimpo = valorStr.replace("R$", "")
                                                    .replace(" ", "")
                                                    .replace(".", "")
                                                    .replace(",", ".");

                        double valor = Double.parseDouble(valorLimpo);

                        // Instancia o controlador de depósito passando o 
                        //investidor logado
                        ControleDeposito controleDep = new ControleDeposito
                        (investidorLogado);

                        if (controleDep.realizarDeposito(valor)) {
                            JOptionPane.showMessageDialog(telaDeposito, "Depósito "
                                    + "de R$ " + String.format("%.2f", valor) + 
                                    " realizado com sucesso!", "Sucesso", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            // Fecha a janela de depósito após o sucesso
                            telaDeposito.dispose(); 
                        } else {
                            JOptionPane.showMessageDialog(telaDeposito, "O valor "
                                    + "do depósito deve ser maior que zero!", 
                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(telaDeposito, "Por favor, "
                                + "digite um número válido!", "Erro", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(telaDeposito, "Por favor, "
                            + "insira um valor para depositar!", "Aviso", 
                            JOptionPane.WARNING_MESSAGE);
                }
            });

            // Configura a ação de fechar a janela ao clicar no botão "Voltar"
            telaDeposito.getBt_voltar().addActionListener(e -> telaDeposito.dispose());

            telaDeposito.setVisible(true);
        }

    // Método para gerir o fluxo de levantamento (saque) de Reais
    public void sacar() {
            Saque telaSaque = new Saque();
            telaSaque.setLocationRelativeTo(tela);

            // Ação acionada ao clicar no botão de confirmação do saque
            telaSaque.getBt_saque().addActionListener(e -> {
                String valorStr = telaSaque.getValorDigitado();

                if (valorStr != null && !valorStr.isEmpty() && !valorStr.equals
                ("R$ 0,00")) {
                    try {
                        // Limpa a formatação monetária inserida no campo
                        String valorLimpo = valorStr.replace("R$", "")
                                                    .replace(" ", "")
                                                    .replace(".", "")
                                                    .replace(",", ".");

                        double valor = Double.parseDouble(valorLimpo);

                        // Instancia o controlador de saque com o investidor atual
                        ControleSaque controleSaq = new ControleSaque
                        (investidorLogado);

                        if (controleSaq.realizarSaque(valor)) {
                            JOptionPane.showMessageDialog(telaSaque, "Saque de "
                                    + "R$ " + String.format("%.2f", valor) + 
                                    " realizado com sucesso!", "Sucesso", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            telaSaque.dispose(); // Fecha a tela de saque
                        } else {
                            JOptionPane.showMessageDialog(telaSaque, "Saldo "
                                    + "insuficiente ou valor inválido para "
                                    + "saque!", "Aviso", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(telaSaque, "Por favor, "
                                + "digite um número válido!", "Erro", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(telaSaque, "Por favor, insira "
                            + "um valor para sacar!", "Aviso", 
                            JOptionPane.WARNING_MESSAGE);
                }
            });

            // Ação do botão voltar
            telaSaque.getBt_voltar().addActionListener(e -> telaSaque.dispose());

            telaSaque.setVisible(true);
        }

    // Método para abrir o fluxo de compra de criptomoedas protegido por senha
    public void comprar() {
            Senha telaSenha = new Senha();
            telaSenha.setLocationRelativeTo(tela);

            telaSenha.getBt_senha().addActionListener(e -> {
                String senhaDigitada = telaSenha.getSenhaDigitada();

                if (senhaDigitada.equals(investidorLogado.getSenha())) {
                    telaSenha.dispose(); // Fecha a verificação de senha

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
    
    // Método para abrir o fluxo de venda de criptomoedas protegido por senha
    public void vender() {
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose();
                
                // Abre a tela de Venda passando o investidor logado
                Vender telaVenda = new Vender(investidorLogado);
                telaVenda.setLocationRelativeTo(tela);
                telaVenda.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }

    // Método para consultar o extrato de transações protegido por senha
    public void extrato() {
        Senha telaSenha = new Senha();
        telaSenha.setLocationRelativeTo(tela);
        
        telaSenha.getBt_senha().addActionListener(e -> {
            String senhaDigitada = telaSenha.getSenhaDigitada();
            
            if (senhaDigitada.equals(investidorLogado.getSenha())) {
                telaSenha.dispose();
                
                // Abre a tela de Extrato passando o investidor logado
                Extrato telaExtrato = new Extrato(investidorLogado);
                telaExtrato.setLocationRelativeTo(tela);
                telaExtrato.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(telaSenha, "Senha incorreta!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        telaSenha.setVisible(true);
    }
    
    // Simula a flutuação e atualização das cotações de mercado em tempo real (-5% a +5%)
    public void atualizarCotacao() {
        cotacaoBtc *= (1 + (Math.random() * 0.10 - 0.05));
        cotacaoEth *= (1 + (Math.random() * 0.10 - 0.05));
        cotacaoXrp *= (1 + (Math.random() * 0.10 - 0.05));

        // Instancia a view de Cotações para exibir os novos valores de mercado
        Cotacao telaCotacao = new Cotacao();
        telaCotacao.setLocationRelativeTo(tela);
        
        // Atribui os valores atualizados formatados às labels correspondentes na view
        telaCotacao.setValoresCotacao(cotacaoBtc, cotacaoEth, cotacaoXrp); 
        
        // Configura o comportamento do botão voltar
        telaCotacao.getBt_voltar().addActionListener(e -> telaCotacao.dispose());
        
        telaCotacao.setVisible(true);
    }

    // Encerra a sessão atual do utilizador e retorna ao ecrã de Login
    public void sair() {
        tela.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    // Método auxiliar privado para sincronizar e atualizar os saldos correntes 
    //diretamente no PostgreSQL
    private void atualizarSaldosNoBanco() {
        Conexao conexao = new Conexao();
        try {
            Connection conn = conexao.getConnection();
            String sql = "UPDATE investidor SET saldo_reais = ?, saldo_btc = ?, "
                    + "saldo_eth = ?, saldo_xrp = ? WHERE cpf = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, investidorLogado.getSaldoReais());
            stmt.setDouble(2, investidorLogado.getSaldoBtc());
            stmt.setDouble(3, investidorLogado.getSaldoEth());
            stmt.setDouble(4, investidorLogado.getSaldoXrp());
            stmt.setString(5, investidorLogado.getCpf());
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(tela, "Erro ao atualizar saldos na "
                    + "base de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}