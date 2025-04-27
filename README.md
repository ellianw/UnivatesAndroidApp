# Univates Android App
Aplicativo Android desenvolvido durante as aulas de Programação para dispositivos móveis. A aplicação foi totalmente desenvolvida através do Android Studio

### Classes da aplicação: 
- CNPJActivity.java 
    > Recebe um input de um CNPJ e valida utilizando uma API.
- CPFActivity.java
    > Em conjunto da CNPJActivity, abre uma webview para validar a situação cadastral de um CNPJ  .
- CustomNotification.java
    > Notificação utilizada na classe DataInsertActivity.
- DataEntryActivity.java
    > Possui vários campos de texto para inserção de dados exibe as informações numa caixa de diálogo.
- DataInsertActivity.java
    > Em conjunto com a NotificationActivity, possui um formulário para inserção de itens em banco SQLite.
- GPSActivity.java
    > Exibe dados sobre os sensores de GPS e Bússola magnética do disposito.
- LoginActivity.java
    > Formulário simples de login que valida informações em um Map.
- LoginWebsocketActivity.java
    > Formulário de login derrivado da LoginActivity, que envia os dados para um websocket e exibe a String retornada.
- MainActivity.java
    > Prova final da disciplina, um CRUD de receitas.
- NotificationActivity.java
    > Tela de listagem dos itens inseridos na DataInsertActivity
- RecipeInsertActivity.java
    > Inserção de receitas em banco de dados, chamada pela MainActivity
- SqlManipulationActivity.java
    > Inserção de itens em SQL.
- DAO\RecipeDAO.java
    > DAO utilizado na prova final
- Entities\Recipe.java
    > Classe de receita utilizada na prova final
- Utils\DatabaseManager.java
    > Database Manager utilizado nas atividade com SQL
- Utils\RecipeUtils.java
    > Classe de utilidades relacionadas as receitas, utilizada na prova final