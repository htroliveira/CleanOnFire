# __CleanOnFire__

Uma conjunto de APIs que tornam o desenvolvimento das suas aplicações Android, mais fácil.
O CleanOnFire é divido em três módulo principais:

 * __CleanOnFireDB__: uma poderosa API de abstração do SQLite, baseada em processamento de anotações;
 * __CleanOnFire Architecture__: Uma API para a implementação da [CleanArchitecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) em aplicações Android;
 * __CleanOnFire VisualizationModel API__: API voltada para a geração de ```Adapters``` baseados em modelos de visualização.

### Instalação
Adicione estas linhas ao seu build.gradle(project)
```groovy
allprojects {
    repositories {
        ...
        maven{
            url 'https://dl.bintray.com/heitor-gia/maven/'
        }
    }
}
```
e estas ao seu build.gradle(module)
```groovy
dependecies{
    ...
    implementation 'com.cleanonfire:cleanonfire-api:1.0.1'
    annotationProcessor 'com.cleanonfire:cleanonfire-processor:1.0.1'
}
```

## CleanOnFireDB

Baseada em processamento de anotações e geração de código-fonte em tempo de compilação, essa API é capaz de tornar a utilização do SQLite muito mais declarativa nas aplicações Android.

Declare seu banco de dados:
```java
@Database(
    version = 1,
    dbname = "mydb"//Opcional
)
public interface MyDatabase{

    //Declare migrações entre versões
    @Migrate(fromVersion = 1, toVersion = 2)
    Migration MIGRATION_1_TO_2 = new Migration(){
    	public void apply(SQLiteDatabase database){
        	database.execSQL("CREATE TABLE MY_NEW_VESION(ID INTEGER PRIMARY KEY)",null;
        }
    };

}
```

Declare seus modelos:

````java
@Table(tablename="usuarios")
public class Usuario{
    @PrimaryKey(autoincrement = true)//defina uma ou mais chaves primárias para sua tabela
    private int id;
    
    @ForeignKey(//Defina chaves estrangeiras para sua tabela
    	target = Perfil.class,//Defina com qual entidade a relação será feita
        name = "perfil", //Defina um nome para a relação(Opcional)
        update = ForeignKeyPolicy.RESTRICT, delete = ForeignKeyPolicy.CASCADE //Defina políticas de update e delete (Opcional)
    )
    private long perfilId;
    
    @FieldInfo(//Defina as configurações do campo(Opcional)
        columnName = "descricao_texto",
        length = 255, 
        defaultValue = "Não possui descrição", 
        nullable = true, 
        unique = false
    )
    private String descricao;
    
    @IgnoreField //Ignore campos que não representam colunas
    private Bitmap foto;
    
    /*... getters e setters ...*/
}
````

A partir deste modelo será gerada uma classe DAO com o nome de ____"nome da classe do modelo"____+```CleanDAO```.
E nesta classe haverão todos os métodos de inserção, edição, deleção e consulta referentes à tabela definida no modelo.

#### As classes DAO podem ser acessadas através da chamada:
```java 
CleanOnFireDB.get().get[Nome da classe do modelo]CleanDAO();
```

#### Alguns exemplos de utilização:
````java
UsuarioCleanDAO dao = CleanOnFireDB.get().getUsuarioCleanDAO();

//Inserção
Usuario novoUsuario = new Usuario();
novoUsuario.setDescrição("Minha primeira descrição")
int idDoNovoUsuario = dao.save(novoUsuario).getId();

//Edição
Usuario usuarioConsultado = dao.getById(idDoNovoUsuario);
usuarioConsultado.setDescrição("Minha segunda descrição");
dao.save(usuarioConsultado);

//Deleção
dao.delete(usuarioConsultado);
	// É equivalente a 
dao.deleteById(usuarioConsultado.getId());


//Consultas
List<Usuario> todosUsuarios = dao.getAll();

QueryCriteria criterioDeConsulta = QueryCriteria
										.builder()
                                        .setSelection("id IN (?,?,?)")
                                        .setSelectionArgs(1, 2, 3)
                                        .build();

List<Usuario> algunsUsuarios = dao.query(criterioDeConsulta);

List<Usuario> outrosUsuarios = dao.rawQuery("SELECT * FROM usuarios WHERE id>?", 4);
````

*O CleanOnFireDB suporta apenas uma base de dados. Uma implementação com múltiplas base de dados já está sendo analisada.

## CleanOnFire Architecture

Baseada nos conceitos arquiteturais da [CleanArchitecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) e do [MVP](https://medium.com/@cervonefrancesco/model-view-presenter-android-guidelines-94970b430ddf), a API oferece uma coleção de utilitários e implementações padrão dos componentes destas arquiteturas.

Declare seus ```UseCases```:
```java
public class GetCarroById implements UseCase<Long,Carro> {
    @Override
    public void execute(Long id, OnResultListener<Carro> resultListener, OnErrorListener errorListener){
        try {
            Carro carro = [...];
            resultListener.onResult(carro);
        } catch (Exception e){
            errorListener.onError(e);
        }
    }
}
```
Declare seus ```Presenters``` e contratos de visualização:
```java

//Approach Java 8
public class MainPresenter extends BasePresenter<MainViewContract> {
    GetCarroByID getCarroById = [...];

    public MainPresenter(UseCaseExecutor executor) {
        super(executor);
    }

    public void getCarros(String stringId) {
        executeUseCase(getCarroById)
                .transformResult(CARRO_TO_CARROVIEW)
                .transformParams(String::valueOf)
                .onResult(carroView ->{
                     getView().renderCarro(carroView);
                })
                .onError(exception ->{
                    [...]
                })
                .on(PostThread.mainThread())
                .with(stringId)
                .run(getExecutor());
    }

    public static Mapper<Carro, CarroView> CARRO_TO_CARROVIEW =
        carro -> {
            CarroView carroView = new CarroView();
            [...]
            return carroView;
        };


}
```
```java
public interface MainViewContract{
    void renderCarro(CarroView carroView);
}
```
A execução de todos os useCases é feita de forma assíncrona. E tem todos os mapeamentos e processamentos que não dizem respeito ao retorno dos dados executados fora da MainThread do Android.

## CleanOnFire VisualizationModel API

Também baseada no processamento de anotações e geração de código-fonte, esta API é capaz de construir ```Adapters``` baseando-se em modelos de visualização.

Defina o seu modelo de visualização:
```java
@VisualizationModel(
    layoutId = R.layout.item_carro, //Defina o layout a ser inflado
    adapterType = AdapterType.RECYCLERVIEW //Defina o tipo de adapter a ser gerado
)
public class CarroVisualization {

    private long id;

    @Bind(
        layoutId = R.id.tvModelo, //Defina o id do componente a ser vinculado
        view = TextView.class, // Defina o tipo de componente
        binder = TextViewBinder.class //Defina a classe responsável por vincular o valor à View (Deve ser uma implementação de ViewBinder)
    )
    private String modelo;

    @Bind(layoutId = R.id.tvAno, view = TextView.class, binder = TextViewBinder.class)
    private String ano;

    @Bind(layoutId = R.id.tvCor, view = TextView.class, binder = TextViewBinder.class, 
            clickable = true, longClickable = true)
    private String cor;

    @Bind(layoutId = R.id.cbVendido, view = CheckBox.class, binder = CompoundViewBinder.class)
    private boolean vendido;

}
```
A partir deste modelo serão geradas as classes de ```Adapter``` e de ```ViewHolderBinder```:

A classe ```Adapter``` será gerada com o nome de __"nome da classe do modelo"__+```Adapter```. 

Já a classe ```ViewHolderBinder``` será gerada com o nome de __"nome da classe do modelo"__+```ViewHolderBinder```.

Declare ```ViewBinders``` customizados:

```java
public class FrescoUriBinder implements ViewBinder<String,SimpleDraweeView>{
    @Override
    public void bind(String uri, SimpleDraweeView draweeView){
        draweeView.setImageURI(uri);
    }
 }
``` 

Utilize os adapters gerados

```java
 
public class MainActivity extends AppCompatActivity {
    RecyclerView rvCarros;
    CarroVisualizationAdapter adapter; 

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ...

        CarroVisualizationViewHolderBinder viewHolderBinder =  new CarroVisualziationViewHolderBinder();
        adapter = new CarroVisualziationAdapter(getCarroVisualizations(), this, viewHolderBinder);
        rvCarros.setAdapter(adapter);
        
        ...
    }

    private List<CarroVisualization> getCarroVisualizations(){
        ...
    }
}
```
A API suporta adapters para ```RecyclerViews``` e ```ListViews```, porém não simultaneamente para um mesmo modelo.

*Ainda não são suportados adapters para listas heterogêneas, porém a funcionalidade já está sendo analisada.
## Requisitos

API do Android : 15+

Versão do plugin do gradle: 2.2+ 

