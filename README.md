Comando para compilar todos os arquivos :

javac src/*.java -d out

Comando para executar: 

java -cp out Main

FileGenerator: Gera palavras aleatórias e cria arquivos menores com essas palavras.

MapReduceController: Inicia threads que executam a função Map em paralelo para processar cada arquivo. Coleta os resultados das threads e executa a função Reduce para agrupar as ocorrências de cada palavra.

Main: Ponto de entrada que gera arquivos e inicia o processo MapReduce.

Essa implementação demonstra como usar threads nativas em Java para um modelo simplificado de MapReduce. Com esse design, o processamento é feito de forma paralela, otimizando a contagem de palavras em múltiplos arquivos.
