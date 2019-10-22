Desenvolver aplicações avançadas de manipulação de arquivos usando uma linguagem real (Java).


Requisitos Funcionais
RF1 O sistema deverá criar bancos de dados.
RF2 O sistema deverá criar tabelas dentro dos bancos de dados.
RF3 O sistema deverá incluir linhas na tabela.
RF4 O sistema deverá listar todas as linhas de uma tabela.
RF5 O sistema poderá inserir linhas em tabelas a partir de um arquivo XML.



Requisitos Não Funcionais
RNF1 O sistema deverá armazenar todos os dados em arquivos binários.
RNF2 O sistema deverá ser desenvolvido em Java utilizando a biblioteca AntLR.
RNF3 O sistema terá uma interface gráfica para o usuário digitar os comandos e visualizar os resultados.
RNF5 O código fonte será controlado através do GitHub.


Regras de Negócio
RN1 Não existe limite na quantidade de tabelas dentro de um banco de dados.
RN2 O nome tem no máximo 20 caracteres.
RN3 No nome são aceitos caracteres sem acento, números e _.
RN4 Não existe limite na quantidade de colunas dentro de uma tabela.
RN5 Todas as colunas são opcionais (nullable).
RN6 Os tipos de colunas aceitos são int, float (usando ponto para o decimal) e char(nn).
RN7 A instrução para criar tabela tem a sintaxe: create table [<nomebd>].<nometabela> ( <nomecol> <tipocol>
[,<nomecol> <tipocol>]*).
RN8 A instrução para inserir linhas na tabela tem a sintaxe: insert into [<nomebd>].<nometabela> (<nomecol>
[,<nomecol>]*) values (<literal> [,<literal>]*)
RN9 A instrução para listar as linhas da tabela tem a sintaxe: select * from [<nomebd>].<nometabela>
RN10 O nome da coluna não precisa seguir na sequência das colunas existentes na tabela.
RN11 Nem todas as colunas precisam constar na inserção.

A opção do XML se refere a um recurso da aplicação em que o usuário pode selecionar um arquivo XML, fazer a
validação desse arquivo com o XSD, e se estiver tudo certo, executar a inclusão das linhas nas tabelas




