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
```
---

# Cadastro
---
<img width="606" height="817" alt="image" src="https://github.com/user-attachments/assets/3df966d0-6983-4894-8770-180d871c257a" />

---

# Login
---
<img width="595" height="812" alt="image" src="https://github.com/user-attachments/assets/1839fc5d-d8a9-415b-bb27-1877524ac3c1" />

---

# Menu Principal
---
<img width="597" height="786" alt="image" src="https://github.com/user-attachments/assets/46558060-1572-48ca-a481-37acf61e3fac" />

---

# Atualizar Cotação
---
<img width="598" height="821" alt="image" src="https://github.com/user-attachments/assets/43995435-c45a-41ee-a394-92ce4f4d8128" />

---

# Confirmação de Senha
---
<img width="448" height="407" alt="image" src="https://github.com/user-attachments/assets/97e0e1c1-4720-4897-88c9-f0d7a783c5e1" />


---
# Saldos
---
<img width="452" height="407" alt="image" src="https://github.com/user-attachments/assets/885b0483-e70c-4383-966b-ca9a9a2ed84b" />
<img width="461" height="412" alt="image" src="https://github.com/user-attachments/assets/e3a968bd-3131-4684-858a-ef0c803ffb61" />
<img width="465" height="416" alt="image" src="https://github.com/user-attachments/assets/76c4592b-4a75-4f77-aaf4-5e13174a5838" />
<img width="457" height="415" alt="image" src="https://github.com/user-attachments/assets/ab752425-1bfe-4b07-be8f-d1b66fe1184a" />

---

# Extrato
---
<img width="980" height="687" alt="image" src="https://github.com/user-attachments/assets/6435f354-52d1-4977-9108-5462db433857" />
# Extrato gerado no arquivo
<img width="1411" height="767" alt="image" src="https://github.com/user-attachments/assets/1b33c508-6371-4db2-982c-781191676a94" />

---

# Depositar
---
<img width="471" height="415" alt="image" src="https://github.com/user-attachments/assets/943ba9e7-4c52-47dc-89f6-534e666488e2" />

---

# Vender
---
<img width="542" height="645" alt="image" src="https://github.com/user-attachments/assets/5f0e2128-83c5-4417-b82c-1249bbf19650" />
<img width="492" height="408" alt="image" src="https://github.com/user-attachments/assets/1c86a8fb-fc73-4d29-a185-b93a8b23d608" />


---
# Comprar
---
<img width="550" height="648" alt="image" src="https://github.com/user-attachments/assets/a292ef96-9cc1-4d7c-bdab-a1eabdcf8f97" />
<img width="500" height="417" alt="image" src="https://github.com/user-attachments/assets/f9206d7a-c277-4343-b2b0-40c73c6498e7" />

---

# Sacar
---
<img width="445" height="403" alt="image" src="https://github.com/user-attachments/assets/391c69ac-527e-4514-b6e4-557d1d112f12" />


