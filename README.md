# Poker Advisor — Tracker Edition

Assistente estratégico de poker com interface web, motor de simulação Monte Carlo e histórico de partidas.

O sistema combina tabelas GTO (Game Theory Optimal) para decisões pré-flop com um motor de cálculo de equidade pós-flop, análise de outs e uma calculadora de all-in baseada em pot odds — tudo acessível pelo navegador sem instalação de interface gráfica.

---

## Como iniciar

**Pré-requisitos**
- Java JDK 17 ou superior
- Biblioteca SQLite JDBC (já inclusa em `lib/sqlite-jdbc-3.41.2.1.jar`)

**Rodando o projeto**

1. Clone ou baixe o repositório
2. Compile e rode a classe `Main.java` pela IDE (IntelliJ IDEA recomendado), ou via terminal:

```bash
javac -cp lib/sqlite-jdbc-3.41.2.1.jar src/*.java -d out
java -cp "out;lib/sqlite-jdbc-3.41.2.1.jar" Main
```

3. O servidor sobe na porta **8080** e o navegador abre automaticamente em `http://localhost:8080`
4. O banco de dados `historico_poker.db` é criado automaticamente na primeira execução

---

## Como usar

### 1. Escolha sua posição
Clique em uma das 6 posições na mesa visual: **UTG, MP, CO, BTN, SB ou BB**

### 2. Selecione suas cartas
Clique em **Escolher Cartas** para abrir o seletor. Escolha suas 2 cartas da mão.

### 3. Ver Recomendação (pré-flop)
Clique em **Ver Recomendação** para consultar a tabela GTO. O sistema informa se a jogada correta é **Aumentar, Pagar, Passar ou Desistir** com base na sua posição e mão.

### 4. Simule o Board
- Clique em **Adicionar Flop** para selecionar as 3 primeiras cartas da mesa
- Após o flop, os botões de **Turn** e **River** são liberados automaticamente
- A simulação roda automaticamente a cada vez que o board é atualizado

### 5. Resultados da simulação
Após cada simulação você verá:
- **Chance de ganhar (%)** — sua probabilidade de vitória
- **Chance do oponente (%)** — probabilidade do adversário
- **Barra visual** — proporção entre os dois
- **Sua mão atual** — ex: "Dois Pares", "Flush", "Trinca"
- **Outs disponíveis** — cartas que ainda podem melhorar sua mão

### 6. Calculadora de All-In
Aparece automaticamente após a simulação. Informe:
- **Pote atual** — total de fichas já na mesa
- **Valor do Call** — quanto você precisa pagar para chamar o all-in

O sistema calcula e exibe:
- Equidade mínima necessária para o all-in ser lucrativo
- Sua equidade atual
- EV esperado em fichas
- Veredicto: **✓ VALE O ALL-IN** ou **✗ NÃO VALE O ALL-IN**

### 7. Nova Mão
Clique em **↺ Nova Mão** no canto superior direito para limpar tudo e começar uma nova rodada.

---

## Perfis de oponente

| Perfil | Descrição | Range aproximado |
|---|---|---|
| **Conservador** | Só entra com mãos premium | TT+, AJS+, AQO+ |
| **Agressivo** | Joga pares médios e conectores suited | 55+, Ax suited, conectores |
| **Maníaco** | Joga quase qualquer mão | Pares, Ás qualquer, conectores baixos |

---

## Endpoints da API

| Endpoint | Descrição |
|---|---|
| `GET /` | Serve a interface web |
| `GET /api/gto?pos=BTN&mao=AKS` | Retorna recomendação GTO para posição + mão |
| `GET /api/equidade?hero=...&board=...&vilao=tight` | Calcula equidade via Monte Carlo (10.000 simulações) + mão atual + outs |
| `GET /api/outs?hero=...&board=...` | Retorna análise de outs da mão |
| `GET /api/historico` | Retorna as últimas 20 partidas salvas em JSON |

---

## Estrutura do projeto

```
src/
├── Main.java             — Servidor HTTP e roteamento dos endpoints
├── HandEvaluator.java    — Identifica e compara os 10 tipos de mão
├── EquityCalculator.java — Motor de simulação Monte Carlo
├── OutsCalculator.java   — Análise de cartas que melhoram a mão
├── RangeBuilder.java     — Converte notação de range em combinações reais
├── GtoAdvisor.java       — Tabelas GTO para as 6 posições
├── DatabaseManager.java  — Persistência do histórico em SQLite
├── DrawAnalysis.java     — Estrutura de dados para outs
├── ResultadoMao.java     — Resultado avaliado de uma mão com comparação
├── TipoMao.java          — Enum dos 10 tipos de mão (Carta Alta → Royal Flush)
├── Carta.java            — Representa uma carta (naipe + valor)
├── Baralho.java          — Baralho de 52 cartas
├── Valor.java            — Enum dos 13 valores com valor numérico
├── Naipe.java            — Enum dos 4 naipes
├── Posicao.java          — Enum das 6 posições
└── AcaoGTO.java          — Enum das ações: RAISE, CALL, FOLD, CHECK
poker-advisor-ui.html     — Interface web completa (HTML + CSS + JS)
lib/
└── sqlite-jdbc-3.41.2.1.jar
```

---

## Tecnologias

- **Java 17+** — lógica principal e servidor HTTP (`com.sun.net.httpserver`)
- **SQLite** via JDBC — persistência do histórico de partidas
- **Monte Carlo** — 10.000 simulações por cálculo de equidade
- **HTML5 / CSS3 / JavaScript vanilla** — interface web sem frameworks externos
- **Google Fonts** — tipografia (Cormorant Garamond, JetBrains Mono, Outfit)
