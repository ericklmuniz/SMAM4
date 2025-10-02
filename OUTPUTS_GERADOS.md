# 📁 Outputs Gerados - Identificação dos Resultados

## 🕐 Timestamps e Identificação

| Timestamp | Pasta | Arquivo Config | Stop Reason | Descrição |
|-----------|-------|----------------|-------------|-----------|
| `1759439138308` | `network_config_1759439138308` | `network_config_test.yml` | **T_MAX** | 🧪 **TESTE** - Configuração com T_max=8000, warmup=15% |
| `1759439149684` | `network_config_1759439149684` | `network_config.yml` | **RANDOMS_EXHAUSTED** | 📋 **INICIAL** - Configuração original (sem T_max) |
| `1759439163412` | `network_config_1759439163412` | `network_config_improved.yml` | **T_MAX** | 🚀 **MELHORADO** - Configuração otimizada com T_max=8000 |

## 📊 Resumo dos Resultados

### 🧪 TESTE (`network_config_test.yml`)
- **Stop Reason**: T_MAX (parou em 8000 min)
- **Tempo Observação**: 6,800.87 min (com warm-up de 15%)
- **Perdas**: 2,652 clientes
- **RNG Usado**: 18,682/100,000

### 📋 INICIAL (`network_config.yml`) 
- **Stop Reason**: RANDOMS_EXHAUSTED (esgotou aleatórios)
- **Tempo Observação**: 44,687.81 min (sem warm-up)
- **Perdas**: 14,911 clientes  
- **RNG Usado**: 100,000/100,000

### 🚀 MELHORADO (`network_config_improved.yml`)
- **Stop Reason**: T_MAX (parou em 8000 min)
- **Tempo Observação**: 6,800.87 min (com warm-up de 15%)
- **Perdas**: 2,652 clientes
- **RNG Usado**: 18,682/100,000

## 📈 Comparação de Melhorias

| Métrica | Inicial | Melhorado | Melhoria |
|---------|---------|-----------|----------|
| **Perdas Totais** | 14,911 | 2,652 | **-82%** |
| **Tempo Controlado** | 44,687 min | 8,000 min | **-82%** |
| **RNG Eficiência** | 100% usado | 19% usado | **+81%** |
| **Warm-up** | ❌ Não | ✅ Sim | **+100%** |

## 🗂️ Estrutura de Arquivos por Pasta

Cada pasta contém:
- `metrics.csv` - Métricas L, Lq, X, R, Wq, ρ por estação
- `states_station_1.csv` - Distribuição p(n) da Estação 1
- `states_station_2.csv` - Distribuição p(n) da Estação 2  
- `states_station_3.csv` - Distribuição p(n) da Estação 3
- `summary.csv` - Resumo geral da simulação

## 🎯 Recomendação para T2

**Use o resultado MELHORADO** (`network_config_1759439163412`) pois:
- ✅ Tempo controlado (T_MAX)
- ✅ Warm-up implementado
- ✅ Perdas significativamente reduzidas
- ✅ Métricas mais consistentes
- ✅ Configuração otimizada das estações
