# Almoxarifado de materiais de limpeza

Projeto de treino para a prova prática do SAEP. Inclui cadastro de usuários, autenticação, controle por perfil, consulta paginada de produtos e histórico de movimentações.

## Executar

1. Confira o Java 21 com `java -version`.
2. No XAMPP, inicie o módulo **MySQL**. No Workbench, conecte a `127.0.0.1` usando a porta exibida no painel e execute [docs/db/schema.sql](docs/db/schema.sql).
3. Rode `SELECT VERSION();` no Workbench. Se a versão incluir **MariaDB**, ative o perfil `xampp`. Para um servidor MySQL, use a configuração padrão.
4. Informe ao Spring o mesmo usuário e senha usados no Workbench. O exemplo usa PowerShell. Digite a senha no prompt para evitar deixá-la no histórico.

   ```powershell
   $env:DB_USER = 'root'
   $env:DB_PASSWORD = Read-Host 'Senha do banco' -MaskInput
   $env:SPRING_PROFILES_ACTIVE = 'xampp'
   .\mvnw.cmd -Pxampp spring-boot:run
   ```

   Com MySQL, omita a linha de perfil em uma sessão nova e execute sem `-Pxampp`. O parâmetro `-Pxampp` inclui o driver JDBC MariaDB. No primeiro uso, execute o comando com acesso ao Maven Central. Se o banco usar outra porta, passe `DB_URL`, por exemplo `jdbc:mariadb://127.0.0.1:3307/saep_db`. Para MySQL, use `jdbc:mysql://` na URL. No IntelliJ, as mesmas variáveis podem ser configuradas em **Run > Edit Configurations > Environment variables**. Para MariaDB, ative também `xampp` em **Maven > Profiles**.
5. Abra `http://localhost:8080/login`.

As variáveis `DB_USER` e `DB_PASSWORD` seguem o [projeto de treino do Marlos](https://github.com/GabrielVanderlinde/prova-marlos/blob/HEAD/README_ALUNO.md). Em um XAMPP local com `root` e senha vazia, a configuração padrão já fornece esses valores. Em Windows PowerShell sem `Read-Host -MaskInput`, informe a senha pela configuração local da IDE.

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
