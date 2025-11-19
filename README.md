**Assistente de Poker GTO e calculadora de equidade**

Um aplicativo desktop desenvolvido para auxiliar jogadores de poker na tomada de decisões estratégicas e analise matematicas das mãos.

O projeto combina tabelas de estratégia GTO (Game theory optimal) para o jogo pré-flop com um poderoso motor de simulação de Monte Carlo para calculos de equidade pós-flop.

Demonstração

**Funcionalidades principais**

**1. Consultar GTO(pré-flop)**

Entrada: Posição do jogador (UTG, MP, CO, BTN, SB, BB) e as duas cartas da mão.
Lógica: Utiliza tabelas pré-definidas em memória para sugerir a melhor ação matemática (RAISE, CALL, FOLD).
Tratamento de dados: Reconhece automaticamente se a mão é "Suited" (mesmo naipe) ou "Offsuit".

**2. Calculadora de equidade (Pós-flop)**

Simulação de Monte Carlo: Ao analisar o flop, turn e river, o sistema roda 100.000 simulações de jogos virtuais em milisegundos.
Cenário realista: Calcula a porcentagem exata de vitória da sua mão contra um oponente com uma mão aleátoria.

**3. Analisador de Outs(vidente)**

Relatório Detalhado: Não apenas conta os outs, mas diz exatamente o qu eles formam.
Exemplo: "9 outs para Flush | 3 outs para trinca".
Cálculo de Odds: Estima porcentagem de chance de melhorar a mão na proxima carta.

**4. Interface Gráfica(GUI)**

Interface amigavel construida no Java Swing
Validação de erros de digitação.
Fluxo continuo de analise: Flop -> turn -> river.


**Tecnologias e Conceitos Utilizados**

Este projeto ofi construido do zero para aplicar conceitos fundamentais e avançados de programação orientada a objetos (POO):

**Java Core**: Logica principal.
**Java Swing**:  Criação da interface gráfica(JFrame, JPanel, Layout Managers).
**Collections Framework**: Uso intensivo de List, Map(HasMap para tabelas GTO) e Set (para contagem de outs unicos).
**Enums:** Para representar Naipes, valores, posições e tipos de mão, garantindo segurança de tipos.
**Algoritmos de ordenação:** Uso de Stream API e Comparator para ordenar cartas e identificar Kickers.
**Tratamento de exceções:** Uso de try-catch para blindar a aplicação contra erros de input.

Estrutura do projeto

O código é modularizado em cérebros especialistas

PokerGUI.java: A interface visual é controle de eventos.
HandEvaluator.java: O HandEvaluator contem a lógica complexa para identificar qualquer mão de poker e desempatar usando kickers.
EquityCalculator.java: o motor de simulação estatistica.
OutsCalculator.java: O modulo analitico que prevê cartas futuras.
GtoAdvisor.java: o banco de dados estrategico.
CardParser.java: Utilitario estatico para converter texto em objetos.

Como Executar

Pré-requisitos
Java JDK instalado( => 17)

Rodando o executavel (.jar)

1.Baixe o arquivo "Sistm_Poker.jar".
2.Dê um duplo clique no arquivo ou rode no terminal:
bash
java -jar Sistm_Poker.jar

