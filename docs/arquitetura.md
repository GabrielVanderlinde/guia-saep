# Arquitetura do sistema

A aplicação Spring Boot usa arquitetura em camadas. As páginas são renderizadas com Thymeleaf e os dados são persistidos em MySQL via Spring Data JPA.

| Camada | Responsabilidade |
| --- | --- |
| Templates | Formulários, mensagens, consulta paginada e histórico |
| Controllers | Recebem requisições, validam a entrada e selecionam views |
| DTOs | Transportam campos de formulário e dados de apresentação |
| Services | Aplicam regras de usuários, produtos e saldo |
| Repositories | Executam consultas, paginação e operações sobre entidades |
| Entities | Mapeiam usuários, produtos e movimentações |
| Config | Configura login, BCrypt, CSRF e permissões das rotas |

## Fluxos principais

- `ProdutoPageController` valida as datas. `ProdutoService` transforma a data final em limite exclusivo do dia seguinte, cria um `PageRequest` com ordenação por ID e consulta `ProdutoRepository` com os filtros combinados.
- `MovimentacaoController` identifica o responsável pela sessão e chama `MovimentacaoEstoqueService`. O serviço obtém bloqueio de escrita no produto, valida o saldo e grava saldo e histórico na mesma transação. A exclusão preserva produtos com histórico.
- `SegurancaConfig` reserva o cadastro de usuários e a exclusão para administradores. `UsuarioService` grava a senha com BCrypt e carrega usuários por login.
