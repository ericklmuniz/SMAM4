# Simulador de Rede de Filas - SMAM4

Este projeto implementa um simulador generalizado de redes de filas que suporta qualquer topologia de rede, incluindo roteamento probabilístico e loops de feedback entre estações.

## Características

- **Configuração via YAML**: O modelo da rede é definido através de um arquivo de configuração YAML
- **Roteamento probabilístico**: Suporte a roteamento probabilístico entre estações
- **Loops de feedback**: Permite que clientes retornem a estações anteriores
- **Múltiplas estações**: Suporte a qualquer número de estações com diferentes capacidades
- **Controle de aleatórios**: Simulação controlada por número limitado de números aleatórios
- **Relatórios detalhados**: Estatísticas completas de cada estação

## Estrutura do Projeto

```
├── NetworkSimulation.java      # Simulador principal
├── QueueSystem.java           # Classe que representa uma estação
├── Customer.java              # Classe que representa um cliente
├── Event.java                 # Classe que representa um evento
├── EventType.java             # Enum para tipos de eventos
├── CountingRandom.java        # Gerador de números aleatórios com limite
├── YamlConfigLoader.java      # Carregador de configuração YAML
├── SimulationConfig.java      # Configuração da simulação
├── StationConfig.java         # Configuração de uma estação
├── RoutingRule.java           # Regra de roteamento
├── network_config.yml         # Arquivo de configuração do modelo
└── README.md                  # Este arquivo
```

## Como Usar

### 1. Compilação

```bash
javac *.java
```

### 2. Execução

```bash
java NetworkSimulation [arquivo_config.yml]
```

Se nenhum arquivo de configuração for especificado, será usado o arquivo `network_config.yml` por padrão.

### 3. Exemplo de Execução

```bash
java NetworkSimulation network_config.yml
```

## Configuração YAML

O arquivo de configuração YAML define toda a topologia da rede de filas. Aqui está um exemplo:

```yaml
simulation:
  max_random_draws: 100000
  first_arrival_time: 2.0
  random_seed: 42

arrivals:
  inter_arrival_min: 2.0
  inter_arrival_max: 4.0
  destination_station: 1

stations:
  - id: 1
    name: "Station 1"
    servers: 1
    capacity: -1  # -1 significa capacidade infinita
    service_time_min: 1.0
    service_time_max: 2.0
    routing:
      - destination: 2
        probability: 0.8
      - destination: 3
        probability: 0.2

  - id: 2
    name: "Station 2"
    servers: 2
    capacity: 5
    service_time_min: 4.0
    service_time_max: 6.0
    routing:
      - destination: 1
        probability: 0.3
      - destination: 3
        probability: 0.5
      - destination: -1  # -1 significa sair do sistema
        probability: 0.2

  - id: 3
    name: "Station 3"
    servers: 2
    capacity: 10
    service_time_min: 5.0
    service_time_max: 15.0
    routing:
      - destination: 2
        probability: 0.7
      - destination: -1  # -1 significa sair do sistema
        probability: 0.3
```

### Parâmetros de Configuração

#### Simulação
- `max_random_draws`: Número máximo de números aleatórios a serem utilizados
- `first_arrival_time`: Tempo da primeira chegada
- `random_seed`: Semente para o gerador de números aleatórios

#### Chegadas
- `inter_arrival_min`: Tempo mínimo entre chegadas
- `inter_arrival_max`: Tempo máximo entre chegadas
- `destination_station`: ID da estação de destino para as chegadas

#### Estações
- `id`: Identificador único da estação
- `name`: Nome da estação
- `servers`: Número de servidores
- `capacity`: Capacidade da estação (-1 para infinita)
- `service_time_min`: Tempo mínimo de serviço
- `service_time_max`: Tempo máximo de serviço
- `routing`: Lista de regras de roteamento
  - `destination`: ID da estação de destino (-1 para sair do sistema)
  - `probability`: Probabilidade de roteamento

## Resultados da Simulação

O simulador produz os seguintes resultados:

1. **Tempo global da simulação**: Tempo total de execução
2. **Aleatórios utilizados**: Quantidade de números aleatórios consumidos
3. **Por cada estação**:
   - Número de clientes perdidos
   - Tempos acumulados por estado
   - Distribuição de probabilidades dos estados
4. **Resumo geral**: Total de clientes perdidos e estatísticas gerais

## Modelo Implementado

O simulador implementa o modelo especificado na atividade:

- **Estação 1 (G/G/1)**: 1 servidor, capacidade infinita, tempo de serviço 1-2 min
- **Estação 2 (G/G/2/5)**: 2 servidores, capacidade 5, tempo de serviço 4-6 min
- **Estação 3 (G/G/2/10)**: 2 servidores, capacidade 10, tempo de serviço 5-15 min

### Roteamento
- **Estação 1 → Estação 2**: 80%
- **Estação 1 → Estação 3**: 20%
- **Estação 2 → Estação 1**: 30% (feedback)
- **Estação 2 → Estação 3**: 50%
- **Estação 2 → Saída**: 20%
- **Estação 3 → Estação 2**: 70% (feedback)
- **Estação 3 → Saída**: 30%

### Chegadas
- Tempo entre chegadas: 2-4 minutos
- Primeira chegada: tempo 2.0
- Destino: Estação 1

## Validação

A simulação foi executada com 100.000 números aleatórios conforme especificado na atividade, produzindo resultados consistentes com o modelo de rede de filas implementado.

## Requisitos

- Java 8 ou superior
- Arquivo de configuração YAML válido

## Autor

Desenvolvido como parte da disciplina de Simulação e Métodos Analíticos (SMAM4) - PUCRS.
