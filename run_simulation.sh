#!/bin/bash

# Script para compilar e executar o simulador de rede de filas

echo "=== Simulador de Rede de Filas - SMAM4 ==="
echo

# Verificar se Java está instalado
if ! command -v javac &> /dev/null; then
    echo "Erro: Java não encontrado. Por favor, instale o Java JDK."
    exit 1
fi

# Compilar o projeto usando script controlado
echo "Compilando o projeto..."
./compile.sh

if [ $? -ne 0 ]; then
    echo "Erro na compilação. Verifique os erros acima."
    exit 1
fi

# Verificar se o arquivo de configuração existe
CONFIG_FILE=${1:-"network_config.yml"}

if [ ! -f "$CONFIG_FILE" ]; then
    echo "Erro: Arquivo de configuração '$CONFIG_FILE' não encontrado."
    echo "Uso: $0 [arquivo_config.yml]"
    exit 1
fi

echo "Executando simulação com arquivo: $CONFIG_FILE"
echo "================================================"
echo

# Executar a simulação
java NetworkSimulation "$CONFIG_FILE"

echo
echo "================================================"
echo "Simulação concluída!"
