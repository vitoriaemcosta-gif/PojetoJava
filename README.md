# Exchange de Criptomoedas - Vitoria Emanuelly Machado Costa

Sistema de corretora de criptomoedas (*Exchange*) desenvolvido em Java Swing com persistência em PostgreSQL via JDBC, seguindo estritamente o padrão arquitetural MVC (Model-View-Controller). Este projeto faz parte da avaliação da disciplina de Arquitetura de Software e Programação Orientada a Objetos do **Centro Universitário FEI**.

---

## 📋 Descrição do Projeto

A plataforma permite que utilizadores registem-se, efetuem autenticação segura e realizem operações financeiras e de investimentos digitais. O sistema suporta negociações em Reais (BRL) e em três das principais criptomoedas do mercado: Bitcoin (BTC), Ethereum (ETH) e Ripple (XRP), aplicando taxas específicas de compra e venda por meio de polimorfismo e interfaces em Java.

---

## ✨ Funcionalidades Implementadas

* **Autenticação e Cadastro:**
  * Registo de novos investidores com validação rigorosa de CPF (11 dígitos) e senha numérica (exatamente 6 dígitos).
  * Login seguro com verificação de credenciais na base de dados.
* **Gestão de Carteira e Saldos:**
  * Consulta protegida por senha de saldo em Reais e individual por criptomoeda (BTC, ETH, XRP).
  * Visualização de cotações em tempo real.
* **Movimentações Financeiras:**
  * Depósito e saque de Reais com atualização imediata de saldos e restrição para evitar saldos negativos.
* **Negociação de Criptomoedas (Compra e Venda):**
  * Compra e venda de Bitcoin, Ethereum e Ripple com cálculo automático de taxas baseadas em regras polimórficas (`Tarifacao`).
  * Ecrã de sucesso interativo exibindo o saldo atualizado da carteira após cada transação.
* **Extrato e Histórico:**
  * Registo automático de todas os movimentos (`+` ou `-`) numa tabela transacional no PostgreSQL.
  * Consulta do histórico detalhado de operações na interface gráfica e exportação para ficheiro de texto `.txt`.
* **Atualização de Cotações:**
  * Simulação de flutuação de mercado com variação aleatória controlada entre (-5%) e (+5%).

---

## 🛠️ Tecnologias, Linguagens e Ferramentas

* **Linguagem:** Java (JDK 17+).
* **Interface Gráfica (GUI):** Java Swing (construído com o editor visual GUI Builder do NetBeans).
* **Base de Dados:** PostgreSQL.
* **Conectividade:** JDBC (`postgresql-driver`).
* **Padrão de Projeto:** MVC (Model, View, Controller), Herança, Classes Abstratas e Interfaces.
* **Controlo de Versão:** Git & GitHub.

---

## ⚙️ Instruções de Configuração e Instalação

### 1. Pré-requisitos
* Ter o NetBeans IDE instalado.
* Ter o PostgreSQL a correr localmente ou num servidor.
* Ter o driver JDBC do PostgreSQL adicionado às bibliotecas do projeto no NetBeans.

### 2. Configuração da Base de Dados
Crie uma base de dados no PostgreSQL (por exemplo, `exchange_db`) e execute os comandos SQL para criar as tabelas necessárias:

```sql
-- Tabela de Investidores
CREATE TABLE investidor (
    cpf VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(6) NOT NULL,
    email VARCHAR(100) NOT NULL,
    saldo_reais NUMERIC(15, 2) DEFAULT 0.00,
    saldo_btc NUMERIC(15, 8) DEFAULT 0.00000000,
    saldo_eth NUMERIC(15, 8) DEFAULT 0.00000000,
    saldo_xrp NUMERIC(15, 8) DEFAULT 0.00000000
);

-- Tabela de Transações para o Extrato
CREATE TABLE transacao (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL,
    data_hora VARCHAR(50) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    moeda VARCHAR(20) NOT NULL,
    valor NUMERIC(15, 4) NOT NULL,
    taxa NUMERIC(15, 4),
    saldo_reais_apos NUMERIC(15, 2),
    saldo_btc_apos NUMERIC(15, 4),
    saldo_eth_apos NUMERIC(15, 4),
    saldo_xrp_apos NUMERIC(15, 4)
);
