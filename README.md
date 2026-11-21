# Almoxarifado de materiais de limpeza

Projeto de treino para a prova prática do SAEP. Inclui cadastro de usuários, autenticação, controle por perfil, consulta paginada de produtos e histórico de movimentações.

## Executar

1. Confira o Java 21 com `java -version`.
2. Inicie o MySQL. Execute [docs/db/schema.sql](docs/db/schema.sql) em um banco de treino.
3. Configure host, porta e credenciais em `src/main/resources/application.yaml` ou passe `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`.
4. Rode `./mvnw spring-boot:run` no Linux/macOS ou `.\mvnw.cmd spring-boot:run` no Windows. Se já tiver Maven instalado, use `mvn spring-boot:run`.
5. Abra `http://localhost:8080/login`.

O banco de demonstração recebe os usuários `giovanni` / `gio123` (administrador) e `maria` / `maria123` (operadora). Use essas credenciais apenas no ensaio local. Após entrar como administrador, abra **Cadastrar usuário**.

## Permissões

| Ação | Operador | Administrador |
| --- | --- | --- |
| Consultar, cadastrar e editar produtos | Sim | Sim |
| Registrar entradas e saídas | Sim | Sim |
| Excluir produtos sem movimentações | | Sim |
| Cadastrar usuários | | Sim |

## Ensaiar e entregar

- [Roteiro de prova](docs/roteiro-prova.txt) com comandos, pontos de adaptação e checklist.
- [Arquitetura](docs/arquitetura.md) e [diagrama do banco](docs/db/der.png).
- [Plano de testes](docs/planotestes.docx) e [modelo de resultados](resultados.docx). Capture as evidências reais em `docs/testes/` e preencha os resultados depois de executar.
- Rode `mvn -Dtest=MovimentacaoEstoqueServiceTest test` para verificar as três saídas no serviço sem banco. Com o MySQL ativo e configurado, rode `mvn test` para incluir o teste de contexto.
- Na prova, parta da branch principal do repositório indicado e siga os requisitos do enunciado do dia. Abra o pull request da branch do aluno para a principal.
