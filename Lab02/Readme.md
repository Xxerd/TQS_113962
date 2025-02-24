# Guia de Laboratório 2: Testes Unitários com Mocking de Dependências

## Lab2_1: Stocks Portfolio

### Conceitos Principais
- **Mocking de dependências**: Criação de objetos simulados (mocks) que substituem dependências externas durante os testes unitários
- **Injeção de mocks**: Uso da anotação `@Mock` para criar automaticamente objetos mock
- **Expectativas em mocks**: Configuração do comportamento esperado com `when(...).thenReturn(...)`
- **Verificação de interações**: Uso de `verify()` para confirmar que os métodos do mock foram chamados corretamente

### Observações Importantes
- Para JUnit 5, usar a anotação `@ExtendWith` para integrar o Mockito
- O aviso `UnnecessaryStubbingException` aparece quando definimos expectativas para casos que não são testados
- Alternativas de asserção como Hamcrest ou AssertJ oferecem sintaxe mais legível

## Lab2_2: Conversion Method Behavior

### Conceitos Principais
- **TDD (Test-Driven Development)**: Escrever testes antes da implementação do código
- **Isolamento de dependências externas**: Usar mocks para simular APIs de terceiros
- **Parsing de JSON**: Converter respostas JSON em objetos Java

## Lab2_3: Connect to Remote Resource and Integration Tests

### Conceitos Principais
- **Testes de integração**: Testes que verificam a interação entre componentes reais (sem mocks)
- **Maven Failsafe Plugin**: Plugin para executar testes de integração
- **Convenções de nomenclatura**: Arquivos de teste de integração terminam com "IT"

### Comandos Maven e Suas Diferenças
- `mvn test`: Executa apenas testes unitários (classes que terminam com "Test")
- `mvn package`: Executa testes unitários e empacota o projeto
- `mvn package -DskipTests=true`: Empacota o projeto sem executar testes
- `mvn failsafe:integration-test`: Executa apenas testes de integração (classes que terminam com "IT")
- `mvn install`: Executa todos os testes (unitários e integração) e instala o artefato no repositório local

### Convenções Importantes
- Testes unitários: nomes de classe terminam com "Test", executados pelo plugin Surefire
- Testes de integração: nomes de classe terminam com "IT", executados pelo plugin Failsafe